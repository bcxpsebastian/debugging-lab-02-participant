#!/usr/bin/env bash
# Regressionstest für Station 4 (ohne Docker).
#
# Prüft:
#   1. Lokale Konfiguration -> keine Fehlermeldung, Erfolgsausgabe + Badge.
#   2. Container-Konfiguration -> genau die drei erwarteten Meldungen.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

fail() { echo "FEHLGESCHLAGEN: $*" >&2; exit 1; }

javac --release 17 -d "$WORK/build" "$STATION"/src/*.java

run_with_region() {
  local region="$1" out="$2"
  RELEASE_REGION="$region" java -Dvalidator.startupDelaySeconds=0 -cp "$WORK/build" ValidationRunner > "$out"
}

echo "== Variante: lokale Konfiguration =="
run_with_region "eu-central" "$WORK/local.txt"
cat "$WORK/local.txt"
grep -q 'could not be validated' "$WORK/local.txt" && fail "Lokale Konfiguration meldet unerwartet Fehler."
grep -q 'All 10000 change requests were validated' "$WORK/local.txt" \
  || fail "Erfolgsausgabe fehlt bei lokaler Konfiguration."
grep -q 'Remote Ranger' "$WORK/local.txt" || fail "Badge-Ausgabe fehlt bei lokaler Konfiguration."

echo
echo "== Variante: Container-Konfiguration =="
run_with_region "EU-CENTRAL" "$WORK/container.txt"
cat "$WORK/container.txt"

expected=$'Change request 1104 could not be validated\nChange request 1603 could not be validated\nChange request 9918 could not be validated\n3 of 10000 change requests could not be validated.'
actual="$(grep -E 'could not be validated' "$WORK/container.txt")"
[[ "$actual" == "$expected" ]] \
  || fail "Ausgabe der Container-Konfiguration weicht vom erwarteten Fehlerzustand ab."

echo
echo "ALLE PRÜFUNGEN ERFOLGREICH"
