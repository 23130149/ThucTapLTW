# Cloudflare Tunnel Chay Tu Laptop

Cach nay dung khi can demo free nhanh ma chua co VPS.

## Nhanh Nhat: Quick Tunnel

1. Chay Tomcat local, dam bao web vao duoc:

```text
http://localhost:8080/
```

2. Cai `cloudflared`.

3. Chay:

```bash
cloudflared tunnel --url http://localhost:8080
```

Cloudflare se tra ve mot URL dang:

```text
https://something.trycloudflare.com
```

Dung URL nay de demo.

## Neu Muon Dung Domain .id.vn

Can dua domain ve Cloudflare DNS hoac tao named tunnel trong Cloudflare Zero Trust.

Y tuong:

```text
handmadehouse.id.vn -> Cloudflare Tunnel -> localhost:8080
```

Nhuoc diem:

- Laptop phai mo lien tuc.
- Mang nha mat la web mat.
- Khong nen dung ban that cho khach.

## Khi Dung reCAPTCHA

Them domain tunnel vao Google reCAPTCHA console:

```text
trycloudflare.com
```

hoac domain that:

```text
handmadehouse.id.vn
www.handmadehouse.id.vn
```
