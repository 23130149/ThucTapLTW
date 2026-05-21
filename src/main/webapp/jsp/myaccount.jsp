<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="util" uri="http://handmade/Util" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Tài khoản của tôi - Handmade House</title>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/account.css">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/header_footer.css">
  <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
</head>

<body>
<header class="header">
  <div class="header-top-container">
    <div class="header-content">
      <div class="logo">
        <a href="${pageContext.request.contextPath}/home">Handmade House</a>
      </div>
      <form class="search-form" action="${pageContext.request.contextPath}/product" method="GET">
        <input type="text" class="search-input" name="keyword" value="${keyword}" placeholder="Tìm kiếm bất cứ thứ gì" aria-label="Tìm kiếm sản phẩm" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false">
        <button type="submit" class="search-btn">
          <i class="bx bx-search-alt-2"></i>
        </button>
      </form>
      <div class="icons">
        <a href="${pageContext.request.contextPath}/favorite" class="icon-btn favorite-header-icon" id="heartBtn" title="Sản phẩm yêu thích">
          <i class='bx bx-heart'></i>
        </a>
        <a href="${pageContext.request.contextPath}/cart" class="icon-btn cart-icon" id="cartBtn">
          <i class='bx  bx-cart'></i>
        
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                        <span class="cart-badge">${sessionScope.cart.totalQuantity}</span>
                    </c:if>
                </a>
        <a href="${pageContext.request.contextPath}/Account" class="icon-btn" id="userBtn">
          <i class='bx  bx-user'></i>
        </a>
      </div>
    </div>
  </div>
  <div class="search-bar-section header-bottom-nav">
    <div class="container nav-only-container">
      <nav class="nav__links">
        <ul>
          <li><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
          <li><a href="${pageContext.request.contextPath}/product">Sản phẩm</a></li>
          <li><a href="${pageContext.request.contextPath}/jsp/blog.jsp">Blog</a></li>
          <li><a href="${pageContext.request.contextPath}/jsp/contact.jsp">Liên hệ</a></li>
        </ul>
      </nav>
    </div>
  </div>
</header>
<main class="about-us-container account-dashboard">
  <h1>Tài khoản của tôi</h1>

  <div class="account-hero">
    <div class="account-avatar">
      <i class='bx bxs-user-circle'></i>
    </div>

    <div class="account-hero-text">
      <p>Xin chào</p>
      <h2>
        <c:choose>
          <c:when test="${empty sessionScope.user.userName}">
            Khách hàng
          </c:when>
          <c:otherwise>
            ${sessionScope.user.userName}
          </c:otherwise>
        </c:choose>
      </h2>
      <span>${sessionScope.user.email}</span>
    </div>
  </div>

  <div class="account-menu-grid">
    <a href="${pageContext.request.contextPath}/OrderHistory" class="account-menu-card">
      <span class="account-menu-icon"><i class='bx bx-receipt'></i></span>
      <span>
        <strong>Lịch sử đơn hàng</strong>
        <small>Xem lại các đơn đã mua</small>
      </span>
      <i class='bx bx-chevron-right'></i>
    </a>

    <a href="${pageContext.request.contextPath}/Profile" class="account-menu-card">
      <span class="account-menu-icon"><i class='bx bx-edit-alt'></i></span>
      <span>
        <strong>Thông tin cá nhân</strong>
        <small>Cập nhật hồ sơ tài khoản</small>
      </span>
      <i class='bx bx-chevron-right'></i>
    </a>

    <a href="${pageContext.request.contextPath}/ChangePassword" class="account-menu-card">
      <span class="account-menu-icon"><i class='bx bx-lock-alt'></i></span>
      <span>
        <strong>Đổi mật khẩu</strong>
        <small>Bảo mật tài khoản</small>
      </span>
      <i class='bx bx-chevron-right'></i>
    </a>

    <a href="${pageContext.request.contextPath}/Address" class="account-menu-card">
      <span class="account-menu-icon"><i class='bx bx-map'></i></span>
      <span>
        <strong>Sổ địa chỉ</strong>
        <small>Quản lý địa chỉ nhận hàng</small>
      </span>
      <i class='bx bx-chevron-right'></i>
    </a>

    <c:if test="${sessionScope.user.role == 'ADMIN'}">
      <a href="${pageContext.request.contextPath}/admin/dashboard" class="account-menu-card admin-switch-card">
        <span class="account-menu-icon"><i class='bx bx-shield-quarter'></i></span>
        <span>
          <strong>Trang quản trị</strong>
          <small>Chuyển sang giao diện admin</small>
        </span>
        <i class='bx bx-chevron-right'></i>
      </a>
    </c:if>
  </div>

  <div class="recent-orders-box account-recent-orders">
    <div class="section-title-row account-orders-title">
      <div>
        <h2>
          <c:choose>
            <c:when test="${recentLimit == 10}">
              10 đơn hàng gần đây
            </c:when>
            <c:otherwise>
              5 đơn hàng gần đây
            </c:otherwise>
          </c:choose>
        </h2>
        <p>Theo dõi nhanh các đơn mua mới nhất của bạn</p>
      </div>

      <c:if test="${not empty orderList && recentLimit < 10}">
        <a href="${pageContext.request.contextPath}/Account?recent=10"
           class="btn-account-secondary">
          <i class='bx bx-right-arrow-alt'></i>
          Xem thêm
        </a>
      </c:if>

      <c:if test="${not empty orderList && recentLimit == 10}">
        <a href="${pageContext.request.contextPath}/Account"
           class="btn-account-secondary">
          <i class='bx bx-up-arrow-alt'></i>
          Thu gọn
        </a>
      </c:if>
    </div>

    <c:choose>
      <c:when test="${empty orderList}">
        <p class="empty-account-state">Bạn chưa có đơn hàng nào.</p>
      </c:when>

      <c:otherwise>
        <table class="orders-table">
          <thead>
          <tr>
            <th>Mã đơn</th>
            <th>Ngày đặt</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Chi tiết</th>
          </tr>
          </thead>

          <tbody>
          <c:forEach var="order" items="${orderList}">
            <tr>
              <td>
                <a class="order-code-link"
                   href="${pageContext.request.contextPath}/OrderDetail?orderId=${order.orderId}">
                    ${order.orderCode}
                </a>
              </td>
              <td>${util:formatDateTime(order.createAt)}</td>
              <td>${util:formatMoney(order.totalPrice)}</td>
              <td>
                <span class="order-status ${order.status}">
                  ${util:orderStatusIcon(order.status)}
                  ${util:orderStatusLabel(order.status)}
                </span>
              </td>
              <td>
                <a class="btn-order-detail"
                   href="${pageContext.request.contextPath}/OrderDetail?orderId=${order.orderId}">
                  Xem chi tiết
                </a>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:otherwise>
    </c:choose>
  </div>

  <div class="account-bottom-actions">
  <a href="${pageContext.request.contextPath}/OrderHistory" class="btn-account-primary">
    <i class='bx bx-receipt'></i>
    Xem tất cả
  </a>

  <a href="${pageContext.request.contextPath}/Logout" class="btn-account-danger">
    <i class='bx bx-log-out'></i>
    Đăng xuất
  </a>
</div>

</main>
<footer class="footer">
  <div class="container">

    <div class="footer-content">

      <div class="footer-column">
        <h3 class="footer-logo">Handmade House</h3>
        <p class="footer-desc">
          Chào mừng đến với Handmade House, ngôi nhà nhỏ của những tâm hồn
          yêu nghệ thuật và thủ công.
        </p>
        <div class="social-links">
          <a href="#"><i class="bx bxl-facebook"></i></a>
          <a href="#"><i class="bx bxl-instagram"></i></a>
          <a href="#"><i class="bx bxl-tiktok"></i></a>
        </div>
      </div>

      <div class="footer-column">
        <h3 class="footer-title">Blog</h3>
        <ul class="footer-links">
          <li><a href="#">Câu chuyện thương hiệu</a></li>
          <li><a href="#">Giá trị & Triết lý</a></li>
          <li><a href="#">Quy trình sản xuất</a></li>
          <li><a href="#">Định hướng bền vững</a></li>
        </ul>
      </div>

      <div class="footer-column">
        <h3 class="footer-title">Hỗ trợ</h3>
        <ul class="footer-links">
          <li><a href="#">Chính sách đổi trả</a></li>
          <li><a href="#">Hướng dẫn đặt hàng</a></li>
          <li><a href="#">Phương thức thanh toán</a></li>
          <li><a href="#">FAQ</a></li>
        </ul>
      </div>

      <div class="footer-column">
        <h3 class="footer-title">Liên hệ</h3>
        <ul class="footer-links">
          <li>📍 Linh Trung, Thủ Đức, TP.HCM</li>
          <li>📞 0944 912 685</li>
          <li>📧 handmadehouse23@handmade.vn</li>
          <li>🕐 8:00 - 17:00 (T2 - CN)</li>
        </ul>
      </div>

    </div>

    <div class="footer-bottom">
      <p>© 2025 Handmade House. Tất cả quyền được bảo lưu.</p>
    </div>

  </div>
</footer>
</body>
</html>
