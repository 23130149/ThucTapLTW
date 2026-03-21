<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Đăng Ký</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/dangky.css">
  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
  <c:if test="${empty step}">
  <form action="${pageContext.request.contextPath}/Register"
        method="post"
        id="registerForm">

    <input type="hidden" name="action" value="sendOtp">

    <h1>Đăng Ký</h1>

    <c:if test="${not empty error}">
      <p style="color:red;text-align:center">${error}</p>
    </c:if>

    <div class="input-box">

      <div class="input-field">
        <input type="text" name="fullName" placeholder="Họ Tên" required>
        <i class='bx bx-user'></i>
      </div>
      <p id="nameMsg" class="msg"></p>

      <div class="input-field">
        <input type="email" name="email" placeholder="Email" required>
        <i class='bx bx-envelope'></i>
      </div>
      <p id="emailMsg" class="msg"></p>

      <div class="input-field">
        <input type="password" name="password" placeholder="Mật khẩu" required>
        <i class='bx bx-lock'></i>
      </div>
      <p id="passwordMsg" class="msg"></p>

      <div class="input-field">
        <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu" required>
        <i class='bx bx-lock'></i>
      </div>
      <p id="confirmMsg" class="msg"></p>

    </div>

    <button type="submit" class="btn">Đăng Ký</button>
  </form>
  </c:if>

  <c:if test="${step eq 'OTP_SENT'}">

  <form action="${pageContext.request.contextPath}/Register"
        method="post"
        class="otp-form">

    <input type="hidden" name="action" value="confirmOtp">

    <h1 class="otp-title">Xác nhận OTP</h1>

    <c:if test="${not empty error}">
      <p style="color:red; text-align:center; margin-bottom:10px;">
          ${error}
      </p>
    </c:if>
    <p class="otp-desc">
      Mã OTP đã được gửi đến email của bạn
    </p>

    <div class="otp-timer">
      OTP hết hạn sau <span id="time">120</span>s
    </div>

    <div class="otp-input-box">
      <input type="text" name="otp" maxlength="6" placeholder="••••••" required>
    </div>

    <div class="otp-actions">
      <button type="submit" class="btn">Xác nhận</button>

      <button type="button"
              id="resendBtn"
              class="resend-btn"
              disabled>
        Gửi lại OTP (<span id="resendTime">30</span>s)
      </button>
    </div>
  </form>

  <form action="${pageContext.request.contextPath}/Register"
        method="post"
        id="resendForm">
    <input type="hidden" name="action" value="sendOtp">
  </form>

  </c:if>

  </head>