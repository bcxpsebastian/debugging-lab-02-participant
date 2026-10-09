import java.time.Instant;

/**
 * Ein geplantes Deployment aus der Deployment-Pipeline.
 *
 * @param number       fortlaufende Deployment-Nummer
 * @param serviceKey   Schlüssel des betroffenen Services
 * @param plannedStart geplanter Startzeitpunkt in UTC
 */
public record Deployment(int number, String serviceKey, Instant plannedStart) {
}
