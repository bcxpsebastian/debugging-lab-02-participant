#!/usr/bin/env bash
# Regressionstest für Station 6 (ohne Build-Tools, nur JDK 17).
#
# Prüft den gelieferten fehlerhaften Ausgangszustand der Übung: Das verzögerte
# Deployment-Audit muss in jedem Lauf acht Change Requests laden, acht
# Audit-Einträge melden und dabei genau die Slots 1 bis 7 als Mismatch und
# ausschließlich Slot 8 als erfolgreich ausgeben.
#
# Hinweis: Nach einem korrekten Teilnehmer-Fix findet dieses Skript die
# Fehlersignatur erwartungsgemäß nicht mehr (siehe Instructor Notes). Für die
# Gegenprobe des Fixes dient 'instructor/station-6/verify-fixed.sh'.
#
# Die Quellen in station-6/src/ werden nicht verändert; kompiliert wird in ein
# temporäres Verzeichnis.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

RUNS=20
RUN_TIMEOUT_SECONDS=30

fail() { echo "FEHLGESCHLAGEN: $*" >&2; exit 1; }

count_matches() {
  grep -c -- "$1" "$2" || true
}

run_once() {
  local out="$1" status=0
  timeout "$RUN_TIMEOUT_SECONDS" java -cp "$WORK/build" DeploymentAuditApplication > "$out" 2>&1 || status=$?
  if [[ "$status" -eq 124 ]]; then
    fail "DeploymentAuditApplication lief länger als ${RUN_TIMEOUT_SECONDS}s und wurde per timeout abgebrochen."
  fi
  [[ "$status" -eq 0 ]] || fail "DeploymentAuditApplication endete mit Exit-Status $status."
}

check_baseline() {
  local out="$1" actual slot

  grep -q '^ACME Deployment Audit$' "$out" \
    || fail "Die Kopfzeile 'ACME Deployment Audit' fehlt."

  grep -q '^Loaded change requests: 8$' "$out" \
    || fail "Es wurden nicht genau acht Change Requests geladen."

  grep -q '^Deferred audit entries: 8$' "$out" \
    || fail "Der Audit-Sink enthält nicht genau acht Einträge."

  actual="$(count_matches '^❌ Audit slot ' "$out")"
  [[ "$actual" -eq 7 ]] || fail "Erwartet genau sieben fehlerhafte Audit-Slots, gemeldet wurden $actual."

  actual="$(count_matches '^✅ Audit slot ' "$out")"
  [[ "$actual" -eq 1 ]] || fail "Erwartet genau einen erfolgreichen Audit-Slot, gemeldet wurden $actual."

  for ((slot = 1; slot <= 7; slot++)); do
    grep -q "^❌ Audit slot $slot mismatch\$" "$out" \
      || fail "Audit-Slot $slot meldet keinen Mismatch."
  done

  grep -q '^✅ Audit slot 8 passed$' "$out" \
    || fail "Audit-Slot 8 wird nicht als erfolgreich gemeldet."

  grep -q '^Audit result: 1 passed, 7 failed$' "$out" \
    || fail "Die Zusammenfassung lautet nicht 'Audit result: 1 passed, 7 failed'."

  grep -q '^Deployment audit rejected\.$' "$out" \
    || fail "Die Abschlussmeldung 'Deployment audit rejected.' fehlt."

  actual="$(count_matches 'Ghost Logger' "$out")"
  [[ "$actual" -eq 0 ]] || fail "Der Badge 'Ghost Logger' wird im Ausgangszustand ausgegeben."

  actual="$(count_matches 'Exception' "$out")"
  [[ "$actual" -eq 0 ]] || fail "Die Ausgabe enthält eine ungefangene Exception."

  actual="$(count_matches '^	at ' "$out")"
  [[ "$actual" -eq 0 ]] || fail "Die Ausgabe enthält einen Stacktrace."
}

echo "== Kompilieren mit Java 17 =="
javac --release 17 -d "$WORK/build" "$STATION"/src/*.java \
  || fail "Die Station lässt sich nicht mit 'javac --release 17' kompilieren."
echo "ok"

echo
echo "== Lauf 1 von $RUNS =="
run_once "$WORK/run-1.txt"
cat "$WORK/run-1.txt"
check_baseline "$WORK/run-1.txt"

echo
echo "== Reproduzierbarkeit: Läufe 2 bis $RUNS =="
for ((run = 2; run <= RUNS; run++)); do
  run_once "$WORK/run-$run.txt"
  check_baseline "$WORK/run-$run.txt"
  printf '.'
done
printf '\n'

echo
echo "ALLE PRÜFUNGEN ERFOLGREICH ($RUNS/$RUNS fehlerhafte Ausgangsläufe reproduziert)"
