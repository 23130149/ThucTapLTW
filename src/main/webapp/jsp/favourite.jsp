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

<main class="favourite-section">
    <div class="container">
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
                <div class="product-list">
                    <c:forEach items="${productList}" var="p">
                        <div class="product-item">
                            <div class="product-top">
                                <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}"
                                   class="product-thumb">
                                    <img src="${p.imageUrl}" alt="${p.productName}">
                                </a>

                                <form action="${pageContext.request.contextPath}/favorite-toggle"
                                      method="post"
                                      class="favorite-form">
                                    <input type="hidden" name="productId" value="${p.productId}">
                                    <button type="submit"
                                            class="favorite-toggle active"
                                            aria-label="Bỏ yêu thích">
                                        <i class="bx bxs-heart"></i>
                                    </button>
                                </form>
                            </div>

                            <div class="product-info">
                                <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}"
                                   class="product-name">
                                        ${p.productName}
                                </a>

                                <div class="price">
                                    <fmt:formatNumber value="${p.productPrice}" type="number" groupingUsed="true"/> đ
                                </div>

                                <a href="${pageContext.request.contextPath}/Add-Cart?id=${p.productId}&quantity=1"
                                   class="add-to-cart">
                                    <i class="bx bx-cart"></i>
                                    Thêm vào giỏ
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/jsp/footer.jsp" />
</body>
</html>
