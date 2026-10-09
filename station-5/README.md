# Station 5 – Deployment Slot Doppelbuchung

**Technik-Tag:** Multi-Thread-Debugging in IntelliJ
**Geschätzte Dauer:** ca. 10–15 Minuten
**Badge:** `Thread Tamer`

## Story

Das Deployment-System garantiert, dass jeder Deployment-Slot exklusiv von
genau einem Change Request belegt wird.

Trotzdem wurden heute Nacht zwei Releases gleichzeitig in denselben
produktiven Slot gestartet.

Alle beteiligten Komponenten verwenden thread-sichere Datenstrukturen. Warum
kam es dennoch zur Doppelbelegung?

## Start

Öffne den Ordner `station-5/` in IntelliJ und starte die mitgelieferte
Konfiguration **Debug Deployment Runner**.

Entry Point ist:

```text
src/DeploymentRunner.java  →  DeploymentRunner.main(...)
```

Alternativ ohne IDE:

```bash
./scripts/run.sh
```

Es wird kein Docker und keine externe Bibliothek benötigt – Java 17 genügt.

## Fehlersymptom

Der Lauf verarbeitet sechs Deployment Requests parallel und endet
reproduzierbar so:

```text
Deployment audit: 6 deployments started for 5 slots.
SAFETY VIOLATION: payments/prod/blue was launched 2 times
Requests: DPL-307, DPL-428
❌ Deployment audit failed: exclusive slot guarantee violated.
```

Die Reihenfolge der Konsolenzeilen schwankt von Lauf zu Lauf – die gemeldete
Doppelbelegung bleibt dieselbe.

## Dein Auftrag

1. Reproduziere den Fehler.
2. Untersuche im Debugger, welche Threads den betroffenen Slot bearbeiten.
   Jeder Request läuft in einem eigenen Thread `deployment-worker-*`.
3. Nutze dafür die **Threads**-Ansicht im Debug-Fenster (Tab *Threads* bzw.
   *Frames*-Dropdown) und nicht nur den gerade angehaltenen Thread.
4. Probiere beide Breakpoint-Varianten aus. Rechtsklick auf den Breakpoint →
   *More* (oder `Ctrl+Shift+F8`) → **Suspend policy**:
   - **All** – hält alle Threads an,
   - **Thread** – hält nur den Thread an, der den Breakpoint erreicht.

   Vergleiche, was du jeweils über den Fortschritt der übrigen Worker
   erkennen kannst. Mit **Suspend: Thread** kannst du einzelne Threads
   gezielt weiterlaufen lassen (*Resume* bei ausgewähltem Thread) und dabei
   die lokalen Variablen mehrerer Worker nebeneinander vergleichen.
5. Benenne, welche fehlerhafte Interaktion zwischen den Threads zur
   Doppelbelegung führt.
6. Baue einen kleinen, threadsicheren Fix ein.

## Erfolgskriterium

Du hast die Station geschafft, wenn du erklären kannst, warum zwei Threads
dieselbe Entscheidung für denselben Slot treffen konnten – und wenn der Lauf
danach so endet:

```text
✅ Deployment audit passed: every slot stayed exclusive.
The deployment train stayed on one track.
🏅 Badge unlocked: Thread Tamer
```

Nicht erlaubt ist dabei:

- die Verarbeitung sequentiell machen oder den Thread-Pool verkleinern,
- einen der sechs Requests entfernen oder die Testdaten ändern,
- `Thread.sleep` einbauen,
- die Meldung des Audits unterdrücken.

Die sechs Requests sollen weiterhin parallel verarbeitet werden.
