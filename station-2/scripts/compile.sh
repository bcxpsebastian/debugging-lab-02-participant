#!/usr/bin/env bash
# Kompiliert den Anwendungscode der Station gegen die Policy-Bibliothek.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

rm -rf "$STATION/build"
mkdir -p "$STATION/build"

javac --release 17 \
  -cp "$STATION/lib/maintenance-policy.jar" \
  -d "$STATION/build" \
  "$STATION"/src/*.java

echo "kompiliert nach: $STATION/build"
