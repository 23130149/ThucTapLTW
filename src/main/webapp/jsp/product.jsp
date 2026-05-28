<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sanpham.css">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<c:if test="${not empty sessionScope.cartMessage}">
    <div class="cart-toast">
        <i class="bx bx-check-circle"></i>
        <span>${sessionScope.cartMessage}</span>
    </div>
    <c:remove var="cartMessage" scope="session"/>
</c:if>

<main class="product-page">
    <div class="container">
        <div class="product-hero">
            <div>
                <h1>Sản phẩm</h1>
                <p>Chọn danh mục, mức giá và sắp xếp để tìm sản phẩm phù hợp.</p>
            </div>
            <div class="product-count">${productCount} sản phẩm</div>
        </div>

        <div class="product-layout">
            <aside class="product-filter-panel">
                <form action="${pageContext.request.contextPath}/product" method="get" class="filter-form">
                    <c:if test="${not empty keyword}">
                        <input type="hidden" name="keyword" value="${keyword}">
                    </c:if>

                    <div class="filter-card">
                        <div class="filter-title">Danh mục</div>
                        <div class="category-check-list">
                            <c:forEach items="${categoryList}" var="cat">
                                <c:set var="catChecked" value="false"/>
                                <c:forEach items="${selectedCategoryIds}" var="cid">
                                    <c:if test="${cid == cat.categoryId}">
                                        <c:set var="catChecked" value="true"/>
                                    </c:if>
                                </c:forEach>
                                <label class="category-check ${catChecked ? 'checked' : ''}">
                                    <input type="checkbox" name="categoryId" value="${cat.categoryId}" ${catChecked ? 'checked' : ''}>
                                    <span>${cat.name}</span>
                                    <b>${cat.productCount}</b>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="filter-card">
                        <div class="filter-title">Khoảng giá</div>
                        <select name="priceRange">
                            <option value="" ${empty priceRange ? 'selected' : ''}>Tất cả</option>
                            <option value="0-100000" ${priceRange == '0-100000' ? 'selected' : ''}>Dưới 100.000đ</option>
                            <option value="100000-500000" ${priceRange == '100000-500000' ? 'selected' : ''}>100.000đ - 500.000đ</option>
                            <option value="500000+" ${priceRange == '500000+' ? 'selected' : ''}>Trên 500.000đ</option>
                        </select>
                    </div>

                    <div class="filter-card">
                        <div class="filter-title">Tình trạng</div>
                        <select name="status">
                            <option value="" ${empty status ? 'selected' : ''}>Tất cả</option>
                            <option value="instock" ${status == 'instock' ? 'selected' : ''}>Còn hàng</option>
                            <option value="outofstock" ${status == 'outofstock' ? 'selected' : ''}>Hết hàng</option>
                        </select>
                    </div>

                    <div class="filter-card">
                        <div class="filter-title">Sắp xếp</div>
                        <select name="sort">
                            <option value="" ${empty sort ? 'selected' : ''}>Mặc định</option>
                            <option value="newest" ${sort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                            <option value="price-asc" ${sort == 'price-asc' ? 'selected' : ''}>Giá thấp đến cao</option>
                            <option value="price-desc" ${sort == 'price-desc' ? 'selected' : ''}>Giá cao đến thấp</option>
                            <option value="best-selling" ${sort == 'best-selling' ? 'selected' : ''}>Bán chạy nhất</option>
                        </select>
                    </div>

                    <div class="filter-actions">
                        <button type="submit">Áp dụng</button>
                        <a href="${pageContext.request.contextPath}/product">Đặt lại</a>
                    </div>
                </form>
            </aside>

            <section class="product-results">
                <div class="result-toolbar">
                    <div class="result-summary">
                        <span>Hiển thị ${productList.size()} sản phẩm</span>
                        <c:if test="${not empty keyword}">
                            <strong>${keyword}</strong>
                        </c:if>
                    </div>
                    <form action="${pageContext.request.contextPath}/product" method="get" class="toolbar-sort">
                        <c:if test="${not empty keyword}">
                            <input type="hidden" name="keyword" value="${keyword}">
                        </c:if>
                        <c:forEach items="${selectedCategoryIds}" var="cid">
                            <input type="hidden" name="categoryId" value="${cid}">
                        </c:forEach>
                        <c:if test="${not empty priceRange}">
                            <input type="hidden" name="priceRange" value="${priceRange}">
                        </c:if>
                        <c:if test="${not empty status}">
                            <input type="hidden" name="status" value="${status}">
                        </c:if>
                        <select name="sort" onchange="this.form.submit()">
                            <option value="" ${empty sort ? 'selected' : ''}>Mặc định</option>
                            <option value="newest" ${sort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                            <option value="price-asc" ${sort == 'price-asc' ? 'selected' : ''}>Giá thấp đến cao</option>
                            <option value="price-desc" ${sort == 'price-desc' ? 'selected' : ''}>Giá cao đến thấp</option>
                            <option value="best-selling" ${sort == 'best-selling' ? 'selected' : ''}>Bán chạy nhất</option>
                        </select>
                    </form>
                </div>

                <div class="active-filters">
                    <c:if test="${not empty keyword}">
                        <span>${keyword}</span>
                    </c:if>
                    <c:forEach items="${categoryList}" var="cat">
                        <c:set var="catChecked" value="false"/>
                        <c:forEach items="${selectedCategoryIds}" var="cid">
                            <c:if test="${cid == cat.categoryId}">
                                <c:set var="catChecked" value="true"/>
                            </c:if>
                        </c:forEach>
                        <c:if test="${catChecked}">
                            <span>${cat.name}</span>
                        </c:if>
                    </c:forEach>
                    <c:if test="${not empty priceRange}">
                        <span>${priceRange}</span>
                    </c:if>
                    <c:if test="${not empty status}">
                        <span>${status == 'instock' ? 'Còn hàng' : 'Hết hàng'}</span>
                    </c:if>
                </div>

                <c:choose>
                    <c:when test="${empty productList}">
                        <div class="empty-product-box">
                            <i class="bx bx-search-alt"></i>
                            <h3>Không tìm thấy sản phẩm</h3>
                            <a href="${pageContext.request.contextPath}/product">Xem tất cả</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="product-grid">
                            <c:forEach items="${productList}" var="p">
                                <article class="product-card">
                                    <div class="product-image-wrap">
                                        <form action="${pageContext.request.contextPath}/favorite-toggle" method="post" class="favorite-form">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            <button type="submit" class="favorite-toggle ${p.favorite ? 'active' : ''}" aria-label="Yêu thích">
                                                <i class="bx ${p.favorite ? 'bxs-heart' : 'bx-heart'}"></i>
                                            </button>
                                        </form>
                                        <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}" class="product-image">
                                            <c:choose>
                                                <c:when test="${not empty p.imageUrl}">
                                                    <img src="${p.imageUrl}" alt="${p.productName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/images/no-image.png" alt="${p.productName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                        <c:choose>
                                            <c:when test="${p.stockQuantity != null and p.stockQuantity > 0}">
                                                <a href="${pageContext.request.contextPath}/Add-Cart?id=${p.productId}&quantity=1" class="add-to-cart-btn">
                                                    <i class="bx bx-shopping-bag"></i>
                                                    <span>Thêm vào giỏ</span>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="out-stock-badge">Hết hàng</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="product-info">
                                        <a href="${pageContext.request.contextPath}/product?categoryId=${p.categoryId}" class="product-category">${p.categoryName}</a>
                                        <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}" class="product-name">${p.productName}</a>
                                        <div class="product-bottom">
                                            <strong><fmt:formatNumber value="${p.productPrice}" type="number" groupingUsed="true"/> đ</strong>
                                            <c:if test="${not empty p.sold and p.sold > 0}">
                                                <span>Đã bán ${p.sold}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                </article>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <c:url var="prevPageUrl" value="/product">
                                <c:param name="page" value="${currentPage - 1}"/>
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:forEach items="${selectedCategoryIds}" var="cid"><c:param name="categoryId" value="${cid}"/></c:forEach>
                                <c:if test="${not empty status}"><c:param name="status" value="${status}"/></c:if>
                                <c:if test="${not empty priceRange}"><c:param name="priceRange" value="${priceRange}"/></c:if>
                                <c:if test="${not empty sort}"><c:param name="sort" value="${sort}"/></c:if>
                            </c:url>
                            <a href="${prevPageUrl}"><i class="bx bx-chevron-left"></i></a>
                        </c:if>

                        <c:forEach items="${paginationItems}" var="pageItem">
                            <c:choose>
                                <c:when test="${pageItem == '...'}">
                                    <span class="dots">...</span>
                                </c:when>
                                <c:when test="${pageItem == currentPageString}">
                                    <span class="current-page">${pageItem}</span>
                                </c:when>
                                <c:otherwise>
                                    <c:url var="pageUrl" value="/product">
                                        <c:param name="page" value="${pageItem}"/>
                                        <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                        <c:forEach items="${selectedCategoryIds}" var="cid"><c:param name="categoryId" value="${cid}"/></c:forEach>
                                        <c:if test="${not empty status}"><c:param name="status" value="${status}"/></c:if>
                                        <c:if test="${not empty priceRange}"><c:param name="priceRange" value="${priceRange}"/></c:if>
                                        <c:if test="${not empty sort}"><c:param name="sort" value="${sort}"/></c:if>
                                    </c:url>
                                    <a href="${pageUrl}">${pageItem}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <c:url var="nextPageUrl" value="/product">
                                <c:param name="page" value="${currentPage + 1}"/>
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:forEach items="${selectedCategoryIds}" var="cid"><c:param name="categoryId" value="${cid}"/></c:forEach>
                                <c:if test="${not empty status}"><c:param name="status" value="${status}"/></c:if>
                                <c:if test="${not empty priceRange}"><c:param name="priceRange" value="${priceRange}"/></c:if>
                                <c:if test="${not empty sort}"><c:param name="sort" value="${sort}"/></c:if>
                            </c:url>
                            <a href="${nextPageUrl}"><i class="bx bx-chevron-right"></i></a>
                        </c:if>
                    </div>
                </c:if>
            </section>
        </div>
    </div>
</main>

<jsp:include page="/jsp/footer.jsp"/>
</body>
</html>
