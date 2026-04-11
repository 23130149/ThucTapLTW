<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Favourite Products</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="preconnect" href="https://unsplash.com">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../css/favourite.css">
    <link rel="stylesheet" href="../Header and Footer/Styles.css">
</head>
<body>
<header class="header">
    <div class="header-top-container">
        <div class="header-content">
            <div class="logo">
                <a href="../html/trangchu.html">Handmade House</a>
            </div>
            <form class="search-form" action="#" method="GET">
                <input type="text" class="search-input" placeholder="Tìm kiếm bất cứ thứ gì..." aria-label="Tìm kiếm sản phẩm">
                <button type="submit" class="search-btn">
                    <i class="bx bx-search-alt-2"></i>
                </button>
            </form>
            <div class="icons" >
                <a href="../html/favourite.html" class="icon-btn" id="heartBtn">
                    <i class='bx  bx-heart'></i>
                </a>
                <a  href="../html/cart.html" class="icon-btn" id="cartBtn">
                    <i class='bx  bx-cart'></i>
                </a>
                <a href="../html/account.html" class="icon-btn" id="userBtn">
                    <i class='bx  bx-user'></i>
                </a>
            </div>
        </div>
    </div>
    <div class="search-bar-section header-bottom-nav">
        <div class="container nav-only-container">
            <nav class="nav__links" >
                <ul>
                    <li><a href="../html/trangchu.html">Trang chủ</a></li>
                    <li><a href="../html/sanpham.html">Sản phẩm</a></li>
                    <li><a href="../html/blog.html">Blog</a></li>
                    <li><a href="../html/contact.html">Liên hệ</a></li>
                </ul>
            </nav>
        </div>
    </div>
</header>
<section class="favourite-section">
    <h2>Sản phẩm yêu thích</h2>

    <div class="product-list">

        <div class="product-item">
            <div class="image-container">
                <img src="https://i.pinimg.com/736x/9c/0f/da/9c0fda2d42833544fba28360869fd5e8.jpg" alt="móc khoá">
                <span class="heart">❤️</span>
            </div>
            <div class="product-info">

                <p class="product-category">Móc khóa</p>

                <h3>Móc khoá lá cờ Việt Nam</h3>
                <p class="price">15.000₫</p>
                <div class="product-actions">
                    <button class="btn btn-cart">Thêm vào giỏ</button>
                    <button class="btn btn-buy">Mua ngay</button>
                </div>
            </div>
        </div>

        <div class="product-item">
            <div class="image-container">
                <img src="https://i.pinimg.com/736x/30/34/a8/3034a8897defe35658b250d8b534256f.jpg" alt="Nến thơm">
                <span class="heart">❤️</span>
            </div>
            <div class="product-info">

                <p class="product-category">Nến thơm</p>

                <h3>Nến thơm xương rồng</h3>
                <p class="price">150.000₫</p>
                <div class="product-actions">
                    <button class="btn btn-cart">Thêm vào giỏ</button>
                    <button class="btn btn-buy">Mua ngay</button>
                </div>
            </div>
        </div>

        <div class="product-item">
            <div class="image-container">
                <img src="https://i.pinimg.com/736x/ca/45/21/ca4521034acf002c3ea9eb9f7cb8688c.jpg" alt="Túi hoa">
                <span class="heart">❤️</span>
            </div>
            <div class="product-info">

                <p class="product-category">Túi xách</p>

                <h3>Túi hoa Tulip</h3>
                <p class="price">240.000₫</p>
                <div class="product-actions">
                    <button class="btn btn-cart">Thêm vào giỏ</button>
                    <button class="btn btn-buy">Mua ngay</button>
                </div>
            </div>
        </div>

        <div class="product-item">
            <div class="image-container">
                <img src="https://i.pinimg.com/736x/82/a0/3f/82a03fc266a5b7d02c2e0d85fda1cce9.jpg" alt="Đèn chùm">
                <span class="heart">❤️</span>
            </div>
            <div class="product-info">

                <p class="product-category">Đèn trang trí</p>

                <h3>Đèn chùm hoa tươi rực rỡ</h3>
                <p class="price">599.000₫</p>
                <div class="product-actions">
                    <button class="btn btn-cart">Thêm vào giỏ</button>
                    <button class="btn btn-buy">Mua ngay</button>
                </div>
            </div>
        </div>
    </div>
</section>
<footer class="footer">
    <div class="container">
        <div class="footer-content">
            <div class="footer-column">
                <h3 class="footer-logo">Handmade House</h3>
                <p class="footer-desc">Chào mừng đến với Handmade House, ngôi nhà nhỏ của những tâm hồn yêu nghệ thuật và thủ công.</p>
                <div class="social-links">
                    <a href="#"><i class="bx bxl-facebook"></i></a>
                    <a href="#"><i class="bx bxl-instagram"></i></a>
                    <a href="#"><i class="bx bxl-tiktok"></i></a>
                </div>
            </div>

            <div class="footer-column">
                <h3 class="footer-title">Blog</h3>
                <ul class="footer-links">
                    <li> <a href="#">Câu chuyện thương hiệu</a></li>
                    <li> <a href="#"> Giá trị & Triết lý thương hiệu</a></li>
                    <li> <a href="#">Quy trình sản xuất</a></li>
                    <li> <a href="#">Cam kết & Định hướng bền vững</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h3 class="footer-title">Hỗ trợ</h3>
                <ul class="footer-links">
                    <li> <a href="#">Chính sách đổi trả</a></li>
                    <li> <a href="#">Hướng dẫn đặt hàng</a></li>
                    <li> <a href="#">Phương thức thanh toán</a></li>
                    <li> <a href="#">Câu hỏi thường gặp</a></li>
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