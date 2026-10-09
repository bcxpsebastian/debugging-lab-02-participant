/**
 * Liest die Region, in der dieser Release-Validator-Prozess betrieben wird.
 *
 * Die Region wird pro Deployment über die Umgebung vorgegeben. Für die
 * lokale Entwicklung ist ein Default hinterlegt, damit der Validator auch
 * ohne zusätzliche Umgebungsvariable startet.
 */
public final class ReleaseRegionConfiguration {

    private static final String ENV_VAR = "RELEASE_REGION";
    private static final String DEFAULT_REGION = "eu-central";

    /**
     * Liefert die für diesen Prozess aktive Region, wie sie zur Laufzeit
     * tatsächlich konfiguriert ist.
     */
    public String activeRegion() {
        String configured = System.getenv(ENV_VAR);
        return configured != null ? configured : DEFAULT_REGION;
    }
}
