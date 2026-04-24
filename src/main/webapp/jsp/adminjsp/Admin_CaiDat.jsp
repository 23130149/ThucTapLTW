<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Cài đặt</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="caidat.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li><a href="../Tongquan/tongquan.html"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li><a href="../Sanpham/qlsanpham.html"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="../Donhang/donhang.html"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li><a href="../Khachhang/khachhang.html"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="../Danhgia/danhgia.html"><i class="bx bx-star"></i>Đánh giá</a></li>
            <li class="active"><a href="../Caidat/caidat.html"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="../../../../../../html/trangchu.html"><i class="bx bx-log-out"></i>Đăng xuất</a>
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
            <span class="notification-badge"><i class="bx bx-bell"></i></span>
            <div class="profile-admin">
                <span class="admin-avatar">L</span>
                <div class="user-details">
                    <span class="user-name">Phan Đình Long</span>
                    <span class="user-role">Quản trị viên</span>
                </div>
            </div>
        </div>
    </header>
    <div class="setting-container">
        <section class="settings-card">
            <h3><i class='bx bx-store-alt'></i> Thông tin cửa hàng</h3>
            <div class="form-grid">
                <div class="form-group">
                    <label>Tên cửa hàng</label>
                    <input type="text" value="Handmade House">
                </div>
                <div class="form-group">
                    <label>Email liên hệ</label>
                    <input type="email" value="handmadehouse23@handmade.vn">
                </div>
                <div class="form-group">
                    <label>Số điện thoại</label>
                    <input type="text" value="0944912685">
                </div>
                <div class="form-group">
                    <label>Website</label>
                    <input type="text" value="https://handmadehouse.com">
                </div>
                <div class="form-group full-width">
                    <label>Địa chỉ</label>
                    <textarea rows="3">Khu phố 6, Phường Linh Trung, TP. Thủ Đức, TP. Hồ Chí Minh123 Đường ABC, Quận 1, TP.HCM</textarea>
                </div>
            </div>
            <div class="card-footer">
                <button class="btn-save"><i class='bx bx-save'></i> Lưu thay đổi</button>
            </div>
        </section>
        <section class="settings-card">
            <h3><i class='bx bx-bell'></i> Thông báo</h3>
            <div class="switch-group">
                <div class="switch-item">
                    <div class="info">
                        <strong>Đơn hàng mới</strong>
                        <p>Nhận thông báo khi có đơn hàng mới từ khách hàng</p>
                    </div>
                    <label class="toggle">
                        <input type="checkbox" checked>
                        <span class="slider"></span>
                    </label>
                </div>
                <div class="switch-item">
                    <div class="info">
                        <strong>Sản phẩm sắp hết</strong>
                        <p>Thông báo khi tồn kho sản phẩm dưới mức tối thiểu</p>
                    </div>
                    <label class="toggle">
                        <input type="checkbox" checked>
                        <span class="slider"></span>
                    </label>
                </div>
            </div>
        </section>
        <section class="settings-card">
            <h3><i class='bx bx-palette'></i> Giao diện</h3>
            <div class="appearance-section">
                <label>Màu chủ đạo</label>
                <div class="color-options">
                    <div class="color-circle c1 active"></div>
                    <div class="color-circle c2"></div>
                    <div class="color-circle c3"></div>
                    <div class="color-circle c4"></div>
                </div>
                <div class="form-group" style="margin-top: 20px; max-width: 300px;">
                    <label>Ngôn ngữ</label>
                    <select>
                        <option>Tiếng Việt</option>
                        <option>English</option>
                    </select>
                </div>
            </div>
        </section>
        <section class="settings-card">
            <h3 class="card-title"><i class='bx bx-lock-alt'></i> Bảo mật</h3>
            <div class="form-grid">
                <div class="form-group full-width">
                    <label>Mật khẩu hiện tại</label>
                    <input type="password" class="form-input" placeholder="••••••••">
                </div>
                <div class="form-group">
                    <label>Mật khẩu mới</label>
                    <input type="password" class="form-input" placeholder="••••••••">
                </div>
                <div class="form-group">
                    <label>Xác nhận mật khẩu mới</label>
                    <input type="password" class="form-input" placeholder="••••••••">
                </div>
            </div>
            <div class="button-row">
                <button class="btn-password">Đổi mật khẩu</button>
            </div>
        </section>
    </div>
</main>


</body>
</html>