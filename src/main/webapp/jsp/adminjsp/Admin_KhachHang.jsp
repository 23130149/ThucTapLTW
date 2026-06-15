<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý khách hàng</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_KhachHang.css">
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
            <li class="active"><a href="${pageContext.request.contextPath}/admin/customers"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/banner"><i class="bx bx-image"></i>Banner</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="${pageContext.request.contextPath}/home">
            <i class="bx bx-home-alt-2"></i> Trang chủ
        </a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Quản lý khách hàng</h2>
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
    <c:choose>
    <c:when test="${accessDenied}">
        <div class="admin-alert error">
            <i class="bx bx-error-circle"></i>
                ${accessDeniedMessage}
        </div>
    </c:when>

    <c:otherwise>
    <div class="customer-summary-grid">
        <div class="summary-card total">
            <div class="summary-icon">
                <i class="bx bx-user"></i>
            </div>

            <div>
                <p>Tổng khách hàng</p>
                <span class="summary-value">${totalCustomers}</span>
                <span class="summary-detail growth">Tất cả khách hàng</span>
            </div>
        </div>

        <div class="summary-card vip">
            <div class="summary-icon">
                <i class="bx bx-crown"></i>
            </div>

            <div>
                <p>Khách VIP</p>
                <span class="summary-value">${vipCustomers}</span>
                <span class="summary-detail growth">Chi tiêu từ 10 triệu</span>
            </div>
        </div>

        <div class="summary-card new">
            <div class="summary-icon">
                <i class="bx bx-user-plus"></i>
            </div>

            <div>
                <p>Khách mới</p>
                <span class="summary-value">${newCustomersThisMonth}</span>
                <span class="summary-detail growth">Trong tháng này</span>
            </div>
        </div>

        <div class="summary-card aov">
            <div class="summary-icon">
                <i class="bx bx-wallet"></i>
            </div>

            <div>
                <p>Giá trị TB/Khách</p>
                <span class="summary-value money">${averageSpendFormatted}</span>
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
        <form method="get"
              action="${pageContext.request.contextPath}/admin/customers"
              class="customer-search-filter-row">

            <div class="search-customer-box">
                <i class="bx bx-search"></i>
                <input type="text"
                       name="keyword"
                       value="${keyword}"
                       placeholder="Tìm theo tên, số điện thoại, email...">
            </div>
            <select name="customerType" class="filter-select"  onchange="this.form.submit();">
                <option value="" ${empty currentCustomerType ? 'selected' : ''}>Tất cả loại</option>
                <option value="vip" ${currentCustomerType == 'vip' ? 'selected' : ''}>VIP</option>
                <option value="regular" ${currentCustomerType == 'regular' ? 'selected' : ''}>Thường xuyên</option>
                <option value="new" ${currentCustomerType == 'new' ? 'selected' : ''}>Mới</option>
            </select>

            <select name="orderRange" class="filter-select" onchange="this.form.submit();">
                <option value="" ${empty currentOrderRange ? 'selected' : ''}>Tất cả số đơn</option>
                <option value="0" ${currentOrderRange == '0' ? 'selected' : ''}>Chưa có đơn</option>
                <option value="1-5" ${currentOrderRange == '1-5' ? 'selected' : ''}>1 - 5 đơn</option>
                <option value="6-10" ${currentOrderRange == '6-10' ? 'selected' : ''}>6 - 10 đơn</option>
                <option value="11+" ${currentOrderRange == '11+' ? 'selected' : ''}>Từ 11 đơn</option>
            </select>

            <a href="${pageContext.request.contextPath}/admin/customers" class="reset-filter-btn">
                <i class="bx bx-refresh"></i>Làm mới
            </a>
        </form>

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
                            <c:set var="detailUrl" value="${pageContext.request.contextPath}/admin/customers?detailId=${customer.userId}&keyword=${keyword}&customerType=${currentCustomerType}&orderRange=${currentOrderRange}&page=${currentPage}" />
                            <tr class="${customer.customerType == 'vip' ? 'vip-row' : ''}">
                                <td class="customer-info">
                                    <span class="customer-avatar ${customer.customerType == 'vip' ? 'vip-avatar' : ''}">
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
                                    <span class="customer-type-tag type-${customer.customerType}">
                                            ${customer.customerTypeLabel}
                                    </span>
                                </td>

                                <td>
                                    <div class="table-actions">
                                        <a href="${detailUrl}" class="action-btn view-btn" title="Xem chi tiết">
                                            <i class="bx bx-show-alt"></i>
                                        </a>

                                        <c:choose>
                                            <c:when test="${fn:contains(sessionScope.permissionCodesText, ',MANAGE_CUSTOMER,')}">
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/customers"
                                                      class="delete-form"
                                                      onsubmit="return confirm('Bạn có chắc muốn xóa khách hàng này không?');">

                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="userId" value="${customer.userId}">

                                                    <button type="submit" class="action-btn delete-btn" title="Xóa">
                                                        <i class="bx bx-trash"></i>
                                                    </button>
                                                </form>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="no-permission-text">Chỉ xem</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/customers?keyword=${keyword}&customerType=${currentCustomerType}&orderRange=${currentOrderRange}&page=${currentPage - 1}"
                        class="page-link">
                        <i class="bx bx-chevron-left"></i>
                        </a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                        <a href="${pageContext.request.contextPath}/admin/customers?keyword=${keyword}&customerType=${currentCustomerType}&orderRange=${currentOrderRange}&page=${pageNumber}"
                           class="page-link ${pageNumber == currentPage ? 'active' : ''}">
                                ${pageNumber}
                        </a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/customers?keyword=${keyword}&customerType=${currentCustomerType}&orderRange=${currentOrderRange}&page=${currentPage + 1}" class="page-link">
                            <i class="bx bx-chevron-right"></i>
                        </a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
    </c:otherwise>
    </c:choose>
</main>
<c:if test="${not accessDenied}">
<c:if test="${not empty selectedCustomer}">
    <c:set var="closeDetailUrl" value="${pageContext.request.contextPath}/admin/customers?keyword=${keyword}&customerType=${currentCustomerType}&orderRange=${currentOrderRange}&page=${currentPage}" />
    <div class="modal-overlay show">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Chi tiết khách hàng</h3>
                <a href="${closeDetailUrl}" class="close-modal">&times;</a>
            </div>

            <div class="customer-detail-card">
                <span class="detail-avatar ${selectedCustomer.customerType == 'vip' ? 'vip-avatar' : ''}">
                    <c:choose>
                        <c:when test="${not empty selectedCustomer.userName}">
                            ${fn:substring(selectedCustomer.userName, 0, 1)}
                        </c:when>
                        <c:otherwise>K</c:otherwise>
                    </c:choose>
                </span>

                <h4>
                    <c:choose>
                        <c:when test="${not empty selectedCustomer.userName}">
                            ${selectedCustomer.userName}
                        </c:when>
                        <c:otherwise>Khách chưa cập nhật tên</c:otherwise>
                    </c:choose>
                </h4>

                <p>${selectedCustomer.customerCode}</p>

                <span class="customer-type-tag type-${selectedCustomer.customerType}">
                        ${selectedCustomer.customerTypeLabel}
                </span>
            </div>

            <div class="detail-grid">
                <div class="detail-item">
                    <span><i class="bx bx-envelope"></i>Email</span>
                    <strong>${selectedCustomer.email}</strong>
                </div>

                <div class="detail-item">
                    <span><i class="bx bx-phone"></i>Số điện thoại</span>
                    <strong>
                        <c:choose>
                            <c:when test="${not empty selectedCustomer.phone}">
                                ${selectedCustomer.phone}
                            </c:when>
                            <c:otherwise>Chưa cập nhật</c:otherwise>
                        </c:choose>
                    </strong>
                </div>

                <div class="detail-item">
                    <span><i class="bx bx-calendar"></i>Ngày tham gia</span>
                    <strong>${selectedCustomer.joinDateFormatted}</strong>
                </div>

                <div class="detail-item">
                    <span><i class="bx bx-receipt"></i>Số đơn hoàn thành</span>
                    <strong>${selectedCustomer.orderCount}</strong>
                </div>

                <div class="detail-item">
                    <span><i class="bx bx-wallet"></i>Tổng chi tiêu</span>
                    <strong>${selectedCustomer.totalSpendFormatted}</strong>
                </div>

                <div class="detail-item">
                    <span><i class="bx bx-user"></i>Giới tính</span>
                    <strong>${selectedCustomer.genderLabel}</strong>
                </div>
            </div>
        </div>
    </div>
</c:if>
</c:if>
<script defer src="${pageContext.request.contextPath}/js/ajax-enhance.js?v=20260615-1"></script>
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
