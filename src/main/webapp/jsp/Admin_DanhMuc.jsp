
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
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
            <li class="active"><a href="../Tongquan/tongquan.html"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li><a href="../../../../../../trangadmin/Sanpham/qlsanpham.html"><i class="bx bx-package"></i>Sản phẩm</a>
            </li>
            <li><a href="../../../../../../trangadmin/Donhang/donhang.html"><i class="bx bx-receipt"></i>Đơn hàng</a>
            </li>
            <li><a href="../../../../../../trangadmin/Khachhang/khachhang.html"><i class="bx bx-group"></i>Khách
                hàng</a></li>
            <li><a href="../../../../../../trangadmin/Danhgia/danhgia.html"><i class="bx bx-star"></i>Đánh giá</a></li>
            <li><a href="../../../../../../trangadmin/Caidat/caidat.html"><i class="bx bx-cog"></i>Cài đặt</a></li>

        </ul>
    </nav>
    <div class="logout">
        <a href="../../../../../../html/trangchu.html"><i class="bx bx-log-out"></i>Đăng xuất</a>
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

</body>
</html>
