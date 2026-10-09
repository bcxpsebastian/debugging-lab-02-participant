/**
 * Gesamtergebnis eines Deployment-Audits.
 *
 * @param checkedEntries Anzahl der geprüften Queue-Einträge
 * @param passedEntries  Anzahl der Einträge, die zum erwarteten Change Request passen
 * @param failedEntries  Anzahl der Einträge, die nicht zum erwarteten Change Request passen
 */
public record AuditResult(int checkedEntries, int passedEntries, int failedEntries) {

    /**
     * Prüft, ob das Audit vollständig erfolgreich war.
     *
     * Erfolgreich bedeutet: Es wurde mindestens ein Eintrag geprüft, kein
     * Eintrag ist fehlerhaft und jeder geprüfte Eintrag wurde als korrekt
     * gemeldet.
     *
     * @return {@code true}, wenn alle geprüften Einträge korrekt sind
     */
    public boolean successful() {
        return checkedEntries > 0 && failedEntries == 0 && passedEntries == checkedEntries;
    }
}
