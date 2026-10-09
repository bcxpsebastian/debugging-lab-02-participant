/**
 * Bewertet geplante Änderungen für den nächtlichen Freigabelauf.
 *
 * Die eigentliche Policy-Auswertung übernimmt die externe Bibliothek
 * (release-policy.jar, Klasse {@code ReleasePolicyClient}). Sie erhält das
 * Ergebnisobjekt und setzt darauf in mehreren internen Schritten den
 * Freigabestatus.
 */
public final class ReleaseAssessmentService {

    private final ReleasePolicyClient policyClient = new ReleasePolicyClient();

    /**
     * Bewertet die übergebene Änderung.
     *
     * @param request die zu bewertende Änderung
     * @return das Ergebnisobjekt mit dem final gesetzten Freigabestatus
     */
    public ReleaseAssessment assess(ChangeRequest request) {
        ReleaseAssessment assessment = new ReleaseAssessment(request.number());
        policyClient.assess(request, assessment);
        return assessment;
    }
}
