import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Audit-Sink, der Dispatch-Ereignisse verzögert verarbeitet.
 *
 * Der Sink nimmt während des Feeds Ereignisse entgegen und reiht sie ein. Das
 * eigentliche Audit läuft erst, nachdem der Feed vollständig eingelesen wurde.
 * Damit bildet die Klasse eine verzögerte Übergabe nach, ohne von zufälligem
 * Thread-Scheduling abhängig zu sein.
 */
public class DeferredAuditSink {

    private final List<MutableDispatchEnvelope> queuedEnvelopes = new ArrayList<>();

    /**
     * Übernimmt ein Dispatch-Ereignis in die Audit-Queue.
     *
     * @param envelope Envelope, das vom Producer übergeben wird
     */
    public void enqueue(MutableDispatchEnvelope envelope) {
        queuedEnvelopes.add(envelope);
    }

    /**
     * Liefert die Anzahl der eingereihten Audit-Einträge.
     */
    public int queuedCount() {
        return queuedEnvelopes.size();
    }

    /**
     * Führt das Audit über alle eingereihten Einträge aus.
     *
     * Jeder Queue-Slot wird mit dem Change Request derselben Position
     * verglichen und einzeln auf der Konsole gemeldet.
     *
     * @param expectedRequests erwartete Change Requests in Feed-Reihenfolge
     * @return Gesamtergebnis des Audits
     * @throws IllegalStateException wenn Queue und Erwartung unterschiedlich lang sind
     */
    public AuditResult runAudit(List<ChangeRequest> expectedRequests) {
        if (expectedRequests.size() != queuedEnvelopes.size()) {
            throw new IllegalStateException("Audit queue holds " + queuedEnvelopes.size()
                    + " entries but " + expectedRequests.size() + " change requests were expected.");
        }

        int passed = 0;
        int failed = 0;
        for (int slot = 1; slot <= queuedEnvelopes.size(); slot++) {
            if (auditSlot(slot, queuedEnvelopes.get(slot - 1), expectedRequests.get(slot - 1))) {
                passed++;
            } else {
                failed++;
            }
        }
        return new AuditResult(queuedEnvelopes.size(), passed, failed);
    }

    /**
     * Auditiert einen einzelnen Queue-Slot und meldet das Teilergebnis.
     *
     * @param slot     Nummer des Queue-Slots, beginnend bei 1
     * @param envelope eingereihter Audit-Eintrag dieses Slots
     * @param expected für diesen Slot erwarteter Change Request
     * @return {@code true}, wenn der Eintrag zur Erwartung passt
     */
    private boolean auditSlot(int slot, MutableDispatchEnvelope envelope, ChangeRequest expected) {
        boolean matching = matchesExpectation(envelope, expected);
        if (matching) {
            System.out.println("✅ Audit slot " + slot + " passed");
        } else {
            System.out.println("❌ Audit slot " + slot + " mismatch");
        }
        return matching;
    }

    /**
     * Vergleicht alle fachlichen Felder eines Audit-Eintrags mit der Erwartung.
     */
    private boolean matchesExpectation(MutableDispatchEnvelope envelope, ChangeRequest expected) {
        return envelope.sequence() == expected.sequence()
                && Objects.equals(envelope.changeRequestId(), expected.changeRequestId())
                && Objects.equals(envelope.service(), expected.service())
                && Objects.equals(envelope.environment(), expected.environment())
                && Objects.equals(envelope.version(), expected.version());
    }
}
