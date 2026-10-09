import java.util.concurrent.ConcurrentHashMap;

/**
 * Führt Buch darüber, welcher Deployment Request welchen Slot belegt.
 *
 * Die Registry wird von allen Deployment-Workern gleichzeitig benutzt und
 * hält die Belegung deshalb in einer thread-sicheren Map.
 */
public class DeploymentSlotRegistry {

    private final ConcurrentHashMap<DeploymentSlot, String> owners = new ConcurrentHashMap<>();

    /**
     * Meldet, ob der Slot aktuell frei ist.
     */
    public boolean isAvailable(DeploymentSlot slot) {
        return !owners.containsKey(slot);
    }

    /**
     * Trägt den Request als Belegung des Slots ein.
     */
    public void reserve(DeploymentSlot slot, String requestId) {
        owners.put(slot, requestId);
    }

    /**
     * Liefert den Request, der aktuell für den Slot eingetragen ist, oder
     * {@code null}, wenn der Slot frei ist.
     */
    public String ownerOf(DeploymentSlot slot) {
        return owners.get(slot);
    }
}
