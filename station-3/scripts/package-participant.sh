#!/usr/bin/env bash
# Erzeugt das verteilbare Teilnehmerpaket für Station 3.
#
# Enthalten ist ausschließlich der Inhalt von station-3/ ohne Build-Reste.
# Nicht enthalten: Instructor Notes, Bibliotheks-Sourcen, Katalogquelldateien,
# Instructor-Build-Skripte und die korrigierte Bibliotheksvariante.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
STATION="$ROOT/station-3"
OUT="${1:-$ROOT/dist}"

WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

PKG="$WORK/station-3"
mkdir -p "$PKG"

cp -r "$STATION/src" "$STATION/lib" "$STATION/scripts" "$PKG/"
cp "$STATION/README.md" "$STATION/.gitignore" "$STATION/station-3.iml" "$PKG/"
cp -r "$STATION/.idea" "$PKG/.idea"
rm -f "$PKG/.idea/workspace.xml"
# Das Packaging-Skript selbst nennt Instructor-Artefakte und gehört nicht ins Paket.
rm -f "$PKG/scripts/package-participant.sh"
mkdir -p "$PKG/build"

# Sicherheitsnetz: nichts aus dem Instructor-Bereich darf mitgehen.
find "$PKG" -name 'INSTRUCTOR*' -delete
find "$PKG" -name 'manual-review-catalog*.properties' -delete
find "$PKG" -name '*.class' -delete

mkdir -p "$OUT"
ARCHIVE="$OUT/station-3.tar.gz"
rm -f "$ARCHIVE"
tar -czf "$ARCHIVE" -C "$WORK" station-3

echo "Teilnehmerpaket erstellt: $ARCHIVE"
echo "Inhalt:"
(cd "$WORK" && find station-3 -type f | sort)
