import java.util.Random;

/**
 * Erzeugt die Bestellmengen des nächtlichen Abrechnungslaufs.
 *
 * Der Generator arbeitet mit einem festen Seed, damit der Lauf
 * reproduzierbar ist.
 */
public class OrderGenerator {

    public static final int ORDER_COUNT = 10_000;

    private static final int SEED = 42;
    private static final int MAX_QUANTITY = 10_000;

    private final Random random = new Random(SEED);

    /**
     * Liefert die Bestellmenge der nächsten Bestellung (1 bis 10.000).
     */
    public int nextQuantity() {
        return random.nextInt(MAX_QUANTITY) + 1;
    }
}
