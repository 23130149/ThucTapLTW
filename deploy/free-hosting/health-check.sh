#!/usr/bin/env bash
set -u

echo "== Services =="
systemctl --no-pager --full status nginx | sed -n '1,12p'
echo
systemctl --no-pager --full status tomcat10 | sed -n '1,16p'

echo
echo "== Listening ports =="
ss -lntp | grep -E ':(80|443|8080)\b' || true

echo
echo "== Tomcat webapps =="
ls -lah /var/lib/tomcat10/webapps || true

echo
echo "== Local HTTP checks =="
curl -I --max-time 10 http://127.0.0.1:8080/home || true
echo
curl -I --max-time 10 http://127.0.0.1/home || true

echo
echo "== Recent Tomcat log =="
journalctl -u tomcat10 -n 80 --no-pager || true

echo
echo "== Recent Nginx error log =="
sudo tail -n 80 /var/log/nginx/error.log || true
