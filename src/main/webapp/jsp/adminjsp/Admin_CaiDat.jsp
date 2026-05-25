<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Cài đặt</title>
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_CaiDat.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/category"><i class="bx bx-category"></i>Danh mục</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/orders"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/banner"><i class="bx bx-image"></i>Banner</a></li>
            <li class="active"><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="${pageContext.request.contextPath}/home">
            <i class="bx bx-log-out"></i> Đăng xuất
        </a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Cài đặt</h2>
        <div class="search-box">
            <input type="text" placeholder="Tìm kiếm...">
            <button><i class="bx bx-search"></i></button>
        </div>
        <div class="user-info">
            <a href="${pageContext.request.contextPath}/admin/notifications" class="notification-btn">
                <i class="bx bx-bell"></i>
                <c:if test="${notificationCount > 0}">
                    <span class="notification-count">${notificationCount}</span>
                </c:if>
            </a>

            <a href="${pageContext.request.contextPath}/admin/setting" class="profile-admin">
                <span class="admin-avatar">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.userName}">
                            ${fn:substring(sessionScope.user.userName, 0, 1)}
                        </c:when>
                        <c:otherwise>A</c:otherwise>
                    </c:choose>
                </span>

                <div class="user-details">
                    <span class="user-name">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user.userName}">
                                ${sessionScope.user.userName}
                            </c:when>
                            <c:otherwise>Admin Test</c:otherwise>
                        </c:choose>
                    </span>
                    <span class="user-role">Quản trị viên</span>
                </div>
            </a>
        </div>
    </header>
    <div class="setting-container">
        <section class="settings-card">
            <div class="settings-card-header">
                <div>
                    <h3>
                        <i class="bx bx-store-alt"></i>
                        Thông tin cửa hàng
                    </h3>
                    <p>Cập nhật thông tin cơ bản hiển thị cho cửa hàng.</p>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/admin/setting" method="post">
                <div class="form-grid">
                    <div class="form-group">
                        <label for="storeName">Tên cửa hàng</label>
                        <input id="storeName" type="text" name="storeName" value="Handmade House">
                    </div>

                    <div class="form-group">
                        <label for="storeEmail">Email liên hệ</label>
                        <input id="storeEmail" type="email" name="storeEmail" value="handmadehouse23@handmade.vn">
                    </div>

                    <div class="form-group">
                        <label for="storePhone">Số điện thoại</label>
                        <input id="storePhone" type="text" name="storePhone" value="0944912685">
                    </div>

                    <div class="form-group">
                        <label for="storeWebsite">Website</label>
                        <input id="storeWebsite" type="text" name="storeWebsite" value="https://handmadehouse.com">
                    </div>

                    <div class="form-group full-width">
                        <label for="storeAddress">Địa chỉ</label>
                        <textarea id="storeAddress" name="storeAddress" rows="3">Khu phố 6, Phường Linh Trung, TP. Thủ Đức, TP. Hồ Chí Minh</textarea>
                    </div>
                </div>

                <div class="card-footer">
                    <button type="submit" class="btn-save">
                        <i class="bx bx-save"></i>
                        Lưu thay đổi
                    </button>
                </div>
            </form>
        </section>

        <section class="settings-card">
            <div class="settings-card-header">
                <div>
                    <h3>
                        <i class="bx bx-bell"></i>
                        Thông báo
                    </h3>
                    <p>Quản lý các loại thông báo dành cho quản trị viên.</p>
                </div>
            </div>

            <div class="switch-group">
                <div class="switch-item">
                    <div class="info">
                        <strong>Đơn hàng mới</strong>
                        <p>Nhận thông báo khi khách hàng tạo đơn hàng mới.</p>
                    </div>

                    <label class="toggle">
                        <input type="checkbox" checked>
                        <span class="slider"></span>
                    </label>
                </div>

                <div class="switch-item">
                    <div class="info">
                        <strong>Đánh giá mới</strong>
                        <p>Nhận thông báo khi có đánh giá mới cần kiểm duyệt.</p>
                    </div>

                    <label class="toggle">
                        <input type="checkbox" checked>
                        <span class="slider"></span>
                    </label>
                </div>

                <div class="switch-item">
                    <div class="info">
                        <strong>Sản phẩm hết hàng</strong>
                        <p>Thông báo khi số lượng tồn kho của sản phẩm bằng 0.</p>
                    </div>

                    <label class="toggle">
                        <input type="checkbox" checked>
                        <span class="slider"></span>
                    </label>
                </div>

                <div class="switch-item">
                    <div class="info">
                        <strong>Liên hệ từ người dùng</strong>
                        <p>Nhận thông báo khi người dùng gửi phản hồi hoặc liên hệ mới.</p>
                    </div>

                    <label class="toggle">
                        <input type="checkbox" checked>
                        <span class="slider"></span>
                    </label>
                </div>
            </div>
        </section>

        <section class="settings-card">
            <div class="settings-card-header">
                <div>
                    <h3>
                        <i class="bx bx-lock-alt"></i>
                        Bảo mật tài khoản
                    </h3>
                    <p>Đổi mật khẩu quản trị viên để tăng tính bảo mật.</p>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/admin/setting" method="post">
                <div class="form-grid">
                    <div class="form-group full-width">
                        <label for="currentPassword">Mật khẩu hiện tại</label>
                        <input id="currentPassword" type="password" name="currentPassword" placeholder="Nhập mật khẩu hiện tại">
                    </div>

                    <div class="form-group">
                        <label for="newPassword">Mật khẩu mới</label>
                        <input id="newPassword" type="password" name="newPassword" placeholder="Nhập mật khẩu mới">
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Xác nhận mật khẩu mới</label>
                        <input id="confirmPassword" type="password" name="confirmPassword" placeholder="Nhập lại mật khẩu mới">
                    </div>
                </div>

                <div class="card-footer">
                    <button type="button" class="btn-password">
                        <i class="bx bx-lock-alt"></i>
                        Đổi mật khẩu
                    </button>
                </div>
            </form>
        </section>
    </div>
</main>
</body>
</html>