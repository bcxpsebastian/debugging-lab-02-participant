import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Gibt einen Deployment-Batch gemeinsam frei.
 *
 * Die Release-Governance verlangt, dass ein Deployment eines Batches erst
 * startet, wenn für jeden Request des Batches die Readiness-Prüfung
 * abgeschlossen ist. Der Verifier hält die Worker dafür am Batch-Gate zurück,
 * bis der vollständige Batch bestätigt ist.
 */
public class BatchReadinessVerifier {

    private static final int CONFIRMATION_TIMEOUT_SECONDS = 20;

    private final CyclicBarrier batchGate;

    public BatchReadinessVerifier(int batchSize) {
        this.batchGate = new CyclicBarrier(batchSize);
    }

    /**
     * Bestätigt die Readiness des Requests und kehrt zurück, sobald der
     * gesamte Batch bestätigt ist.
     */
    public void confirmBatchReadiness(DeploymentRequest request) {
        try {
            batchGate.await(CONFIRMATION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "batch readiness confirmation interrupted for " + request.requestId(), e);
        } catch (BrokenBarrierException | TimeoutException e) {
            throw new IllegalStateException(
                    "batch readiness confirmation incomplete for " + request.requestId(), e);
        }
    }
}
