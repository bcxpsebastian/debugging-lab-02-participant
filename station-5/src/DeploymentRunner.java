import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Einstiegspunkt der Station.
 *
 * Verarbeitet den Deployment-Batch parallel – ein Worker-Thread je Request –
 * und prüft anschließend, ob jeder Slot exklusiv belegt geblieben ist.
 */
public class DeploymentRunner {

    private static final int SHUTDOWN_TIMEOUT_SECONDS = 30;

    public static void main(String[] args) throws InterruptedException {
        List<DeploymentRequest> requests = new DeploymentRequestGenerator().createRequests();

        DeploymentSlotRegistry slotRegistry = new DeploymentSlotRegistry();
        BatchReadinessVerifier readinessVerifier = new BatchReadinessVerifier(requests.size());
        DeploymentLauncher launcher = new DeploymentLauncher();
        DeploymentCoordinator coordinator =
                new DeploymentCoordinator(slotRegistry, readinessVerifier, launcher);

        System.out.println("Processing " + requests.size() + " deployment requests...");

        ExecutorService workers = Executors.newFixedThreadPool(requests.size(), workerThreadFactory());
        try {
            List<Future<?>> inFlight = new ArrayList<>();
            for (DeploymentRequest request : requests) {
                inFlight.add(workers.submit(() -> coordinator.process(request)));
            }
            awaitCompletion(inFlight);
        } finally {
            workers.shutdown();
            if (!workers.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                workers.shutdownNow();
            }
        }

        new DeploymentAudit().verifyExclusiveSlots(launcher.launchedRequestsBySlot());
    }

    private static void awaitCompletion(List<Future<?>> inFlight) throws InterruptedException {
        for (Future<?> task : inFlight) {
            try {
                task.get();
            } catch (ExecutionException e) {
                System.out.println("Deployment worker stopped: " + e.getCause().getMessage());
            }
        }
    }

    private static ThreadFactory workerThreadFactory() {
        AtomicInteger workerNumber = new AtomicInteger();
        return runnable -> new Thread(runnable, "deployment-worker-" + workerNumber.incrementAndGet());
    }
}
