<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giỏ Hàng</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">

    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

<section class="cart-page">

    <h1 class="cart-header">
        <i class='bx bx-cart'></i> Giỏ Hàng Của Bạn
    </h1>

    <c:if test="${not empty sessionScope.cartError}">
        <div class="cart-message error">
            ${sessionScope.cartError}
        </div>
        <c:remove var="cartError" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.cartSuccess}">
        <div class="cart-message success">
            ${sessionScope.cartSuccess}
        </div>
        <c:remove var="cartSuccess" scope="session"/>
    </c:if>
    <c:choose>
        <c:when test="${empty sessionScope.cart.data}">
            <p>Giỏ Hàng Của Bạn Đang Trống</p>
            <a href="${pageContext.request.contextPath}/product">Tiếp tục mua sắm</a>
        </c:when>
        <c:otherwise>
            <div class="cart-summary-bar">
                <div class="summary-left">
                    <div class="summary-title">Tổng tiền ước tính</div>

                    <div class="summary-price" id="selectedTotalTop">
                        <fmt:formatNumber value="${sessionScope.cart.totalPrice}" type="number" maxFractionDigits="0"/> đ
                    </div>

                    <div class="summary-note">
                        <span id="selectedQuantityTop">${sessionScope.cart.totalQuantity}</span> sản phẩm đã chọn
                    </div>
                </div>

                <button type="submit"
                        form="cartSelectionForm"
                        class="summary-checkout checkout-submit">
                    Thanh toán
                </button>
            </div>

            <form id="cartSelectionForm" action="${pageContext.request.contextPath}/payment" method="get">
                <div class="cart-action">
                    <label class="check-all">
                        <input type="checkbox" id="checkAll" checked>
                        <span>Chọn tất cả (<c:out value="${sessionScope.cart.totalQuantity}"/>)</span>
                    </label>

                    <button type="submit" class="btn-delete-selected"
                            formaction="${pageContext.request.contextPath}/DelSelectProduct"
                            formmethod="post">
                        <i class='bx bx-trash'></i> Xóa đã chọn
                    </button>
                </div>

                <div class="cart-list">
                    <c:forEach items="${sessionScope.cart.data.values()}" var="item">
                        <div class="cart-item ${item.product.stockQuantity <= 0 ? 'out-of-stock' : ''}">
                            <input type="checkbox"
                                   class="item-checkbox"
                                   name="productIds"
                                   value="${item.product.productId}"
                                   data-total="${item.total}"
                                   data-quantity="${item.quantity}"
                                   ${item.product.stockQuantity > 0 ? 'checked' : 'disabled'}>

                            <img src="${item.product.imageUrl}" alt="${item.product.productName}"
                                 onerror="this.src='${pageContext.request.contextPath}/images/no-image.png'">

                            <div class="product-info">
                                <div class="product-name">${item.product.productName}</div>
                                <div class="unit-price">
                                    <fmt:formatNumber value="${item.price}" type="number" maxFractionDigits="0"/> đ
                                </div>
                                <div class="cart-stock ${item.product.stockQuantity <= 0 ? 'out-of-stock' : ''}">
                                    <c:choose>
                                        <c:when test="${item.product.stockQuantity > 0}">
                                            Còn ${item.product.stockQuantity} sản phẩm trong kho
                                        </c:when>
                                        <c:otherwise>
                                            Hết hàng · chỉ có thể xóa khỏi giỏ
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="qty-box">
                                <c:choose>
                                    <c:when test="${item.product.stockQuantity > 0}">
                                        <a class="qty-btn" href="${pageContext.request.contextPath}/CartUpdate?productId=${item.product.productId}&amp;action=dec">−</a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="qty-btn disabled">−</span>
                                    </c:otherwise>
                                </c:choose>
                                <span class="qty">${item.quantity}</span>
                                <c:choose>
                                    <c:when test="${item.product.stockQuantity > item.quantity}">
                                        <a class="qty-btn" href="${pageContext.request.contextPath}/CartUpdate?productId=${item.product.productId}&amp;action=inc">+</a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="qty-btn disabled">+</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="item-total-price">
                                <fmt:formatNumber value="${item.total}" type="number" maxFractionDigits="0"/> đ
                            </div>

                            <a class="item-remove" href="${pageContext.request.contextPath}/DelProduct?id=${item.product.productId}" title="Xóa">
                                <i class='bx bx-trash'></i>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </form>

        </c:otherwise>
    </c:choose>


</section>
<jsp:include page="/jsp/footer.jsp"/>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const checkAll = document.getElementById("checkAll");
        const getItemCheckboxes = function () {
            return Array.from(document.querySelectorAll(".item-checkbox"));
        };

        const selectedTotalTop = document.getElementById("selectedTotalTop");
        const selectedQuantityTop = document.getElementById("selectedQuantityTop");
        const selectedQuantityBottom = document.getElementById("selectedQuantityBottom");
        const selectedSubtotalBottom = document.getElementById("selectedSubtotalBottom");
        const selectedTotalBottom = document.getElementById("selectedTotalBottom");

        if (!checkAll || getItemCheckboxes().length === 0) {
            return;
        }

        function formatCurrency(value) {
            return new Intl.NumberFormat("vi-VN").format(value) + " đ";
        }

        function updateSelectedTotal() {
            let total = 0;
            let quantity = 0;
            let checkedCount = 0;

            const itemCheckboxes = getItemCheckboxes();

            itemCheckboxes.forEach(function (checkbox) {
                const cartItem = checkbox.closest(".cart-item");

                if (checkbox.checked) {
                    total += Number(checkbox.dataset.total || 0);
                    quantity += Number(checkbox.dataset.quantity || 0);
                    checkedCount++;

                    if (cartItem) {
                        cartItem.classList.remove("unchecked");
                    }
                } else if (cartItem) {
                    cartItem.classList.add("unchecked");
                }
            });

            if (selectedTotalTop) {
                selectedTotalTop.textContent = formatCurrency(total);
            }

            if (selectedQuantityTop) {
                selectedQuantityTop.textContent = quantity;
            }

            if (selectedQuantityBottom) {
                selectedQuantityBottom.textContent = quantity;
            }

            if (selectedSubtotalBottom) {
                selectedSubtotalBottom.textContent = formatCurrency(total);
            }

            if (selectedTotalBottom) {
                selectedTotalBottom.textContent = formatCurrency(total);
            }

            if (checkedCount === itemCheckboxes.length) {
                checkAll.checked = true;
                checkAll.indeterminate = false;
            } else if (checkedCount === 0) {
                checkAll.checked = false;
                checkAll.indeterminate = false;
            } else {
                checkAll.checked = false;
                checkAll.indeterminate = true;
            }
        }

        checkAll.addEventListener("change", function () {
            getItemCheckboxes().forEach(function (checkbox) {
                checkbox.checked = checkAll.checked;
            });

            updateSelectedTotal();
        });

        getItemCheckboxes().forEach(function (checkbox) {
            checkbox.addEventListener("change", updateSelectedTotal);
        });

        const cartSelectionForm = document.getElementById("cartSelectionForm");

        if (cartSelectionForm) {
            cartSelectionForm.addEventListener("submit", function (event) {
                const submitter = event.submitter;
                const action = submitter && submitter.getAttribute("formaction")
                    ? submitter.getAttribute("formaction")
                    : cartSelectionForm.getAttribute("action");

                const isCheckout = action && action.indexOf("/payment") !== -1;
                const hasCheckedItem = getItemCheckboxes().some(function (checkbox) {
                    return checkbox.checked;
                });

                if (isCheckout && !hasCheckedItem) {
                    event.preventDefault();
                    alert("Vui lòng chọn ít nhất một sản phẩm để thanh toán.");
                }
            });
        }

        updateSelectedTotal();
    });

    window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
