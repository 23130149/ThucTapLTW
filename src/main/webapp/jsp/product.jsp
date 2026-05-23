<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sanpham.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header_footer.css">
    <meta charset="UTF-8">
    <title>Sản phẩm</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="preconnect" href="https://unsplash.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
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
                <input type="text"
                       class="search-input"
                       name="keyword"
                       value="${keyword}"
                       placeholder="Tìm kiếm bất cứ thứ gì... VD: áo + len"
                       aria-label="Tìm kiếm sản phẩm"
                       autocomplete="off"
                       autocorrect="off"
                       autocapitalize="off"
                       spellcheck="false">
                <button type="submit" class="search-btn">
                    <i class="bx bx-search-alt-2"></i>
                </button>
            </form>
            <div class="icons">
                <a href="${pageContext.request.contextPath}/favorite" class="icon-btn favorite-header-icon" id="heartBtn" title="Sản phẩm yêu thích">
                    <i class='bx bx-heart'></i>
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

<main class="main-content">
    <div class="container">
        <div class="page-meta">
            <h1 class="page-title-main">Sản phẩm</h1>
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                <a href="${pageContext.request.contextPath}/product"><i class="bx bx-chevron-right"></i></a>
                <span>Sản phẩm</span>
            </div>
        </div>

        <div class="product-page-layout">
            <div class="product-listing-area">
                <div class="sort-stats-bar">
                    <div class="product-stats">
                        Hiển thị ${productList.size()} / ${productCount} sản phẩm
                    </div>
                    <form class="sort-options" action="${pageContext.request.contextPath}/product" method="get">
                        <c:if test="${not empty keyword}"><input type="hidden" name="keyword" value="${keyword}"></c:if>
                        <c:if test="${not empty selectedCategoryId}"><input type="hidden" name="categoryId" value="${selectedCategoryId}"></c:if>
                        <c:if test="${not empty status}"><input type="hidden" name="status" value="${status}"></c:if>
                        <c:if test="${not empty priceRange}"><input type="hidden" name="priceRange" value="${priceRange}"></c:if>
                        <c:if test="${not empty material}"><input type="hidden" name="material" value="${material}"></c:if>
                        <c:if test="${not empty usage}"><input type="hidden" name="usage" value="${usage}"></c:if>
                        <div class="custom-select-wrapper">
                            <select class="sort-options-select" name="sort" onchange="this.form.submit()">
                                <option value="" ${empty sort ? 'selected' : ''}>Sắp xếp mặc định</option>
                                <option value="newest" ${sort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                                <option value="price-asc" ${sort == 'price-asc' ? 'selected' : ''}>Giá tăng dần</option>
                                <option value="price-desc" ${sort == 'price-desc' ? 'selected' : ''}>Giá giảm dần</option>
                                <option value="best-selling" ${sort == 'best-selling' ? 'selected' : ''}>Bán chạy nhất</option>
                            </select>
                            <i class="dropdown-arrow bx bx-chevron-down"></i>
                        </div>
                    </form>
                </div>

                <div class="active-filter-line">
                    <c:if test="${not empty keyword}"><span>Từ khóa: ${keyword}</span></c:if>
                    <c:if test="${not empty priceRange}"><span>Giá: ${priceRange}</span></c:if>
                    <c:if test="${not empty material}"><span>Vật liệu: ${material}</span></c:if>
                    <c:if test="${not empty usage}"><span>Nhu cầu: ${usage}</span></c:if>
                    <c:if test="${not empty status}"><span>Kho: ${status == 'instock' ? 'Còn hàng' : 'Hết hàng'}</span></c:if>
                </div>

                <c:choose>
                    <c:when test="${empty productList}">
                        <div class="empty-product-box">
                            <i class='bx bx-search-alt'></i>
                            <h3>Không tìm thấy sản phẩm phù hợp</h3>
                            <p>Thử đổi từ khóa hoặc bỏ bớt bộ lọc nha.</p>
                            <a href="${pageContext.request.contextPath}/product">Xem tất cả sản phẩm</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="product-grid">
                            <c:forEach items="${productList}" var="p">
                                <div class="product-item">
                                    <div class="product-top">
                                        <form action="${pageContext.request.contextPath}/favorite-toggle" method="post" class="favorite-form">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            <button type="submit" class="favorite-toggle ${p.favorite ? 'active' : ''}" aria-label="Yêu thích ${p.productName}">
                                                <i class="bx ${p.favorite ? 'bxs-heart' : 'bx-heart'}"></i>
                                            </button>
                                        </form>
                                        <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}" class="product-thumb">
                                            <c:choose>
                                                <c:when test="${not empty p.imageUrl}">
                                                    <img src="${p.imageUrl}" alt="${p.productName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/images/no-image.png" alt="No image">
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/Add-Cart?id=${p.productId}&quantity=1" class="add-to-cart-btn">
                                            <i class="bx bx-shopping-bag"></i>Thêm vào giỏ
                                        </a>
                                    </div>
                                    <div class="product-info">
                                        <a href="${pageContext.request.contextPath}/product?categoryId=${p.categoryId}" class="product-cat">${p.categoryName}</a>
                                        <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}" class="product-name">${p.productName}</a>
                                        <div class="product-price"><fmt:formatNumber value="${p.productPrice}" type="number" groupingUsed="true"/> đ</div>
                                        <c:if test="${not empty p.sold and p.sold > 0}">
                                            <div class="product-sold"><i class='bx bx-trending-up'></i> Đã bán ${p.sold}</div>
                                        </c:if>
                                    </div>
                                </div>
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
                                <c:if test="${not empty selectedCategoryId}"><c:param name="categoryId" value="${selectedCategoryId}"/></c:if>
                                <c:if test="${not empty status}"><c:param name="status" value="${status}"/></c:if>
                                <c:if test="${not empty priceRange}"><c:param name="priceRange" value="${priceRange}"/></c:if>
                                <c:if test="${not empty material}"><c:param name="material" value="${material}"/></c:if>
                                <c:if test="${not empty usage}"><c:param name="usage" value="${usage}"/></c:if>
                                <c:if test="${not empty sort}"><c:param name="sort" value="${sort}"/></c:if>
                            </c:url>
                            <a href="${prevPageUrl}"><i class="bx bx-chevron-left"></i></a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:url var="pageUrl" value="/product">
                                <c:param name="page" value="${i}"/>
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:if test="${not empty selectedCategoryId}"><c:param name="categoryId" value="${selectedCategoryId}"/></c:if>
                                <c:if test="${not empty status}"><c:param name="status" value="${status}"/></c:if>
                                <c:if test="${not empty priceRange}"><c:param name="priceRange" value="${priceRange}"/></c:if>
                                <c:if test="${not empty material}"><c:param name="material" value="${material}"/></c:if>
                                <c:if test="${not empty usage}"><c:param name="usage" value="${usage}"/></c:if>
                                <c:if test="${not empty sort}"><c:param name="sort" value="${sort}"/></c:if>
                            </c:url>
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="current-page">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageUrl}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <c:url var="nextPageUrl" value="/product">
                                <c:param name="page" value="${currentPage + 1}"/>
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:if test="${not empty selectedCategoryId}"><c:param name="categoryId" value="${selectedCategoryId}"/></c:if>
                                <c:if test="${not empty status}"><c:param name="status" value="${status}"/></c:if>
                                <c:if test="${not empty priceRange}"><c:param name="priceRange" value="${priceRange}"/></c:if>
                                <c:if test="${not empty material}"><c:param name="material" value="${material}"/></c:if>
                                <c:if test="${not empty usage}"><c:param name="usage" value="${usage}"/></c:if>
                                <c:if test="${not empty sort}"><c:param name="sort" value="${sort}"/></c:if>
                            </c:url>
                            <a href="${nextPageUrl}"><i class="bx bx-chevron-right"></i></a>
                        </c:if>
                    </div>
                </c:if>
            </div>

            <aside class="sidebar-filters">
                <div class="filter-group category-filter">
                    <h4 class="filter-group-title">Danh mục</h4>
                    <ul class="category-list-filter">
                        <li>
                            <c:url var="allCatUrl" value="/product">
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:if test="${not empty status}"><c:param name="status" value="${status}"/></c:if>
                                <c:if test="${not empty priceRange}"><c:param name="priceRange" value="${priceRange}"/></c:if>
                                <c:if test="${not empty material}"><c:param name="material" value="${material}"/></c:if>
                                <c:if test="${not empty usage}"><c:param name="usage" value="${usage}"/></c:if>
                                <c:if test="${not empty sort}"><c:param name="sort" value="${sort}"/></c:if>
                            </c:url>
                            <a href="${allCatUrl}" class="${empty selectedCategoryId ? 'active' : ''}">
                                <span>Tất cả</span>
                                <span class="category-count">${productCount}</span>
                            </a>
                        </li>
                        <c:forEach items="${categoryList}" var="cat">
                            <li>
                                <c:url var="catUrl" value="/product">
                                    <c:param name="categoryId" value="${cat.categoryId}"/>
                                    <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                    <c:if test="${not empty status}"><c:param name="status" value="${status}"/></c:if>
                                    <c:if test="${not empty priceRange}"><c:param name="priceRange" value="${priceRange}"/></c:if>
                                    <c:if test="${not empty material}"><c:param name="material" value="${material}"/></c:if>
                                    <c:if test="${not empty usage}"><c:param name="usage" value="${usage}"/></c:if>
                                    <c:if test="${not empty sort}"><c:param name="sort" value="${sort}"/></c:if>
                                </c:url>
                                <a href="${catUrl}" class="${selectedCategoryId == cat.categoryId ? 'active' : ''}">
                                    <span>${cat.name}</span>
                                    <span class="category-count">${cat.productCount}</span>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="filter-group advanced-filter">
                    <h4 class="filter-group-title">Bộ lọc</h4>
                    <form class="filter-form" action="${pageContext.request.contextPath}/product" method="get">
                        <c:if test="${not empty keyword}"><input type="hidden" name="keyword" value="${keyword}"></c:if>
                        <c:if test="${not empty selectedCategoryId}"><input type="hidden" name="categoryId" value="${selectedCategoryId}"></c:if>

                        <label>Khoảng giá</label>
                        <select name="priceRange">
                            <option value="" ${empty priceRange ? 'selected' : ''}>Tất cả mức giá</option>
                            <option value="0-100000" ${priceRange == '0-100000' ? 'selected' : ''}>Dưới 100.000đ</option>
                            <option value="100000-500000" ${priceRange == '100000-500000' ? 'selected' : ''}>100.000đ - 500.000đ</option>
                            <option value="500000+" ${priceRange == '500000+' ? 'selected' : ''}>Trên 500.000đ</option>
                        </select>

                        <label>Tình trạng</label>
                        <select name="status">
                            <option value="" ${empty status ? 'selected' : ''}>Tất cả</option>
                            <option value="instock" ${status == 'instock' ? 'selected' : ''}>Còn hàng</option>
                            <option value="outofstock" ${status == 'outofstock' ? 'selected' : ''}>Hết hàng</option>
                        </select>

                        <label>Vật liệu</label>
                        <select name="material">
                            <option value="" ${empty material ? 'selected' : ''}>Tất cả vật liệu</option>
                            <option value="len" ${material == 'len' ? 'selected' : ''}>Len</option>
                            <option value="vai" ${material == 'vai' ? 'selected' : ''}>Vải</option>
                            <option value="go" ${material == 'go' ? 'selected' : ''}>Gỗ</option>
                            <option value="giay" ${material == 'giay' ? 'selected' : ''}>Giấy</option>
                            <option value="da" ${material == 'da' ? 'selected' : ''}>Da</option>
                            <option value="hat" ${material == 'hat' ? 'selected' : ''}>Hạt</option>
                            <option value="soi" ${material == 'soi' ? 'selected' : ''}>Sợi</option>
                        </select>

                        <label>Nhu cầu sử dụng</label>
                        <select name="usage">
                            <option value="" ${empty usage ? 'selected' : ''}>Tất cả nhu cầu</option>
                            <option value="trang-tri" ${usage == 'trang-tri' ? 'selected' : ''}>Trang trí</option>
                            <option value="thoi-trang" ${usage == 'thoi-trang' ? 'selected' : ''}>Thời trang</option>
                            <option value="qua-tang" ${usage == 'qua-tang' ? 'selected' : ''}>Quà tặng</option>
                            <option value="gia-dung" ${usage == 'gia-dung' ? 'selected' : ''}>Gia dụng</option>
                        </select>

                        <label>Sắp xếp nhanh</label>
                        <select name="sort">
                            <option value="" ${empty sort ? 'selected' : ''}>Mặc định</option>
                            <option value="price-asc" ${sort == 'price-asc' ? 'selected' : ''}>Giá thấp đến cao</option>
                            <option value="price-desc" ${sort == 'price-desc' ? 'selected' : ''}>Giá cao đến thấp</option>
                            <option value="best-selling" ${sort == 'best-selling' ? 'selected' : ''}>Bán chạy nhất</option>
                            <option value="newest" ${sort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                        </select>

                        <button type="submit" class="apply-filter-btn">
                            <i class='bx bx-filter-alt'></i> Áp dụng lọc
                        </button>
                        <a href="${pageContext.request.contextPath}/product" class="clear-filter-btn">Xóa bộ lọc</a>
                    </form>
                </div>
            </aside>
        </div>
    </div>
</main>
<footer class="footer">
    <div class="container">
        <div class="footer-content">
            <div class="footer-column">
                <h3 class="footer-logo">Handmade House</h3>
                <p class="footer-desc">Chào mừng đến với Handmade House, ngôi nhà nhỏ của những tâm hồn yêu nghệ thuật và thủ công.</p>
                <div class="social-links">
                    <a href="#"><i class="bx bxl-facebook"></i></a>
                    <a href="#"><i class="bx bxl-instagram"></i></a>
                    <a href="#"><i class="bx bxl-tiktok"></i></a>
                </div>
            </div>
            <div class="footer-column">
                <h3 class="footer-title">Blog</h3>
                <ul class="footer-links">
                    <li><a href="#">Câu chuyện thương hiệu</a></li>
                    <li><a href="#">Giá trị & Triết lý thương hiệu</a></li>
                    <li><a href="#">Quy trình sản xuất</a></li>
                    <li><a href="#">Cam kết & Định hướng bền vững</a></li>
                </ul>
            </div>
            <div class="footer-column">
                <h3 class="footer-title">Hỗ trợ</h3>
                <ul class="footer-links">
                    <li><a href="#">Chính sách đổi trả</a></li>
                    <li><a href="#">Hướng dẫn đặt hàng</a></li>
                    <li><a href="#">Phương thức thanh toán</a></li>
                    <li><a href="#">Câu hỏi thường gặp</a></li>
                </ul>
            </div>
            <div class="footer-column">
                <h3 class="footer-title">Liên hệ</h3>
                <ul class="footer-links">
                    <li>📍 Khu phố 6, Phường Linh Trung, TP. Thủ Đức, TP. Hồ Chí Minh</li>
                    <li>📞 0944912685</li>
                    <li>📧 handmadehouse23@handmade.vn</li>
                    <li>🕐 T2 - CN: 8:00 - 17:00</li>
                </ul>
            </div>
        </div>
        <div class="footer-bottom">
            <p>@2025 Handmade. Tất cả quyền được bảo lưu.</p>
        </div>
    </div>
</footer>
<script>
    window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
