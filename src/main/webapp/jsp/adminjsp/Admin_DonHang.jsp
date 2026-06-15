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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-font-standard.css">
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
      <i class="bx bx-home-alt-2"></i> Trang chủ
    </a>
  </div>
</aside>
<main class="main-content">
  <header class="header">
    <h2>Quản lý đơn hàng</h2>
    <div class="user-info">
      <div class="notification-wrapper">
        <a href="javascript:void(0)" class="notification-btn">
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
    <c:choose>
        <c:when test="${accessDenied}">
            <div class="admin-alert error">
                <i class="bx bx-error-circle"></i>
                    ${accessDeniedMessage}
            </div>
        </c:when>

        <c:otherwise>
  <c:if test="${not empty sessionScope.adminOrderMessage}">
    <div class="admin-order-message">${sessionScope.adminOrderMessage}</div>
    <c:remove var="adminOrderMessage" scope="session"/>
  </c:if>
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

    <a class="tab-btn ${currentStatus == 'PENDING_PAYMENT' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=PENDING_PAYMENT&keyword=${keyword}">
      <i class="bx bx-credit-card"></i>
      Chờ thanh toán <span class="count">${pendingPaymentCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'PAYMENT_FAILED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=PAYMENT_FAILED&keyword=${keyword}">
      <i class="bx bx-error-circle"></i>
      Thanh toán lỗi <span class="count">${paymentFailedCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'PROCESSING' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=PROCESSING&keyword=${keyword}">
      <i class="bx bx-loader-circle"></i>
      Đang xử lý <span class="count">${processingCount}</span>
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

    <a class="tab-btn ${currentStatus == 'DELIVERED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=DELIVERED&keyword=${keyword}">
      <i class="bx bx-check-shield"></i>
      Chờ xác nhận <span class="count">${deliveredCount}</span>
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
    <a class="tab-btn ${currentStatus == 'RETURN_REQUESTED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=RETURN_REQUESTED&keyword=${keyword}">
      <i class="bx bx-undo"></i>
      Yêu cầu trả <span class="count">${returnRequestedCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'RETURNED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=RETURNED&keyword=${keyword}">
      <i class="bx bx-check-shield"></i>
      Đã trả <span class="count">${returnedCount}</span>
    </a>

    <a class="tab-btn ${currentStatus == 'RETURN_REJECTED' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/admin/orders?status=RETURN_REJECTED&keyword=${keyword}">
      <i class="bx bx-x"></i>
      Từ chối trả <span class="count">${returnRejectedCount}</span>
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

              <c:when test="${order.status == 'PENDING_PAYMENT'}">
                <c:set var="statusClass" value="status-payment"/>
                <c:set var="statusText" value="Chờ thanh toán"/>
              </c:when>

              <c:when test="${order.status == 'PAYMENT_FAILED'}">
                <c:set var="statusClass" value="status-cancelled"/>
                <c:set var="statusText" value="Thanh toán lỗi"/>
              </c:when>

              <c:when test="${order.status == 'PROCESSING'}">
                <c:set var="statusClass" value="status-processing"/>
                <c:set var="statusText" value="Đang xử lý"/>
              </c:when>

              <c:when test="${order.status == 'SHIPPED'}">
                <c:set var="statusClass" value="status-shipping"/>
                <c:set var="statusText" value="Đang giao"/>
              </c:when>

              <c:when test="${order.status == 'DELIVERED'}">
                <c:set var="statusClass" value="status-delivered"/>
                <c:set var="statusText" value="GHN đã giao"/>
              </c:when>

              <c:when test="${order.status == 'COMPLETED'}">
                <c:set var="statusClass" value="status-completed"/>
                <c:set var="statusText" value="Hoàn thành"/>
              </c:when>

              <c:when test="${order.status == 'CANCELLED'}">
                <c:set var="statusClass" value="status-cancelled"/>
                <c:set var="statusText" value="Đã hủy"/>
              </c:when>
              <c:when test="${order.status == 'RETURN_REQUESTED'}">
                <c:set var="statusClass" value="status-return"/>
                <c:set var="statusText" value="Yêu cầu trả hàng"/>
              </c:when>

              <c:when test="${order.status == 'RETURNED'}">
                <c:set var="statusClass" value="status-returned"/>
                <c:set var="statusText" value="Đã trả hàng"/>
              </c:when>

              <c:when test="${order.status == 'RETURN_REJECTED'}">
                <c:set var="statusClass" value="status-cancelled"/>
                <c:set var="statusText" value="Từ chối trả hàng"/>
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
                    <span class="payment payment-paid">Đã thu tiền</span>
                  </c:when>

                  <c:when test="${order.paymentStatus == 'FAILED'}">
                    <span class="payment payment-failed">Thanh toán lỗi</span>
                  </c:when>

                  <c:otherwise>
                    <span class="payment payment-unpaid">Chưa thu tiền</span>
                  </c:otherwise>
                </c:choose>
              </td>

              <td>
                <c:if test="${order.status == 'CONFIRMED' && not empty order.ghnOrderCode}">
                  <c:set var="statusText" value="Đang chuẩn bị hàng"/>
                </c:if>
                <span class="status ${statusClass}">${statusText}</span>
                <c:if test="${not empty order.ghnOrderCode}">
                  <strong class="ghn-code">${order.ghnOrderCode}</strong>
                  <small class="ghn-status">${order.ghnStatusLabel}</small>
                </c:if>
              </td>

              <td>
                <div class="table-actions">
                  <a href="${pageContext.request.contextPath}/admin/orders?detailId=${order.orderId}&status=${currentStatus}&keyword=${keyword}"
                     class="action-link"
                     title="Xem chi tiết">
                    <i class="bx bx-show-alt"></i>
                    <span>Xem</span>
                  </a>

                  <c:choose>
                    <c:when test="${fn:contains(sessionScope.permissionCodesText, ',MANAGE_ORDER,')}">

                      <c:if test="${order.status == 'PENDING'}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/orders"
                              class="inline-form">
                          <input type="hidden" name="action" value="updateStatus">
                          <input type="hidden" name="orderId" value="${order.orderId}">
                          <input type="hidden" name="status" value="PROCESSING">

                          <button type="submit" class="action-btn confirm-btn" title="Chuyển sang đang xử lý">
                            <i class="bx bx-check"></i>
                            <span>Xử lý</span>
                          </button>
                        </form>
                      </c:if>

                      <c:if test="${order.status == 'PROCESSING'}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/orders"
                              class="inline-form">
                          <input type="hidden" name="action" value="updateStatus">
                          <input type="hidden" name="orderId" value="${order.orderId}">
                          <input type="hidden" name="status" value="CONFIRMED">

                          <button type="submit" class="action-btn confirm-btn" title="Xác nhận đơn hàng">
                            <i class="bx bx-check-circle"></i>
                            <span>Xác nhận</span>
                          </button>
                        </form>
                      </c:if>

                      <c:if test="${order.status == 'CONFIRMED' && empty order.ghnOrderCode}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/orders"
                              class="inline-form">
                          <input type="hidden" name="action" value="createGhn">
                          <input type="hidden" name="orderId" value="${order.orderId}">

                          <button type="submit" class="action-btn ship-btn" title="Tạo vận đơn GHN">
                            <i class="bx bx-package"></i>
                            <span>Tạo GHN</span>
                          </button>
                        </form>
                      </c:if>

                      <c:if test="${not empty order.ghnOrderCode && order.status == 'CONFIRMED'}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/orders"
                              class="inline-form">
                          <input type="hidden" name="action" value="syncGhn">
                          <input type="hidden" name="orderId" value="${order.orderId}">

                          <button type="submit" class="action-btn ship-btn" title="Giao hàng cho GHN">
                            <i class="bx bx-send"></i>
                            <span>Giao GHN</span>
                          </button>
                        </form>
                      </c:if>

                      <c:if test="${not empty order.ghnOrderCode && order.status == 'SHIPPED'}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/orders"
                              class="inline-form">
                          <input type="hidden" name="action" value="syncGhn">
                          <input type="hidden" name="orderId" value="${order.orderId}">

                          <button type="submit" class="action-btn complete-btn" title="Cập nhật trạng thái vận chuyển">
                            <i class="bx bx-refresh"></i>
                            <span>Cập nhật</span>
                          </button>
                        </form>
                      </c:if>

                      <c:if test="${order.status == 'RETURN_REQUESTED'}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/orders"
                              class="inline-form">
                          <input type="hidden" name="action" value="updateStatus">
                          <input type="hidden" name="orderId" value="${order.orderId}">
                          <input type="hidden" name="status" value="RETURNED">

                          <button type="submit" class="action-btn complete-btn" title="Xác nhận đã trả hàng">
                            <i class="bx bx-check-double"></i>
                            <span>Đã trả</span>
                          </button>
                        </form>

                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/orders"
                              class="inline-form">
                          <input type="hidden" name="action" value="updateStatus">
                          <input type="hidden" name="orderId" value="${order.orderId}">
                          <input type="hidden" name="status" value="RETURN_REJECTED">

                          <button type="submit" class="action-btn reject-btn" title="Từ chối trả hàng">
                            <i class="bx bx-x"></i>
                            <span>Từ chối</span>
                          </button>
                        </form>
                      </c:if>

                      <c:if test="${order.paymentStatus != 'PAID'
                              && ((order.paymentMethodId == 1 && (order.status == 'DELIVERED' || order.status == 'COMPLETED'))
                                  || (order.paymentMethodId == 2
                                      && order.status != 'CANCELLED'
                                      && order.status != 'PAYMENT_FAILED'
                                      && order.status != 'RETURNED'))}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/orders"
                              class="inline-form">
                          <input type="hidden" name="action" value="markPaymentPaid">
                          <input type="hidden" name="orderId" value="${order.orderId}">

                          <button type="submit" class="action-btn paid-btn" title="Xác nhận đã thu tiền">
                            <i class="bx bx-money"></i>
                            <span>Đã thu tiền</span>
                          </button>
                        </form>
                      </c:if>

                    </c:when>

                    <c:otherwise>
                      <span class="no-permission-text">Chỉ xem</span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
      </tbody>
    </table>
  </div>
</c:otherwise>
</c:choose>
</main>
<c:if test="${not accessDenied}">
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
            <strong>Mã vận đơn GHN:</strong>
            <c:choose>
              <c:when test="${not empty selectedOrder.ghnOrderCode}">${selectedOrder.ghnOrderCode}</c:when>
              <c:otherwise>Chưa tạo vận đơn</c:otherwise>
            </c:choose>
          </p>

          <p><strong>Trạng thái GHN:</strong> ${selectedOrder.ghnStatusLabel}</p>

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
                Đã thu tiền
              </c:when>

              <c:when test="${selectedOrder.paymentStatus == 'FAILED'}">
                Thanh toán lỗi
              </c:when>

              <c:otherwise>
                Chưa thu tiền
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
</c:if>
<script defer src="${pageContext.request.contextPath}/js/ajax-enhance.js?v=20260615-1"></script>
<script>
  document.addEventListener("DOMContentLoaded", function () {
    const wrappers = document.querySelectorAll(".notification-wrapper");

    wrappers.forEach(function (wrapper) {
      const button = wrapper.querySelector(".notification-btn");
      const dropdown = wrapper.querySelector(".notification-dropdown");

      button.addEventListener("click", function (event) {
        event.preventDefault();
        event.stopPropagation();

        wrappers.forEach(function (item) {
          if (item !== wrapper) {
            item.classList.remove("active");
          }
        });

        wrapper.classList.toggle("active");
      });

      dropdown.addEventListener("click", function (event) {
        event.stopPropagation();
      });
    });

    document.addEventListener("click", function () {
      wrappers.forEach(function (wrapper) {
        wrapper.classList.remove("active");
      });
    });
  });
</script>
</body>
</html>
