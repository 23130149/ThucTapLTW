<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán chuyển khoản - Handmade House</title>
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/banking_payment.css">
    <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

<main class="banking-page">
    <div class="banking-container">
        <div class="banking-heading">
            <a href="${pageContext.request.contextPath}/OrderHistory" class="back-link">
                <i class='bx bx-arrow-back'></i> Quay lại đơn hàng
            </a>
            <h1>Thanh toán chuyển khoản</h1>
            <p>Vui lòng quét mã QR ngân hàng bên dưới và nhập đúng nội dung chuyển khoản để shop đối chiếu đơn hàng.</p>
        </div>

        <div class="banking-card">
            <div class="qr-box">
                <img src="${pageContext.request.contextPath}/images/bank_qr.jpg" alt="QR ngân hàng" class="bank-qr">
            </div>

            <div class="banking-info">
                <h2>Thông tin thanh toán</h2>

                <div class="info-row">
                    <span>Mã đơn hàng</span>
                    <strong>${order.orderCode}</strong>
                </div>

                <div class="info-row">
                    <span>Số tiền</span>
                    <strong class="amount"><fmt:formatNumber value="${order.totalPrice}" type="number" maxFractionDigits="0"/> đ</strong>
                </div>

                <div class="info-row">
                    <span>Nội dung chuyển khoản</span>
                    <strong>THANHTOAN ${order.orderCode}</strong>
                </div>

                <div class="info-row">
                    <span>Trạng thái</span>
                    <strong>Chờ xác nhận thanh toán</strong>
                </div>

                <div class="warning-box">
                    <i class='bx bx-info-circle'></i>
                    Sau khi chuyển khoản, admin sẽ kiểm tra giao dịch và cập nhật trạng thái cho đơn hàng.
                </div>

                <div class="action-row">
                    <a href="${pageContext.request.contextPath}/OrderHistory" class="primary-btn">Xem đơn hàng</a>
                    <a href="${pageContext.request.contextPath}/home" class="secondary-btn">Tiếp tục mua sắm</a>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/footer.jsp"/>

</body>
</html>
