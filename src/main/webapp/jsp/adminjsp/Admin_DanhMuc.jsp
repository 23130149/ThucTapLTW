
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Admin_DanhMuc.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li class="active"><a href="${pageContext.request.contextPath}/jsp/adminjsp/Admin_DanhMuc.jsp"><i class="bx bx-category"></i>Danh mục</a></li>
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
        <a href="../../../../../../html/trangchu.html"><i class="bx bx-log-out"></i>Đăng xuất</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Quản lý danh mục</h2>
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
    <div class="category-summary-grid">
        <div class="summary-card total-category">
            <p>Tổng danh mục</p>
            <h3></h3>
        </div>
        <div class="summary-card total-product">
            <p>Tổng sản phẩm</p>
            <h3></h3>
        </div>
        <div class="summary-card avg-category">
            <p>Trung bình mỗi danh mục</p>
            <h3></h3>
        </div>
    </div>
    <div class="customer-search-filter-row">
        <div class="search-customer-box">
            <i class="bx bx-search"></i>
            <input type="text" placeholder="Tìm kiếm danh mục...">
        </div>
        <button class="add-category-btn"><i class="bx bx-plus"></i>Thêm danh mục</button>
    </div>
    <div class="order-table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>Tên danh mục</th>
                <th>Sản phẩm</th>
                <th>Ngày tạo</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>Móc khóa</td>
                <td>28</td>
                <td>28/12/2024</td>
                <td>
                    <i class="bx bx-edit action-icon"></i>
                    <i class="bx bx-trash action-icon"></i>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
