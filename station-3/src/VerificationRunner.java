import java.util.ArrayList;
import java.util.List;

/**
 * Führt den automatisierten Freigabelauf aus und prüft, ob alle geplanten
 * Änderungen korrekt bewertet wurden.
 *
 * Die eigentliche Bewertung übernimmt die Policy-Bibliothek
 * (com.acme.release.policy.ReleasePolicyClient). Änderungen, die am Ende
 * nicht freigegeben wurden, werden gesammelt und am Ende gemeldet; der Lauf
 * wird davon nicht abgebrochen.
 *
 * Einstiegspunkt der Station: diese Klasse ausführen bzw. debuggen.
 */
public class VerificationRunner {

    public static void main(String[] args) {
        ReleaseAssessmentService assessmentService = new ReleaseAssessmentService();
        ChangeGenerator generator = new ChangeGenerator();

        List<Integer> heldChangeNumbers = new ArrayList<>();

        for (int i = 0; i < ChangeGenerator.CHANGE_COUNT; i++) {
            ChangeRequest request = generator.next();

            ReleaseAssessment assessment = assessmentService.assess(request);

            if (assessment.getStatus() != AssessmentStatus.APPROVED) {
                heldChangeNumbers.add(request.number());
            }
        }

        for (int changeNumber : heldChangeNumbers) {
            System.out.println("Änderung Nr. " + changeNumber + " wurde nicht korrekt bewertet");
        }

        if (heldChangeNumbers.isEmpty()) {
            System.out.println("✅ Alle 10.000 Änderungen wurden korrekt bewertet.");
            System.out.println("🏅 Badge freigeschaltet: State Sentinel");
        } else {
            System.out.println(heldChangeNumbers.size()
                    + " von 10.000 Änderungen wurden nicht korrekt bewertet.");
        }
    }
}
