<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông tin cá nhân - Handmade House</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/account.css">
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

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
<jsp:include page="/jsp/footer.jsp"/>
</body>
</html>
