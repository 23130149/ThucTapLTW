<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Liên Hệ</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/contact.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>

<body>

<jsp:include page="/jsp/header.jsp"/>

<section class="banner">
    <div class="banner-content">
        <h2>Liên Hệ Với Chúng Tôi</h2>
        <p>Chúng tôi rất mong được nghe từ bạn! Hãy chia sẻ ý tưởng, câu hỏi hoặc một lời chào thân thiện.</p>
    </div>
</section>

<section class="info-section">
    <div class="info-card">
        <i class='bx bx-map'></i>
        <h3>Địa chỉ</h3>
        <p>789 Linh Trung, Thủ Đức, TP.HCM</p>
    </div>

    <div class="info-card">
        <i class='bx bx-envelope'></i>
        <h3>Email</h3>
        <p>hello@handmadehouse.vn</p>
    </div>

    <div class="info-card">
        <i class='bx bx-phone'></i>
        <h3>Điện thoại</h3>
        <p>0123 456 789</p>
    </div>

    <div class="info-card">
        <i class='bx bx-time'></i>
        <h3>Giờ làm việc</h3>
        <p>8:00 – 20:00 (Thứ 2 - Chủ Nhật)</p>
    </div>
</section>

<section class="contact-container">

    <div class="contact-form">
        <h2>Gửi Tin Nhắn</h2>
        <p>Điền thông tin vào form dưới đây, chúng tôi sẽ phản hồi trong vòng 24 giờ.</p>

        <c:if test="${not empty error}">
            <p style="color:red; margin-bottom:12px;">${error}</p>
        </c:if>

        <c:if test="${not empty success}">
            <p style="color:green; margin-bottom:12px;">${success}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/contact"
              method="post">

            <label>Họ và tên *</label>
            <input type="text"
                   name="name"
                   placeholder="Nhập họ tên của bạn"
                   required>

            <div class="row">
                <div class="col">
                    <label>Email *</label>
                    <input type="email"
                           name="email"
                           placeholder="example@email.com"
                           required>
                </div>

                <div class="col">
                    <label>Số điện thoại</label>
                    <input type="tel"
                           name="phone"
                           pattern="[0-9]{9,11}"
                           placeholder="0123456789">
                </div>
            </div>

            <label>Tiêu đề *</label>
            <input type="text"
                   name="subject"
                   placeholder="Bạn muốn hỏi về điều gì?"
                   required>

            <label>Nội dung tin nhắn *</label>
            <textarea name="message"
                      placeholder="Chia sẻ suy nghĩ của bạn với chúng tôi..."
                      required></textarea>

            <button type="submit">Gửi tin nhắn</button>
        </form>
    </div>

    <div class="sidebar">

        <div class="side-box">
            <h3>🐤 Đặt Hàng Custom</h3>
            <p>Bạn có ý tưởng riêng cho sản phẩm handmade? Chúng tôi nhận đặt hàng custom theo yêu cầu của bạn!</p>
            <p>Liên hệ với chúng tôi để được tư vấn chi tiết về thiết kế, chất liệu và thời gian thực hiện.</p>
        </div>

        <div class="side-box">
            <h3>❤️ Ưu Đãi Đặc Biệt</h3>
            <p>Nhận ngay mã giảm giá 10% cho lần mua hàng đầu tiên khi đăng ký nhận bản tin.</p>
            <p>Nhập email trong form liên hệ và ghi chú "Đăng ký nhận tin".</p>
        </div>

    </div>
</section>

<jsp:include page="/jsp/footer.jsp"/>



<script>
  window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
