/**
 * Ergebnis der Freigabebewertung einer einzelnen Änderung.
 *
 * Der Status wird über den normalen Setter geschrieben. Ein Field Watchpoint
 * auf {@code status} zeigt jeden Schreibzugriff inklusive Call Stack.
 */
public final class ReleaseAssessment {

    private final int changeNumber;
    private AssessmentStatus status;

    public ReleaseAssessment(int changeNumber) {
        this.changeNumber = changeNumber;
    }

    public int getChangeNumber() {
        return changeNumber;
    }

    public AssessmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssessmentStatus status) {
        this.status = status;
    }
}
