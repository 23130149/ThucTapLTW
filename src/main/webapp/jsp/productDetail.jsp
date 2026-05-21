<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>${product.productName}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chitietsp.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header_footer.css">
  <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
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
        <a href="${pageContext.request.contextPath}/favorite" class="icon-btn favorite-header-icon" id="heartBtn" title="Sản phẩm yêu thích">
          <i class='bx bx-heart'></i>
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

<div class="page-title">
  <div class="page-title-container">
    <h2 class="page-main-title">Chi tiết sản phẩm</h2>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/home">Trang chủ</a>

      <span>/</span>

      <a href="${pageContext.request.contextPath}/products?category=${product.categoryName}">
        ${product.categoryName}
      </a>

      <span>/</span>

      <span class="current-page">
        ${product.productName}
      </span>
    </div>
  </div>
</div>

<main class="product-detail-page">
  <div class="container">
    <div class="product-detail-content">
      <div class="product-image">
        <div class="main-image favorite-image-box">
          <form action="${pageContext.request.contextPath}/favorite-toggle" method="post" class="favorite-form">
            <input type="hidden" name="productId" value="${product.productId}">
            <button type="submit" class="favorite-toggle ${product.favorite ? 'active' : ''}" aria-label="Yêu thích ${product.productName}">
              <i class="bx ${product.favorite ? 'bxs-heart' : 'bx-heart'}"></i>
            </button>
          </form>
          <c:choose>
            <c:when test="${not empty product.imageUrl}">
              <img class="main-product-image" src="${product.imageUrl}" alt="${product.productName}">
            </c:when>
            <c:otherwise>
              <img class="main-product-image" src="${pageContext.request.contextPath}/images/no-image.png" alt="No image">
            </c:otherwise>
          </c:choose>
        </div>

        <div class="thumbnail-list">
          <c:choose>
            <c:when test="${not empty product.imageUrl}">
              <img class="thumbnail-item active" src="${product.imageUrl}" alt="${product.productName}">
              <img class="thumbnail-item" src="${product.imageUrl}" alt="${product.productName}">
              <img class="thumbnail-item" src="${product.imageUrl}" alt="${product.productName}">
            </c:when>
            <c:otherwise>
              <img class="thumbnail-item active" src="${pageContext.request.contextPath}/images/no-image.png" alt="No image">
              <img class="thumbnail-item" src="${pageContext.request.contextPath}/images/no-image.png" alt="No image">
              <img class="thumbnail-item" src="${pageContext.request.contextPath}/images/no-image.png" alt="No image">
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="product-info-detail">
        <h1 class="product-title">${product.productName}</h1>
        <div class="product-rating">
          <div class="stars">
            <i class="bx bxs-star"></i>
            <i class="bx bxs-star"></i>
            <i class="bx bxs-star"></i>
            <i class="bx bxs-star"></i>
            <i class="bx bxs-star"></i>
          </div>
          <span class="rating-text">5.0</span>
        </div>

        <p class="price"><fmt:formatNumber value="${product.productPrice}" type="number" groupingUsed="true"/> đ</p>

        <div class="product-des">
          <h2>Mô tả sản phẩm</h2>
          <p>${product.productDescription}</p>
        </div>

        <div class="purchase-box">
          <div class="quantity-input-box">
            <input type="number" class="quantity-input" value="1" min="1" max="${product.stockQuantity}">
            <div class="quantity-arrows">
              <button type="button" class="arrow-up"><i class="bx bx-chevron-up"></i></button>
              <button type="button" class="arrow-down"><i class="bx bx-chevron-down"></i></button>
            </div>
          </div>

          <div class="action-buttons">
            <a class="btn btn-add-to-cart" href="${pageContext.request.contextPath}/Add-Cart?id=${product.productId}&quantity=1">
              <i class="bx bx-cart"></i> Thêm vào giỏ hàng
            </a>

            <a class="btn btn-buy-now" href="${pageContext.request.contextPath}/Add-Cart?id=${product.productId}&quantity=1&buyNow=1">
              Mua ngay
            </a>
          </div>
        </div>

        <div class="product-meta">
          <p><strong>Danh mục: </strong><a href="${pageContext.request.contextPath}/product?categoryId=${product.categoryId}">${product.categoryName}</a></p>
          <div class="share-links">
            <strong>Chia sẻ:</strong>
            <a href="#"><i class="bx bxl-facebook"></i></a>
            <a href="#"><i class="bx bxl-instagram"></i></a>
            <a href="#"><i class="bx bxl-tiktok"></i></a>
          </div>
        </div>
      </div>
    </div>

    <section class="related-products">
      <h2 id="related-title">Sản phẩm liên quan</h2>
      <div class="product-grid">
        <c:forEach var="rp" items="${relatedProducts}">
          <div class="product-item">
            <div class="product-top">
              <form action="${pageContext.request.contextPath}/favorite-toggle" method="post" class="favorite-form">
                <input type="hidden" name="productId" value="${rp.productId}">
                <button type="submit" class="favorite-toggle ${rp.favorite ? 'active' : ''}" aria-label="Yêu thích ${rp.productName}">
                  <i class="bx ${rp.favorite ? 'bxs-heart' : 'bx-heart'}"></i>
                </button>
              </form>
              <a href="${pageContext.request.contextPath}/product-detail?id=${rp.productId}" class="product-thumb">
                <c:choose>
                  <c:when test="${not empty rp.imageUrl}">
                    <img src="${rp.imageUrl}" alt="${rp.productName}">
                  </c:when>
                  <c:otherwise>
                    <img src="${pageContext.request.contextPath}/images/no-image.png" alt="No image">
                  </c:otherwise>
                </c:choose>
              </a>
            </div>
            <div class="product-info">
              <a href="${pageContext.request.contextPath}/product?categoryId=${rp.categoryId}" class="product-cat">${rp.categoryName}</a>
              <a href="${pageContext.request.contextPath}/product-detail?id=${rp.productId}" class="product-name">${rp.productName}</a>
              <div class="product-price"><fmt:formatNumber value="${rp.productPrice}" type="number" groupingUsed="true"/> đ</div>
            </div>
          </div>
        </c:forEach>
      </div>
    </section>
  </div>
</main>

<jsp:include page="/jsp/footer.jsp" />

<script>
  document.querySelectorAll('.thumbnail-item').forEach(function (thumb) {
    thumb.addEventListener('click', function () {
      const mainImg = document.querySelector('.main-product-image');
      if (mainImg) {
        mainImg.src = this.src;
      }
      document.querySelectorAll('.thumbnail-item').forEach(function (item) {
        item.classList.remove('active');
      });
      this.classList.add('active');
    });
  });
</script>
</body>
</html>
