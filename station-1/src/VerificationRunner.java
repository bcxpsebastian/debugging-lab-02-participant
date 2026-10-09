import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Führt den nächtlichen Abrechnungslauf aus und vergleicht die berechneten
 * Rabatte mit der offiziellen Rabatt-Spezifikation des Fachbereichs
 * (spec/pricing-spec.properties).
 *
 * Einstiegspunkt der Station: diese Klasse ausführen bzw. debuggen.
 */
public class VerificationRunner {

    private static final String SPEC_FILE = "pricing-spec.properties";

    public static void main(String[] args) throws IOException {
        NavigableMap<Integer, Double> specification = loadSpecification();
        OrderGenerator generator = new OrderGenerator();

        int mismatches = 0;

        for (int orderNumber = 1; orderNumber <= OrderGenerator.ORDER_COUNT; orderNumber++) {
            int quantity = generator.nextQuantity();

            double specifiedRate = specification.floorEntry(quantity).getValue();
            double calculatedRate = DiscountCalculator.calculateDiscount(quantity);

            if (Double.compare(specifiedRate, calculatedRate) != 0) {
                mismatches++;
                System.out.println("Bestellung Nr. " + orderNumber + " hat falschen Rabatt");
            }
        }

        if (mismatches == 0) {
            System.out.println("✅ Alle 10.000 Bestellungen wurden korrekt berechnet.");
            System.out.println("🏅 Badge freigeschaltet: Condition Hunter");
        }
    }

    /**
     * Liest die Spezifikation als Staffel ein: Schlüssel ist die Menge, ab der
     * der zugehörige Rabattsatz gilt.
     */
    private static NavigableMap<Integer, Double> loadSpecification() throws IOException {
        Properties properties = new Properties();
        try (InputStream in = openSpecification()) {
            properties.load(in);
        }

        NavigableMap<Integer, Double> staffel = new TreeMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            int fromQuantity = Integer.parseInt(entry.getKey().toString().trim());
            double rate = Double.parseDouble(entry.getValue().toString().trim());
            staffel.put(fromQuantity, rate);
        }
        return staffel;
    }

    private static InputStream openSpecification() throws IOException {
        for (String candidate : new String[] {"spec", "station-1/spec", "../spec"}) {
            Path path = Path.of(candidate, SPEC_FILE);
            if (Files.isReadable(path)) {
                return Files.newInputStream(path);
            }
        }
        throw new IOException("Rabatt-Spezifikation " + SPEC_FILE + " nicht gefunden. "
                + "Bitte das Arbeitsverzeichnis auf den Projektordner station-1 setzen.");
    }
}
