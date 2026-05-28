<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin - Quản lý đơn hàng</title>
  <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_DonHang.css">
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
    <li class="active"><a href="${pageContext.request.contextPath}/admin/orders"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
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
    <h2>Quản lý đơn hàng</h2>
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