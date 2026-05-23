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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header_footer.css">

    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
</head>
<body>

<header class="header">
    <div class="header-content">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/home">Handmade House</a>
        </div>

        <form class="search-form">
            <input type="text" class="search-input" placeholder="Tìm kiếm bất cứ thứ gì">
            <button type="submit" class="search-btn">
                <i class="bx bx-search-alt-2"></i>
            </button>
        </form>

        <div class="icons">
            <a href="${pageContext.request.contextPath}/favorite" class="icon-btn favorite-header-icon" id="heartBtn" title="Sản phẩm yêu thích">
              <i class='bx bx-heart'></i>
            </a>
            <a href="${pageContext.request.contextPath}/cart" class="icon-btn cart-icon"><i class='bx bx-cart'></i>
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                        <span class="cart-badge">${sessionScope.cart.totalQuantity}</span>
                    </c:if>
                </a>
            <a href="${pageContext.request.contextPath}/Account" class="icon-btn"><i class='bx bx-user'></i></a>
        </div>
    </div>
</header>

<section class="cart-page">

    <h1 class="cart-header">
        <i class='bx bx-cart'></i> Giỏ Hàng Của Bạn
    </h1>
    <c:choose>
        <c:when test="${empty sessionScope.cart.data}">
            <p>Giỏ Hàng Của Bạn Đang Trống</p>
            <a href="${pageContext.request.contextPath}/product">Tiếp tục mua sắm</a>
        </c:when>
        <c:otherwise>
            <div class="cart-summary-bar">
                <div>
                    <div class="summary-title">Tổng tiền ước tính</div>
                    <div class="summary-price">
                        <fmt:formatNumber value="${sessionScope.cart.totalPrice}" type="number" maxFractionDigits="0"/> đ
                    </div>
                    <div class="summary-note">
                        <c:out value="${sessionScope.cart.totalQuantity}"/> sản phẩm đã chọn
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/payment" class="summary-checkout">Thanh toán</a>
            </div>

            <form action="${pageContext.request.contextPath}/DelSelectProduct" method="post">
                <div class="cart-action">
                    <label class="check-all">
                        <input type="checkbox" id="checkAll">
                        <span>Chọn tất cả (<c:out value="${sessionScope.cart.totalQuantity}"/>)</span>
                    </label>

                    <button type="submit" class="btn-delete-selected">
                        <i class='bx bx-trash'></i> Xóa đã chọn
                    </button>
                </div>

                <div class="cart-list">
                    <c:forEach items="${sessionScope.cart.data.values()}" var="item">
                        <div class="cart-item">
                            <input type="checkbox" class="item-checkbox" name="productIds" value="${item.product.productId}">

                            <img src="${item.product.imageUrl}" alt="${item.product.productName}"
                                 onerror="this.src='${pageContext.request.contextPath}/images/no-image.png'">

                            <div class="product-info">
                                <div class="product-name">${item.product.productName}</div>
                                <div class="unit-price">
                                    <fmt:formatNumber value="${item.price}" type="number" maxFractionDigits="0"/> đ
                                </div>
                            </div>

                            <div class="qty-box">
                                <a class="qty-btn" href="${pageContext.request.contextPath}/CartUpdate?productId=${item.product.productId}&amp;action=dec">−</a>
                                <span class="qty">${item.quantity}</span>
                                <a class="qty-btn" href="${pageContext.request.contextPath}/CartUpdate?productId=${item.product.productId}&amp;action=inc">+</a>
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

            <div class="cart-total">
                <h3>Đơn Hàng Của Bạn</h3>

                <div class="line">
                    <span>Tổng sản phẩm</span>
                    <span><c:out value="${sessionScope.cart.totalQuantity}"/></span>
                </div>

                <div class="line">
                    <span>Tạm tính</span>
                    <span><fmt:formatNumber value="${sessionScope.cart.totalPrice}" type="number" maxFractionDigits="0"/> đ</span>
                </div>

                <div class="line">
                    <span>Phí vận chuyển</span>
                    <span class="free">Miễn phí</span>
                </div>

                <hr>

                <div class="line total">
                    <span>Tổng cộng</span>
                    <span><fmt:formatNumber value="${sessionScope.cart.totalPrice}" type="number" maxFractionDigits="0"/> đ</span>
                </div>

                <a href="${pageContext.request.contextPath}/payment" class="summary-checkout">
                    Thanh toán
                </a>
            </div>
        </c:otherwise>
    </c:choose>


</section>

<script>
  window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>