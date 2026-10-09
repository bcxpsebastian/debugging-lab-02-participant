import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Erzeugt die geplanten Deployments eines Prüflaufs.
 *
 * Der Generator arbeitet mit einem festen Seed, damit der Lauf reproduzierbar
 * ist: Reihenfolge, Service-Schlüssel und Zeitpunkte sind bei jedem Start
 * identisch.
 */
public class DeploymentGenerator {

    public static final int DEPLOYMENT_COUNT = 10_000;

    private static final int SEED = 20_240_124;

    private static final Instant WINDOW_START =
            Instant.parse("2025-03-24T00:00:00Z");
    private static final int WINDOW_MINUTES = 14 * 24 * 60;

    private static final List<String> DOMAINS = List.of(
            "billing", "invoice", "catalog", "checkout", "identity",
            "inventory", "logistics", "notification", "payment", "pricing",
            "reporting", "search", "shipping", "subscription", "support",
            "tenant", "tracking", "warehouse", "workflow", "audit");

    private static final List<String> COMPONENTS = List.of(
            "api", "adapter", "aggregator", "archive", "batch",
            "bridge", "cache", "connector", "dispatcher", "export",
            "gateway", "importer", "indexer", "ingest", "listener",
            "orchestrator", "projection", "proxy", "reconciler", "registry",
            "scheduler", "sync", "validator", "worker", "writer");

    private static final List<String> VERSIONS = List.of("v1", "v2", "v3");

    private static final List<String> SERVICE_KEYS = buildServiceKeys();

    private final Random random = new Random(SEED);

    private int number;

    /**
     * Liefert das nächste geplante Deployment.
     */
    public Deployment next() {
        number++;
        String serviceKey = SERVICE_KEYS.get(random.nextInt(SERVICE_KEYS.size()));
        Instant plannedStart =
                WINDOW_START.plus(random.nextInt(WINDOW_MINUTES), ChronoUnit.MINUTES);
        return new Deployment(number, serviceKey, plannedStart);
    }

    /**
     * Baut die Liste der Service-Schlüssel der Plattform auf.
     */
    private static List<String> buildServiceKeys() {
        List<String> keys = new ArrayList<>(DOMAINS.size() * COMPONENTS.size() * VERSIONS.size());
        for (String domain : DOMAINS) {
            for (String component : COMPONENTS) {
                for (String version : VERSIONS) {
                    keys.add(domain + "-" + component + "-" + version);
                }
            }
        }
        return List.copyOf(keys);
    }
}
