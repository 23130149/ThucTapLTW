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
  <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>

<body>
<jsp:include page="/jsp/header.jsp"/>
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
<jsp:include page="/jsp/footer.jsp"/>


<script>
  window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
