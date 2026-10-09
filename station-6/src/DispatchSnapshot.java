/**
 * Unveränderliche Momentaufnahme eines Dispatch-Ereignisses.
 *
 * Ein Snapshot hält dieselben fachlichen Felder wie ein
 * {@link MutableDispatchEnvelope}, ist danach aber nicht mehr veränderbar.
 * Dadurch bleibt ein Snapshot auch dann gültig, wenn das Envelope, aus dem er
 * erzeugt wurde, später weiterverwendet wird.
 *
 * @param sequence        laufende Nummer des Change Requests im Feed
 * @param changeRequestId fachliche Kennung des Change Requests
 * @param service         Service, der ausgeliefert werden soll
 * @param environment     Zielumgebung des Deployments
 * @param version         auszuliefernde Version des Services
 */
public record DispatchSnapshot(
        int sequence,
        String changeRequestId,
        String service,
        String environment,
        String version) {

    /**
     * Erzeugt eine Momentaufnahme der Werte, die das Envelope aktuell enthält.
     *
     * @param envelope Envelope, dessen aktueller Zustand kopiert wird
     * @return unveränderliche Kopie aller fachlichen Felder
     */
    public static DispatchSnapshot from(MutableDispatchEnvelope envelope) {
        return new DispatchSnapshot(
                envelope.sequence(),
                envelope.changeRequestId(),
                envelope.service(),
                envelope.environment(),
                envelope.version());
    }
}
