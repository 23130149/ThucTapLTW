<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý Banner</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Admin_Banner.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/orders"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
            <li class="active"><a href="${pageContext.request.contextPath}/jsp/adminjsp/Admin_Banner.jsp"><i class="bx bx-image"></i>Banner</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="${pageContext.request.contextPath}/home"><i class="bx bx-log-out"></i>Đăng xuất</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Banner</h2>
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
    <div class="banner-summary">
        <div class="card">
            <p>Tổng Banner</p>
            <h3></h3>
        </div>
        <div class="card">
            <p></p>
            <h3 class="active"></h3>
        </div>
        <div class="card">
            <p></p>
            <h3 class="inactive"></h3>
        </div>
    </div>
    <div class="search-review-box">
        <i class="bx bx-search"></i>
        <input type="text" placeholder="Tìm kiếm...">
    </div>
    <div class="banner-action">
        <button class="btn-add">
            <i class="bx bx-plus"></i> Thêm Banner Mới
        </button>
    </div>

    <div class="table card">
        <table>
            <thead>
            <tr>
                <th>#</th>
                <th>Hình ảnh</th>
                <th>Tiêu đề</th>
                <th>Đường dẫn</th>
                <th>Trạng thái</th>
                <th>Ngày tạo</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody>

            <tr>
                <td></td>
                <td><img src="img/banner1.jpg" class="banner-img"></td>
                <td></td>
                <td></td>
                <td><span class="status active"></span></td>
                <td></td>
                <td>
                    <i class="bx bx-edit action edit"></i>
                    <i class="bx bx-trash action delete"></i>
                </td>
            </tr>
            

            </tbody>
        </table>
    </div>
</main>

</body>
</html>