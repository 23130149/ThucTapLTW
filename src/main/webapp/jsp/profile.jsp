<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông tin cá nhân - Handmade House</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/account.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/header_footer.css">
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
</head>
<body>
<header class="header">
    <div class="header-top-container">
        <div class="header-content">

            <div class="logo">
                <a href="${pageContext.request.contextPath}/home">Handmade House</a>
            </div>

            <form class="search-form"
                  action="${pageContext.request.contextPath}/product"
                  method="GET">
                <input type="text" class="search-input"
                       name="keyword"
                       placeholder="Tìm kiếm bất cứ thứ gì...">
                <button type="submit" class="search-btn">
                    <i class="bx bx-search-alt-2"></i>
                </button>
            </form>

            <div class="icons">

                <a href="${pageContext.request.contextPath}/favorite" class="icon-btn favorite-header-icon" id="heartBtn" title="Sản phẩm yêu thích">
                  <i class='bx bx-heart'></i>
                </a>
                <a href="${pageContext.request.contextPath}/cart" class="icon-btn cart-icon">
                    <i class='bx bx-cart'></i>
                
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                        <span class="cart-badge">${sessionScope.cart.totalQuantity}</span>
                    </c:if>
                </a>
                <a href="${pageContext.request.contextPath}/Account" class="icon-btn">
                    <i class='bx bx-user'></i>
                </a>
            </div>

        </div>
    </div>

    <div class="search-bar-section header-bottom-nav">
        <div class="container nav-only-container">
            <nav class="nav__links">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/product">Sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/jsp/blog.jsp">Blog</a></li>
                    <li><a href="${pageContext.request.contextPath}/jsp/contact.jsp">Liên hệ</a></li>
                </ul>
            </nav>
        </div>
    </div>
</header>

<main class="about-us-container profile-dashboard">
    <h1>Thông tin cá nhân</h1>

    <c:if test="${not empty sessionScope.profileMessage}">
        <div class="form-alert form-alert-success">${sessionScope.profileMessage}</div>
        <c:remove var="profileMessage" scope="session" />
    </c:if>

    <div class="account-hero">
        <div class="account-avatar">
            <i class='bx bxs-user-circle'></i>
        </div>

        <div class="account-hero-text">
            <p>Hồ sơ tài khoản</p>
            <h2>
                <c:choose>
                    <c:when test="${empty sessionScope.user.userName}">
                        Chưa cập nhật tên
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.user.userName}
                    </c:otherwise>
                </c:choose>
            </h2>
            <span>${sessionScope.user.email}</span>
        </div>
    </div>

    <div class="profile-info-grid">
        <div class="profile-info-card">
            <i class='bx bx-id-card'></i>
            <span>Mã khách hàng</span>
            <strong>
                <c:choose>
                    <c:when test="${empty sessionScope.user.customerCode}">
                        Chưa cập nhật
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.user.customerCode}
                    </c:otherwise>
                </c:choose>
            </strong>
        </div>

        <div class="profile-info-card">
            <i class='bx bx-user'></i>
            <span>Họ và tên</span>
            <strong>
                <c:choose>
                    <c:when test="${empty sessionScope.user.userName}">
                        Chưa cập nhật
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.user.userName}
                    </c:otherwise>
                </c:choose>
            </strong>
        </div>

        <div class="profile-info-card">
            <i class='bx bx-envelope'></i>
            <span>Email</span>
            <strong>${sessionScope.user.email}</strong>
        </div>

        <div class="profile-info-card">
            <i class='bx bx-phone'></i>
            <span>Số điện thoại</span>
            <strong>
                <c:choose>
                    <c:when test="${empty sessionScope.user.phone}">
                        Chưa cập nhật
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.user.phone}
                    </c:otherwise>
                </c:choose>
            </strong>
        </div>

        <div class="profile-info-card">
            <i class='bx bx-calendar-heart'></i>
            <span>Ngày sinh</span>
            <strong>${sessionScope.user.dateOfBirthFormatted}</strong>
        </div>

        <div class="profile-info-card">
            <i class='bx bx-user-pin'></i>
            <span>Giới tính</span>
            <strong>${sessionScope.user.genderLabel}</strong>
        </div>

        <div class="profile-info-card">
            <i class='bx bx-calendar'></i>
            <span>Ngày tham gia</span>
            <strong>${sessionScope.user.joinDateFormatted}</strong>
        </div>

        <div class="profile-info-card">
            <i class='bx bx-message-rounded-detail'></i>
            <span>Giới thiệu</span>
            <strong>
                <c:choose>
                    <c:when test="${empty sessionScope.user.bio}">
                        Chưa cập nhật
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.user.bio}
                    </c:otherwise>
                </c:choose>
            </strong>
        </div>
    </div>

    <div class="account-bottom-actions">
        <a href="${pageContext.request.contextPath}/Account" class="btn-account-secondary">
            <i class='bx bx-arrow-back'></i>
            Quay lại tài khoản
        </a>

        <a href="${pageContext.request.contextPath}/Profile/Edit" class="btn-account-primary">
            <i class='bx bx-edit-alt'></i>
            Chỉnh sửa hồ sơ
        </a>

        <a href="${pageContext.request.contextPath}/ChangePassword" class="btn-account-secondary">
            <i class='bx bx-lock-alt'></i>
            Đổi mật khẩu
        </a>
    </div>

</main>
<footer class="footer">
    <div class="container">
        <div class="footer-content">
            <div class="footer-column">
                <h3 class="footer-logo">Handmade House</h3>
                <p class="footer-desc">Chào mừng đến với Handmade House, ngôi nhà nhỏ của những tâm hồn yêu nghệ thuật
                    và thủ công.</p>
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
                    <li><a href="#"> Giá trị & Triết lý thương hiệu</a></li>
                    <li><a href="#">Quy trình sản xuất</a></li>
                    <li><a href="#">Cam kết & Định hướng bền vững</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h3 class="footer-title">Hỗ trợ</h3>
                <ul class="footer-links">
                    <li><a href="#">Chính sách đổi trả</a></li>
                    <li><a href="#">Hướng dẫn đặt hàng</a></li>
                    <li><a href="#">Phương thức thanh toán</a></li>
                    <li><a href="#">Câu hỏi thường gặp</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h3 class="footer-title">Liên hệ</h3>
                <ul class="footer-links">
                    <li>📍 Khu phố 6, Phường Linh Trung, TP. Thủ Đức, TP. Hồ Chí Minh</li>
                    <li>📞 0944912685</li>
                    <li>📧 handmadehouse23@handmade.vn</li>
                    <li>🕐 T2 - CN: 8:00 - 17:00</li>
                </ul>
            </div>
        </div>
        <div class="footer-bottom">
            <p>@2025 Handmade. Tất cả quyền được bảo lưu.</p>
        </div>
    </div>
</footer>
</body>
</html>
