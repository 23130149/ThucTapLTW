<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chính sách quyền riêng tư - Handmade House</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/legal.css">
    <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<main class="legal-page">
    <section class="legal-container">
        <h1>Chính sách quyền riêng tư</h1>
        <p class="legal-updated">Cập nhật lần cuối: 15/06/2026</p>

        <p>
            Handmade House tôn trọng quyền riêng tư của khách hàng. Chính sách này giải thích cách chúng tôi thu thập,
            sử dụng và bảo vệ thông tin khi bạn đăng nhập, mua sắm hoặc liên hệ với cửa hàng.
        </p>

        <h2>1. Thông tin chúng tôi thu thập</h2>
        <ul>
            <li>Thông tin tài khoản như họ tên, email, số điện thoại.</li>
            <li>Thông tin đăng nhập từ Google hoặc Facebook, bao gồm mã định danh tài khoản và email nếu được cung cấp.</li>
            <li>Thông tin đơn hàng, địa chỉ giao hàng, sản phẩm yêu thích và lịch sử mua hàng.</li>
            <li>Nội dung bạn gửi qua biểu mẫu liên hệ hoặc đánh giá sản phẩm.</li>
        </ul>

        <h2>2. Mục đích sử dụng</h2>
        <ul>
            <li>Xác thực tài khoản và hỗ trợ đăng nhập.</li>
            <li>Xử lý giỏ hàng, đơn hàng, thanh toán và giao hàng.</li>
            <li>Cải thiện trải nghiệm mua sắm, gợi ý sản phẩm phù hợp hơn.</li>
            <li>Phản hồi yêu cầu hỗ trợ và bảo vệ hệ thống khỏi hành vi gian lận.</li>
        </ul>

        <h2>3. Chia sẻ dữ liệu</h2>
        <p>
            Chúng tôi không bán thông tin cá nhân của bạn. Dữ liệu chỉ được chia sẻ khi cần thiết cho vận hành hệ thống,
            ví dụ đơn vị thanh toán, giao hàng hoặc khi pháp luật yêu cầu.
        </p>

        <h2>4. Bảo mật</h2>
        <p>
            Handmade House áp dụng các biện pháp phù hợp để bảo vệ dữ liệu, bao gồm mã hóa mật khẩu và giới hạn quyền
            truy cập quản trị. Tuy vậy, không có hệ thống trực tuyến nào an toàn tuyệt đối.
        </p>

        <h2>5. Quyền của người dùng</h2>
        <p>
            Bạn có thể yêu cầu xem, chỉnh sửa hoặc xóa dữ liệu cá nhân. Hướng dẫn xóa dữ liệu được công bố tại trang
            <a href="${pageContext.request.contextPath}/data-deletion">Xóa dữ liệu người dùng</a>.
        </p>

        <div class="legal-contact">
            <strong>Liên hệ:</strong>
            <p>Email: handmadehouse23@handmade.vn</p>
        </div>
    </section>
</main>

<jsp:include page="/jsp/footer.jsp"/>
</body>
</html>
