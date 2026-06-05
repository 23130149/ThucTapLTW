<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán chuyển khoản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/banking_payment.css">
    <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

<section class="banking-payment-page">
    <div class="banking-container">
        <div class="banking-heading">
            <h1>Thanh toán chuyển khoản</h1>
            <p>Quét mã QR ngân hàng và chuyển khoản đúng nội dung để shop xác nhận đơn hàng nhanh hơn.</p>
        </div>

        <div class="banking-card">
            <div class="qr-section">
                <div class="qr-box">
                    <img src="${pageContext.request.contextPath}/images/bank_qr.jpg"
                         alt="QR chuyển khoản ngân hàng"
                         onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                    <div class="qr-placeholder">
                        <i class='bx bx-qr-scan'></i>
                        <span>Thêm ảnh QR tại<br>src/main/webapp/images/bank_qr.jpg</span>
                    </div>
                </div>
            </div>

            <div class="banking-info">
                <h2>Thông tin đơn hàng</h2>

                <div class="info-line">
                    <span>Mã đơn hàng</span>
                    <strong>${order.orderCode}</strong>
                </div>

                <div class="info-line">
                    <span>Số tiền cần chuyển</span>
                    <strong class="amount">
                        <fmt:formatNumber value="${order.totalPrice}" type="number" maxFractionDigits="0"/> đ
                    </strong>
                </div>

                <div class="info-line">
                    <span>Nội dung chuyển khoản</span>
                    <strong>THANHTOAN ${order.orderCode}</strong>
                </div>

                <div class="bank-note">
                    Sau khi chuyển khoản, đơn hàng sẽ ở trạng thái chờ xử lý. Admin sẽ kiểm tra giao dịch và xác nhận thanh toán thủ công.
                </div>

                <div class="bank-actions">
                    <a href="${pageContext.request.contextPath}/OrderHistory" class="btn-primary">Xem đơn hàng</a>
                    <a href="${pageContext.request.contextPath}/product" class="btn-secondary">Tiếp tục mua sắm</a>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/jsp/footer.jsp"/>

<script>
    window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
