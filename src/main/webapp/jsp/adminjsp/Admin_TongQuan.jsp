<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_TongQuan.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li class="active"><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/category"><i class="bx bx-category"></i>Danh mục</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/orders"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"><i class="bx bx-group"></i>Khách hàng</a></li>
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
        <h2>Tổng quan</h2>
        <div class="search-box">
            <input type="text" placeholder="Tìm kiếm...">
            <button><i class="bx bx-search"></i></button>
        </div>
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
    <div class="stats-grid">
        <div class="stat-card stat-revenue">
            <div class="stat-icon"><i class="bx bx-money"></i></div>
            <div class="stat-details">
                <div class="title">Doanh thu</div>
                <div class="value">
                    <fmt:formatNumber value="${totalRevenue}" groupingUsed="true"/>đ
                </div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/admin/orders?status=COMPLETED"
           class="stat-card stat-new-orders dashboard-link-card">
            <div class="stat-icon"><i class="bx bx-receipt"></i></div>
            <div class="stat-details">
                <div class="title">Đơn hoàn thành</div>
                <div class="value">${totalOrders}</div>
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/admin/customers"
           class="stat-card stat-customers dashboard-link-card">
            <div class="stat-icon"><i class="bx bx-user"></i></div>
            <div class="stat-details">
                <div class="title">Khách hàng</div>
                <div class="value">${totalUsers}</div>
            </div>
        </a>
    </div>
    <div class="charts-section">
    <div class="chart-card">
        <div class="card-header">
            <h3>Doanh thu ${range == '30' ? '30 ngày' : '7 ngày'}</h3>
            <select onchange="changeRange(this.value)">
                <option value="7" ${range=='7'?'selected':''}>7 ngày</option>
                <option value="30" ${range=='30'?'selected':''}>30 ngày</option>
            </select>
        </div>
        <div class="bar-chart-container">
            <c:choose>
                <c:when test="${empty revenueChart}">
                    <p class="empty-notification">Chưa có dữ liệu doanh thu</p>
                </c:when>

                <c:otherwise>
                    <c:forEach items="${revenueChart}" var="item">
                        <div class="bar-item">
                            <div class="bar-value">
                                <fmt:formatNumber value="${item.value}" groupingUsed="true"/>đ
                            </div>
                            <div class="bar-column">
                                <div class="bar-fill" style="height:${item.percent}%;"></div>
                            </div>

                            <div class="bar-date">${item.label}</div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="chart-card">
        <h3>Top sản phẩm</h3>
        <div class="top-products">
            <c:forEach items="${topProducts}" var="p" varStatus="st">
                <a href="${pageContext.request.contextPath}/admin/products?editId=${p.productId}"
                   class="product-item">
                    <div class="product-rank rank-${st.count}">
                        #${st.count}
                    </div>
                    <c:choose>
                        <c:when test="${not empty p.imageUrl}">
                            <c:choose>
                                <c:when test="${fn:startsWith(p.imageUrl, 'http')}">
                                    <img src="${p.imageUrl}"
                                         class="product-img"
                                         alt="${p.productName}"
                                         onerror="this.src='${pageContext.request.contextPath}/images/default.png'">
                                </c:when>
                                <c:when test="${fn:startsWith(p.imageUrl, '/')}">
                                    <img src="${pageContext.request.contextPath}${p.imageUrl}"
                                         class="product-img"
                                         alt="${p.productName}"
                                         onerror="this.src='${pageContext.request.contextPath}/images/default.png'">
                                </c:when>

                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/${p.imageUrl}"
                                         class="product-img"
                                         alt="${p.productName}"
                                         onerror="this.src='${pageContext.request.contextPath}/images/default.png'">
                                </c:otherwise>
                            </c:choose>
                        </c:when>

                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/images/default.png"
                                 class="product-img"
                                 alt="${p.productName}">
                        </c:otherwise>
                    </c:choose>
                    <div class="product-info">
                        <div class="product-name">${p.productName}</div>
                        <div class="product-sales">${p.sold} đã bán</div>
                    </div>
                    <div class="product-price">
                        <fmt:formatNumber value="${p.revenue}" groupingUsed="true"/>đ
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>
    </div>
    <div class="table-card">
        <div class="table-title-row">
            <h3>Đơn hàng mới nhất</h3>

            <a href="${pageContext.request.contextPath}/admin/orders" class="view-all-link">
                <i class="bx bx-list-ul"></i>
                <span>Xem tất cả</span>
            </a>
        </div>

        <div class="order-table-container">
            <table class="data-table">
                <thead>
                <tr>
                    <th>Mã đơn</th>
                    <th>Khách hàng</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                    <th>Ngày đặt</th>
                    <th>Thao tác</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${empty latestOrders}">
                        <tr>
                            <td colspan="6" class="empty-table">Chưa có đơn hàng nào</td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <c:forEach items="${latestOrders}" var="o">
                            <tr>
                                <td>${o.orderCode}</td>

                                <td>
                                    <c:choose>
                                        <c:when test="${not empty o.userName}">
                                            ${o.userName}
                                        </c:when>
                                        <c:otherwise>Khách hàng</c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <fmt:formatNumber value="${o.totalPrice}" groupingUsed="true"/>đ
                                </td>

                                <td>
                                <span class="status
                                    ${o.status == 'PENDING' ? 'status-pending' :
                                      o.status == 'COMPLETED' ? 'status-completed' :
                                      o.status == 'SHIPPED' ? 'status-shipping' :
                                      o.status == 'CONFIRMED' ? 'status-confirmed' : ''}">
                                        ${o.status}
                                </span>
                                </td>

                                <td>${o.createAtFormatted}</td>

                                <td>
                                    <a class="detail-link"
                                       href="${pageContext.request.contextPath}/admin/orders?detailId=${o.orderId}">
                                        <i class="bx bx-show-alt"></i>
                                        Chi tiết
                                    </a>
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
<script>
    function changeRange(range) {
        window.location.href = '${pageContext.request.contextPath}/admin/dashboard?range=' + range;
    }
</script>
</body>
</html>