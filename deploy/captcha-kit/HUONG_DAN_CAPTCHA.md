# Huong dan gan Google reCAPTCHA v2

Goi nay gom cac file lien quan den captcha trong project:

- `src/main/java/util/RecaptchaUtil.java`
- Controller: dang nhap, dang ky, quen mat khau, lien he, chi tiet san pham, gui danh gia
- JSP: `signin.jsp`, `register.jsp`, `forgotpassword.jsp`, `contact.jsp`, `productDetail.jsp`
- CSS hien thi captcha
- `build.gradle` de dam bao co thu vien Gson

## 1. Tao key reCAPTCHA

1. Vao trang quan ly Google reCAPTCHA.
2. Tao site moi, chon loai `reCAPTCHA v2` -> `I'm not a robot Checkbox`.
3. Them domain dang dung:
   - Local: `localhost`
   - Khi deploy: them domain hosting thuc te.
4. Lay 2 gia tri:
   - `Site key`
   - `Secret key`

## 2. Cau hinh key

Co 2 cach cau hinh. Nen dung bien moi truong khi deploy.

### Cach A: Bien moi truong

Dat:

```text
RECAPTCHA_SITE_KEY=site_key_cua_ban
RECAPTCHA_SECRET_KEY=secret_key_cua_ban
```

Neu chay trong IntelliJ/Tomcat, them 2 bien nay vao Run Configuration cua server.

### Cach B: `web.xml`

Them vao trong the `<web-app>`:

```xml
<context-param>
    <param-name>recaptcha.siteKey</param-name>
    <param-value>site_key_cua_ban</param-value>
</context-param>

<context-param>
    <param-name>recaptcha.secretKey</param-name>
    <param-value>secret_key_cua_ban</param-value>
</context-param>
```

Khong nen commit secret key len GitHub neu repo cong khai.

## 3. Cac man hinh da co captcha

- Dang nhap: captcha hien khi can xac minh.
- Dang ky: captcha khi gui dang ky va gui lai OTP.
- Quen mat khau: captcha khi gui OTP va gui lai OTP.
- Lien he: captcha khi gui lien he.
- Danh gia san pham: captcha khi gui danh gia.

Neu chua cau hinh key, captcha tu dong khong hien va server se bo qua buoc xac minh de de chay local.

## 4. Kiem tra

1. Cau hinh `RECAPTCHA_SITE_KEY` va `RECAPTCHA_SECRET_KEY`.
2. Rebuild/restart server.
3. Mo cac trang dang ky, quen mat khau, lien he hoac chi tiet san pham.
4. Tick captcha roi submit form.
5. Thu submit khi chua tick captcha, he thong phai bao loi xac minh.

## 5. Luu y loi thuong gap

- Captcha khong hien: sai site key, thieu script Google, hoac domain chua duoc khai bao trong Google reCAPTCHA.
- Submit luon bi loi: sai secret key, server khong goi duoc internet toi Google, hoac token captcha da het han.
- Local chay duoc nhung deploy loi: can them domain deploy vao cau hinh reCAPTCHA cua Google.
