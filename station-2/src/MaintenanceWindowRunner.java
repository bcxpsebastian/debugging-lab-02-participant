import com.acme.deploy.policy.MaintenancePolicy;
import com.acme.deploy.policy.PolicyDecision;

import java.util.ArrayList;
import java.util.List;

/**
 * Prüft die geplanten Deployments gegen die regionalen Wartungsfenster.
 *
 * Die Bewertung übernimmt die Policy-Bibliothek
 * (com.acme.deploy.policy.MaintenancePolicy). Deployments, für die keine
 * Aussage getroffen werden konnte, werden gesammelt und am Ende gemeldet;
 * der Lauf wird davon nicht abgebrochen.
 *
 * Einstiegspunkt der Station: diese Klasse ausführen bzw. debuggen.
 */
public class MaintenanceWindowRunner {

    public static void main(String[] args) {
        MaintenancePolicy maintenancePolicy = new MaintenancePolicy();
        DeploymentGenerator generator = new DeploymentGenerator();

        List<Integer> failedDeploymentNumbers = new ArrayList<>();

        for (int i = 0; i < DeploymentGenerator.DEPLOYMENT_COUNT; i++) {
            Deployment deployment = generator.next();

            PolicyDecision decision =
                    maintenancePolicy.evaluate(deployment.serviceKey(), deployment.plannedStart());

            if (!decision.isAvailable()) {
                failedDeploymentNumbers.add(deployment.number());
            }
        }

        for (int deploymentNumber : failedDeploymentNumbers) {
            System.out.println("Deployment Nr. " + deploymentNumber + " konnte nicht geprüft werden");
        }

        if (failedDeploymentNumbers.isEmpty()) {
            System.out.println("✅ Alle 10.000 Deployments konnten geprüft werden.");
            System.out.println("🏅 Badge freigeschaltet: Exception Tracker");
        } else {
            System.out.println(failedDeploymentNumbers.size()
                    + " von 10.000 Deployments konnten nicht geprüft werden.");
        }
    }
}
