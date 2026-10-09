# Station 6 – Die Geister im Deployment-Audit

**Technik-Tag:** Nicht unterbrechende Logging-Breakpoints in IntelliJ
**Geschätzte Dauer:** ca. 10–15 Minuten
**Schwierigkeit:** Mittel
**Badge:** `Ghost Logger`

## Story

Das Deployment-Audit der ACME Trading GmbH meldet nach einem Release-Lauf
mehrere falsche Zuordnungen.

Die eingelesenen Change Requests sind korrekt. Trotzdem scheinen im verzögert
ausgeführten Audit fast alle Deployment-Ereignisse durch dasselbe Ereignis
ersetzt worden zu sein.

Der Anwendungscode wurde bereits oberflächlich geprüft und sieht unauffällig
aus.

## Voraussetzungen

- Java 17 (oder neuer)
- IntelliJ IDEA

Es wird kein Docker, kein Maven/Gradle und keine externe Bibliothek benötigt.

## Start

Öffne den Ordner `station-6/` in IntelliJ und starte die mitgelieferte
Konfiguration **DeploymentAuditApplication**.

Entry Point ist:

```text
src/DeploymentAuditApplication.java  →  DeploymentAuditApplication.main(...)
```

Alternativ ohne IDE:

```bash
./scripts/run.sh
```

## Fehlersymptom

Der Lauf endet reproduzierbar so:

```text
ACME Deployment Audit
Loaded change requests: 8
Deferred audit entries: 8

❌ Audit slot 1 mismatch
❌ Audit slot 2 mismatch
❌ Audit slot 3 mismatch
❌ Audit slot 4 mismatch
❌ Audit slot 5 mismatch
❌ Audit slot 6 mismatch
❌ Audit slot 7 mismatch
✅ Audit slot 8 passed

Audit result: 1 passed, 7 failed
Deployment audit rejected.
```

Acht Change Requests werden geladen, acht Einträge landen im Audit – und
sieben Slots passen trotzdem nicht zur Erwartung. Die Konsolenausgabe sagt
bewusst nicht, welche Werte tatsächlich verarbeitet wurden.

## Dein Auftrag

1. Reproduziere den Fehler.
2. Beobachte den Zustand der Ereignisse **beim Einreihen** und **beim
   späteren Audit**.
3. Beobachte dabei **ohne den Programmablauf anzuhalten** und **ohne
   Diagnoseausgaben in den Java-Code einzubauen**. Kein zusätzliches
   `System.out.println(...)`, kein temporäres Logging, keine Hilfsfelder – die
   Produktion würde solche Änderungen nicht akzeptieren.
4. Erkläre, wodurch die falschen Zuordnungen entstehen.
5. Implementiere einen fachlich sauberen Fix und führe das Audit erneut aus.

### Werkzeug dieser Station

IntelliJ kann einen ganz normalen Line Breakpoint so konfigurieren, dass er
den Thread **nicht** anhält und stattdessen einen Java-Ausdruck auswertet und
ausgibt.

Der Klickpfad dafür:

```text
Rechtsklick auf den Breakpoint  →  More  (bzw. Ctrl+Shift+F8)
  →  Suspend  ausschalten
  →  Evaluate and log  einschalten und einen Ausdruck eintragen
```

Shortcuts können je nach Keymap und Betriebssystem abweichen.

So entsteht im Konsolenfenster eine fortlaufende Spur über den gesamten Lauf,
die man anschließend als Ganzes vergleichen kann. Setze die Spur an mehr als
einer Stelle des Datenflusses – interessant ist nicht nur ein einzelner
Zeitpunkt, sondern der Vergleich zweier Zeitpunkte.

Ein normaler, anhaltender Breakpoint würde den Lauf achtfach unterbrechen und
die Ereignisfolge zerreißen. Genau das soll hier vermieden werden.

## Erfolgskriterium

Du hast die Station geschafft, wenn du

- mindestens einen nicht unterbrechenden Logging-Breakpoint verwendet hast,
- die Ursache der falschen Zuordnungen benennen kannst,
- sie von der bloßen Tatsache unterscheiden kannst, dass das Audit verzögert
  läuft,
- den Fehler behoben hast, ohne die Testdaten, die Erwartungswerte oder den
  Prüfumfang des Audits anzutasten,

und der Lauf danach so endet:

```text
Audit result: 8 passed, 0 failed
Deployment audit accepted.
🏅 Badge unlocked: Ghost Logger
```

Nicht erlaubt ist dabei:

- das Audit direkt nach jedem Einreihen ausführen, statt es verzögert zu
  lassen,
- einzelne Change Requests, Slots oder Prüfungen aus dem Audit entfernen,
- die Erwartungswerte an das beobachtete Verhalten anpassen,
- einzelne Change-Request-IDs sonderbehandeln,
- `Thread.sleep(...)` einbauen,
- Diagnoseausgaben dauerhaft im Anwendungscode stehen lassen.

Dein Fix muss für beliebige zukünftige Change Requests funktionieren, nicht
nur für diese acht Datensätze.

## Selbstkontrolle

```bash
./scripts/verify.sh
```

**Wichtig:** `verify.sh` prüft den **ausgelieferten fehlerhaften
Ausgangszustand** – also ob die Übung überhaupt korrekt vor dir liegt
(acht geladene Change Requests, Slots 1–7 fehlerhaft, Slot 8 erfolgreich,
kein Badge).

Solange du noch nichts geändert hast, muss das Skript durchlaufen und mit
`ALLE PRÜFUNGEN ERFOLGREICH` enden.

Nach einem erfolgreichen Fix schlägt `verify.sh` **erwartungsgemäß fehl**
(z. B. „Erwartet genau sieben fehlerhafte Audit-Slots, gemeldet wurden 0“).
Das ist kein Defekt des Skripts und kein Fehler deines Fixes – es bedeutet,
dass die Fehlersignatur verschwunden ist. Dein eigentlicher Nachweis ist die
Ausgabe des Laufs mit acht erfolgreichen Slots und dem Badge.

Weitere Skripte:

| Skript | Zweck |
|--------|-------|
| `scripts/compile.sh` | Kompiliert die Station nach `build/`. |
| `scripts/run.sh` | Kompiliert und startet das Deployment-Audit ohne IDE. |
| `scripts/verify.sh` | Prüft den fehlerhaften Ausgangszustand (siehe oben). |
