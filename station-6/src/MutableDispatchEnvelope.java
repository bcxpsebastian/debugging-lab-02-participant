/**
 * Veränderliches Transportobjekt der Release-Pipeline.
 *
 * Der Producer hält genau eine Instanz dieses Envelopes und befüllt sie für
 * jeden Change Request neu. Das vermeidet eine große Zahl kurzlebiger
 * Objekte, wenn ein Feed sehr viele Change Requests enthält.
 *
 * Das Envelope kennt bewusst weder die Queue noch das Audit. Es transportiert
 * ausschließlich die fachlichen Felder eines Change Requests.
 */
public class MutableDispatchEnvelope {

    private int sequence;
    private String changeRequestId;
    private String service;
    private String environment;
    private String version;

    /**
     * Befüllt das Envelope vollständig aus dem übergebenen Change Request.
     *
     * Alle fachlichen Felder werden überschrieben, es bleiben keine Werte
     * eines vorherigen Change Requests stehen.
     *
     * @param changeRequest Change Request, dessen Daten übernommen werden
     */
    public void fillFrom(ChangeRequest changeRequest) {
        this.sequence = changeRequest.sequence();
        this.changeRequestId = changeRequest.changeRequestId();
        this.service = changeRequest.service();
        this.environment = changeRequest.environment();
        this.version = changeRequest.version();
    }

    /**
     * Liefert die laufende Nummer des aktuell transportierten Change Requests.
     */
    public int sequence() {
        return sequence;
    }

    /**
     * Liefert die Kennung des aktuell transportierten Change Requests.
     */
    public String changeRequestId() {
        return changeRequestId;
    }

    /**
     * Liefert den Service des aktuell transportierten Change Requests.
     */
    public String service() {
        return service;
    }

    /**
     * Liefert die Zielumgebung des aktuell transportierten Change Requests.
     */
    public String environment() {
        return environment;
    }

    /**
     * Liefert die Version des aktuell transportierten Change Requests.
     */
    public String version() {
        return version;
    }
}
