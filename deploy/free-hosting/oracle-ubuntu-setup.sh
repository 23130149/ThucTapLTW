#!/usr/bin/env bash
set -euo pipefail

sudo apt update
sudo apt install -y \
  openjdk-21-jdk \
  tomcat10 \
  nginx \
  mysql-server \
  certbot \
  python3-certbot-nginx \
  git \
  curl \
  unzip

sudo systemctl enable --now mysql
sudo systemctl enable --now tomcat10
sudo systemctl enable --now nginx

sudo mkdir -p /opt/handmade-house
sudo chown "$USER":"$USER" /opt/handmade-house

if command -v ufw >/dev/null 2>&1; then
  sudo ufw allow OpenSSH || true
  sudo ufw allow 'Nginx Full' || true
fi

echo "Done. Next:"
echo "1. Configure /etc/default/tomcat10 using deploy/free-hosting/tomcat10-env.example"
echo "2. Import MySQL database"
echo "3. Run deploy/free-hosting/deploy-war.sh from project root"
echo "4. Configure Nginx and Certbot"
