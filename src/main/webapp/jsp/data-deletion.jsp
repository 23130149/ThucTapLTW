<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xóa dữ liệu người dùng - Handmade House</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/legal.css">
    <jsp:include page="/jsp/layout-assets.jsp"/>
</head>
<body>
<jsp:include page="/jsp/header.jsp"/>

<main class="legal-page">
    <section class="legal-container">
        <h1>Hướng dẫn xóa dữ liệu người dùng</h1>
        <p class="legal-updated">Cập nhật lần cuối: 15/06/2026</p>

        <p>
            Nếu bạn đã đăng nhập Handmade House bằng Facebook, Google hoặc tài khoản email thông thường, bạn có thể yêu
            cầu xóa dữ liệu cá nhân khỏi hệ thống theo hướng dẫn dưới đây.
        </p>

        <h2>Cách gửi yêu cầu xóa dữ liệu</h2>
        <ol>
            <li>Gửi email đến <strong>handmadehouse23@handmade.vn</strong>.</li>
            <li>Tiêu đề email: <strong>Yêu cầu xóa dữ liệu Handmade House</strong>.</li>
            <li>Trong nội dung email, cung cấp email tài khoản bạn đã dùng để đăng nhập.</li>
            <li>Chúng tôi sẽ xác minh yêu cầu và phản hồi trong vòng 7 ngày làm việc.</li>
        </ol>

        <h2>Dữ liệu được xóa</h2>
        <ul>
            <li>Thông tin tài khoản như tên, email, số điện thoại.</li>
            <li>Liên kết đăng nhập Facebook hoặc Google.</li>
            <li>Địa chỉ giao hàng, sản phẩm yêu thích và dữ liệu giỏ hàng.</li>
            <li>Nội dung đánh giá hoặc liên hệ nếu không cần giữ lại cho mục đích xử lý tranh chấp.</li>
        </ul>

        <h2>Dữ liệu có thể được giữ lại</h2>
        <p>
            Một số dữ liệu giao dịch có thể được lưu trong thời gian cần thiết để đáp ứng nghĩa vụ kế toán, chống gian
            lận, xử lý khiếu nại hoặc yêu cầu pháp lý.
        </p>

        <div class="legal-contact">
            <strong>Liên hệ xóa dữ liệu:</strong>
            <p>Email: handmadehouse23@handmade.vn</p>
        </div>
    </section>
</main>

<jsp:include page="/jsp/footer.jsp"/>
</body>
</html>
