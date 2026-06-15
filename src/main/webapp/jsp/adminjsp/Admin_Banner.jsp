<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý Banner</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_Banner.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-font-standard.css">
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
            <li><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
            <li class="active"><a href="${pageContext.request.contextPath}/admin/banner"><i class="bx bx-image"></i>Banner</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="${pageContext.request.contextPath}/home"><i class="bx bx-home-alt-2"></i>Trang chủ</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Quản lý banner</h2>

        <div class="user-info">
            <div class="notification-wrapper">
                <a href="javascript:void(0)" class="notification-btn">
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

    <c:if test="${not empty sessionScope.adminBannerMessage}">
    <div class="admin-banner-message">${sessionScope.adminBannerMessage}</div>
        <c:remove var="adminBannerMessage" scope="session"/>
    </c:if>

    <div class="summary-grid">
        <div class="summary-card">
            <i class="bx bx-image"></i>
            <div>
                <p>Tổng banner</p>
                <h3>${totalBanners}</h3>
            </div>
        </div>

        <div class="summary-card">
            <i class="bx bx-show"></i>
            <div>
                <p>Đang hiển thị</p>
                <h3>${activeBanners}</h3>
            </div>
        </div>

        <div class="summary-card">
            <i class="bx bx-hide"></i>
            <div>
                <p>Đang ẩn</p>
                <h3>${inactiveBanners}</h3>
            </div>
        </div>
    </div>

    <div class="banner-form-card">
        <h3>
            <c:choose>
                <c:when test="${not empty selectedBanner}">Cập nhật banner</c:when>
                <c:otherwise>Thêm banner mới</c:otherwise>
            </c:choose>
        </h3>

        <form method="post" action="${pageContext.request.contextPath}/admin/banner" class="banner-form">
            <input type="hidden" name="action" value="${not empty selectedBanner ? 'update' : 'add'}">
            <input type="hidden" name="bannerId" value="${selectedBanner.bannerId}">
            <input type="hidden" name="keyword" value="${keyword}">
            <input type="hidden" name="currentStatus" value="${currentStatus}">

            <div class="form-grid">
                <div class="form-group">
                    <label>Dòng tiêu đề 1</label>
                    <input type="text" name="titleLine1" value="${selectedBanner.titleLine1}" required>
                </div>

                <div class="form-group">
                    <label>Dòng tiêu đề 2</label>
                    <input type="text" name="titleLine2" value="${selectedBanner.titleLine2}">
                </div>

                <div class="form-group full">
                    <label>Mô tả banner</label>
                    <input type="text" name="subtitle" value="${selectedBanner.subtitle}">
                </div>

                <div class="form-group full">
                    <label>Đường dẫn ảnh</label>
                    <input type="text" name="imageUrl" value="${selectedBanner.imageUrl}" placeholder="https://... hoặc /images/banner/banner1.jpg" required>
                </div>

                <div class="form-group">
                    <label>Link chuyển hướng</label>
                    <input type="text" name="targetUrl" value="${selectedBanner.targetUrl}" placeholder="/product">
                </div>

                <div class="form-group">
                    <label>Thứ tự</label>
                    <input type="number" name="sortOrder" value="${empty selectedBanner ? 0 : selectedBanner.sortOrder}">
                </div>

                <div class="form-group">
                    <label>Trạng thái</label>
                    <select name="status">
                        <option value="ACTIVE" ${selectedBanner.status == 'ACTIVE' ? 'selected' : ''}>Đang hiển thị</option>
                        <option value="INACTIVE" ${selectedBanner.status == 'INACTIVE' ? 'selected' : ''}>Đang ẩn</option>
                    </select>
                </div>
            </div>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/banner" class="reset-btn">Làm mới</a>
                <button type="submit" class="save-btn">
                    <i class="bx bx-save"></i>Lưu banner
                </button>
            </div>
        </form>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/admin/banner" class="filter-row">
        <div class="search-box">
            <i class="bx bx-search"></i>
            <input type="text" name="keyword" value="${keyword}" placeholder="Tìm banner...">
        </div>

        <select name="status" onchange="this.form.submit()">
            <option value="" ${empty currentStatus ? 'selected' : ''}>Tất cả trạng thái</option>
            <option value="ACTIVE" ${currentStatus == 'ACTIVE' ? 'selected' : ''}>Đang hiển thị</option>
            <option value="INACTIVE" ${currentStatus == 'INACTIVE' ? 'selected' : ''}>Đang ẩn</option>
        </select>

        <button type="submit" class="search-btn">Tìm kiếm</button>
    </form>

    <div class="table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Ảnh</th>
                <th>Tiêu đề</th>
                <th>Mô tả</th>
                <th>Link</th>
                <th>Thứ tự</th>
                <th>Trạng thái</th>
                <th>Ngày tạo</th>
                <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <c:choose>
                <c:when test="${empty banners}">
                    <tr>
                        <td colspan="9" class="empty-row">Chưa có banner nào</td>
                    </tr>
                </c:when>

                <c:otherwise>
                    <c:forEach items="${banners}" var="banner">
                        <tr>
                            <td>#${banner.bannerId}</td>
                            <td>
                                <c:set var="imgUrl" value="${banner.imageUrl}"/>
                                <c:if test="${not fn:startsWith(banner.imageUrl, 'http')}">
                                    <c:set var="imgUrl" value="${pageContext.request.contextPath}${banner.imageUrl}"/>
                                </c:if>
                                <img src="${imgUrl}" class="banner-img" alt="${banner.titleLine1}">
                            </td>
                            <td>
                                <strong>${banner.titleLine1}</strong><br>
                                <span>${banner.titleLine2}</span>
                            </td>
                            <td>${banner.subtitle}</td>
                            <td>${banner.targetUrl}</td>
                            <td>${banner.sortOrder}</td>
                            <td>
                                <span class="status ${banner.statusClass}">
                                        ${banner.statusLabel}
                                </span>
                            </td>
                            <td>${banner.createAtFormatted}</td>
                            <td>
                                <div class="table-actions">
                                    <a href="${pageContext.request.contextPath}/admin/banner?editId=${banner.bannerId}&keyword=${keyword}&status=${currentStatus}"
                                       class="edit-btn">
                                        Sửa
                                    </a>

                                    <form method="post" action="${pageContext.request.contextPath}/admin/banner" class="inline-form">
                                        <input type="hidden" name="action" value="toggleStatus">
                                        <input type="hidden" name="bannerId" value="${banner.bannerId}">
                                        <input type="hidden" name="status" value="${banner.active ? 'INACTIVE' : 'ACTIVE'}">
                                        <input type="hidden" name="keyword" value="${keyword}">
                                        <input type="hidden" name="currentStatus" value="${currentStatus}">

                                        <button type="submit" class="toggle-btn">
                                                ${banner.active ? 'Ẩn' : 'Hiện'}
                                        </button>
                                    </form>

                                    <form method="post" action="${pageContext.request.contextPath}/admin/banner" class="inline-form"
                                          onsubmit="return confirm('Bạn có chắc muốn xóa banner này không?')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="bannerId" value="${banner.bannerId}">
                                        <input type="hidden" name="keyword" value="${keyword}">
                                        <input type="hidden" name="currentStatus" value="${currentStatus}">

                                        <button type="submit" class="delete-btn">Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</main>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const wrappers = document.querySelectorAll(".notification-wrapper");

        wrappers.forEach(function (wrapper) {
            const button = wrapper.querySelector(".notification-btn");
            const dropdown = wrapper.querySelector(".notification-dropdown");

            button.addEventListener("click", function (event) {
                event.preventDefault();
                event.stopPropagation();

                wrappers.forEach(function (item) {
                    if (item !== wrapper) {
                        item.classList.remove("active");
                    }
                });

                wrapper.classList.toggle("active");
            });

            dropdown.addEventListener("click", function (event) {
                event.stopPropagation();
            });
        });

        document.addEventListener("click", function () {
            wrappers.forEach(function (wrapper) {
                wrapper.classList.remove("active");
            });
        });
    });
</script>
</body>
</html>
