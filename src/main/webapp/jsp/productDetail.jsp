<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/chitietsp.css">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/header_footer.css">
  <meta charset="UTF-8">
  <title>${product.productName}</title>
  <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
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
        <input type="text" class="search-input" name="keyword" placeholder="Tìm kiếm bất cứ thứ gì..." aria-label="Tìm kiếm sản phẩm">
        <button type="submit" class="search-btn">
          <i class="bx bx-search-alt-2"></i>
        </button>
      </form>
      <div class="icons" >
        <a href="${pageContext.request.contextPath}/favorite" class="icon-btn favorite-header-icon" id="heartBtn" title="Sản phẩm yêu thích">
          <i class='bx bx-heart'></i>
        </a>
        <a href="${pageContext.request.contextPath}/cart" class="icon-btn cart-icon" id="cartBtn">
          <i class='bx  bx-cart'></i>
        
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                        <span class="cart-badge">${sessionScope.cart.totalQuantity}</span>
                    </c:if>
                </a>
        <a href="${pageContext.request.contextPath}/Account" class="icon-btn" id="userBtn">
          <i class='bx  bx-user'></i>
        </a>
      </div>
    </div>
  </div>
  <div class="search-bar-section header-bottom-nav">
    <div class="container nav-only-container">
      <nav class="nav__links" >
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
<div class="page-title">
  <div class="page-title-container">
    <h2 class="page-main-title">Chi tiết sản phẩm</h2>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
      <i class="bx bx-chevron-right"></i>
      <a href="${pageContext.request.contextPath}/product?categoryId=${product.categoryId}">${product.categoryName}</a>
      <i class="bx bx-chevron-right"></i>
      <span>${product.productName}</span>
    </div>
  </div>
</div>
<main class="product-detail-page">
  <div class="product-detail-wrapper">

    <div class="product-image">
      <div class="main-image-box">
        <c:choose>
          <c:when test="${product.stockQuantity > 0}">
            <div class="stock-badge">
              Còn ${product.stockQuantity} sản phẩm
            </div>
          </c:when>

          <c:otherwise>
            <div class="sold-out-overlay">HẾT HÀNG</div>
          </c:otherwise>
        </c:choose>
        <form action="${pageContext.request.contextPath}/favorite-toggle" method="post" class="favorite-form">
          <input type="hidden" name="productId" value="${product.productId}">
          <button type="submit"
                  class="image-favorite-btn ${product.favorite ? 'active' : ''}"
                  aria-label="Yêu thích ${product.productName}">
            <i class="bx ${product.favorite ? 'bxs-heart' : 'bx-heart'}"></i>
          </button>
        </form>
        <img id="mainImage"
             src="${not empty productImages ? productImages[0].imageUrl : product.imageUrl}"
             alt="${product.productName}"
             class="main-product-image">
      </div>

      <div class="thumbnail-list">
        <c:forEach var="img" items="${productImages}" varStatus="status">
          <img src="${img.imageUrl}"
               data-src="${img.imageUrl}"
               alt="${product.productName}"
               class="thumbnail-item ${status.first ? 'active' : ''}">
        </c:forEach>
      </div>
    </div>

    <div class="product-info-detail">
      <h1 class="product-title">${product.productName}</h1>

      <div class="product-rating">
        <div class="stars">
          <c:forEach begin="1" end="5" var="i">
            <i class="bx ${avgRating >= i ? 'bxs-star' : 'bx-star'}"></i>
          </c:forEach>
        </div>

        <span>${avgRating}/5 (${reviewCount} đánh giá)</span>
      </div>

      <p class="price">
        <fmt:formatNumber value="${product.productPrice}" groupingUsed="true"/> đ
      </p>

      <div class="stock-info">
        <c:choose>
          <c:when test="${product.stockQuantity > 0}">
            <span class="in-stock">Còn hàng</span>
            <span class="stock-number">Số lượng còn lại: ${product.stockQuantity}</span>
          </c:when>

          <c:otherwise>
            <span class="out-stock">Sản phẩm hiện đã hết hàng</span>
          </c:otherwise>
        </c:choose>
      </div>

      <div class="product-des">
        <h2>Mô tả sản phẩm</h2>
        <p>${product.productDescription}</p>
      </div>

      <form class="purchase-box" action="${pageContext.request.contextPath}/Add-Cart" method="get">
        <input type="hidden" name="id" value="${product.productId}">

        <div class="purchase-inline-row">
          <div class="quantity-input-box">
            <button type="button" class="qty-btn arrow-down">-</button>

            <input type="number"
                   name="quantity"
                   class="quantity-input"
                   value="1"
                   min="1"
                   max="${product.stockQuantity}">

            <button type="button" class="qty-btn arrow-up">+</button>
          </div>

          <button type="submit"
                  class="btn btn-add-to-cart"
          ${product.stockQuantity <= 0 ? 'disabled' : ''}>
            <i class="bx bx-cart"></i>
            Thêm vào giỏ hàng
          </button>

          <button type="submit"
                  name="buyNow"
                  value="1"
                  class="btn btn-buy-now"
          ${product.stockQuantity <= 0 ? 'disabled' : ''}>
            Mua ngay
          </button>
        </div>

      </form>

      <div class="product-meta">
        <p>
          <strong>Danh mục: </strong>
          <a href="${pageContext.request.contextPath}/product?categoryId=${product.categoryId}">
            ${product.categoryName}
          </a>
        </p>

        <div class="share-links">
          <strong>Chia sẻ:</strong>
          <a href="#"><i class="bx bxl-facebook"></i></a>
          <a href="#"><i class="bx bxl-instagram"></i></a>
          <a href="#"><i class="bx bxl-tiktok"></i></a>
        </div>
      </div>
    </div>
  </div>
    <div class="review-list">
      <h3>Bình luận từ khách hàng (${reviewCount})</h3>
      <c:if test="${empty reviews}">
        <p>Chưa có đánh giá nào.</p>
      </c:if>
      <c:forEach var="r" items="${reviews}">
        <div class="review-item">
          <div class="review-header">
            <span class="user-avatar">${fn:substring(r.userName,0,1)}</span>
            <div class="user-info">
              <p class="user-name">${r.userName}</p>
              <div class="review-rating">
                <c:forEach begin="1" end="5" var="i">
                  <i class="bx ${i <= r.rating ? 'bxs-star' : 'bx-star'}"></i>
                </c:forEach>
                <span class="review-date">${r.createAt}</span>
              </div>
            </div>
          </div>
          <p class="review-text">${r.comment}</p>
        </div>
      </c:forEach>
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
                <img src="${rp.imageUrl}"
                     alt="${rp.productName}">
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
          <li> <a href="#">Câu chuyện thương hiệu</a></li>
          <li> <a href="#"> Giá trị & Triết lý thương hiệu</a></li>
          <li> <a href="#">Quy trình sản xuất</a></li>
          <li> <a href="#">Cam kết & Định hướng bền vững</a></li>
        </ul>
      </div>

      <div class="footer-column">
        <h3 class="footer-title">Hỗ trợ</h3>
        <ul class="footer-links">
          <li> <a href="#">Chính sách đổi trả</a></li>
          <li> <a href="#">Hướng dẫn đặt hàng</a></li>
          <li> <a href="#">Phương thức thanh toán</a></li>
          <li> <a href="#">Câu hỏi thường gặp</a></li>
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
  const mainImage = document.getElementById("mainImage");
  const thumbnails = document.querySelectorAll(".thumbnail-item");

  thumbnails.forEach(function (thumb) {
    thumb.addEventListener("click", function () {
      mainImage.src = this.dataset.src;

      thumbnails.forEach(function (item) {
        item.classList.remove("active");
      });

      this.classList.add("active");
    });
  });

  const quantityInput = document.querySelector(".quantity-input");
  const arrowUp = document.querySelector(".arrow-up");
  const arrowDown = document.querySelector(".arrow-down");

  if (quantityInput && arrowUp && arrowDown) {
    arrowUp.addEventListener("click", function () {
      const max = parseInt(quantityInput.max);
      let value = parseInt(quantityInput.value);

      if (value < max) {
        quantityInput.value = value + 1;
      }
    });

    arrowDown.addEventListener("click", function () {
      let value = parseInt(quantityInput.value);

      if (value > 1) {
        quantityInput.value = value - 1;
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