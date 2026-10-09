# Station 1 – Der nächtliche Abrechnungslauf

## Story

Bei der ACME Trading GmbH läuft jede Nacht ein Abrechnungslauf über alle
Bestellungen des Tages. Die Mengenrabatte werden dabei aus dem zentralen
Preis-Katalog gelesen — einer Bibliothek, die ein anderes Team pflegt und die
euch nur als `lib/discount-catalog.jar` ausgeliefert wird.

Seit dem letzten Release meldet der Kontrolllauf einzelne Bestellungen mit
falschem Rabatt. Der Fachbereich beharrt darauf, dass die offizielle
Rabatt-Spezifikation in `spec/pricing-spec.properties` unverändert gilt und
korrekt ist. Der Anwendungscode wurde bereits mehrfach durchgelesen — er sieht
unauffällig aus.

## Rabatt-Spezifikation des Fachbereichs

| Menge         | Rabatt |
|---------------|--------|
| 0 – 99        | 0 %    |
| 100 – 499     | 5 %    |
| 500 – 999     | 10 %   |
| 1.000 – 4.999 | 15 %   |
| ab 5.000      | 20 %   |

## Dein Auftrag

1. Starte den Kontrolllauf und schau dir die Fehlermeldungen an.
2. Finde heraus, **welche Bestellungen** betroffen sind und **was sie gemeinsam haben**.
3. Finde die Ursache der Abweichung — mit dem Debugger, nicht durch Raten.
4. Benenne präzise, welcher Wert an welcher Stelle korrigiert werden muss.

Der Lauf gilt als bestanden, wenn er nach der Korrektur des Katalogs eine
Erfolgsmeldung samt Badge ausgibt.

## Startinformationen

- Java 17, kein Framework, kein Server, kein Docker.
- Einstiegspunkt: `src/VerificationRunner.java` → `main()`.
- Öffentliche API der Berechnung: `DiscountCalculator.calculateDiscount(int quantity)`.
- Der Lauf ist reproduzierbar: 10.000 Bestellungen, fester Seed, Mengen von 1 bis 10.000.
  Bestellnummern und Mengen sind bei jedem Start identisch.
- Der Anwendungscode liegt vollständig in `src/`. Der Preis-Katalog liegt
  **nur als Jar** vor — ohne Sourcen, ohne Debug-Symbole. Was er tatsächlich
  liefert, siehst du zur Laufzeit.

### IntelliJ-Setup

1. `File > Open…` und den Ordner `station-1` auswählen.
2. Falls nachgefragt: Project SDK auf ein **JDK 17** setzen
   (`File > Project Structure > Project > SDK`).
3. `src` ist als Source-Root konfiguriert, `lib/discount-catalog.jar` ist als
   Modul-Bibliothek eingetragen.
4. Die Run-Konfiguration **VerificationRunner** ist mitgeliefert.
   Wichtig: Das Arbeitsverzeichnis muss der Projektordner `station-1` sein,
   damit `spec/pricing-spec.properties` gefunden wird.
5. Ausführen mit ▶ oder debuggen mit 🐞.

### Alternativ auf der Kommandozeile

```bash
cd station-1
javac -cp lib/discount-catalog.jar -d out src/*.java
java -cp out:lib/discount-catalog.jar VerificationRunner
```

## Hinweis zum Vorgehen

Diese Station ist so gebaut, dass reines Lesen des Anwendungscodes nicht
ausreicht. Nutze die Werkzeuge des Debuggers, um dir anzusehen, welche Daten
zur Laufzeit tatsächlich verarbeitet werden — und zwar gezielt für den Fall,
der schiefgeht.
