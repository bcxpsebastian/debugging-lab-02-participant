#!/usr/bin/env bash
# Regressionstest für Station 5 (ohne Build-Tools, nur JDK 17).
#
# Prüft den gelieferten Ausgangszustand der Übung: Die parallele Verarbeitung
# des Batches muss bei jedem Lauf genau eine Doppelbelegung des Slots
# payments/prod/blue melden, verursacht von DPL-307 und DPL-428.
#
# Hinweis: Nach dem Teilnehmer-Fix findet dieses Skript die Fehlersignatur
# erwartungsgemäß nicht mehr (siehe Instructor Notes).
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

RUNS=20
RUN_TIMEOUT_SECONDS=30

UNIQUE_SLOTS=(catalog/staging/green accounts/prod/green search/prod/canary notifications/staging/blue)
ALL_REQUESTS=(DPL-104 DPL-219 DPL-307 DPL-311 DPL-428 DPL-509)

fail() { echo "FEHLGESCHLAGEN: $*" >&2; exit 1; }

count_matches() {
  grep -c -- "$1" "$2" || true
}

run_once() {
  local out="$1" status=0
  timeout "$RUN_TIMEOUT_SECONDS" java -cp "$WORK/build" DeploymentRunner > "$out" 2>&1 || status=$?
  if [[ "$status" -eq 124 ]]; then
    fail "DeploymentRunner lief länger als ${RUN_TIMEOUT_SECONDS}s und wurde abgebrochen."
  fi
  [[ "$status" -eq 0 ]] || fail "DeploymentRunner endete mit Exit-Status $status."
}

check_signature() {
  local out="$1" actual requests slot request

  grep -q '^Processing 6 deployment requests\.\.\.$' "$out" \
    || fail "Startmeldung der parallelen Verarbeitung fehlt."

  actual="$(count_matches '^SAFETY VIOLATION:' "$out")"
  [[ "$actual" -eq 1 ]] || fail "Erwartet genau eine Safety-Violation, gemeldet wurden $actual."

  grep -q '^SAFETY VIOLATION: payments/prod/blue was launched 2 times$' "$out" \
    || fail "Die erwartete Doppelbelegung von payments/prod/blue wird nicht gemeldet."

  requests="$(grep '^Requests: ' "$out")" || fail "Zeile mit den beteiligten Request-IDs fehlt."
  [[ "$requests" == *DPL-307* ]] || fail "DPL-307 wird nicht als beteiligter Request genannt."
  [[ "$requests" == *DPL-428* ]] || fail "DPL-428 wird nicht als beteiligter Request genannt."

  grep -q 'Deployment audit failed: exclusive slot guarantee violated\.' "$out" \
    || fail "Abschlussmeldung des Audits fehlt."

  actual="$(count_matches ' launching ' "$out")"
  [[ "$actual" -eq 6 ]] || fail "Erwartet 6 gestartete Deployments, gestartet wurden $actual."

  actual="$(count_matches ' launching payments/prod/blue$' "$out")"
  [[ "$actual" -eq 2 ]] || fail "payments/prod/blue wurde ${actual}-mal gestartet, erwartet: 2."

  for slot in "${UNIQUE_SLOTS[@]}"; do
    actual="$(count_matches " launching $slot\$" "$out")"
    [[ "$actual" -eq 1 ]] || fail "Slot $slot wurde ${actual}-mal gestartet, erwartet: 1."
  done

  for request in "${ALL_REQUESTS[@]}"; do
    grep -q "$request launching " "$out" || fail "Request $request wurde nicht gestartet."
  done
}

echo "== Kompilieren mit Java 17 =="
javac --release 17 -d "$WORK/build" "$STATION"/src/*.java
echo "ok"

echo
echo "== Lauf 1 von $RUNS =="
run_once "$WORK/run-1.txt"
cat "$WORK/run-1.txt"
check_signature "$WORK/run-1.txt"

echo
echo "== Reproduzierbarkeit: Läufe 2 bis $RUNS =="
for ((run = 2; run <= RUNS; run++)); do
  run_once "$WORK/run-$run.txt"
  check_signature "$WORK/run-$run.txt"
  printf '.'
done
printf '\n'

echo
echo "ALLE PRÜFUNGEN ERFOLGREICH ($RUNS Läufe mit identischer Fehlersignatur)"
