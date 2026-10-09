import java.time.Instant;

/**
 * Ein geplanter Change Request aus dem Freigabelauf.
 *
 * @param number       fortlaufende Change-Request-Nummer
 * @param domain       fachlicher Servicebereich des Requests
 * @param changeType   fachliche Art der Änderung
 * @param riskClass    eingestufte Risikoklasse
 * @param plannedStart geplanter Startzeitpunkt in UTC
 */
public record ChangeRequest(int number, String domain, String changeType, String riskClass, Instant plannedStart) {
}
