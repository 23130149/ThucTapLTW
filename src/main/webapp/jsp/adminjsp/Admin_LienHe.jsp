<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Liên hệ</title>
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_LienHe.css">
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
            <li><a href="${pageContext.request.contextPath}/admin/banner"><i class="bx bx-image"></i>Banner</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="${pageContext.request.contextPath}/home"><i class="bx bx-log-out"></i>Đăng xuất</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Quản lý liên hệ</h2>
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
    <div class="summary-grid">
        <div class="summary-card">
            <p>Tổng liên hệ</p>
            <span class="summary-value">${totalContacts}</span>
            <span class="summary-detail">Tin nhắn từ khách hàng</span>
        </div>
    </div>
    <section class="table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Khách hàng</th>
                <th>Email</th>
                <th>Số điện thoại</th>
                <th>Chủ đề</th>
                <th>Nội dung</th>
                <th>Ngày gửi</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty contacts}">
                    <tr>
                        <td colspan="8" class="empty-row">Chưa có liên hệ</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${contacts}" var="contact">
                        <tr>
                            <td>
                                <strong>#${contact.contactId}</strong>
                            </td>

                            <td>
                                <c:out value="${contact.contactName}" />
                            </td>

                            <td>
                                <c:out value="${contact.contactEmail}" />
                            </td>

                            <td>
                                <c:out value="${contact.phone}" />
                            </td>

                            <td>
                                <c:out value="${contact.subject}" />
                            </td>

                            <td class="message-cell">
                                <c:out value="${contact.message}" />
                            </td>

                            <td>
                                    ${contact.createAtFormatted}
                            </td>

                            <td>
                                <div class="action-group">
                                    <a href="${pageContext.request.contextPath}/admin/contacts?detailId=${contact.contactId}"
                                       class="action-btn view-btn"
                                       title="Xem chi tiết">
                                        <i class="bx bx-show-alt"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </section>
    <c:if test="${not empty selectedContact}">
        <div class="modal-overlay">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Chi tiết liên hệ #${selectedContact.contactId}</h3>

                    <a href="${pageContext.request.contextPath}/admin/contacts" class="close-modal">
                        &times;
                    </a>
                </div>

                <div class="detail-grid">
                    <div class="detail-item">
                        <span>Họ tên</span>
                        <strong>
                            <c:out value="${selectedContact.contactName}" />
                        </strong>
                    </div>

                    <div class="detail-item">
                        <span>Email</span>
                        <strong>
                            <c:out value="${selectedContact.contactEmail}" />
                        </strong>
                    </div>

                    <div class="detail-item">
                        <span>Số điện thoại</span>
                        <strong>
                            <c:out value="${selectedContact.phone}" />
                        </strong>
                    </div>

                    <div class="detail-item">
                        <span>Ngày gửi</span>
                        <strong>${selectedContact.createAtFormatted}</strong>
                    </div>
                </div>

                <div class="message-detail-box">
                    <span>Chủ đề</span>
                    <p>
                        <c:out value="${selectedContact.subject}" />
                    </p>
                </div>

                <div class="message-detail-box">
                    <span>Nội dung khách hàng</span>
                    <p>
                        <c:out value="${selectedContact.message}" />
                    </p>
                </div>
            </div>
        </div>
    </c:if>
</main>
</body>
</html>
