#!/usr/bin/env bash
# Startet die parallele Verarbeitung des Deployment-Batches.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

"$STATION/scripts/compile.sh" > /dev/null

java -cp "$STATION/build" DeploymentRunner
