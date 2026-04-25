<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Admin_TongQuan.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li class="active"><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/adminjsp/Admin_DanhMuc.jsp"><i class="bx bx-category"></i>Danh mục</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/orders"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/adminjsp/Admin_Banner.jspanner.jsp"><i class="bx bx-image"></i>Banner</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="${pageContext.request.contextPath}/home"><i class="bx bx-log-out"></i>Đăng Xuất</a>
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
            <span class="notification-badge"><i class="bx bx-bell"></i></span>
            <div class="profile-admin">
                <span class="admin-avatar">L</span>
                <div class="user-details">
                    <span class="user-name">Phan Đình Long</span>
                    <span class="user-role">Quản trị viên</span>
                </div>
            </div>
        </div>
    </header>
    <div class="stats-grid">
        <div class="stat-card stat-revenue">
            <div class="stat-icon"><i class="bx bx-money"></i></div>
            <div class="stat-details">
                <p class="title">Doanh thu</p>
                <p class="value">
                    <fmt:formatNumber value="${totalRevenue}" groupingUsed="true"/>đ
                </p>
                <span class="stat-change positive">Tổng doanh thu hệ thống</span>
            </div>
        </div>
        <div class="stat-card stat-new-orders">
            <div class="stat-icon"><i class="bx bx-receipt"></i></div>
            <div class="stat-details">
                <p class="title">Đơn hàng</p>
                <p class="value">${totalOrders}</p>
                <span class="stat-change positive">Tổng đơn đã tạo</span>
            </div>
        </div>
        <div class="stat-card stat-customers">
            <div class="stat-icon"><i class="bx bx-user-pin"></i></div>
            <div class="stat-details">
                <p class="title">Khách hàng</p>
                <p class="value">${totalUsers}</p>
                <span class="stat-change positive">Tổng người dùng hệ thống</span>
            </div>
        </div>
    </div>
    <div class="charts-section">
        <div class="chart-card">
            <div class="card-header">
                <h3 class="card-title">
                    Doanh thu ${range == '30' ? '30 ngày' : '7 ngày'}
                </h3>

                <select class="chart-filter"
                        onchange="location.href='${pageContext.request.contextPath}/admin/dashboard?range=' + this.value">
                    <option value="7" ${range == '7' ? 'selected' : ''}>7 ngày</option>
                    <option value="30" ${range == '30' ? 'selected' : ''}>30 ngày</option>
                </select>
            </div>
            <div class="chart-area">
                <div class="bar-chart-container">
                    <c:forEach items="${revenueChart}" var="item">
                        <div class="bar-chart" style="height: ${item.value}%">
                            <span class="revenue-label">
                                <fmt:formatNumber value="${item.originalValue}" groupingUsed="true"/>đ
                            </span>
                            <p>${item.key}</p>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="chart-card">
            <div class="card-header">
                <h3 class="card-title">Top sản phẩm bán chạy</h3>
            </div>
            <div class="top-products">
                <c:forEach items="${topProducts}" var="p" varStatus="st">
                <div class="product-item">
                    <span class="product-rank">${st.count}</span>
                    <div class="product-info">
                        <p class="product-name">${p.productName}</p>
                        <p class="product-sales">${p.sold} đã bán</p>
                    </div>
                    <span class="product-price">
                        <fmt:formatNumber value="${p.revenue}" groupingUsed="true"/>đ
                    </span>
                </div>
            </div>
        </div>
    </div>
    <div class="order-table-container">
        <div class="card-header">
            <h3 class="card-title">Đơn hàng mới nhất</h3>
        </div>
        <table class="data-table">
            <thead>
            <tr>
                <th>Mã đơn</th>
                <th>Khách hàng</th>
                <th>Sản phẩm</th>
                <th>Số lượng</th>
                <th>Tổng tiền</th>
                <th>Trạng thái</th>
                <th>Ngày đặt</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>#DH001</td>
                <td class="customer-info">
                    <span class="customer-avatar">P</span>
                    Nguyễn Thanh Phú
                </td>
                <td>Ốp lưng điện thoại</td>
                <td>3</td>
                <td>267.000đ</td>
                <td><span class="status status-pending">Đang xử lý</span></td>
                <td>17/10/2025</td>
                <td>
                    <i class="bx bx-show-alt action-icon"></i>
                </td>
            </tr>
            <tr>
                <td>${o.orderCode}</td>
                <td class="customer-info">
                    <span class="customer-avatar">
                            ${fn:substring(o.userName,0,1)}
                    </span>
                    ${o.userName}
                </td>
                <td>${o.productName}</td>
                <td>${o.quantity}</td>
                <td>
                    <fmt:formatNumber value="${o.totalPrice}" groupingUsed="true"/>đ
                </td>
                <td>
                    <span class="status ${statusClass}">
                        ${o.status}
                    </span>
                </td>
                <td>${o.createAtFormatted}</td>
                <td>
                    <i class="bx bx-show-alt action-icon"></i>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</main>

<div id="orderModal" class="modal">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <h3>Chi Tiết Đơn Hàng</h3>
        <hr>
        <div id="modalBody">
        </div>
    </div>
</div>

</body>
</html>