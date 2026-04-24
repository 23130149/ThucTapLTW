
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý khách hàng</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="khachhang.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li><a href="../Tongquan/tongquan.html"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li><a href="../Sanpham/qlsanpham.html"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="../Donhang/donhang.html"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li class="active"><a href="../Khachhang/khachhang.html"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="../Danhgia/danhgia.html"><i class="bx bx-star"></i>Đánh giá</a></li>
            <li><a href="../Caidat/caidat.html"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="../../../../../../html/trangchu.html"><i class="bx bx-log-out"></i>Đăng xuất</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Khách hàng</h2>
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
    <div class="customer-summary-grid">
        <div class="summary-card">
            <p>Tổng khách hàng</p>
            <span class="summary-value">535</span>
            <span class="summary-detail growth">+8% tháng này</span>
        </div>
        <div class="summary-card vip">
            <p>Khách VIP</p>
            <span class="summary-value">87</span>
            <span class="summary-detail growth">+12% tháng này</span>
        </div>
        <div class="summary-card new">
            <p>Khách mới</p>
            <span class="summary-value">156</span>
            <span class="summary-detail growth">+23% tháng này</span>
        </div>
        <div class="summary-card aov">
            <p>Giá trị TB/Khách</p>
            <span class="summary-value">₫3.6M</span>
            <span class="summary-detail decline">-7% tháng này</span>
        </div>
    </div>
    <div class="customer-search-filter-row">
        <div class="search-customer-box">
            <i class="bx bx-search"></i>
            <input type="text" placeholder="Tìm kiếm khách hàng...">
        </div>
        <button class="filter-button-icon"><i class="bx bx-filter"></i>Lọc</button>
        <button class="add-customer-btn"><i class="bx bx-plus"></i>Thêm khách hàng</button>
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
            <tr>
                <td class="customer-info">
                    <span class="customer-avatar">P</span>
                    Nguyễn Thanh Phú
                </td>
                <td>0944912685</td>
                <td>28</td>
                <td>15.000.000đ</td>
                <td>28/12/2024</td>
                <td><span class="customer-type-tag type-vip">Vip</span></td>
                <td>
                    <i class="bx bx-show-alt action-icon"></i>
                </td>
            </tr>
            <tr>
                <td class="customer-info">
                    <span class="customer-avatar">K</span>
                    Lê Viết Khanh
                </td>
                <td>098573125</td>
                <td>14</td>
                <td>3.000.000đ</td>
                <td>30/4/2025</td>
                <td><span class="customer-type-tag type-thuongxuyen">Thường xuyên</span></td>
                <td>
                    <i class="bx bx-show-alt action-icon"></i>
                </td>
            </tr>
            <tr>
                <td class="customer-info">
                    <span class="customer-avatar">Q</span>
                    Trần Hoàng Quân
                </td>
                <td>038757486</td>
                <td>20</td>
                <td>9.300.000đ</td>
                <td>12/1/2025</td>
                <td><span class="customer-type-tag type-moi">Mới</span></td>
                <td>
                    <i class="bx bx-show-alt action-icon"></i>
                </td>
            </tr>
            <tr>
                <td class="customer-info">
                    <span class="customer-avatar">Đ</span>
                    Nguyễn Lê Tiến Đạt
                </td>
                <td>0268565975</td>
                <td>3</td>
                <td>1.050.000đ</td>
                <td>2/10/2025</td>
                <td><span class="customer-type-tag type-thuongxuyen">Thường xuyên</span></td>
                <td>
                    <i class="bx bx-show-alt action-icon"></i>
                </td>
            </tr>
            <tr>
                <td class="customer-info">
                    <span class="customer-avatar">B</span>
                    Nguyễn Huy Bảo
                </td>
                <td>095838756</td>
                <td>10</td>
                <td>2.500.000đ</td>
                <td>12/8/2025</td>
                <td><span class="customer-type-tag type-vip">Vip</span></td>
                <td>
                    <i class="bx bx-show-alt action-icon"></i>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</main>
<div id="customerModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Chi Tiết Khách Hàng</h3>
            <span class="close-modal">&times;</span>
        </div>
        <div id="customerDetailBody" class="modal-body"></div>
    </div>
</div>

<div id="addCustomerModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Thêm Khách Hàng Mới</h3>
            <span class="close-add-modal">&times;</span>
        </div>
        <form id="addCustomerForm">
            <div class="form-group">
                <label>Họ và tên</label>
                <input type="text" id="newCustName" required>
            </div>
            <div class="form-group">
                <label>Số điện thoại</label>
                <input type="text" id="newCustPhone" required>
            </div>
            <div class="form-group">
                <label>Loại khách hàng</label>
                <select id="newCustType">
                    <option value="type-moi">Mới</option>
                    <option value="type-thuongxuyen">Thường xuyên</option>
                    <option value="type-vip">Vip</option>
                </select>
            </div>
            <button type="submit" class="btn-save">Lưu khách hàng</button>
        </form>
    </div>
</div>
<script src="khachhang.js"></script>
</body>
</html>