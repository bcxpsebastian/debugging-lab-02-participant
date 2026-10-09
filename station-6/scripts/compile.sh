#!/usr/bin/env bash
# Kompiliert den Anwendungscode der Station mit JDK-Bordmitteln.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

if ! command -v javac > /dev/null 2>&1; then
  echo "FEHLER: 'javac' wurde nicht gefunden. Bitte ein JDK 17 oder neuer installieren." >&2
  exit 1
fi

if ! compgen -G "$STATION/src/*.java" > /dev/null; then
  echo "FEHLER: In '$STATION/src' liegen keine Java-Quellen." >&2
  exit 1
fi

rm -rf "$STATION/build"
mkdir -p "$STATION/build"

if ! javac --release 17 -d "$STATION/build" "$STATION"/src/*.java; then
  echo "FEHLER: Die Kompilierung ist fehlgeschlagen. Bitte die Meldungen von javac oben prüfen." >&2
  exit 1
fi

echo "kompiliert nach: $STATION/build"
