import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Prüft nach Abschluss des Batches, ob jeder Deployment-Slot exklusiv
 * belegt geblieben ist.
 */
public class DeploymentAudit {

    /**
     * Wertet das Startprotokoll aus und meldet jeden Slot, für den mehr als
     * ein Deployment gestartet wurde.
     *
     * @return {@code true}, wenn kein Slot mehrfach gestartet wurde
     */
    public boolean verifyExclusiveSlots(Map<DeploymentSlot, List<String>> launchedRequestsBySlot) {
        int startedDeployments = launchedRequestsBySlot.values().stream().mapToInt(List::size).sum();

        System.out.println("Deployment audit: " + startedDeployments + " deployments started for "
                + launchedRequestsBySlot.size() + " slots.");

        List<Map.Entry<DeploymentSlot, List<String>>> violations = launchedRequestsBySlot.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .sorted(Comparator.comparing(entry -> entry.getKey().toString()))
                .toList();

        for (Map.Entry<DeploymentSlot, List<String>> violation : violations) {
            List<String> requestIds = violation.getValue().stream().sorted().toList();
            System.out.println("SAFETY VIOLATION: " + violation.getKey() + " was launched "
                    + requestIds.size() + " times");
            System.out.println("Requests: " + String.join(", ", requestIds));
        }

        if (!violations.isEmpty()) {
            System.out.println("❌ Deployment audit failed: exclusive slot guarantee violated.");
            return false;
        }

        System.out.println("✅ Deployment audit passed: every slot stayed exclusive.");
        System.out.println("The deployment train stayed on one track.");
        System.out.println("🏅 Badge unlocked: Thread Tamer");
        return true;
    }
}
