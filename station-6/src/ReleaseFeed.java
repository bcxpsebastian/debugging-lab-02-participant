import java.util.List;

/**
 * Liefert die Change Requests des aktuellen Release-Laufs.
 *
 * Der Feed ist fest verdrahtet: keine Zufallsdaten, keine Uhrzeit, keine
 * Dateien und keine Netzwerkzugriffe. Jeder Lauf verarbeitet damit exakt
 * dieselbe Folge von Change Requests.
 */
public class ReleaseFeed {

    /**
     * Liefert die acht Change Requests des Release-Laufs in fester Reihenfolge.
     *
     * @return unveränderliche Liste der eingelesenen Change Requests
     */
    public List<ChangeRequest> changeRequests() {
        return List.of(
                new ChangeRequest(1, "CR-4812", "pricing-api", "production-eu", "3.8.1"),
                new ChangeRequest(2, "CR-4815", "catalog-api", "production-eu", "5.14.0"),
                new ChangeRequest(3, "CR-4821", "checkout-worker", "production-eu", "2.7.4"),
                new ChangeRequest(4, "CR-4827", "inventory-sync", "production-us", "4.3.2"),
                new ChangeRequest(5, "CR-4830", "payment-gateway", "production-eu", "7.1.0"),
                new ChangeRequest(6, "CR-4836", "notification-hub", "production-us", "6.5.3"),
                new ChangeRequest(7, "CR-4840", "customer-profile", "production-eu", "8.0.2"),
                new ChangeRequest(8, "CR-4844", "edge-router", "production-us", "9.2.0"));
    }
}
