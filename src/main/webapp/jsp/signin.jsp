<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Đăng nhập</title>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/dangnhap.css">
  <link href="https://cdn.boxicons.com/fonts/basic/boxicons.min.css" rel="stylesheet">
  <script src="https://accounts.google.com/gsi/client" async defer></script>
</head>

<body>
<div class="wrapper">

  <% if (request.getAttribute("error") != null) { %>
  <p style="color:red; text-align:center; margin-bottom: 10px;">
    <%= request.getAttribute("error") %>
  </p>
  <% } %>

  <form action="${pageContext.request.contextPath}/SignIn"
        method="post">

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
      <label><input type="checkbox"> Lưu đăng nhập</label>
      <a href="${pageContext.request.contextPath}/ForgotPassword">
        Quên mật khẩu?
      </a>

    </div>

    <button type="submit" class="btn">Đăng Nhập</button>
    <div style="margin-top:15px; text-align:center;">
      <div class="g_id_signin"
           data-type="standard"
           data-size="large"
           data-theme="outline"
           data-text="signin_with"
           data-shape="rectangular"
           data-logo_alignment="left">
      </div>
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
<div id="g_id_onload"
     data-client_id="1027811499981-o189kbf29m7ucr73kr6npqq7v6t6u494.apps.googleusercontent.com"
     data-login_uri="${pageContext.request.contextPath}/GoogleAuth"
     data-auto_prompt="false">
</div>

</html>
