# Station 2 – Das Wartungsfenster-Gate

## Story

Bei der ACME Trading GmbH prüft ein internes DevOps-Werkzeug jede Nacht alle
geplanten Deployments gegen die regionalen Wartungsfenster. Für jedes Deployment
werden die fortlaufende Deployment-Nummer, der Service-Schlüssel und der
geplante UTC-Zeitpunkt verarbeitet.

Die eigentliche Bewertung übernimmt die Policy-Bibliothek des
Platform-Operations-Teams. Sie ordnet Services ihren betrieblichen Zeitzonen zu
und berechnet daraus die lokale Startzeit. Euch wird sie nur als
`lib/maintenance-policy.jar` ausgeliefert — ohne Sourcen, ohne Debug-Symbole.

Seit einiger Zeit meldet der Prüflauf, dass einzelne Deployments **nicht geprüft
werden konnten**. Der Lauf bricht dabei nicht ab, beendet sich erfolgreich und
gibt keinen Stacktrace aus. Der Anwendungscode wurde bereits durchgelesen — er
sieht unauffällig aus und erhält von der Bibliothek nur ein generisches
negatives Ergebnis.

## Dein Auftrag

> Das Batch-Programm beendet sich erfolgreich, kann aber einige Deployments
> nicht prüfen. Es wird kein Stacktrace ausgegeben. Finde mit einem **Java
> Exception Breakpoint** heraus, welche Exception während der Verarbeitung
> entsteht und welche Laufzeitdaten sie auslösen.

1. Starte den Prüflauf und schau dir die Meldungen an.
2. Stelle fest, was dir an der Stelle der Fehlermeldung noch zur Verfügung
   steht — und was nicht mehr.
3. Richte einen Java Exception Breakpoint ein und starte im Debug-Modus.
4. Untersuche zum Zeitpunkt des Halts das Exception-Objekt und den Call Stack.
5. Benenne präzise, welcher Wert an welcher Stelle korrigiert werden muss.

Der Lauf gilt als bestanden, wenn er nach der Korrektur eine Erfolgsmeldung
samt Badge ausgibt.

## Startinformationen

- Java 17, kein Framework, kein Server, kein Docker.
- Einstiegspunkt: `src/MaintenanceWindowRunner.java` → `main()`.
- Öffentliche API der Bewertung:
  `MaintenancePolicy.evaluate(String serviceKey, Instant plannedStart)`
  liefert ein `PolicyDecision` mit `isAvailable()` und `localStart()`.
- Der Lauf ist reproduzierbar: 10.000 Deployments, fester Seed. Nummern,
  Service-Schlüssel und Zeitpunkte sind bei jedem Start identisch.
- Der Anwendungscode liegt vollständig in `src/`. Die Policy-Bibliothek liegt
  **nur als Jar** vor. Was sie tatsächlich verarbeitet, siehst du zur Laufzeit.

### IntelliJ-Setup

1. `File > Open…` und den Ordner `station-2` auswählen.
2. Falls nachgefragt: Project SDK auf ein **JDK 17** setzen
   (`File > Project Structure > Project > SDK`).
3. `src` ist als Source-Root konfiguriert, `lib/maintenance-policy.jar` ist als
   Modul-Bibliothek eingetragen. Falls IntelliJ das nicht übernimmt, `src` als
   Sources Root markieren und das Jar als Dependency hinzufügen.
4. Die Run-Konfiguration **MaintenanceWindowRunner** ist mitgeliefert.
5. Ausführen mit ▶ oder debuggen mit 🐞.

### Alternativ auf der Kommandozeile

```bash
cd station-2
./scripts/compile.sh
./scripts/run.sh
```

## Hinweis zum Vorgehen

Diese Station ist so gebaut, dass ein gewöhnlicher Breakpoint an der
Fehlermeldung zu spät kommt: Dort ist die ursprüngliche Situation nicht mehr
rekonstruierbar. Ein **Exception Breakpoint** hält dagegen genau in dem Moment
an, in dem das Problem entsteht — und stellt damit den Laufzeitkontext wieder
her, den die spätere Meldung nicht mehr enthält.

Wichtig: Prüfe genau, für welche Art von Exceptions dein Breakpoint gilt.
