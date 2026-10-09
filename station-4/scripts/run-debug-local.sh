#!/usr/bin/env bash
# Fallback ohne Docker: startet den Release Validator lokal mit JDWP auf
# Port 5005 und derselben Konfiguration wie im Container.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD="$STATION/build"

rm -rf "$BUILD"
mkdir -p "$BUILD"
javac --release 17 -d "$BUILD" "$STATION"/src/*.java

export RELEASE_REGION=EU-CENTRAL
exec java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
  -cp "$BUILD" ValidationRunner
