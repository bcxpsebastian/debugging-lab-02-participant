#!/usr/bin/env bash
# Startet das Deployment-Audit der Station.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

if ! "$STATION/scripts/compile.sh" > /dev/null; then
  echo "FEHLER: Die Station konnte nicht kompiliert werden. Bitte './scripts/compile.sh' direkt aufrufen." >&2
  exit 1
fi

status=0
java -cp "$STATION/build" DeploymentAuditApplication || status=$?
exit "$status"
