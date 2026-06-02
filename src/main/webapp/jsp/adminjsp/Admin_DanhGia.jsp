<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_DanhGia.css">
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
            <li class="active"><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
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
        <h2>Quản lý đánh giá</h2>
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
    <section class="stats-grid">
        <div class="stat-card stat-total">
            <div class="stat-icon"><i class="bx bx-message-square-detail"></i></div>
            <div class="stat-details">
                <p class="title">Tổng đánh giá</p>
                <p class="value">${totalReviews}</p>
            </div>
        </div>

        <div class="stat-card stat-rating">
            <div class="stat-icon"><i class="bx bxs-star"></i></div>
            <div class="stat-details">
                <p class="title">Đánh giá trung bình</p>
                <p class="value">
                    <fmt:formatNumber value="${averageRating}" minFractionDigits="1" maxFractionDigits="1"/>
                </p>
            </div>
        </div>

        <div class="stat-card stat-pending">
            <div class="stat-icon"><i class="bx bx-time-five"></i></div>
            <div class="stat-details">
                <p class="title">Chờ duyệt</p>
                <p class="value">${pendingReviews}</p>
            </div>
        </div>

        <div class="stat-card stat-five">
            <div class="stat-icon"><i class="bx bx-trending-up"></i></div>
            <div class="stat-details">
                <p class="title">Tỷ lệ 5 sao</p>
                <p class="value">${fiveStarPercent}%</p>
            </div>
        </div>
    </section>

    <section class="review-panel">
        <div class="panel-header">
            <div>
                <h3>Danh sách đánh giá</h3>
                <p>Quản lý đánh giá của khách hàng về sản phẩm</p>
            </div>
        </div>

        <div class="search-filter-row">
            <div class="search-review-box">
                <i class="bx bx-search"></i>
                <input type="text" placeholder="Tìm kiếm đánh giá..." disabled>
            </div>

            <select class="filter-select" disabled>
                <option>Tất cả số sao</option>
            </select>

            <select class="filter-select" disabled>
                <option>Tất cả trạng thái</option>
            </select>

            <button class="filter-btn" disabled>
                <i class="bx bx-filter-alt"></i>Lọc
            </button>
        </div>

        <div class="order-table-container">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Mã</th>
                    <th>Khách hàng</th>
                    <th>Sản phẩm</th>
                    <th>Số sao</th>
                    <th>Nội dung</th>
                    <th>Ngày đánh giá</th>
                    <th>Hữu ích</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td colspan="9">
                        <div class="empty-state">
                            <i class="bx bx-message-square-x"></i>
                            <p>Chưa có dữ liệu đánh giá.</p>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </section>
</main>
<div id="replyModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Phản hồi đánh giá</h3>
            <span class="close-modal" id="closeReply">&times;</span>
        </div>
        <div class="modal-body">
            <p><strong>Khách hàng:</strong> <span id="replyToName"></span></p>
            <textarea id="replyText" placeholder="Nhập nội dung phản hồi của bạn..."></textarea>
            <button id="sendReplyBtn" class="btn-save" style="margin-top: 15px;">Gửi phản hồi</button>
        </div>
    </div>
</div>
<div id="viewModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Chi tiết đánh giá</h3>
            <span class="close-modal" id="closeView">&times;</span>
        </div>
        <div id="viewDetailContent" class="modal-body">
        </div>
    </div>
</div>
</body>
</html>