<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý sản phẩm</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="qlsanpham.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li><a href="../Tongquan/tongquan.html"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li class="active"><a href="../Sanpham/qlsanpham.html"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="../Donhang/donhang.html"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li><a href="../Khachhang/khachhang.html"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="../Danhgia/danhgia.html"><i class="bx bx-star"></i>Đánh giá</a></li>
            <li><a href="../Caidat/caidat.html"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="../../../../../../html/trangchu.html"><i class="bx bx-log-out"></i>Đăng xuất</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Sản phẩm</h2>
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
    <div class="stats-grid">
        <div class="stat-card stat-revenue">
            <div class="stat-icon"><i class="bx bx-cube"></i></div>
            <div class="stat-details">
                <p class="title">Tổng sản phẩm</p>
                <p class="value">80</p>
                <span class="stat-change positive"></span>
            </div>
        </div>
        <div class="stat-card stat-new-orders">
            <div class="stat-icon"><i class="bx bx-dollar-circle"></i></div>
            <div class="stat-details">
                <p class="title">Tổng giá trị hàng</p>
                <p class="value">48.700.000đ</p>
                <span class="stat-change positive"></span>
            </div>
        </div>
        <div class="stat-card stat-customers">
            <div class="stat-icon"><i class="bx bx-error-alt"></i></div>
            <div class="stat-details">
                <p class="title">Sản phẩm hết hàng</p>
                <p class="value">0</p>
                <span class="stat-change positive"></span>
            </div>
        </div>
        <div class="stat-card stat-customers">
            <div class="stat-icon"><i class="bx bx-package"></i></div>
            <div class="stat-details">
                <p class="title">Tổng tồn kho</p>
                <p class="value">330</p>
                <span class="stat-change positive"></span>
            </div>
        </div>
    </div>
    <div class="search-filter-row">
        <div class="search-review-box">
            <i class="bx bx-search"></i>
            <input type="text" placeholder="Tìm kiếm sản phẩm...">
        </div>
        <button class="filter-button-icon"><i class="bx bx-filter"></i>Lọc</button>
        <button class="view-all-btn"><i class="bx bx-plus"></i>Thêm sản phẩm</button>
    </div>
    <div class="order-table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>Mã đơn</th>
                <th>Tên sản phẩm</th>
                <th>Giá bán</th>
                <th>Tồn kho</th>
                <th>Đã bán</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>#DH001</td>
                <td>Ốp lưng điện thoại</td>
                <td>89.000đ</td>
                <td>18</td>
                <td>29</td>
                <td><span class="status status-completed">Còn hàng</span></td>
                <td>
                    <button class="action-icon"><i class="bx bx-pencil"></i></button>
                    <button class="action-icon"><i class="bx bx-trash"></i></button>
                </td>
            </tr>
            <tr>
                <td>#DH002</td>
                <td>Móc khóa lá cờ Việt Nam</td>
                <td>15.000đ</td>
                <td>0</td>
                <td>41</td>
                <td><span class="status status-pending">Hết hàng</span></td>
                <td>
                    <button class="action-icon"><i class="bx bx-pencil"></i></button>
                    <button class="action-icon"><i class="bx bx-trash"></i></button>
                </td>
            </tr>
            <tr>
                <td>#DH003</td>
                <td>Nến thơm xương rồng</td>
                <td>150.000đ</td>
                <td>18</td>
                <td>29</td>
                <td><span class="status status-completed">Còn hàng</span></td>
                <td>
                    <button class="action-icon"><i class="bx bx-pencil"></i></button>
                    <button class="action-icon"><i class="bx bx-trash"></i></button>
                </td>
            </tr>
            <tr>
                <td>#DH004</td>
                <td>Kẹp tóc hoa dâu tây nhí</td>
                <td>30.000đ</td>
                <td>10</td>
                <td>41</td>
                <td><span class="status status-completed">Còn hàng</span></td>
                <td>
                    <button class="action-icon"><i class="bx bx-pencil"></i></button>
                    <button class="action-icon"><i class="bx bx-trash"></i></button>
                </td>
            </tr>
            <tr>
                <td>#DH005</td>
                <td>Túi hoa Tulip</td>
                <td>120.000đ</td>
                <td>5</td>
                <td>30</td>
                <td><span class="status status-warning">Sắp hết</span></td>
                <td>
                    <button class="action-icon"><i class="bx bx-pencil"></i></button>
                    <button class="action-icon"><i class="bx bx-trash"></i></button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</main>
<div id="toast-container"></div>
<div id="productModal" class="modal">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <h3 id="modalTitle">Thông tin sản phẩm</h3>
        <form id="productForm">
            <div class="form-group">
                <label>Tên sản phẩm</label>
                <input type="text" id="prodName" required>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Giá bán</label>
                    <input type="text" id="prodPrice" required>
                </div>
                <div class="form-group">
                    <label>Tồn kho</label>
                    <input type="number" id="prodStock" required>
                </div>
            </div>
            <button type="submit" class="btn-save">Lưu dữ liệu</button>
        </form>
    </div>
</div>
</body>
</html>