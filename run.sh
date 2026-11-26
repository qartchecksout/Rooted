#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "Starting Rooted via Embedded Tomcat..."
./mvnw compile exec:java
