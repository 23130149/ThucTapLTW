<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý khách hàng</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_KhachHang.css">
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
            <li class="active"><a href="${pageContext.request.contextPath}/admin/customers"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/banner"><i class="bx bx-image"></i>Banner</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="${pageContext.request.contextPath}/home">
            <i class="bx bx-log-out"></i> Đăng xuất
        </a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Quản lý khách hàng</h2>
        <div class="user-info">
            <div class="notification-wrapper">
                <a href="${pageContext.request.contextPath}/admin/notifications" class="notification-btn">
                    <i class="bx bx-bell"></i>
                    <c:if test="${notificationCount > 0}">
                        <span class="notification-count">${notificationCount}</span>
                    </c:if>
                </a>
                <div class="notification-dropdown">
                    <h4>Thông báo Admin</h4>
                    <c:choose>
                        <c:when test="${empty latestNotifications}">
                            <p class="empty-notification">Không có thông báo mới</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${latestNotifications}" var="n">
                                <a href="${pageContext.request.contextPath}${n.url}" class="notification-item">
                                    <span class="notification-type">${n.type}</span>
                                    <p>${n.message}</p>
                                </a>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/admin/setting" class="profile-admin">
                <span class="admin-avatar">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.userName}">
                            ${fn:substring(sessionScope.user.userName, 0, 1)}
                        </c:when>
                        <c:otherwise>A</c:otherwise>
                    </c:choose>
                </span>
                <div>
                    <p class="user-name">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user.userName}">
                                ${sessionScope.user.userName}
                            </c:when>
                            <c:otherwise>Admin</c:otherwise>
                        </c:choose>
                    </p>
                    <small class="user-role">Quản trị viên</small>
                </div>
            </a>
        </div>
    </header>
    <div class="customer-summary-grid">
        <div class="summary-card total">
            <div class="summary-icon">
                <i class="bx bx-user"></i>
            </div>

            <div>
                <p>Tổng khách hàng</p>
                <span class="summary-value">0</span>
                <span class="summary-detail growth">Tất cả khách hàng</span>
            </div>
        </div>

        <div class="summary-card vip">
            <div class="summary-icon">
                <i class="bx bx-crown"></i>
            </div>

            <div>
                <p>Khách VIP</p>
                <span class="summary-value">0</span>
                <span class="summary-detail growth">Chi tiêu cao</span>
            </div>
        </div>

        <div class="summary-card new">
            <div class="summary-icon">
                <i class="bx bx-user-plus"></i>
            </div>

            <div>
                <p>Khách mới</p>
                <span class="summary-value">0</span>
                <span class="summary-detail growth">Trong tháng này</span>
            </div>
        </div>

        <div class="summary-card aov">
            <div class="summary-icon">
                <i class="bx bx-wallet"></i>
            </div>

            <div>
                <p>Giá trị TB/Khách</p>
                <span class="summary-value money">0 ₫</span>
                <span class="summary-detail">Theo đơn hoàn thành</span>
            </div>
        </div>
    </div>
    <div class="customer-panel">
        <div class="panel-title-row">
            <div>
                <h3>Danh sách khách hàng</h3>
                <p>Quản lý thông tin và phân loại khách hàng</p>
            </div>
        </div>
        <div class="customer-search-filter-row">
            <div class="search-customer-box">
                <i class="bx bx-search"></i>
                <input type="text" placeholder="Tìm kiếm khách hàng...">
            </div>
            <button class="filter-button-icon"><i class="bx bx-filter"></i>Lọc</button>
        </div>
        <div class="order-table-container">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Khách hàng</th>
                    <th>Liên hệ</th>
                    <th>Số đơn</th>
                    <th>Tổng chi tiêu</th>
                    <th>Ngày tham gia</th>
                    <th>Loại khách hàng</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty customers}">
                        <tr>
                            <td colspan="7" class="empty-row">Chưa có khách hàng nào.</td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <c:forEach items="${customers}" var="customer">
                            <tr>
                                <td class="customer-info">
                        <span class="customer-avatar">
                            <c:choose>
                                <c:when test="${not empty customer.userName}">
                                    ${fn:substring(customer.userName, 0, 1)}
                                </c:when>
                                <c:otherwise>K</c:otherwise>
                            </c:choose>
                        </span>

                                    <div>
                                        <strong>
                                            <c:choose>
                                                <c:when test="${not empty customer.userName}">
                                                    ${customer.userName}
                                                </c:when>
                                                <c:otherwise>Khách chưa cập nhật tên</c:otherwise>
                                            </c:choose>
                                        </strong>
                                        <small>${customer.customerCode}</small>
                                    </div>
                                </td>

                                <td>
                                    <div class="contact-info">
                                        <span>
                                            <i class="bx bx-phone"></i>
                                            <c:choose>
                                                <c:when test="${not empty customer.phone}">
                                                    ${customer.phone}
                                                </c:when>
                                                <c:otherwise>Chưa cập nhật</c:otherwise>
                                            </c:choose>
                                        </span>

                                        <span>
                                            <i class="bx bx-envelope"></i>
                                            ${customer.email}
                                        </span>
                                    </div>
                                </td>

                                <td>
                                    <span class="order-count">${customer.orderCount}</span>
                                </td>

                                <td class="money-cell">${customer.totalSpendFormatted}</td>
                                <td>${customer.joinDateFormatted}</td>

                                <td>
                                    <span class="customer-type-tag type-moi">Mới</span>
                                </td>

                                <td>
                                    <button class="action-btn view-btn" type="button">
                                        <i class="bx bx-show-alt"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>
</body>
</html>