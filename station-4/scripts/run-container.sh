#!/usr/bin/env bash
# Baut und startet den Release Validator im Docker-Container.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

cd "$STATION"

# Manche WSL2/Rancher-Desktop-Setups kennen nur das Bindestrich-Kommando.
if docker compose version >/dev/null 2>&1; then
  docker compose up --build
else
  docker-compose up --build
fi
