/**
 * Ein Deployment Request aus dem aktuellen Release-Batch.
 *
 * @param requestId   fachliche Kennung des Requests
 * @param service     Service, der deployt werden soll
 * @param environment Ziel-Environment
 * @param slot        Name des Ziel-Slots innerhalb des Environments
 */
public record DeploymentRequest(String requestId, String service, String environment, String slot) {

    /**
     * Liefert den Deployment-Slot, den dieser Request belegen möchte.
     */
    public DeploymentSlot targetSlot() {
        return new DeploymentSlot(service, environment, slot);
    }
}
