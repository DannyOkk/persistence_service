#!/bin/sh
# Parse CONSUL_URL (e.g. "http://consul:8500") into CONSUL_HOST and CONSUL_PORT
if [ -n "$CONSUL_URL" ]; then
  stripped=$(echo "$CONSUL_URL" | sed 's|https\?://||')
  export CONSUL_HOST=$(echo "$stripped" | cut -d: -f1)
  export CONSUL_PORT=$(echo "$stripped" | cut -d: -f2)
fi
exec java -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar "$@"
