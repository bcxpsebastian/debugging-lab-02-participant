#!/usr/bin/env bash
# Startet den Freigabelauf der Station.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

if [[ ! -f "$STATION/build/VerificationRunner.class" ]]; then
  "$STATION/scripts/compile.sh"
fi

java -cp "$STATION/build:$STATION/lib/release-policy.jar" VerificationRunner
