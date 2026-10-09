/**
 * Ein Change Request aus dem Release-Feed.
 *
 * Das Record ist unveränderlich und enthält keine Geschäftslogik. Es dient
 * als fachliche Wahrheit, gegen die das Deployment-Audit später prüft.
 *
 * @param sequence        laufende Nummer des Change Requests im Feed
 * @param changeRequestId fachliche Kennung des Change Requests
 * @param service         Service, der ausgeliefert werden soll
 * @param environment     Zielumgebung des Deployments
 * @param version         auszuliefernde Version des Services
 */
public record ChangeRequest(
        int sequence,
        String changeRequestId,
        String service,
        String environment,
        String version) {
}
