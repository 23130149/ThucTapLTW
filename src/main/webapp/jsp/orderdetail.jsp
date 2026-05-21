<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="util" uri="http://handmade/Util" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng - Handmade House</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header_footer.css">
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
                <a href="${pageContext.request.contextPath}/cart" class="icon-btn" id="cartBtn">
                    <i class='bx bx-cart'></i>
                </a>
                <a href="${pageContext.request.contextPath}/Account" class="icon-btn" id="userBtn">
                    <i class='bx bx-user'></i>
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
                    <li><a href="${pageContext.request.contextPath}/blog.jsp">Blog</a></li>
                    <li><a href="${pageContext.request.contextPath}/Contact">Liên hệ</a></li>
                </ul>
            </nav>
        </div>
    </div>
</header>

<main class="about-us-container order-detail-page">
    <h1>Chi tiết đơn hàng</h1>

    <c:if test="${not empty sessionScope.orderMessage}">
        <div class="form-alert form-alert-success">${sessionScope.orderMessage}</div>
        <c:remove var="orderMessage" scope="session" />
    </c:if>

    <div class="order-detail-summary">
        <div>
            <span>Mã đơn</span>
            <strong>${order.orderCode}</strong>
        </div>
        <div>
            <span>Trạng thái</span>
            <strong class="order-status ${order.status}">
                ${util:orderStatusIcon(order.status)}
                ${util:orderStatusLabel(order.status)}
            </strong>
        </div>
        <div>
            <span>Tổng tiền</span>
            <strong>${util:formatMoney(order.totalPrice)}</strong>
        </div>
    </div>

    <div class="recent-orders-box order-detail-box">
        <h2>Thông tin giao hàng</h2>

        <table class="orders-table order-info-table">
            <tr>
                <th>Ngày đặt</th>
                <td>${util:formatDateTime(order.createAt)}</td>
            </tr>
            <tr>
                <th>Thời gian giao dự kiến</th>
                <td>${order.estimatedDeliveryFormatted}</td>
            </tr>
            <tr>
                <th>Ngày giao</th>
                <td>${order.deliveredAtFormatted}</td>
            </tr>
            <tr>
                <th>Địa chỉ nhận hàng</th>
                <td>${order.shipAddress}</td>
            </tr>
            <tr>
                <th>Ghi chú</th>
                <td>
                    <c:choose>
                        <c:when test="${empty order.note}">Không có</c:when>
                        <c:otherwise>${order.note}</c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </div>

    <<div class="recent-orders-box order-detail-box">
    <div class="order-products-heading">
        <div>
            <h2>Sản phẩm trong đơn</h2>
            <p>Các món handmade bạn đã đặt trong đơn hàng này.</p>
        </div>
    </div>

    <table class="orders-table order-products-table">
        <thead>
        <tr>
            <th>Sản phẩm</th>
            <th>Đơn giá</th>
            <th>Số lượng</th>
            <th>Thành tiền</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="item" items="${orderItems}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/product-detail?id=${item.productId}"
                       class="order-product-link">
                        <span class="order-product-image">
                            <c:choose>
                                <c:when test="${not empty item.imageUrl}">
                                    <img src="${item.imageUrl}" alt="${item.productName}">
                                </c:when>
                                <c:otherwise>
                                    <span class="order-product-image-empty">
                                        <i class='bx bx-image'></i>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </span>

                        <span class="order-product-text">
                            <span class="order-product-name">${item.productName}</span>
                            <span class="order-product-view">
                                Xem lại sản phẩm
                                <i class='bx bx-right-arrow-alt'></i>
                            </span>
                        </span>
                    </a>
                </td>

                <td>${util:formatMoney(item.unitPrice)}</td>

                <td>
                    <span class="order-product-quantity">x${item.quantity}</span>
                </td>

                <td>
                    <strong class="order-product-total">
                            ${util:formatMoney(item.totalPrice)}
                    </strong>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

    <c:if test="${order.cancellable || order.returnable}">
        <div class="recent-orders-box order-detail-box order-action-panel">
            <h2>Thao tác đơn hàng</h2>

            <c:if test="${order.cancellable}">
                <form action="${pageContext.request.contextPath}/OrderHistory" method="post" class="order-action-form">
                    <input type="hidden" name="orderId" value="${order.orderId}">
                    <input type="hidden" name="action" value="cancel">
                    <label for="cancelReason">Lý do hủy đơn</label>
                    <textarea id="cancelReason" name="reason" rows="3" required placeholder="Ví dụ: Tôi đặt nhầm sản phẩm..."></textarea>
                    <button type="submit" class="btn-account-danger"
                            onclick="return confirm('Xác nhận hủy đơn hàng này?')">
                        <i class='bx bx-x-circle'></i>
                        Hủy đơn hàng
                    </button>
                </form>
            </c:if>

            <c:if test="${order.returnable}">
                <form action="${pageContext.request.contextPath}/OrderHistory" method="post" enctype="multipart/form-data" class="order-action-form">
                    <input type="hidden" name="orderId" value="${order.orderId}">
                    <input type="hidden" name="action" value="return">
                    <label for="returnReason">Lý do trả hàng</label>
                    <textarea id="returnReason" name="reason" rows="3" required placeholder="Mô tả lý do muốn trả hàng..."></textarea>
                    <label for="returnImage">Ảnh minh chứng nếu có</label>
                    <input type="file" id="returnImage" name="returnImage" accept="image/*">
                    <button type="submit" class="btn-account-secondary">
                        <i class='bx bx-revision'></i>
                        Gửi yêu cầu trả hàng
                    </button>
                </form>
            </c:if>
        </div>
    </c:if>

    <div class="account-bottom-actions order-detail-actions">
        <a href="${pageContext.request.contextPath}/OrderHistory" class="btn-account-secondary">
            <i class='bx bx-arrow-back'></i>
            Quay lại lịch sử đơn hàng
        </a>
        <a href="${pageContext.request.contextPath}/product" class="btn-account-primary">
            <i class='bx bx-store'></i>
            Tiếp tục mua sắm
        </a>
    </div>
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
            <p>© 2025 Handmade House. All rights reserved.</p>
        </div>
    </div>
</footer>
</body>
</html>
