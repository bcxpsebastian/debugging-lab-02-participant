/**
 * Validiert Change Requests für den Freigabelauf.
 *
 * Die meisten Change Requests werden ohne weitere Prüfung freigegeben.
 * Ausgenommen sind Requests mit Cross-Region-Cutover in der Compliance-Domäne
 * und hoher Risikoklasse: Sie dürfen nur von einem Validator freigegeben
 * werden, der in der dafür vorgesehenen Heimatregion betrieben wird.
 */
public final class ReleaseValidator {

    private static final String CROSS_REGION_HOME = "eu-central";

    private final ReleaseRegionConfiguration regionConfiguration = new ReleaseRegionConfiguration();

    /**
     * Prüft, ob der übergebene Change Request freigegeben werden kann.
     */
    public boolean isValid(ChangeRequest request) {
        if (!requiresRegionGate(request)) {
            return true;
        }
        return CROSS_REGION_HOME.equals(regionConfiguration.activeRegion());
    }

    private boolean requiresRegionGate(ChangeRequest request) {
        return "compliance".equals(request.domain())
                && "cross-region-cutover".equals(request.changeType())
                && "high".equals(request.riskClass());
    }
}
