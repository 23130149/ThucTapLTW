<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
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
    <a class="tab-btn ${empty currentStatus ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?keyword=${keyword}">
      <i class="bx bx-list-ul"></i>
      Tất cả <span class="count">${allCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'PENDING' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=PENDING&keyword=${keyword}">
      <i class="bx bx-time-five"></i>
      Chờ xác nhận <span class="count">${pendingCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'CONFIRMED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=CONFIRMED&keyword=${keyword}">
      <i class="bx bx-check-circle"></i>
      Đã xác nhận <span class="count">${confirmedCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'SHIPPED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=SHIPPED&keyword=${keyword}">
      <i class="bx bx-package"></i>
      Đang giao <span class="count">${shippedCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'COMPLETED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=COMPLETED&keyword=${keyword}">
      <i class="bx bx-check-double"></i>
      Hoàn thành <span class="count">${completedCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'CANCELLED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=CANCELLED&keyword=${keyword}">
      <i class="bx bx-x-circle"></i>
      Đã hủy <span class="count">${cancelledCount}</span>
    </a>
  </div>
  <div class="search-filter-row">
    <form method="get" action="${pageContext.request.contextPath}/admin/orders" class="search-review-box">
      <c:if test="${not empty currentStatus}">
        <input type="hidden" name="status" value="${currentStatus}">
      </c:if>

      <i class="bx bx-search"></i>
      <input type="text" name="keyword" value="${keyword}" placeholder="Tìm kiếm đơn hàng...">
    </form>

    <div class="action-group">
      <a href="${pageContext.request.contextPath}/admin/orders" class="filter-button-icon">
        <i class="bx bx-refresh"></i>Làm mới
      </a>

      <button type="button" class="view-all-btn" onclick="window.print()">
        <i class="bx bx-printer"></i>In hóa đơn
      </button>
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
      <c:choose>
        <c:when test="${empty orders}">
          <tr>
            <td colspan="8" class="empty-row">
              Không tìm thấy đơn hàng nào.
            </td>
          </tr>
        </c:when>

        <c:otherwise>
          <c:forEach items="${orders}" var="order">
            <c:set var="statusClass" value="status-pending"/>
            <c:set var="statusText" value="Chờ xác nhận"/>

            <c:choose>
              <c:when test="${order.status == 'CONFIRMED'}">
                <c:set var="statusClass" value="status-confirmed"/>
                <c:set var="statusText" value="Đã xác nhận"/>
              </c:when>

              <c:when test="${order.status == 'SHIPPED'}">
                <c:set var="statusClass" value="status-shipping"/>
                <c:set var="statusText" value="Đang giao"/>
              </c:when>

              <c:when test="${order.status == 'COMPLETED'}">
                <c:set var="statusClass" value="status-completed"/>
                <c:set var="statusText" value="Hoàn thành"/>
              </c:when>

              <c:when test="${order.status == 'CANCELLED'}">
                <c:set var="statusClass" value="status-cancelled"/>
                <c:set var="statusText" value="Đã hủy"/>
              </c:when>
            </c:choose>

            <tr>
              <td>
                <strong>
                  <c:choose>
                    <c:when test="${not empty order.orderCode}">
                      ${order.orderCode}
                    </c:when>
                    <c:otherwise>
                      #${order.orderId}
                    </c:otherwise>
                  </c:choose>
                </strong>
              </td>

              <td>
                <c:choose>
                  <c:when test="${not empty order.shipName}">
                    ${order.shipName}
                  </c:when>
                  <c:when test="${not empty order.userName}">
                    ${order.userName}
                  </c:when>
                  <c:otherwise>Khách hàng</c:otherwise>
                </c:choose>
              </td>

              <td>${order.totalQuantity} sản phẩm</td>

              <td>${order.totalPriceFormatted}</td>

              <td>${order.createAtFormatted}</td>

              <td>
                <c:choose>
                  <c:when test="${order.paymentStatus == 'PAID'}">
                    <span class="payment payment-paid">Đã thanh toán</span>
                  </c:when>

                  <c:when test="${order.paymentStatus == 'FAILED'}">
                    <span class="payment payment-failed">Thanh toán lỗi</span>
                  </c:when>

                  <c:otherwise>
                    <span class="payment payment-unpaid">Chưa thanh toán</span>
                  </c:otherwise>
                </c:choose>
              </td>

              <td>
                <span class="status ${statusClass}">${statusText}</span>
              </td>

              <td>
                <div class="table-actions">
                  <a href="${pageContext.request.contextPath}/admin/orders?detailId=${order.orderId}&status=${currentStatus}&keyword=${keyword}"
                     class="action-link"
                     title="Xem chi tiết">
                    <i class="bx bx-show-alt action-icon"></i>
                  </a>

                  <c:if test="${order.status == 'PENDING'}">
                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/orders"
                          class="inline-form">
                      <input type="hidden" name="action" value="updateStatus">
                      <input type="hidden" name="orderId" value="${order.orderId}">
                      <input type="hidden" name="status" value="CONFIRMED">

                      <button type="submit" class="action-btn confirm-btn" title="Xác nhận đơn hàng">
                        <i class="bx bx-check"></i>
                      </button>
                    </form>
                  </c:if>

                  <c:if test="${order.status == 'CONFIRMED'}">
                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/orders"
                          class="inline-form">
                      <input type="hidden" name="action" value="updateStatus">
                      <input type="hidden" name="orderId" value="${order.orderId}">
                      <input type="hidden" name="status" value="SHIPPED">

                      <button type="submit" class="action-btn ship-btn" title="Chuyển sang đang giao">
                        <i class="bx bx-package"></i>
                      </button>
                    </form>
                  </c:if>

                  <c:if test="${order.status == 'SHIPPED'}">
                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/orders"
                          class="inline-form">
                      <input type="hidden" name="action" value="updateStatus">
                      <input type="hidden" name="orderId" value="${order.orderId}">
                      <input type="hidden" name="status" value="COMPLETED">

                      <button type="submit" class="action-btn complete-btn" title="Hoàn thành đơn hàng">
                        <i class="bx bx-check-double"></i>
                      </button>
                    </form>
                  </c:if>
                </div>
              </td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
      </tbody>
    </table>
  </div>
</main>

<c:if test="${not empty selectedOrder}">
  <div id="orderDetailModal" class="modal" style="display: flex;">
    <div class="modal-content">
      <a href="${pageContext.request.contextPath}/admin/orders?status=${currentStatus}&keyword=${keyword}" class="close-btn">
        &times;
      </a>

      <h3>Chi Tiết Hóa Đơn</h3>
      <hr>

      <div id="orderDetailBody">
        <div class="order-info-grid">
          <p>
            <strong>Mã đơn:</strong>
            <c:choose>
              <c:when test="${not empty selectedOrder.orderCode}">
                ${selectedOrder.orderCode}
              </c:when>
              <c:otherwise>
                #${selectedOrder.orderId}
              </c:otherwise>
            </c:choose>
          </p>

          <p>
            <strong>Khách hàng:</strong>
            <c:choose>
              <c:when test="${not empty selectedOrder.shipName}">
                ${selectedOrder.shipName}
              </c:when>
              <c:when test="${not empty selectedOrder.userName}">
                ${selectedOrder.userName}
              </c:when>
              <c:otherwise>Khách hàng</c:otherwise>
            </c:choose>
          </p>

          <p>
            <strong>Số điện thoại:</strong>
            <c:choose>
              <c:when test="${not empty selectedOrder.shipPhone}">
                ${selectedOrder.shipPhone}
              </c:when>
              <c:otherwise>Chưa có</c:otherwise>
            </c:choose>
          </p>

          <p>
            <strong>Địa chỉ giao hàng:</strong>
            <c:choose>
              <c:when test="${not empty selectedOrder.shipAddress}">
                ${selectedOrder.shipAddress}
              </c:when>
              <c:otherwise>Chưa có</c:otherwise>
            </c:choose>
          </p>

          <p><strong>Ngày đặt:</strong> ${selectedOrder.createAtFormatted}</p>
          <p><strong>Tổng tiền:</strong> ${selectedOrder.totalPriceFormatted}</p>

          <p>
            <strong>Ghi chú:</strong>
            <c:choose>
              <c:when test="${not empty selectedOrder.note}">
                ${selectedOrder.note}
              </c:when>
              <c:otherwise>Không có</c:otherwise>
            </c:choose>
          </p>

          <p>
            <strong>Thanh toán:</strong>
            <c:choose>
              <c:when test="${selectedOrder.paymentStatus == 'PAID'}">
                Đã thanh toán
              </c:when>

              <c:when test="${selectedOrder.paymentStatus == 'FAILED'}">
                Thanh toán lỗi
              </c:when>

              <c:otherwise>
                Chưa thanh toán
              </c:otherwise>
            </c:choose>
          </p>
        </div>

        <h4 class="detail-title">Sản phẩm trong đơn</h4>

        <table class="detail-table">
          <thead>
          <tr>
            <th>Sản phẩm</th>
            <th>Số lượng</th>
            <th>Đơn giá</th>
            <th>Thành tiền</th>
          </tr>
          </thead>

          <tbody>
          <c:choose>
            <c:when test="${empty selectedOrderItems}">
              <tr>
                <td colspan="4" class="empty-row">
                  Đơn hàng chưa có sản phẩm.
                </td>
              </tr>
            </c:when>

            <c:otherwise>
              <c:forEach items="${selectedOrderItems}" var="item">
                <tr>
                  <td>${item.productName}</td>
                  <td>${item.quantity}</td>
                  <td>${item.unitPriceFormatted}</td>
                  <td>${item.totalPriceFormatted}</td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
          </tbody>
        </table>
      </div>

      <div class="modal-footer">
        <button onclick="window.print()" class="btn-print">In hóa đơn</button>
      </div>
    </div>
  </div>
</c:if>

<div id="toast-container"></div>

</body>
</html>