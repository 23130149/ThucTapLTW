<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Liên hệ</title>
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_DanhMuc.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/category"><i class="bx bx-category"></i>Danh mục</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/orders"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
            <li class="active"><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="${pageContext.request.contextPath}/home"><i class="bx bx-log-out"></i>Đăng xuất</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Liên hệ</h2>
        <form class="search-customer-box" method="get" action="${pageContext.request.contextPath}/admin/contacts">
            <i class="bx bx-search"></i>
            <input type="text" name="keyword" value="${keyword}" placeholder="Tìm tên, email hoặc chủ đề">
        </form>
        <div class="user-info">
            <span class="notification-badge"><i class="bx bx-bell"></i></span>
            <a href="${pageContext.request.contextPath}/admin/setting" class="profile-admin">
                <span class="admin-avatar">A</span>
                <div class="user-details">
                    <span class="user-name">Admin</span>
                    <span class="user-role">Quản trị viên</span>
                </div>
            </a>
        </div>
    </header>
    <div class="summary-grid">
        <div class="summary-card">
            <p>Tổng liên hệ</p>
            <span class="summary-value">${totalContacts}</span>
            <span class="summary-detail">Tin nhắn từ khách hàng</span>
        </div>
    </div>
    <div class="table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>Khách hàng</th>
                <th>Email</th>
                <th>Số điện thoại</th>
                <th>Chủ đề</th>
                <th>Nội dung</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty contacts}">
                    <tr>
                        <td colspan="5">Chưa có liên hệ</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="contact" items="${contacts}">
                        <tr>
                            <td>${contact.contactName}</td>
                            <td>${contact.contactEmail}</td>
                            <td>${contact.phone}</td>
                            <td>${contact.subject}</td>
                            <td>${contact.message}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
