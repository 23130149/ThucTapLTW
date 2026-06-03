
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - Handmade House</title>

    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payment.css">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>
<main class="page">
    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <i class='bx bx-chevron-right'></i>
        <a href="${pageContext.request.contextPath}/cart">Giỏ hàng</a>
        <i class='bx bx-chevron-right'></i>
        <strong>Thanh toán</strong>
    </div>

    <h1 class="page-title">Thanh toán</h1>

    <c:if test="${not empty sessionScope.paymentError}">
        <div class="payment-alert error">
            ${sessionScope.paymentError}
        </div>
        <c:remove var="paymentError" scope="session"/>
    </c:if>

    <form action="${pageContext.request.contextPath}/payment" method="post" id="checkoutForm">
        <c:forEach items="${cartItems}" var="checkoutItem">
            <input type="hidden" name="productIds" value="${checkoutItem.product.productId}">
        </c:forEach>
        <div class="checkout-layout">
            <div class="left-column">
                <section class="card">
                    <div class="card-header">
                        <i class='bx bx-map'></i>
                        <span>Thông tin giao hàng</span>
                    </div>

                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty addresses}">
                                <div class="address-empty">
                                    Bạn chưa có địa chỉ nhận hàng. Vui lòng thêm địa chỉ trước khi đặt hàng.
                                </div>
                                <a href="${pageContext.request.contextPath}/Address" class="manage-address">
                                    <i class='bx bx-plus-circle'></i>
                                    Thêm địa chỉ
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="address-dropdown" id="addressDropdown">
                                    <button type="button" class="selected-address-btn" id="addressDropdownToggle">
                                        <span class="selected-address-icon"><i class='bx bx-map-pin'></i></span>
                                        <span class="selected-address-text">
                                            <span class="selected-address-title" id="selectedAddressTitle">Địa chỉ nhận hàng</span>
                                            <span class="selected-address-detail" id="selectedAddressDetail">
                                                ${address.street}, ${address.district}, ${address.province}, ${address.country}
                                            </span>
                                        </span>
                                        <i class='bx bx-chevron-down dropdown-arrow'></i>
                                    </button>

                                    <div class="address-dropdown-menu" id="addressDropdownMenu">
                                        <c:forEach items="${addresses}" var="addr" varStatus="status">
                                            <label class="address-option">
                                                <input type="radio"
                                                       name="addressId"
                                                       value="${addr.userAddressId}"
                                                       data-title="Địa chỉ nhận hàng ${status.index + 1}"
                                                       data-full-address="${addr.street}, ${addr.district}, ${addr.province}, ${addr.country}"
                                                    ${status.first || addr.userAddressId == address.userAddressId ? "checked" : ""}>

                                                <span class="address-custom-radio"></span>

                                                <div class="address-content">
                                                    <div class="address-name">Địa chỉ nhận hàng ${status.index + 1}</div>
                                                    <div class="address-detail">
                                                        ${addr.street}, ${addr.district}, ${addr.province}, ${addr.country}
                                                    </div>
                                                </div>
                                            </label>
                                        </c:forEach>
                                    </div>
                                </div>

                                <a href="${pageContext.request.contextPath}/Address" class="manage-address">
                                    <i class='bx bx-plus-circle'></i>
                                    Thêm địa chỉ
                                </a>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-grid" style="margin-top: 24px;">
                            <div class="field">
                                <label>Họ và tên <span class="required">*</span></label>
                                <input type="text" name="receiverName" value="${empty sessionScope.user.userName ? '' : sessionScope.user.userName}" placeholder="Nhập họ và tên" required>
                            </div>

                            <div class="field">
                                <label>Số điện thoại <span class="required">*</span></label>
                                <input type="tel" name="receiverPhone" value="${empty sessionScope.user.phone ? '' : sessionScope.user.phone}" placeholder="Nhập số điện thoại" required>
                            </div>

                            <div class="field full">
                                <label>Email</label>
                                <input type="email" name="receiverEmail" value="${empty sessionScope.user.email ? '' : sessionScope.user.email}" placeholder="Nhập email nếu có">
                            </div>

                            <div class="field full">
                                <label>Ghi chú đơn hàng</label>
                                <textarea name="orderNote" placeholder="Ví dụ: giao giờ hành chính, gọi trước khi giao..."></textarea>
                            </div>
                        </div>
                    </div>
                </section>

                <section class="card">
                    <div class="card-header">
                        <i class='bx bx-credit-card'></i>
                        <span>Phương thức thanh toán</span>
                    </div>

                    <div class="card-body">
                        <div class="payment-methods">
                            <label class="method-option">
                                <input type="radio" name="paymentMethod" value="COD" checked>
                                <span class="method-icon"><i class='bx bx-package'></i></span>
                                <span>
                                    <span class="method-title">Thanh toán khi nhận hàng</span>
                                    <span class="method-desc">Thanh toán bằng tiền mặt khi nhận được hàng tại địa chỉ giao hàng</span>
                                </span>
                                <span class="method-radio"></span>
                            </label>

                            <label class="method-option">
                                <input type="radio" name="paymentMethod" value="VNPAY">
                                <span class="method-icon"><i class='bx bx-qr-scan'></i></span>
                                <span>
                                    <span class="method-title">Thanh toán VNPAY QR</span>
                                    <span class="method-desc">Quét mã QR hoặc thanh toán qua cổng VNPAY</span>
                                </span>
                                <span class="method-radio"></span>
                            </label>
                        </div>
                    </div>
                </section>
            </div>

            <aside class="right-column">
                <section class="card summary-card">
                    <div class="card-header">
                        <i class='bx bx-cube'></i>
                        <span>Tóm tắt đơn hàng</span>
                    </div>

                    <div class="card-body">
                        <div class="order-items">
                            <c:forEach items="${cartItems}" var="item">
                                <div class="order-item">
                                    <img src="${item.product.imageUrl}" alt="${item.product.productName}">
                                    <div>
                                        <div class="order-name">${item.product.productName}</div>
                                        <div class="order-qty">Số lượng: ${item.quantity}</div>
                                    </div>
                                    <div class="order-price">
                                        <fmt:formatNumber value="${item.quantity * item.price}" type="number"/>₫
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="divider"></div>

                        <div class="summary-row">
                            <span>Tạm tính</span>
                            <strong id="subtotalText">
                                <fmt:formatNumber value="${totalPrice}" type="number"/>₫
                            </strong>
                        </div>

                        <div class="summary-row">
                            <span>Phí giao hàng</span>
                            <strong id="shippingFeeText">
                                <fmt:formatNumber value="${empty shippingFee ? 0 : shippingFee}" type="number"/>₫
                            </strong>
                        </div>

                        <div class="distance-note" id="distanceNote">
                            <i class='bx bx-map-pin'></i>
                            Phí giao hàng được tính tự động theo khoảng cách từ cửa hàng đến địa chỉ nhận hàng.
                        </div>

                        <div class="grand-total">
                            <span>Tổng thanh toán</span>
                            <span class="amount" id="grandTotalText">
                                <fmt:formatNumber value="${empty grandTotal ? totalPrice : grandTotal}" type="number"/>₫
                            </span>
                        </div>

                        <input type="hidden" name="shippingFee" id="shippingFeeInput" value="${empty shippingFee ? 0 : shippingFee}">
                        <input type="hidden" name="distanceKm" id="distanceKmInput" value="">

                        <button type="submit" class="checkout-btn">Đặt hàng ngay</button>

                        <div class="safe-text">
                            <i class='bx bx-lock-alt'></i>
                            Thông tin của bạn được bảo mật an toàn
                        </div>

                        <div class="trust-row">
                            <span>Bảo mật SSL</span>
                            <span>Kiểm tra đơn hàng</span>
                            <span>Hỗ trợ đổi trả</span>
                        </div>
                    </div>
                </section>
            </aside>
        </div>
    </form>
</main>

<jsp:include page="/jsp/footer.jsp"/>

<script>
    const contextPath = '${pageContext.request.contextPath}';
    const subtotal = Number('${empty totalPrice ? 0 : totalPrice}');

    const shippingFeeText = document.getElementById('shippingFeeText');
    const grandTotalText = document.getElementById('grandTotalText');
    const shippingFeeInput = document.getElementById('shippingFeeInput');
    const distanceKmInput = document.getElementById('distanceKmInput');
    const distanceNote = document.getElementById('distanceNote');
    const checkoutForm = document.getElementById('checkoutForm');

    function formatVnd(value) {
        return new Intl.NumberFormat('vi-VN').format(Math.max(0, Math.round(value))) + '₫';
    }

    function updateShippingUI(shippingFee, distanceKm, sourceText) {
        const grandTotal = subtotal + Number(shippingFee || 0);

        shippingFeeText.textContent = formatVnd(shippingFee);
        grandTotalText.textContent = formatVnd(grandTotal);
        shippingFeeInput.value = Math.round(Number(shippingFee || 0));
        distanceKmInput.value = distanceKm ? Number(distanceKm).toFixed(2) : '';

        if (distanceKm) {
            distanceNote.innerHTML = "<i class='bx bx-map-pin'></i> Khoảng cách ước tính: <strong>" +
                Number(distanceKm).toFixed(1) + " km</strong>. " + sourceText;
        } else {
            distanceNote.innerHTML = "<i class='bx bx-map-pin'></i> " + sourceText;
        }
    }

    function calculateFallbackFee(distanceKm) {
        const distance = Number(distanceKm || 0);
        if (distance <= 0) return Number('${empty shippingFee ? 30000 : shippingFee}');
        if (distance <= 5) return 20000;
        return 20000 + Math.ceil(distance - 5) * 4000;
    }

    async function loadShippingFeeByAddress(addressInput) {
        if (!addressInput) {
            updateShippingUI(0, null, 'Vui lòng chọn địa chỉ nhận hàng để tính phí giao hàng.');
            return;
        }

        const addressId = addressInput.value;
        const fallbackDistance = addressInput.dataset.distance;

        distanceNote.innerHTML = "<i class='bx bx-loader-alt bx-spin'></i> Đang tính phí giao hàng theo khoảng cách...";

        try {

            const res = await fetch(contextPath + '/api/shipping-fee?addressId=' + encodeURIComponent(addressId), {
                headers: { 'Accept': 'application/json' }
            });

            if (!res.ok) throw new Error('Shipping API is not ready');

            const data = await res.json();
            if (data.source === 'GHN') {
                updateShippingUI(data.shippingFee, null, 'Phí giao hàng được tính từ Giao Hàng Nhanh.');
            } else {
                updateShippingUI(data.shippingFee, data.distanceKm, 'Đang dùng phí tạm tính vì địa chỉ chưa có mã GHN hoặc GHN chưa trả phí.');
            }
        } catch (error) {
            const fee = calculateFallbackFee(fallbackDistance);
            updateShippingUI(fee, fallbackDistance, 'Đang dùng công thức tạm thời vì API tính phí chưa được kết nối.');
        }
    }

    const addressDropdown = document.getElementById('addressDropdown');
    const addressDropdownToggle = document.getElementById('addressDropdownToggle');
    const selectedAddressTitle = document.getElementById('selectedAddressTitle');
    const selectedAddressDetail = document.getElementById('selectedAddressDetail');

    function updateSelectedAddress(input) {
        if (!input || !selectedAddressTitle || !selectedAddressDetail) return;

        selectedAddressTitle.textContent = input.dataset.title || 'Địa chỉ nhận hàng';
        selectedAddressDetail.textContent = input.dataset.fullAddress || '';
    }

    if (addressDropdownToggle && addressDropdown) {
        addressDropdownToggle.addEventListener('click', function () {
            addressDropdown.classList.toggle('open');
        });

        document.addEventListener('click', function (event) {
            if (!addressDropdown.contains(event.target)) {
                addressDropdown.classList.remove('open');
            }
        });
    }

    document.querySelectorAll("input[name='addressId']").forEach(function (input) {
        input.addEventListener('change', function () {
            updateSelectedAddress(input);
            if (addressDropdown) {
                addressDropdown.classList.remove('open');
            }
            loadShippingFeeByAddress(input);
        });
    });

    checkoutForm.addEventListener('submit', function (e) {
        const checkedAddress = document.querySelector("input[name='addressId']:checked");

        if (!checkedAddress) {
            e.preventDefault();
            alert('Vui lòng chọn địa chỉ nhận hàng');
            return;
        }

        const checkedPayment = document.querySelector("input[name='paymentMethod']:checked");
        if (!checkedPayment) {
            e.preventDefault();
            alert('Vui lòng chọn phương thức thanh toán');
        }
    });

    document.addEventListener('DOMContentLoaded', function () {
        const checkedAddress = document.querySelector("input[name='addressId']:checked");
        updateSelectedAddress(checkedAddress);
        loadShippingFeeByAddress(checkedAddress);
    });
</script>
</body>
</html>
