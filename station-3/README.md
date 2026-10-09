# Station 3 – Der zweite Schreibzugriff

## Story

Bei der ACME Trading GmbH läuft jede Nacht ein automatisierter Freigabelauf
für geplante Änderungen. Für jede Änderung werden die fortlaufende
Änderungsnummer, der betroffene Servicebereich, die Änderungsart, die
Risikoklasse und der geplante UTC-Zeitpunkt verarbeitet.

Die eigentliche Bewertung übernimmt die Freigabe-Policy-Bibliothek des
Release-Engineering-Teams. Euch wird sie nur als `lib/release-policy.jar`
ausgeliefert — ohne Sourcen, ohne Debug-Symbole.

Seit einiger Zeit meldet der Prüflauf, dass einzelne Änderungen **nicht
korrekt bewertet** wurden. Der Lauf bricht dabei nicht ab, beendet sich
erfolgreich und gibt keinen Stacktrace und keine fachlichen Detailwerte aus.
Der Anwendungscode wurde bereits durchgelesen — er sieht unauffällig aus und
erhält von der Bibliothek nur ein generisches Ergebnisobjekt.

## Dein Auftrag

> Das Ergebnisobjekt `ReleaseAssessment` besitzt ein beschreibbares Feld
> `status`. Setze auf diesem Feld einen **Field Watchpoint für
> Schreibzugriffe** (IntelliJ: Field Breakpoint) und starte den Lauf im
> Debug-Modus. Finde heraus, wann und durch welchen Aufruf ein zunächst
> korrekter Status seinen Wert wieder verliert.

1. Starte den Freigabelauf und schau dir die Meldungen an.
2. Inspiziere das `ReleaseAssessment` einer betroffenen Änderung — was siehst
   du dort, und was verrät es dir (noch nicht)?
3. Setze auf `ReleaseAssessment.status` einen Field Watchpoint für
   Schreibzugriffe und starte den Debug-Lauf erneut.
4. Beobachte alle Schreibzugriffe auf das Feld für dieselbe Änderung.
5. Inspiziere bei jedem Treffer den Call Stack und die Argumente.
6. Benenne präzise, aus welchem Aufruf der entscheidende Schreibzugriff stammt
   und welche Laufzeitdaten ihn auslösen.

Der Lauf gilt als bestanden, wenn er nach der Korrektur eine Erfolgsmeldung
samt Badge ausgibt.

## Startinformationen

- Java 17, kein Framework, kein Server, kein Docker.
- Einstiegspunkt: `src/VerificationRunner.java` → `main()`.
- Öffentliche API der Bewertung:
  `ReleaseAssessmentService.assess(ChangeRequest request)` liefert ein
  `ReleaseAssessment` mit `getStatus()`.
- Der Lauf ist reproduzierbar: 10.000 Änderungen, fester Seed. Nummern,
  Änderungsarten, Risikoklassen und Zeitpunkte sind bei jedem Start identisch.
- Der Anwendungscode liegt vollständig in `src/`. Die Policy-Bibliothek liegt
  **nur als Jar** vor. Was sie tatsächlich verarbeitet, siehst du zur
  Laufzeit.

### IntelliJ-Setup

1. `File > Open…` und den Ordner `station-3` auswählen.
2. Falls nachgefragt: Project SDK auf ein **JDK 17** setzen
   (`File > Project Structure > Project > SDK`).
3. `src` ist als Source-Root konfiguriert, `lib/release-policy.jar` ist als
   Modul-Bibliothek eingetragen. Falls IntelliJ das nicht übernimmt, `src` als
   Sources Root markieren und das Jar als Dependency hinzufügen.
4. Die Run-Konfiguration **VerificationRunner** ist mitgeliefert.
5. Ausführen mit ▶ oder debuggen mit 🐞.

### Alternativ auf der Kommandozeile

```bash
cd station-3
./scripts/compile.sh
./scripts/run.sh
```

## Hinweis zum Vorgehen

Diese Station ist so gebaut, dass ein gewöhnlicher Breakpoint an der
Fehlermeldung nur den bereits falschen Endzustand zeigt. Ein **Field
Watchpoint** hält dagegen bei *jedem* Schreibzugriff auf das Feld an — und
zeigt dir damit sowohl den korrekten als auch den überschreibenden Zugriff
inklusive Call Stack.

Wichtig: Prüfe genau, wie viele Schreibzugriffe für ein und dieselbe Änderung
auf das Feld erfolgen, und aus welchem Code sie jeweils stammen.
