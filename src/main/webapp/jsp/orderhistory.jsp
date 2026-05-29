<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="util" uri="http://handmade/Util" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Lịch sử đơn hàng - Handmade House</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account.css">
  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<main class="about-us-container order-history-page">
  <h1>Lịch sử đơn hàng</h1>

  <c:if test="${not empty sessionScope.orderMessage}">
    <div class="form-alert form-alert-success">${sessionScope.orderMessage}</div>
    <c:remove var="orderMessage" scope="session" />
  </c:if>

  <div class="recent-orders-box order-history-box">
    <div class="section-title-row account-orders-title">
      <div>
        <h2>Đơn hàng của bạn</h2>
        <p>Theo dõi đơn hàng theo từng trạng thái, không còn cuộn dài mỏi tay.</p>
      </div>
    </div>

    <div class="order-status-tabs">
      <a class="tab-btn ${activeStatus == 'all' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/OrderHistory?status=all">
        Tất cả <span>${statusCounts.all}</span>
      </a>
      <a class="tab-btn ${activeStatus == 'processing' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/OrderHistory?status=processing">
        Đang xử lý <span>${statusCounts.processing}</span>
      </a>
      <a class="tab-btn ${activeStatus == 'shipping' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/OrderHistory?status=shipping">
        Đang giao <span>${statusCounts.shipping}</span>
      </a>
      <a class="tab-btn ${activeStatus == 'completed' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/OrderHistory?status=completed">
        Đã giao <span>${statusCounts.completed}</span>
      </a>
      <a class="tab-btn ${activeStatus == 'cancelled' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/OrderHistory?status=cancelled">
        Đã huỷ <span>${statusCounts.cancelled}</span>
      </a>
      <a class="tab-btn ${activeStatus == 'returned' ? 'active' : ''}"
         href="${pageContext.request.contextPath}/OrderHistory?status=returned">
        Trả hàng <span>${statusCounts.returned}</span>
      </a>
    </div>

    <c:choose>
      <c:when test="${empty orderList}">
        <p class="empty-account-state">Không có đơn hàng trong mục này.</p>
      </c:when>

      <c:otherwise>
        <table class="orders-table order-history-table">
          <thead>
          <tr>
            <th>Mã đơn</th>
            <th>Ngày đặt</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
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
              <td class="order-actions-cell">
                <a class="btn-order-detail"
                   href="${pageContext.request.contextPath}/OrderDetail?orderId=${order.orderId}">
                  Xem chi tiết
                </a>

                <c:if test="${order.cancellable}">
                  <details class="order-inline-action">
                    <summary>Hủy đơn</summary>
                    <form action="${pageContext.request.contextPath}/OrderHistory" method="post">
                      <input type="hidden" name="orderId" value="${order.orderId}">
                      <input type="hidden" name="action" value="cancel">
                      <textarea name="reason" rows="2" required placeholder="Nhập lý do hủy đơn..."></textarea>
                      <button type="submit" class="btn-mini-danger"
                              onclick="return confirm('Xác nhận hủy đơn hàng này?')">
                        Xác nhận hủy
                      </button>
                    </form>
                  </details>
                </c:if>

                <c:if test="${order.returnable}">
                  <details class="order-inline-action">
                    <summary>Trả hàng</summary>
                    <form action="${pageContext.request.contextPath}/OrderHistory" method="post" enctype="multipart/form-data">
                      <input type="hidden" name="orderId" value="${order.orderId}">
                      <input type="hidden" name="action" value="return">
                      <textarea name="reason" rows="2" required placeholder="Nhập lý do trả hàng..."></textarea>
                      <input type="file" name="returnImage" accept="image/*">
                      <button type="submit" class="btn-mini-secondary">
                        Gửi yêu cầu
                      </button>
                    </form>
                  </details>
                </c:if>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>

        <c:if test="${totalPages > 1}">
          <div class="pagination">
            <c:if test="${currentPage > 1}">
              <a href="${pageContext.request.contextPath}/OrderHistory?status=${activeStatus}&page=${currentPage - 1}">
                Trước
              </a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="pageNumber">
              <a href="${pageContext.request.contextPath}/OrderHistory?status=${activeStatus}&page=${pageNumber}"
                 class="${pageNumber == currentPage ? 'active' : ''}">
                  ${pageNumber}
              </a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
              <a href="${pageContext.request.contextPath}/OrderHistory?status=${activeStatus}&page=${currentPage + 1}">
                Sau
              </a>
            </c:if>
          </div>
        </c:if>
      </c:otherwise>
    </c:choose>
  </div>

  <a href="${pageContext.request.contextPath}/Account" class="btn-account-secondary order-back-btn">
    <i class='bx bx-arrow-back'></i>
    Quay lại tài khoản
  </a>
</main>

<footer class="footer">
  <div class="container">
    <div class="footer-content">
      <div class="footer-column">
        <h3 class="footer-logo">Handmade House</h3>
        <p class="footer-desc">
          Chào mừng đến với Handmade House, ngôi nhà nhỏ của những tâm hồn yêu nghệ thuật và thủ công.
        </p>
        <div class="social-links">
          <a href="#"><i class="bx bxl-facebook"></i></a>
          <a href="#"><i class="bx bxl-instagram"></i></a>
          <a href="#"><i class="bx bxl-tiktok"></i></a>
        </div>
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
        </ul>
      </div>
    </div>

    <div class="footer-bottom">
      <p>© 2025 Handmade House. Tất cả quyền được bảo lưu.</p>
    </div>
  </div>
</footer>

<script>
  window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
