#!/usr/bin/env bash
# Erzeugt das verteilbare Teilnehmerpaket für Station 2.
#
# Enthalten ist ausschließlich der Inhalt von station-2/ ohne Build-Reste.
# Nicht enthalten: Instructor Notes, Bibliotheks-Sourcen, Katalogquelldateien,
# Instructor-Build-Skripte und die korrigierte Bibliotheksvariante.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
STATION="$ROOT/station-2"
OUT="${1:-$ROOT/dist}"

WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

PKG="$WORK/station-2"
mkdir -p "$PKG"

cp -r "$STATION/src" "$STATION/lib" "$STATION/scripts" "$PKG/"
cp "$STATION/README.md" "$STATION/.gitignore" "$STATION/station-2.iml" "$PKG/"
cp -r "$STATION/.idea" "$PKG/.idea"
rm -f "$PKG/.idea/workspace.xml"
# Das Packaging-Skript selbst nennt Instructor-Artefakte und gehört nicht ins Paket.
rm -f "$PKG/scripts/package-participant.sh"
mkdir -p "$PKG/build"

# Sicherheitsnetz: nichts aus dem Instructor-Bereich darf mitgehen.
find "$PKG" -name 'INSTRUCTOR*' -delete
find "$PKG" -name 'service-zones*.properties' -delete
find "$PKG" -name '*.class' -delete
find "$PKG" -path '*com/acme*' -name '*.java' -delete

mkdir -p "$OUT"
ARCHIVE="$OUT/station-2.tar.gz"
rm -f "$ARCHIVE"
tar -czf "$ARCHIVE" -C "$WORK" station-2

echo "Teilnehmerpaket erstellt: $ARCHIVE"
echo "Inhalt:"
(cd "$WORK" && find station-2 -type f | sort)
