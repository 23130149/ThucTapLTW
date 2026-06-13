# Deploy Free 100% Cho Do An

Bo nay chuan bi 2 cach deploy:

1. **Khuyen dung:** `.id.vn` mien phi + Oracle Cloud Always Free + Tomcat/MySQL/Nginx.
2. **Demo nhanh:** `.id.vn`/URL tam + laptop cua ban + Cloudflare Tunnel.

## Cach 1: Oracle Cloud Always Free

Dung cach nay neu muon web chay that 24/7 ma khong phu thuoc laptop.

### Can chuan bi

- Ten mien `.id.vn` mien phi.
- Tai khoan Oracle Cloud Free Tier.
- VPS Ubuntu tren Oracle Cloud.
- Source code project da push len GitHub.
- File SQL database cua project.
- Key/token production:
  - reCAPTCHA site key/secret key
  - GHN token/shop id neu dung GHN that
  - SMTP email
  - VNPAY neu dung thanh toan that

### Tren Oracle Cloud

Tao VM Ubuntu, mo cong:

- `22`: SSH
- `80`: HTTP
- `443`: HTTPS

Neu can test truc tiep Tomcat thi mo them `8080`, nhung production nen chi public qua Nginx cong 80/443.

### Cai server

SSH vao VPS, copy thu muc project hoac clone GitHub, sau do chay:

```bash
chmod +x deploy/free-hosting/oracle-ubuntu-setup.sh
./deploy/free-hosting/oracle-ubuntu-setup.sh
```

Script se cai:

- Java 21
- Tomcat 10
- MySQL
- Nginx
- Certbot
- Git

### Cau hinh Tomcat

Mo file:

```bash
sudo nano /etc/default/tomcat10
```

Copy noi dung tu:

```text
deploy/free-hosting/tomcat10-env.example
```

Doi cac gia tri `CHANGE_ME`.

Project da duoc chinh `DBProperties` de uu tien doc config tu VM options/system properties/env. Local van doc `src/main/resources/db.properties` nhu cu.

### Import database

Vi du:

```bash
sudo mysql
```

```sql
CREATE DATABASE project CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'handmade_user'@'localhost' IDENTIFIED BY 'CHANGE_ME_STRONG_PASSWORD';
GRANT ALL PRIVILEGES ON project.* TO 'handmade_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

Import file SQL:

```bash
mysql -u handmade_user -p project < database.sql
```

### Build va deploy WAR

Tu root project tren server:

```bash
chmod +x deploy/free-hosting/deploy-war.sh
./deploy/free-hosting/deploy-war.sh
```

Script build WAR va copy thanh:

```text
/var/lib/tomcat10/webapps/ROOT.war
```

Nhu vay domain se vao thang app root, khong bi them `/thuctap`.

### Cau hinh Nginx

Copy template:

```bash
sudo cp deploy/free-hosting/nginx-site.conf.template /etc/nginx/sites-available/handmade-house
sudo nano /etc/nginx/sites-available/handmade-house
```

Doi:

```text
YOUR_DOMAIN.id.vn
```

thanh domain that.

Bat site:

```bash
sudo ln -s /etc/nginx/sites-available/handmade-house /etc/nginx/sites-enabled/handmade-house
sudo nginx -t
sudo systemctl reload nginx
```

### Tro DNS

Trong trang quan ly `.id.vn`, tao record:

```text
A    @      IP_VPS
A    www    IP_VPS
```

Cho DNS cap nhat.

### Bat HTTPS

Sau khi domain tro dung IP:

```bash
sudo certbot --nginx -d YOUR_DOMAIN.id.vn -d www.YOUR_DOMAIN.id.vn
```

## Cach 2: Cloudflare Tunnel Chay Tu Laptop

Dung de demo nhanh khi khong tao duoc Oracle Cloud.

Doc:

```text
deploy/free-hosting/cloudflare-tunnel-local.md
```

Nhuoc diem: laptop tat la web tat.

## Luu Y Bao Mat

- Khong commit `db.properties` co token/password that len GitHub public.
- Neu token GHN da tung push public, nen doi token tren GHN.
- reCAPTCHA secret key chi de server doc, khong dua vao JSP.
- Sau khi deploy len domain that, them domain vao Google reCAPTCHA console.

## Kiem Tra Sau Deploy

- `https://YOUR_DOMAIN.id.vn/home`
- Dang nhap + reCAPTCHA.
- Lien he + reCAPTCHA.
- Dang ky gui OTP.
- Quen mat khau gui OTP.
- Them dia chi, tinh phi ship GHN/fallback.
- Dat hang.
- Admin tao/sync GHN neu dung.
