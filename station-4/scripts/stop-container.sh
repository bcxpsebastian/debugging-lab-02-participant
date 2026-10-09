#!/usr/bin/env bash
# Stoppt den Release-Validator-Container.
set -euo pipefail

STATION="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

cd "$STATION"

# Manche WSL2/Rancher-Desktop-Setups kennen nur das Bindestrich-Kommando.
if docker compose version >/dev/null 2>&1; then
  docker compose down
else
  docker-compose down
fi
