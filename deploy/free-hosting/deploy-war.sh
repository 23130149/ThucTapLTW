#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$PROJECT_DIR"

./gradlew clean war

WAR_FILE="$(find build/libs -maxdepth 1 -name '*.war' | head -n 1)"
if [ -z "$WAR_FILE" ]; then
  echo "WAR file not found in build/libs"
  exit 1
fi

sudo systemctl stop tomcat10
sudo rm -rf /var/lib/tomcat10/webapps/ROOT /var/lib/tomcat10/webapps/ROOT.war
sudo cp "$WAR_FILE" /var/lib/tomcat10/webapps/ROOT.war
sudo chown tomcat:tomcat /var/lib/tomcat10/webapps/ROOT.war || true
sudo systemctl start tomcat10

echo "Deployed $WAR_FILE to /var/lib/tomcat10/webapps/ROOT.war"
echo "Check: http://SERVER_IP:8080/"
