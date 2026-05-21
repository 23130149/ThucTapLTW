<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sản phẩm yêu thích - Handmade House</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header_footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/favourite.css">
</head>
<body>
<c:if test="${not empty sessionScope.cartMessage}">
    <div class="cart-toast">
        <i class='bx bx-check-circle'></i>
        <span>${sessionScope.cartMessage}</span>
    </div>
    <c:remove var="cartMessage" scope="session"/>
</c:if>
<header class="header">
    <div class="header-top-container">
        <div class="header-content">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/home">Handmade House</a>
            </div>
            <form class="search-form" action="${pageContext.request.contextPath}/product" method="GET">
                <input type="text" class="search-input" name="keyword" value="${keyword}" placeholder="Tìm kiếm bất cứ thứ gì..." aria-label="Tìm kiếm sản phẩm">
                <button type="submit" class="search-btn">
                    <i class="bx bx-search-alt-2"></i>
                </button>
            </form>
            <div class="icons">
                <a href="${pageContext.request.contextPath}/favorite" class="icon-btn favorite-header-icon active" id="heartBtn" title="Sản phẩm yêu thích">
                    <i class='bx bxs-heart'></i>
                </a>
                <a href="${pageContext.request.contextPath}/cart" class="icon-btn cart-icon" id="cartBtn">
                    <i class='bx bx-cart'></i>
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                        <span class="cart-badge">${sessionScope.cart.totalQuantity}</span>
                    </c:if>
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
                    <li><a href="${pageContext.request.contextPath}/jsp/blog.jsp">Blog</a></li>
                    <li><a href="${pageContext.request.contextPath}/jsp/contact.jsp">Liên hệ</a></li>
                </ul>
            </nav>
        </div>
    </div>
</header>

<main class="favourite-section">
    <div class="container">
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            <span>/</span>
            <span class="current-page">Sản phẩm yêu thích</span>
        </div>

        <div class="favourite-hero">
            <span class="favourite-hero-icon"><i class='bx bxs-heart'></i></span>
            <div>
                <h1>Sản phẩm yêu thích</h1>
                <p>${favoriteCount} món đồ đang nằm trong chiếc hộp tim của bạn.</p>
            </div>
        </div>

        <c:if test="${not empty sessionScope.favoriteMessage}">
            <div class="favorite-message">${sessionScope.favoriteMessage}</div>
            <c:remove var="favoriteMessage" scope="session"/>
        </c:if>

        <c:choose>
            <c:when test="${empty productList}">
                <div class="empty-favourite">
                    <i class='bx bx-heart-circle'></i>
                    <h2>Chưa có sản phẩm yêu thích</h2>
                    <p>Ra trang sản phẩm và bấm tim ở góc phải ảnh để lưu lại món bạn thích nha.</p>
                    <a href="${pageContext.request.contextPath}/product" class="btn-view-products">Khám phá sản phẩm</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="favorite-toolbar">
                    <div class="favorite-select-all">
                        <input type="checkbox" id="selectAllFavorites">
                        <label for="selectAllFavorites">Chọn tất cả trên trang này</label>
                    </div>
                    <form id="favoriteBulkForm" action="${pageContext.request.contextPath}/favorite-selected-cart" method="post">
                        <button type="submit" name="action" value="cart" class="favorite-bulk-btn">
                            <i class='bx bx-cart-add'></i>
                            Thêm đã chọn vào giỏ
                        </button>
                        <button type="submit" name="action" value="buy" class="favorite-bulk-btn favorite-bulk-buy">
                            <i class='bx bx-credit-card'></i>
                            Mua sản phẩm đã chọn
                        </button>
                    </form>
                </div>

                <div class="product-list">
                    <c:forEach items="${productList}" var="p">
                        <div class="product-item">
                            <div class="product-top">
                                <label class="favorite-select-box">
                                    <input type="checkbox" name="selectedProductIds" value="${p.productId}" form="favoriteBulkForm" class="favorite-product-checkbox">
                                    <span></span>
                                </label>

                                <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}" class="product-thumb">
                                    <img src="${p.imageUrl}" alt="${p.productName}">
                                </a>

                                <form action="${pageContext.request.contextPath}/favorite-toggle" method="post" class="favorite-form">
                                    <input type="hidden" name="productId" value="${p.productId}">
                                    <button type="submit" class="favorite-toggle active" aria-label="Bỏ yêu thích">
                                        <i class="bx bxs-heart"></i>
                                    </button>
                                </form>
                            </div>

                            <div class="product-info">
                                <a href="${pageContext.request.contextPath}/product?categoryId=${p.categoryId}" class="product-category">${p.categoryName}</a>
                                <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}" class="product-name">${p.productName}</a>
                                <div class="price">
                                    <fmt:formatNumber value="${p.productPrice}" type="number" groupingUsed="true"/> đ
                                </div>
                                <a href="${pageContext.request.contextPath}/Add-Cart?id=${p.productId}&quantity=1" class="add-to-cart">
                                    <i class="bx bx-shopping-bag"></i>
                                    Thêm vào giỏ
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/favorite?page=${currentPage - 1}"><i class='bx bx-chevron-left'></i></a>
                        </c:if>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="current-page">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/favorite?page=${i}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}/favorite?page=${currentPage + 1}"><i class='bx bx-chevron-right'></i></a>
                        </c:if>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/jsp/footer.jsp" />
<script>
    const selectAllFavorites = document.getElementById('selectAllFavorites');
    const favoriteBulkForm = document.getElementById('favoriteBulkForm');

    if (selectAllFavorites) {
        selectAllFavorites.addEventListener('change', function () {
            document.querySelectorAll('.favorite-product-checkbox').forEach(function (checkbox) {
                checkbox.checked = selectAllFavorites.checked;
            });
        });
    }

    if (favoriteBulkForm) {
        favoriteBulkForm.addEventListener('submit', function (event) {
            const checkedCount = document.querySelectorAll('.favorite-product-checkbox:checked').length;
            if (checkedCount === 0) {
                event.preventDefault();
                alert('Vui lòng chọn ít nhất một sản phẩm.');
            }
        });
    }
</script>
</body>
</html>
