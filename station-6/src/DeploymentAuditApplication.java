import java.util.List;

/**
 * Einstiegspunkt der Station.
 *
 * Liest den Release-Feed ein, übergibt jeden Change Request über ein
 * wiederverwendetes Transport-Envelope an den Audit-Sink und startet das
 * Deployment-Audit erst, nachdem der Feed vollständig eingelesen wurde.
 */
public class DeploymentAuditApplication {

    public static void main(String[] args) {
        List<ChangeRequest> changeRequests = new ReleaseFeed().changeRequests();
        DeferredAuditSink auditSink = new DeferredAuditSink();
        MutableDispatchEnvelope envelope = new MutableDispatchEnvelope();

        System.out.println("ACME Deployment Audit");
        System.out.println("Loaded change requests: " + changeRequests.size());

        for (ChangeRequest changeRequest : changeRequests) {
            envelope.fillFrom(changeRequest);
            auditSink.enqueue(envelope);
        }

        System.out.println("Deferred audit entries: " + auditSink.queuedCount());
        System.out.println();

        AuditResult result = auditSink.runAudit(changeRequests);

        System.out.println();
        reportResult(result, changeRequests.size());
    }

    /**
     * Gibt das Gesamtergebnis aus und schaltet den Badge nur bei einem
     * vollständig erfolgreichen Audit frei.
     *
     * @param result        Gesamtergebnis des Audits
     * @param expectedCount Anzahl der Change Requests, die geprüft werden mussten
     */
    private static void reportResult(AuditResult result, int expectedCount) {
        System.out.println("Audit result: " + result.passedEntries() + " passed, "
                + result.failedEntries() + " failed");

        if (result.successful() && result.checkedEntries() == expectedCount) {
            System.out.println("Deployment audit accepted.");
            System.out.println("🏅 Badge unlocked: Ghost Logger");
        } else {
            System.out.println("Deployment audit rejected.");
        }
    }
}
