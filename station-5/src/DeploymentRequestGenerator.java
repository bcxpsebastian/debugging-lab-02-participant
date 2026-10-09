import java.util.List;

/**
 * Stellt die Deployment Requests des aktuellen Release-Fensters zusammen.
 *
 * Der Batch ist fest hinterlegt, damit jeder Lauf dieselben Requests in
 * derselben Eingangsreihenfolge verarbeitet.
 */
public class DeploymentRequestGenerator {

    /**
     * Liefert die Requests des Batches in Eingangsreihenfolge.
     */
    public List<DeploymentRequest> createRequests() {
        return List.of(
                new DeploymentRequest("DPL-104", "catalog", "staging", "green"),
                new DeploymentRequest("DPL-219", "accounts", "prod", "green"),
                new DeploymentRequest("DPL-307", "payments", "prod", "blue"),
                new DeploymentRequest("DPL-311", "search", "prod", "canary"),
                new DeploymentRequest("DPL-428", "payments", "prod", "blue"),
                new DeploymentRequest("DPL-509", "notifications", "staging", "blue"));
    }
}
