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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/favourite.css">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>

<jsp:include page="/jsp/header.jsp"/>

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
                        <div class="product-item ${p.stockQuantity <= 0 ? 'out-of-stock' : ''}">
                            <div class="product-top">
                                <label class="favorite-select-box">
                                    <input type="checkbox" name="selectedProductIds" value="${p.productId}" form="favoriteBulkForm"
                                           class="favorite-product-checkbox" ${p.stockQuantity <= 0 ? 'disabled' : ''}>
                                    <span></span>
                                </label>

                                <c:if test="${p.stockQuantity <= 0}">
                                    <span class="favorite-stock-badge">Hết hàng</span>
                                </c:if>

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
                                <div class="favorite-stock ${p.stockQuantity <= 0 ? 'out-of-stock' : ''}">
                                    ${p.stockQuantity > 0 ? 'Còn ' : 'Hết hàng · còn '}${p.stockQuantity > 0 ? p.stockQuantity : 0} sản phẩm
                                </div>
                                <c:choose>
                                    <c:when test="${p.stockQuantity > 0}">
                                        <a href="${pageContext.request.contextPath}/Add-Cart?id=${p.productId}&quantity=1" class="add-to-cart">
                                            <i class="bx bx-shopping-bag"></i>
                                            Thêm vào giỏ
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="add-to-cart disabled">
                                            <i class="bx bx-block"></i>
                                            Hết hàng
                                        </span>
                                    </c:otherwise>
                                </c:choose>
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

<jsp:include page="/jsp/footer.jsp"/>
<script>
    const selectAllFavorites = document.getElementById('selectAllFavorites');
    const favoriteBulkForm = document.getElementById('favoriteBulkForm');

    if (selectAllFavorites) {
        selectAllFavorites.addEventListener('change', function () {
            document.querySelectorAll('.favorite-product-checkbox:not(:disabled)').forEach(function (checkbox) {
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

<script>
  window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
