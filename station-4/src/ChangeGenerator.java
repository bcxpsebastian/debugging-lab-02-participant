import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

/**
 * Erzeugt die Change Requests eines Freigabelaufs.
 *
 * Der Generator arbeitet mit einem festen Seed, damit der Lauf reproduzierbar
 * ist: Reihenfolge, Servicebereich, Änderungsart, Risikoklasse und Zeitpunkt
 * sind bei jedem Start identisch.
 */
public class ChangeGenerator {

    public static final int CHANGE_COUNT = 10_000;

    private static final int SEED = 20_240_505;

    private static final Instant WINDOW_START =
            Instant.parse("2025-05-01T00:00:00Z");
    private static final int WINDOW_MINUTES = 21 * 24 * 60;

    private static final List<String> DOMAINS = List.of(
            "compliance", "billing", "invoice", "catalog", "checkout",
            "identity", "inventory", "logistics", "notification", "payment",
            "pricing", "reporting", "search", "shipping", "subscription",
            "support", "tenant", "tracking", "warehouse", "workflow");

    private static final List<String> CHANGE_TYPES = List.of(
            "cross-region-cutover", "config-update", "feature-toggle", "schema-migration",
            "capacity-scale", "security-patch", "dependency-bump", "index-rebuild",
            "cache-warmup", "credential-rotation", "route-change", "queue-resize",
            "endpoint-deprecation", "retry-policy", "log-level-change", "backup-schedule",
            "alert-threshold", "cert-renewal", "cleanup-job", "data-archival",
            "rollback", "canary-rollout", "traffic-shift", "quota-adjustment",
            "template-update");

    private static final List<String> RISK_CLASSES = List.of("low", "standard", "high");

    private final Random random = new Random(SEED);

    private int number;

    /**
     * Liefert den nächsten Change Request.
     */
    public ChangeRequest next() {
        number++;
        String domain = DOMAINS.get(random.nextInt(DOMAINS.size()));
        String changeType = CHANGE_TYPES.get(random.nextInt(CHANGE_TYPES.size()));
        String riskClass = RISK_CLASSES.get(random.nextInt(RISK_CLASSES.size()));
        Instant plannedStart =
                WINDOW_START.plus(random.nextInt(WINDOW_MINUTES), ChronoUnit.MINUTES);
        return new ChangeRequest(number, domain, changeType, riskClass, plannedStart);
    }
}
