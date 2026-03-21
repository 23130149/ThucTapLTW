<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giỏ Hàng</title>

    <link rel="stylesheet" href="css/cart.css">
    <link rel="stylesheet" href="Header_Footer/Styles.css">

    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
</head>

<body>

<header class="header">
    <div class="header-content">
        <div class="logo">
            <a href="#">Handmade House</a>
        </div>

        <form class="search-form">
            <input type="text" class="search-input" placeholder="Tìm kiếm bất cứ thứ gì">
            <button type="submit" class="search-btn">
                <i class="bx bx-search-alt-2"></i>
            </button>
        </form>

        <div class="icons">
            <a href="#" class="icon-btn"><i class='bx bx-cart'></i></a>
            <a href="#" class="icon-btn"><i class='bx bx-user'></i></a>
        </div>
    </div>
</header>

<section class="cart-page">

    <h1 class="cart-header">
        <i class='bx bx-cart'></i> Giỏ Hàng Của Bạn
    </h1>

    <div class="cart-summary-bar">
        <div>
            <div class="summary-title">Tổng tiền ước tính</div>
            <div class="summary-price">500.000đ</div>
            <div class="summary-note">2 sản phẩm đã chọn</div>
        </div>
        <button class="summary-checkout">
            <a href="#">Thanh toán</a>
        </button>
    </div>

    <div class="cart-action">
        <label>
            <input type="checkbox" id="checkAll">
            <span>Chọn tất cả</span>
        </label>

        <button class="btn-delete-selected">
            <i class='bx bx-trash'></i> Xóa đã chọn
        </button>
    </div>

    <div class="cart-list">

        <div class="cart-item">
            <input type="checkbox" class="item-checkbox">

            <img src="https://via.placeholder.com/100" alt="">

            <div class="product-info">
                <div class="product-name">Sản phẩm 1</div>
                <div class="unit-price">200.000đ</div>
            </div>

            <div class="qty-box">
                <button class="qty-btn">−</button>
                <span class="qty">1</span>
                <button class="qty-btn">+</button>
            </div>

            <div class="item-total-price">200.000đ</div>

            <i class='bx bx-trash item-remove'></i>
        </div>

        <!-- ITEM 2 -->
        <div class="cart-item">
            <input type="checkbox" class="item-checkbox">

            <img src="https://via.placeholder.com/100" alt="">

            <div class="product-info">
                <div class="product-name">Sản phẩm 2</div>
                <div class="unit-price">300.000đ</div>
            </div>

            <div class="qty-box">
                <button class="qty-btn">−</button>
                <span class="qty">1</span>
                <button class="qty-btn">+</button>
            </div>

            <div class="item-total-price">300.000đ</div>

            <i class='bx bx-trash item-remove'></i>
        </div>

    </div>

    <div class="cart-total">
        <h3>Đơn Hàng Của Bạn</h3>

        <div class="line">
            <span>Tạm tính</span>
            <span>500.000đ</span>
        </div>

        <div class="line">
            <span>Phí vận chuyển</span>
            <span class="free">Miễn phí</span>
        </div>

        <hr>

        <div class="line total">
            <span>Tổng cộng</span>
            <span>500.000đ</span>
        </div>

        <a href="#" class="summary-checkout">
            Thanh toán
        </a>
    </div>

</section>
</body>
</html>