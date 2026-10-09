/**
 * Fachlicher Schlüssel eines Deployment-Slots.
 *
 * Ein Slot wird eindeutig durch Service, Environment und Slot-Namen
 * identifiziert. Pro Slot darf zu einem Zeitpunkt nur ein Deployment laufen.
 *
 * @param service     Service, zu dem der Slot gehört
 * @param environment Environment, in dem der Slot liegt
 * @param name        Name des Slots
 */
public record DeploymentSlot(String service, String environment, String name) {

    @Override
    public String toString() {
        return service + "/" + environment + "/" + name;
    }
}
