import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Startet Deployments und protokolliert, welche Requests tatsächlich für
 * welchen Slot gestartet wurden.
 *
 * Das Protokoll wird von allen Deployment-Workern gleichzeitig geschrieben
 * und ist deshalb durchgehend thread-sicher aufgebaut.
 */
public class DeploymentLauncher {

    private final ConcurrentHashMap<DeploymentSlot, List<String>> launchedRequests =
            new ConcurrentHashMap<>();

    /**
     * Startet das Deployment des Requests in seinem Slot.
     */
    public void launch(DeploymentRequest request) {
        DeploymentSlot slot = request.targetSlot();
        launchedRequests.computeIfAbsent(slot, key -> new CopyOnWriteArrayList<>())
                .add(request.requestId());

        System.out.println("[" + Thread.currentThread().getName() + "] " + request.requestId()
                + " launching " + slot);
    }

    /**
     * Liefert eine unveränderliche Momentaufnahme des Startprotokolls.
     */
    public Map<DeploymentSlot, List<String>> launchedRequestsBySlot() {
        Map<DeploymentSlot, List<String>> snapshot = new HashMap<>();
        launchedRequests.forEach((slot, requestIds) -> snapshot.put(slot, List.copyOf(requestIds)));
        return Map.copyOf(snapshot);
    }
}
