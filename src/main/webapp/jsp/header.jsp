<%@ page contentType="text/html;charset=UTF-8" %>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/Header_Footer/Styles.css">
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">

<header class="header">
  <div class="header-top-container">
    <div class="header-content">
      <div class="logo">
        <a href="${pageContext.request.contextPath}/home">Handmade House</a>
      </div>

      <form class="search-form" action="${pageContext.request.contextPath}/product" method="GET">
        <input type="text" class="search-input" name="keyword"
               value="${keyword}" placeholder="Tìm kiếm bất cứ thứ gì">
        <button type="submit" class="search-btn">
          <i class="bx bx-search-alt-2"></i>
        </button>
      </form>

      <div class="icons">
        <a href="${pageContext.request.contextPath}/cart" class="icon-btn">
          <i class='bx bx-cart'></i>
        </a>

        <a href="${pageContext.request.contextPath}/Account" class="icon-btn">
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