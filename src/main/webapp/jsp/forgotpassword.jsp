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
<footer class="footer">
    <div class="container">
        <div class="footer-content">

            <div class="footer-column">
                <h3 class="footer-logo">Handmade House</h3>
                <p class="footer-desc">
                    Chào mừng đến với Handmade House, ngôi nhà nhỏ của những tâm hồn yêu nghệ thuật và thủ công.
                </p>
                <div class="social-links">
                    <a href="#"><i class="bx bxl-facebook"></i></a>
                    <a href="#"><i class="bx bxl-instagram"></i></a>
                    <a href="#"><i class="bx bxl-tiktok"></i></a>
                </div>
            </div>

            <div class="footer-column">
                <h3 class="footer-title">Blog</h3>
                <ul class="footer-links">
                    <li><a href="#">Câu chuyện thương hiệu</a></li>
                    <li><a href="#">Giá trị & Triết lý</a></li>
                    <li><a href="#">Quy trình sản xuất</a></li>
                    <li><a href="#">Định hướng bền vững</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h3 class="footer-title">Hỗ trợ</h3>
                <ul class="footer-links">
                    <li><a href="#">Chính sách đổi trả</a></li>
                    <li><a href="#">Hướng dẫn đặt hàng</a></li>
                    <li><a href="#">Thanh toán</a></li>
                    <li><a href="#">FAQ</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h3 class="footer-title">Liên hệ</h3>
                <ul class="footer-links">
                    <li>📍 Thủ Đức, TP.HCM</li>
                    <li>📞 0944912685</li>
                    <li>📧 handmadehouse23@handmade.vn</li>
                </ul>
            </div>

        </div>

        <div class="footer-bottom">
            <p>© 2025 Handmade House. Tất cả quyền được bảo lưu.</p>
        </div>
    </div>
</footer>


<script>
  window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
