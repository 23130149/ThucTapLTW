<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/blog.css">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/Header_Footer/Styles.css">
  <meta charset="UTF-8">
  <title>Blog - Handmade House</title>

  <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
  <link rel="preconnect" href="https://unsplash.com">
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
        <input type="text" class="search-input" name="keyword" value="${keyword}" placeholder="Tìm kiếm bất cứ thứ gì" aria-label="Tìm kiếm sản phẩm" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false">
        <button type="submit" class="search-btn">
          <i class="bx bx-search-alt-2"></i>
        </button>
      </form>
      <div class="icons">
        <a href="${pageContext.request.contextPath}/cart" class="icon-btn" id="cartBtn">
          <i class='bx  bx-cart'></i>
        </a>
        <a href="${pageContext.request.contextPath}/Account" class="icon-btn" id="userBtn">
          <i class='bx  bx-user'></i>
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
<main class="about-us-container">
  <h1>Blog</h1>

  <p>Chào mừng đến với Handmade House! Chúng tôi tin rằng mỗi sản phẩm thủ công không chỉ là một vật dụng, mà còn là
    một câu chuyện, một tác phẩm nghệ thuật chứa đựng tâm huyết và sự sáng tạo. Hãy cùng tìm hiểu thêm về ngôi nhà
    của chúng tôi.</p>

  <section id="cau-chuyen" class="about-section">
    <h2>Câu chuyện thương hiệu</h2>
    <p>Handmade House không chỉ là một cái tên, đó là một hành trình. Chúng tôi bắt đầu vào năm 2025 từ một căn gác
      nhỏ, với niềm tin rằng những sản phẩm làm bằng tay mang trong mình một giá trị tinh thần mà máy móc công
      nghiệp không bao giờ có thể thay thế. Giữa một thế giới vận động không ngừng, chúng tôi muốn tạo ra một
      "ngôi nhà" lưu giữ nét đẹp của sự tỉ mỉ, sự kiên nhẫn và câu chuyện đằng sau mỗi tác phẩm.</p>

    <p>Sứ mệnh của chúng tôi là kết nối thế hệ trẻ Việt Nam với nghệ thuật thủ công truyền thống, nhưng qua một lăng
      kính hiện đại và sáng tạo. Chúng tôi muốn mỗi sản phẩm bạn cầm trên tay không chỉ đẹp, mà còn kể một câu
      chuyện về văn hóa, về sự khéo léo và về niềm đam mê của người nghệ nhân đã tạo ra nó.</p>
    <img src=https://img.freepik.com/free-photo/handcraft-handmade-diy-skills-drawing_53876-125030.jpg?semt=ais_hybrid&w=740&q=80
         class="brand-story-image">
  </section>

  <section id="gia-tri" class="about-section">
    <h2>Giá trị & Triết lý thương hiệu</h2>
    <p>Tại Handmade House, chúng tôi tin rằng giá trị thật sự không nằm ở số lượng sản phẩm làm ra, mà ở tình yêu và
      tâm huyết gửi gắm trong từng chi tiết nhỏ nhất.
      Chúng tôi tôn vinh sự chậm rãi, tỉ mỉ và tinh tế – những điều tưởng chừng đơn giản nhưng lại làm nên linh
      hồn của nghệ thuật thủ công.</p>
    <p>Mỗi sản phẩm được tạo ra không chỉ để sử dụng, mà còn để kể một câu chuyện: về bàn tay khéo léo của người
      nghệ nhân, về nét đẹp của chất liệu tự nhiên, và về niềm tin rằng những điều được làm bằng tay luôn mang
      trong mình hơi ấm của con người.</p>
    <p> Triết lý của chúng tôi là “Làm bằng tay – gửi bằng tim”. Đó không chỉ là khẩu hiệu, mà là cách Handmade
      House sống, sáng tạo và kết nối với khách hàng mỗi ngày.</p>
  </section>
  <section id="quy-trinh" class="about-section">
    <h2>Quy trình sản xuất</h2>
    <p>Để đảm bảo mỗi sản phẩm đến tay bạn là hoàn hảo, chúng tôi tuân thủ một quy trình sản xuất tỉ mỉ, kết hợp
      giữa kỹ thuật truyền thống và kiểm soát chất lượng hiện đại.</p>
    <ol>
      <li><strong>Lên ý tưởng & Phác thảo:</strong> Đội ngũ thiết kế của chúng tôi liên tục nghiên cứu xu hướng và
        tìm kiếm cảm hứng từ văn hóa, thiên nhiên để tạo ra các bản phác thảo độc đáo.
      </li>
      <li><strong>Tuyển chọn nguyên liệu:</strong> Chúng tôi ưu tiên các vật liệu thân thiện với môi trường và có
        nguồn gốc rõ ràng, từ sợi len cotton, đất sét tự nhiên, đến gỗ tái chế.
      </li>
      <li><strong>Chế tác thủ công:</strong> Đây là bước quan trọng nhất. Các nghệ nhân sẽ dùng đôi bàn tay khéo
        léo của mình để biến nguyên liệu thô thành tác phẩm. Mỗi chi tiết đều được chăm chút cẩn thận.
      </li>
      <li><strong>Kiểm tra chất lượng (KCS):</strong> Từng sản phẩm hoàn thiện phải vượt qua vòng kiểm tra nghiêm
        ngặt về độ bền, tính thẩm mỹ và độ an toàn trước khi được đóng gói.
      </li>
      <li><strong>Đóng gói & Gửi trao:</strong> Chúng tôi sử dụng bao bì tối giản, thân thiện với môi trường, đảm
        bảo sản phẩm đến tay bạn vẹn nguyên như một món quà.
      </li>
      <img src=https://th.bing.com/th/id/OIP.Zojxj7XTFiJR2ST72a3CUQHaD4?w=345&h=181&c=7&r=0&o=7&cb=ucfimg2&dpr=1.3&pid=1.7&rm=3&ucfimg=1
           class="brand-story-image">
    </ol>
  </section>

  <section id="tuyen-dung" class="about-section">
    <h2>Cam kết & Định hướng bền vững</h2>
    <p>Handmade House hướng đến một tương lai nơi thủ công và bền vững song hành.
      Chúng tôi cam kết sử dụng nguyên liệu thân thiện với môi trường, ưu tiên tái chế và tận dụng phần dư thừa
      trong quá trình sản xuất.</p>
    <p>Từ sợi dây đan, tấm vải, đến bao bì sản phẩm – mọi thứ đều được lựa chọn cẩn trọng để giảm thiểu tác động đến
      thiên nhiên.
      Chúng tôi cũng hợp tác cùng các làng nghề địa phương, giúp duy trì sinh kế và gìn giữ giá trị văn hóa truyền
      thống Việt Nam.
      Mỗi sản phẩm ra đời là một lời hứa: đẹp, bền vững và có ý nghĩa. .</p>
    <img src=https://tse3.mm.bing.net/th/id/OIP.ufFB01OapiU0I96TFpwnKQHaE7?cb=ucfimg2ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3
         class="brand-story-image">
  </section>

</main>
<footer class="footer">
  <div class="container">
    <div class="footer-content">
      <div class="footer-column">
        <h3 class="footer-logo">Handmade House</h3>
        <p class="footer-desc">Chào mừng đến với Handmade House, ngôi nhà nhỏ của những tâm hồn yêu nghệ thuật
          và thủ công.</p>
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
          <li><a href="#"> Giá trị & Triết lý thương hiệu</a></li>
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
</body>
</html>