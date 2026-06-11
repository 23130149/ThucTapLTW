<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Đăng nhập</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/dangnhap.css">

  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">

  <script src="https://accounts.google.com/gsi/client" async defer></script>
</head>

<body>
<div class="wrapper">

  <% if (session.getAttribute("loginMessage") != null) { %>
  <p class="error-message">
    <%= session.getAttribute("loginMessage") %>
  </p>
  <% session.removeAttribute("loginMessage"); %>
  <% } %>

  <% if (request.getAttribute("error") != null) { %>
  <p class="error-message">
    <%= request.getAttribute("error") %>
  </p>
  <% } %>

  <form action="${pageContext.request.contextPath}/SignIn" method="post">

    <h1>Đăng Nhập</h1>

    <div class="input-box">
      <input type="text" placeholder="Email" name="email" required>
      <i class='bx bx-user'></i>
    </div>

    <div class="input-box">
      <input type="password" placeholder="Mật Khẩu" name="pass" required>
      <i class='bx bx-lock'></i>
    </div>

    <div class="remember-forgot">
      <label>
        <input type="checkbox">
        <span>Lưu đăng nhập</span>
      </label>
      <a href="${pageContext.request.contextPath}/ForgotPassword">Quên mật khẩu?</a>
    </div>

    <button type="submit" class="btn">Đăng Nhập</button>

    <div class="social-login">

      <div id="g_id_onload"
           data-client_id="1027811499981-o189kbf29m7ucr73kr6npqq7v6t6u494.apps.googleusercontent.com"
           data-login_uri="${pageContext.request.contextPath}/GoogleAuth"
           data-auto_prompt="false">
      </div>

      <div class="google-btn-wrap">
        <div class="g_id_signin"
             data-type="standard"
             data-size="large"
             data-theme="outline"
             data-text="signin_with"
             data-shape="rectangular"
             data-width="340"
             data-logo_alignment="left">
        </div>
      </div>

      <a href="${pageContext.request.contextPath}/login-facebook"
         class="social-btn fb">
        <i class='bx bxl-facebook-circle'></i>
        <span>Đăng nhập bằng Facebook</span>
      </a>

    </div>

    <div class="register-link">
      <p>
        Bạn chưa có tài khoản?
        <a href="${pageContext.request.contextPath}/Register">Đăng Ký</a>
      </p>
    </div>

  </form>
</div>
</body>
</html>
