/**
 * Verarbeitet einen einzelnen Deployment Request von der Slot-Prüfung bis
 * zum Start des Deployments.
 *
 * Jeder Deployment-Worker ruft {@link #process(DeploymentRequest)} für genau
 * einen Request auf.
 */
public class DeploymentCoordinator {

    private final DeploymentSlotRegistry slotRegistry;
    private final BatchReadinessVerifier readinessVerifier;
    private final DeploymentLauncher launcher;

    public DeploymentCoordinator(DeploymentSlotRegistry slotRegistry,
                                 BatchReadinessVerifier readinessVerifier,
                                 DeploymentLauncher launcher) {
        this.slotRegistry = slotRegistry;
        this.readinessVerifier = readinessVerifier;
        this.launcher = launcher;
    }

    /**
     * Führt den Request durch die Slot- und Readiness-Prüfung und startet
     * anschließend das Deployment.
     */
    public void process(DeploymentRequest request) {
        DeploymentSlot slot = request.targetSlot();

        if (!slotRegistry.isAvailable(slot)) {
            reject(request, slot);
            return;
        }

        readinessVerifier.confirmBatchReadiness(request);

        slotRegistry.reserve(slot, request.requestId());
        launcher.launch(request);
    }

    private void reject(DeploymentRequest request, DeploymentSlot slot) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + request.requestId()
                + " rejected: slot " + slot + " is held by " + slotRegistry.ownerOf(slot));
    }
}
