import com.acme.pricing.catalog.DiscountCatalog;
import com.acme.pricing.catalog.DiscountTier;

import java.util.List;

/**
 * Berechnet den Mengenrabatt für eine Bestellung.
 *
 * Die Rabattstufen werden vom zentralen Preis-Katalog
 * (Bibliothek com.acme.pricing.catalog) bereitgestellt.
 */
public class DiscountCalculator {

    private static final List<DiscountTier> TIERS = DiscountCatalog.loadTiers();

    /**
     * Liefert den Rabattsatz für die angegebene Bestellmenge.
     *
     * @param quantity bestellte Menge
     * @return Rabattsatz als Dezimalzahl (z. B. 0.05 für 5 %)
     */
    public static double calculateDiscount(int quantity) {
        for (DiscountTier tier : TIERS) {
            if (tier.covers(quantity)) {
                return tier.getRate();
            }
        }
        throw new IllegalArgumentException("Keine Rabattstufe für Menge " + quantity);
    }
}
