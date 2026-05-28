<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu - Handmade House</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/account.css">

    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>
<main class="about-us-container">
    <h1>Quên mật khẩu</h1>
    <c:if test="${not empty error}">
        <p style="color:red; text-align:center; margin-bottom: 15px;">
                ${error}
        </p>
    </c:if>
    <div class="password-box">
        <form action="${pageContext.request.contextPath}/ForgotPassword"
              method="post">
            <c:if test="${empty step}">
                <div class="form-row">
                    <label>Email đăng ký</label>
                    <input type="email"
                           name="email"
                           placeholder="Nhập Gmail của bạn"
                           required>
                </div>
                <div class="password-actions">
                    <button type="submit"
                            class="btn-save"
                            name="action"
                            value="sendOtp">
                        Gửi OTP
                    </button>
                    <a href="${pageContext.request.contextPath}/SignIn"
                       class="btn-back">
                        Quay lại
                    </a>
                </div>
            </c:if>
            <c:if test="${step == 'OTP_SENT'}">

                <div class="form-row">
                    <label>OTP</label>
                    <input type="text"
                           name="otp"
                           placeholder="Nhập mã OTP"
                           required>
                </div>
                <div class="form-row">
                    <label>Mật khẩu mới</label>
                    <input type="password"
                           name="newPassword"
                           required>
                </div>
                <div class="form-row">
                    <label>Nhập lại mật khẩu mới</label>
                    <input type="password"
                           name="confirmPassword"
                           required>
                </div>
                <c:if test="${resendRemain > 0}">
                    <div class="countdown">
                        Gửi lại OTP sau ${resendRemain} giây
                    </div>
                </c:if>
                <div class="password-actions">
                    <button type="submit"
                            class="btn-save"
                            name="action"
                            value="confirm">
                        Xác nhận
                    </button>
                    <button type="submit"
                            class="btn-back"
                            name="action"
                            value="sendOtp"
                        ${resendRemain > 0 ? "disabled" : ""}>
                        Gửi lại OTP
                    </button>
                </div>
            </c:if>
        </form>
    </div>
</main>
<jsp:include page="/jsp/footer.jsp"/>
</body>
</html>
