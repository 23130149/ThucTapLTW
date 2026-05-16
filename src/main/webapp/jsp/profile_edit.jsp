<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chỉnh sửa thông tin - Handmade House</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/account.css">
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/header_footer.css">
  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">


  <style>
    .profile-edit-box {
      max-width: 650px;
      margin: 30px auto;
      background: #fff;
      padding: 30px;
      border-radius: 14px;
    }

    .profile-edit-box h2 {
      margin-bottom: 20px;
    }

    .form-row {
      margin-bottom: 16px;
    }

    .form-row label {
      font-weight: 600;
      display: block;
      margin-bottom: 6px;
    }

    .form-row input {
      width: 100%;
      padding: 11px 12px;
      border-radius: 8px;
      border: 1px solid #ddd;
      font-size: 14px;
    }

    .profile-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 24px;
    }

    .btn-save {
      background: #111;
      color: #fff;
      padding: 10px 22px;
      border-radius: 8px;
      border: none;
      cursor: pointer;
    }

    .btn-danger {
      background: #d32f2f;
      color: #fff;
      padding: 10px 18px;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
    }
  </style>
</head>

<body>

<header class="header">
  <div class="header-top-container">
    <div class="header-content">

      <div class="logo">
        <a href="${pageContext.request.contextPath}/Home">Handmade House</a>
      </div>

      <form class="search-form"
            action="${pageContext.request.contextPath}/Search"
            method="GET">
        <input type="text" class="search-input"
               name="keyword"
               placeholder="Tìm kiếm bất cứ thứ gì...">
        <button type="submit" class="search-btn">
          <i class="bx bx-search-alt-2"></i>
        </button>
      </form>

      <div class="icons">
        <a href="${pageContext.request.contextPath}/Cart" class="icon-btn">
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
          <li><a href="${pageContext.request.contextPath}/Home">Trang chủ</a></li>
          <li><a href="${pageContext.request.contextPath}/Products">Sản phẩm</a></li>
          <li><a href="${pageContext.request.contextPath}/Blog">Blog</a></li>
          <li><a href="${pageContext.request.contextPath}/Contact">Liên hệ</a></li>
        </ul>
      </nav>
    </div>
  </div>
</header>
<main class="about-us-container">

  <h1>Chỉnh sửa thông tin cá nhân</h1>

  <div class="profile-edit-box">
    <h2>Hồ sơ của bạn</h2>

    <form action="${pageContext.request.contextPath}/Profile/Edit" method="post">

      <div class="form-row">
        <label>Họ và tên</label>
        <input type="text" name="userName"
               value="${sessionScope.user.userName}">
      </div>

      <div class="form-row">
        <label>Email</label>
        <input type="email"
               value="${sessionScope.user.email}" disabled>
      </div>

      <div class="form-row">
        <label>Số điện thoại</label>
        <input type="text" name="phone"
               value="${sessionScope.user.phone}">
      </div>
      <div class="form-row">
        <label>Ngày sinh</label>
        <input type="date" name="dateOfBirth"
               value="${sessionScope.user.dateOfBirth}">
      </div>

      <div class="form-row">
        <label>Giới tính</label>
        <select name="gender">
          <option value="">Chọn giới tính</option>
          <option value="MALE" ${sessionScope.user.gender == 'MALE' ? 'selected' : ''}>Nam</option>
          <option value="FEMALE" ${sessionScope.user.gender == 'FEMALE' ? 'selected' : ''}>Nữ</option>
          <option value="OTHER" ${sessionScope.user.gender == 'OTHER' ? 'selected' : ''}>Khác</option>
        </select>
      </div>

      <div class="form-row">
        <label>Ảnh đại diện URL</label>
        <input type="text" name="avatarUrl"
               value="${sessionScope.user.avatarUrl}">
      </div>

      <div class="form-row">
        <label>Giới thiệu ngắn</label>
        <input type="text" name="bio"
               value="${sessionScope.user.bio}">
      </div>

      <div class="profile-actions">
        <button type="submit" class="btn-save">
          Lưu thay đổi
        </button>

        <a href="${pageContext.request.contextPath}/ChangePassword"
           class="btn-danger">
          Đổi mật khẩu
        </a>
      </div>

    </form>
  </div>

</main>

<footer class="footer">
  <div class="container">
    <div class="footer-bottom">
      <p>© 2025 Handmade House. Tất cả quyền được bảo lưu.</p>
    </div>
  </div>
</footer>

</body>
</html>
