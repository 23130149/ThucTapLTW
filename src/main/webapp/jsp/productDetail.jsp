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
<header class="header">
  <div class="header-top-container">
    <div class="header-content">
      <div class="logo">
        <a href="${pageContext.request.contextPath}/jsp/home.jsp">Handmade House</a>      </div>
      <form class="search-form" action="#" method="GET">
        <input type="text" class="search-input" placeholder="Tìm kiếm bất cứ thứ gì..." aria-label="Tìm kiếm sản phẩm">
        <button type="submit" class="search-btn">
          <i class="bx bx-search-alt-2"></i>
        </button>
      </form>
      <div class="icons" >
        <a href="${pageContext.request.contextPath}/cart.jsp" class="icon-btn" id="cartBtn">
          <i class='bx  bx-cart'></i>
        </a>
        <a href="${pageContext.request.contextPath}/account.jsp" class="icon-btn" id="userBtn">
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
          <li><a href="${pageContext.request.contextPath}/blog.jsp">Blog</a></li>
          <li><a href="${pageContext.request.contextPath}/contact.jsp">Liên hệ</a></li>
        </ul>
      </nav>
    </div>
  </div>
</header>
<div class="page-title">
  <div class="page-title-container">
    <h2 class="page-main-title">Chi tiết sản phẩm</h2>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/jsp/home.jsp">Trang chủ</a>
      <a href="#"><i class="bx bx-chevron-right"></i></a>
      <span>Chi tiết sản phẩm</span>
      <i class="bx bx-chevron-right"></i>
      <span>${product.categoryName}</span>
      <i class="bx bx-chevron-right"></i>
      <span>${product.productName}</span
    </div>
  </div>
</div>
<main class="product-detail-page">
  <div class="container">
    <div class="product-detail-content">
      <div class="product-image">
        <div class="main-image">
          <img src="${product.imageUrl}" alt="${product.productName}">
        </div>
      </div>
      <div class="product-info">
        <h1 class="product-title">${product.productName}</h1>
        <div class="product-rating">
          <div class="stars">
            <i class="bx bxs-star"></i>
            <i class="bx bxs-star"></i>
            <i class="bx bxs-star"></i>
            <i class="bx bxs-star"></i>
            <i class="bx bxs-star"></i>
          </div>
          <span class="rating-text">5.0 (86 đánh giá)</span>
        </div>
        <p class="price">${product.productPrice}</p>
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
            <button class="btn btn-add-to-cart">
              <i class="bx bx-cart"></i> Thêm vào giỏ hàng
            </button>
            <div class="extra-action">
              <button class="btn btn-icon-action" aria-label="Yêu thích">
                <i class="bx bx-heart"></i>
              </button>
            </div>
          </div>
          <button type="button" class="btn btn-buy-now">Mua ngay</button>
        </div>
        <div class="product-meta">
          <p><strong>Danh mục: </strong><a href="${pageContext.request.contextPath}/product?categoryId=${product.category_id}">${product.category_name}</a></p>
          <div class="share-links">
            <strong>Chia sẻ:</strong>
            <a href="#"><i class="bx bxl-facebook"></i></a>
            <a href="#"><i class="bx bxl-instagram"></i></a>
            <a href="#"><i class="bx bxl-tiktok"></i></a>
          </div>
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
          <span class="user-avatar"> ${fn:substring(r.userName,0,1)}</span>
          <div class="user-info">
          <p class="user-name">${r.userName}</p>
            <div class="review-rating">
              <c:forEach begin="1" end="5" var="i">
                <i class="bx ${i <= r.rating ? 'bxs-star' : 'bx-star'}"></i>
              </c:forEach>
              <span class="review-date">${r.createAt}</span>
            </div>
          </div>
        <p class="review-text">${r.comment}</p>
    </c:forEach>
    </div>
    <section class="related-products">
      <h2 id="related-title">Sản phẩm liên quan</h2>
      <div class="product-grid">
        <c:forEach var="rp" items="${relatedProducts}">
          <div class="product-item">
            <div class="product-top">
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
</body>
</html>