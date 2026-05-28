<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/trangchu.css">

  <meta charset="UTF-8">
  <title>Handmade Shop</title>
  <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
  <link rel="preconnect" href="https://unsplash.com">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>
<c:if test="${not empty sessionScope.cartMessage}">
  <div class="cart-toast">
    <i class='bx bx-check-circle'></i>
    <span>${sessionScope.cartMessage}</span>
  </div>
  <c:remove var="cartMessage" scope="session"/>
</c:if>
<jsp:include page="/jsp/header.jsp"/>
<section class="section__container" id="home">
  <div class="wavy-top"></div>
  <div class="slider-wrapper">
    <div class="slide slide-active"
         style="background-image: url('https://plus.unsplash.com/premium_photo-1661753115934-cdf47487b294?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=871');">
      <div class="slide-overlay"></div>
      <div class="slide-content">
        <h1 class="section-title">
          <span class="section-title-line">The Beauty</span>
          <span class="section-title-line">Of Handmade</span>
        </h1>
        <p class="section-subtitle">
          Chào mừng bạn đến với Handmade House, một chủ đề đầy cảm hứng được tạo ra riêng cho hành trình sáng
          tạo của bạn!<br>
          Dù là dự án thủ công hay sản phẩm nghệ thuật, Handmade House đều có mọi thứ bạn cần.
        </p>
        <a href="${pageContext.request.contextPath}/product" class="btn-view-more">Xem thêm</a>
      </div>
    </div>
    <div class="slide"
         style="background-image: url('https://bepos.io/wp-content/uploads/2021/08/lam-do-handmade-tai-nha-220.jpg')">
      <div class="slide-overlay"></div>
      <div class="slide-content">
        <h1 class="section-title">
          <span class="section-title-line">HANDMADE GOODS</span>
          <span class="section-title-line">FOR GENZ</span>
        </h1>
        <p class="section-subtitle">
          Tự tin thể hiện gu thẩm mỹ và sự độc đáo qua từng sản phẩm thủ công, dẫn đầu xu hướng.
        </p>
        <a href="${pageContext.request.contextPath}/product" class="btn-view-more">Xem thêm</a>
      </div>
    </div>
    <div class="slide"
         style="background-image: url('https://t4.ftcdn.net/jpg/03/97/34/39/360_F_397343924_6WlXOaMVHNKkhMs2l8AHJ5e9MQ03YiBU.jpg');">
      <div class="slide-overlay"></div>
      <div class="slide-content">
        <h1 class="section-title">
          <span class="section-title-line">ELEVATE YOUR SPACE</span>
          <span class="section-title-line">AND PERSONALITY</span>
        </h1>
        <p class="section-subtitle">
          Mang đến những sản phẩm thủ công chất lượng, kết hợp hoàn hảo giữa nghệ thuật và công năng.
        </p>
        <a href="${pageContext.request.contextPath}/product" class="btn-view-more">Xem thêm</a>
      </div>
    </div>
  </div>
</section>
<section class="categories">
  <div class="container">
    <div class="heading">
      <h3>Danh mục sản phẩm</h3>
      <p>Khám phá bộ sưu tập sản phẩm của chúng tôi</p>
    </div>
    <div class="category-list">
      <c:forEach items="${categoryList}" var="cat">
        <a href="${pageContext.request.contextPath}/product?categoryId=${cat.categoryId}"
           class="category-item">
          <img src="${cat.imageUrl}" alt="${cat.name}">
          <div class="category-overlay">
            <h4>${cat.name}</h4>
            <span>Xem sản phẩm</span>
          </div>
        </a>
      </c:forEach>
    </div>
  </div>
</section>
<section class="products-section">
  <div class="container">
    <div class="heading">
      <h3>Sản phẩm nổi bật</h3>
      <p>Chiêm ngưỡng những gợi ý hàng đầu của chúng tôi</p>
    </div>
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
            <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}"  class="product-thumb">
              <c:choose>
                <c:when test="${not empty p.imageUrl}">
                  <img src="${p.imageUrl}" alt="${p.productName}">
                </c:when>
                <c:otherwise>
                  <img src="${pageContext.request.contextPath}/images/no-image.png"
                       alt="No image">
                </c:otherwise>
              </c:choose>
            </a>
            <a href="${pageContext.request.contextPath}/Add-Cart?id=${p.productId}&quantity=1"
               class="add-to-cart-btn">
              <i class="bx bx-shopping-bag"></i>
              Thêm vào giỏ
            </a>
          </div>
          <div class="product-info">
            <a href="${pageContext.request.contextPath}/product?categoryId=${p.categoryId}" class="product-cat">${p.categoryName}</a>
            <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}" class="product-name">${p.productName}</a>
            <div class="product-price"><fmt:formatNumber value="${p.productPrice}" type="number" groupingUsed="true"/> đ</div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</section>
<section class="story-section">
  <div class="container">
    <div class="story-header">
      <h2 class="story-heading-main">Về chúng tôi</h2>
      <p class="story-subtitle">Câu chuyện của chúng tôi bắt đầu từ niềm đam mê thủ công</p>
    </div>
    <div class="story-content">
      <div class="story-text-container">
        <h3 class="story-title">Thủ công cho Gen Z</h3>
        <p>Handmade House chính thức ra đời năm 2025 với mục tiêu duy nhất là biến nghệ thuật thủ công truyền
          thống thành những món đồ mới mẻ, độc đáo, thể hiện cá tính riêng của giới trẻ hiện nay. Chúng tôi
          tin rằng phong cách không nên là hàng loạt, mà phải là duy nhất.</p>
        <p>Chúng tôi hoạt động như một studio sáng tạo, nơi đội ngũ thiết kế trẻ tuổi làm việc trực tiếp với hơn
          3 nghệ nhân để liên tục cập nhật các xu hướng TikTok, Instagram, và Facebook mới nhất. Sự kết hợp
          giữa kỹ thuật thủ công lâu đời và thẩm mỹ hiện đại tạo ra những sản phẩm vừa chất lượng, vừa
          "viral".</p>
        <p>Tại Handmade House, bạn không chỉ mua một phụ kiện; bạn đang mua một món đồ thể hiện cá tính. Từ móc
          khóa cá nhân hóa, túi xách đan lát theo mùa, đến nến thơm được custom (tùy chỉnh) mùi hương độc
          quyền – mỗi món đồ đều phản ánh sự tự do và sáng tạo của người sở hữu.</p>
        <p>Hãy cùng chúng tôi phá vỡ ranh giới giữa truyền thống và hiện đại. Ủng hộ Handmade House là cách bạn
          tham gia vào cộng đồng yêu thích sự độc đáo, trân trọng giá trị bền vững, và luôn dẫn đầu các xu
          hướng phong cách.</p>
      </div>
      <div class="story-image-container">
        <img src="https://i.pinimg.com/736x/25/26/13/2526135b0ee31195600ccfa88e2ed474.jpg"
             alt="Nhân viên đang làm việc" class="story-image-main">
      </div>
    </div>
  </div>
</section>
<jsp:include page="/jsp/footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/trangchu.js"></script>

<script>
  window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>