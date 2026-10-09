import java.time.Instant;

/**
 * Eine geplante Änderung aus dem Freigabelauf.
 *
 * @param number       fortlaufende Änderungsnummer
 * @param domain       fachlicher Servicebereich der Änderung
 * @param changeType   fachliche Art der Änderung
 * @param riskClass    eingestufte Risikoklasse
 * @param plannedStart geplanter Startzeitpunkt in UTC
 */
public record ChangeRequest(int number, String domain, String changeType, String riskClass, Instant plannedStart) {
}
