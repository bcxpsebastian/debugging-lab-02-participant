# Station 4 – Container Config Mirage

**Technik-Tag:** Remote JVM Debugging
**Geschätzte Dauer:** ca. 5 Minuten
**Badge:** `Remote Ranger`

## Story

Der Release-Validation-Service läuft lokal problemlos. Im Container werden
jedoch einige Change Requests als ungültig abgelehnt.

Das Deployment-Team behauptet, dieselbe Konfiguration wie lokal verwendet zu
haben. Finde heraus, welchen Wert der laufende Service tatsächlich
verarbeitet.

## Start

```bash
docker compose up --build
```

Falls dein Docker-Setup den Befehl `docker compose` (ohne Bindestrich) nicht
kennt – etwa bei manchen WSL2/Rancher-Desktop-Installationen –, nutze
stattdessen `docker-compose up --build` oder das Skript
`./scripts/run-container.sh`, das beide Varianten automatisch erkennt.

Der Container startet die Anwendung und aktiviert JDWP auf Port `5005`.
Die Verarbeitung beginnt bewusst mit Verzögerung – die Konsole nennt dir,
wie viel Zeit dir zum Attachen bleibt.

Nutze die mitgelieferte IntelliJ-Konfiguration **Attach to Release
Validator**, um dich während dieser Zeit an den laufenden Prozess zu hängen.

## Fallback ohne Docker

Falls auf deinem Rechner kein Docker verfügbar ist:

```bash
./scripts/run-debug-local.sh
```

Das Skript startet dieselbe Anwendung lokal mit JDWP auf Port `5005`.
Verwende danach unverändert die IntelliJ-Konfiguration **Attach to Release
Validator**.

Alternativ: Pairing an einem Docker-fähigen Rechner.

## Erfolgskriterium

Untersuche den laufenden Prozess, benenne die Ursache für die abgelehnten
Change Requests und behebe sie im Code. Starte den Container danach neu:

```bash
docker compose up --build
```

(bzw. `docker-compose up --build` / `./scripts/run-container.sh`, siehe oben)

Bei korrektem Fix laufen alle Change Requests fehlerfrei durch.
