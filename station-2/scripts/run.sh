#!/usr/bin/env bash
# Startet den Prüflauf der Station.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

if [[ ! -f "$STATION/build/MaintenanceWindowRunner.class" ]]; then
  "$STATION/scripts/compile.sh"
fi

java -cp "$STATION/build:$STATION/lib/maintenance-policy.jar" MaintenanceWindowRunner
