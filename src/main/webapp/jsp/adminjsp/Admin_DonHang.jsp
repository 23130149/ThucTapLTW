<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin - Quản lý đơn hàng</title>
  <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
  <link rel="stylesheet" href="donhang.css">
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
      <li class="active"><a href="../Donhang/donhang.html"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
      <li><a href="../Khachhang/khachhang.html"><i class="bx bx-group"></i>Khách hàng</a></li>
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
    <h2>Đơn hàng</h2>
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
  <div class="order-status-tabs">
    <button class="tab-btn active">Tất cả</button>
    <button class="tab-btn">Hoàn thành <span class="count">2</span></button>
    <button class="tab-btn">Đang xử lý <span class="count">1</span></button>
    <button class="tab-btn">Chờ xác nhận <span class="count">1</span></button>
    <button class="tab-btn">Đã hủy <span class="count">1</span></button>
  </div>
  <div class="search-filter-row">
    <div class="search-review-box">
      <i class="bx bx-search"></i>
      <input type="text" placeholder="Tìm kiếm đơn hàng...">
    </div>
    <div class="action-group">
      <button class="filter-button-icon"><i class="bx bx-filter"></i>Lọc</button>
      <button class="view-all-btn"><i class="bx bx-download"></i>In hóa đơn</button>
    </div>
  </div>
  <div class="order-table-container">
    <table class="data-table">
      <thead>
      <tr>
        <th>Mã đơn</th>
        <th>Khách hàng</th>
        <th>Sản phẩm</th>
        <th>Tổng tiền</th>
        <th>Ngày đặt</th>
        <th>Thanh toán</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td>#DH001</td>
        <td>Nguyễn Thanh Phú</td>
        <td>2 sản phẩm</td>
        <td>178.000đ</td>
        <td>15/10/2025</td>
        <td>Đã thanh toán</td>
        <td><span class="status status-completed">Hoàn thành</span></td>
        <td>
          <i class="bx bx-show-alt action-icon"></i>
        </td>
      </tr>
      <tr>
        <td>#DH002</td>
        <td>Lê Viết Khanh</td>
        <td>1 sản phẩm</td>
        <td>15.000đ</td>
        <td>2/9/2025</td>
        <td>Đã thanh toán</td>
        <td><span class="status status-shipping">Đang xử lý</span></td>
        <td>
          <i class="bx bx-show-alt action-icon"></i>
        </td>
      </tr>
      <tr>
        <td>#DH003</td>
        <td>Trần Hoàng Quân</td>
        <td>1 sản phẩm</td>
        <td>150.000đ</td>
        <td>17/9/2025</td>
        <td>Chưa thanh toán</td>
        <td><span class="status status-pending">Chờ xác nhận</span></td>
        <td>
          <i class="bx bx-show-alt action-icon"></i>
        </td>
      </tr>
      <tr>
        <td>#DH004</td>
        <td>Nuyễn Lê Tiến Đạt</td>
        <td>3 sản phẩm</td>
        <td>90.000đ</td>
        <td>10/10/2025</td>
        <td>Đã thanh toán</td>
        <td><span class="status status-completed">Hoàn thành</span></td>
        <td>
          <i class="bx bx-show-alt action-icon"></i>
        </td>
      </tr>
      <tr>
        <td>#DH005</td>
        <td>Nguyễn Huy Bảo</td>
        <td>1 sản phẩm</td>
        <td>120.000đ</td>
        <td>12/11/2025</td>
        <td>Đã hoàn tiền</td>
        <td><span class="status status-cancelled">Đã hủy</span></td>
        <td>
          <i class="bx bx-show-alt action-icon"></i>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</main>
<div id="toast-container"></div>

<div id="orderDetailModal" class="modal">
  <div class="modal-content">
    <span class="close-btn">&times;</span>
    <h3>Chi Tiết Hóa Đơn</h3>
    <hr>
    <div id="orderDetailBody"></div>
    <div class="modal-footer">
      <button onclick="window.print()" class="btn-print">In hóa đơn</button>
    </div>
  </div>
</div>
</body>
</html>