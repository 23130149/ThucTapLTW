<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="danhgia.css">
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
            <li class="active"><a href="../Danhgia/danhgia.html"><i class="bx bx-star"></i>Đánh giá</a></li>
            <li><a href="../Caidat/caidat.html"><i class="bx bx-cog"></i>Cài đặt</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="../../../../../../html/trangchu.html"><i class="bx bx-log-out"></i>Đăng xuất</a>
    </div>
</aside>
<main class="main-content">
    <header class="header">
        <h2>Đánh giá</h2>
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
    <div class="table card">
        <div class="reviews-summary-grid">
            <div class="summary-card">
                <p>Tổng đánh giá</p>
                <span class="summary-value">240</span>
                <span class="summary-detail">+6 đánh giá mới</span>
            </div>
            <div class="summary-card">
                <p>Đánh giá trung bình</p>
                <span class="summary-value">4.6
                        <i class="bx bxs-star"></i>
                        <i class="bx bxs-star"></i>
                        <i class="bx bxs-star"></i>
                        <i class="bx bxs-star"></i>
                        <i class="bx bxs-star-half"></i>
                    </span>
            </div>
            <div class="summary-card">
                <p>Chờ duyệt</p>
                <span class="summary-value">2</span>
                <span class="summary-detail">Cần xem xét</span>
            </div>
            <div class="summary-card">
                <p>Tỷ lệ 5 sao</p>
                <span class="summary-value">76%</span>
                <span class="summary-detail">Xuất sắc!</span>
            </div>
        </div>
        <div class="rating-breakdown-card">
            <h3>Phân bố đánh giá</h3>
            <div class="rating-bar-item">
                <span class="rating-star-label">5<i class="bx bxs-star"></i></span>
                <div class="progress-bar-container">
                    <div class="progress-bar" style="width: 75%;"></div>
                </div>
                <span class="rating-count">150 đánh giá</span>
            </div>
            <div class="rating-bar-item">
                <span class="rating-star-label">4<i class="bx bxs-star"></i></span>
                <div class="progress-bar-container">
                    <div class="progress-bar" style="width: 15%;"></div>
                </div>
                <span class="rating-count"> 50 đánh giá</span>
            </div>
            <div class="rating-bar-item">
                <span class="rating-star-label">3<i class="bx bxs-star"></i></span>
                <div class="progress-bar-container">
                    <div class="progress-bar" style="width: 7%;"></div>
                </div>
                <span class="rating-count">25 đánh giá</span>
            </div>
            <div class="rating-bar-item">
                <span class="rating-star-label">2<i class="bx bxs-star"></i></span>
                <div class="progress-bar-container">
                    <div class="progress-bar" style="width: 2%;"></div>
                </div>
                <span class="rating-count">10 đánh giá</span>
            </div>
            <div class="rating-bar-item">
                <span class="rating-star-label">1<i class="bx bxs-star"></i></span>
                <div class="progress-bar-container">
                    <div class="progress-bar" style="width: 1%;"></div>
                </div>
                <span class="rating-count">5 đánh giá</span>
            </div>
        </div>
        <div class="rating-filter-row">
            <div class="rating-filter-buttons">
                <button class="filter-button active">Tất cả</button>
                <button class="filter-button">5<i class="bx bxs-star"></i></button>
                <button class="filter-button">4<i class="bx bxs-star"></i></button>
                <button class="filter-button">3<i class="bx bxs-star"></i></button>
                <button class="filter-button">2<i class="bx bxs-star"></i></button>
                <button class="filter-button">1<i class="bx bxs-star"></i></button>
            </div>
        </div>
        <div class="search-filter-row">
            <div class="search-review-box">
                <input type="text" placeholder="Tìm kiếm đánh giá...">
            </div>
            <button class="filter-button-icon"><i class="bx bx-filter"></i>Lọc</button>
        </div>
        <div class="review-list-container">
            <div class="review-item">
                <span class="customer-avatar-review">P</span>
                <div class="review-content">
                    <div class="review-header">
                        <div>
                            <span class="reviewer-name">Nguyễn Thanh Phú</span>
                            <div class="rating-stars">
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                            </div>
                        </div>
                        <div class="review-actions">
                            <span class="status-tag">Đã duyệt</span>
                            <div class="review-action-icons">
                                <i class="bx bx-check"></i>
                                <i class="bx bx-show-alt action-icon"></i>
                            </div>
                        </div>
                    </div>
                    <p class="review-product">Móc khóa lá cờ Việt Nam</p>
                    <span class="review-item-date">1/12/2025</span>
                    <p class="review-text">Móc khóa dễ thương, chắc chắn lần sau tui sẽ mua tiếp.</p>
                    <div class="shop-response">
                        <p class="response-title"><i class="bx bx-reply"></i> Phản hồi từ shop:</p>
                        <p class="response-text">Cảm ơn bạn đã ủng hộ shop! Chúc bạn sử dụng sản phẩm vui vẻ ạ <i
                                class="bx bxs-heart"></i></p>
                    </div>
                    <div class="review-utility-container">
                        <span class="review-utility">
                            <i class="bx bx-like"></i> Hữu ích (28)
                        </span>
                    </div>
                </div>
            </div>
            <div class="review-item">
                <span class="customer-avatar-review">Đ</span>
                <div class="review-content">
                    <div class="review-header">
                        <div>
                            <span class="reviewer-name">Nguyễn Lê Tiến Đạt</span>
                            <div class="rating-stars">
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                            </div>
                        </div>
                        <div class="review-actions">
                            <span class="status-tag">Đã duyệt</span>
                            <div class="review-action-icons">
                                <i class="bx bx-check"></i>
                                <i class="bx bx-show-alt action-icon"></i>
                            </div>
                        </div>
                    </div>
                    <p class="review-product">Túi hoa Tulip</p>
                    <span class="review-item-date">24/12/2025</span>
                    <p class="review-text">Sản phẩm rất đẹp không có chỗ nào chê.</p>
                    <div class="review-utility-container">
                        <span class="review-utility">
                            <i class="bx bx-like"></i> Hữu ích (15)
                        </span>
                        <button class="review-reply-action"><i class="bx bx-reply"></i>Phản hồi</button>
                    </div>
                </div>
            </div>
            <div class="review-item">
                <span class="customer-avatar-review">B</span>
                <div class="review-content">
                    <div class="review-header">
                        <div>
                            <span class="reviewer-name">Nguyễn Huy Bảo</span>
                            <div class="rating-stars">
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bx-star"></i>
                            </div>
                        </div>
                        <div class="review-actions">
                            <span class="status-tag">Đã duyệt</span>
                            <div class="review-action-icons">
                                <i class="bx bx-check"></i>
                                <i class="bx bx-show-alt action-icon"></i>
                            </div>
                        </div>
                    </div>
                    <p class="review-product">Kẹp tóc hoa dâu tulip</p>
                    <span class="review-item-date">10/12/2025</span>
                    <p class="review-text">Sản phẩm rất đẹp. Tuy nhiên giao hơi lâu so với dự kiến. Nhưng nhìn chung vẫn
                        ok.</p>
                    <div class="review-utility-container">
                        <span class="review-utility">
                            <i class="bx bx-like"></i> Hữu ích (12)
                        </span>
                        <button class="review-reply-action"><i class="bx bx-reply"></i>Phản hồi</button>
                    </div>
                </div>
            </div>
            <div class="review-item">
                <span class="customer-avatar-review">Q</span>
                <div class="review-content">
                    <div class="review-header">
                        <div>
                            <span class="reviewer-name">Trần Hoàng Quan</span>
                            <div class="rating-stars">
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                            </div>
                        </div>
                        <div class="review-actions">
                            <span class="status-tag">Đã duyệt</span>
                            <div class="review-action-icons">
                                <i class="bx bx-check"></i>
                                <i class="bx bx-show-alt action-icon"></i>
                            </div>
                        </div>
                    </div>
                    <p class="review-product">Ốp lưng điện thoại</p>
                    <span class="review-item-date">7/12/2025</span>
                    <p class="review-text">Sản phẩm rất tốt, chắc chắn sẽ mua tiếp.</p>
                    <div class="shop-response">
                        <p class="response-title"><i class="bx bx-reply"></i> Phản hồi từ shop:</p>
                        <p class="response-text">Cảm ơn bạn đã ủng hộ shop! Chúc bạn sử dụng sản phẩm vui vẻ ạ <i
                                class="bx bxs-heart"></i></p>
                    </div>
                    <div class="review-utility-container">
                        <span class="review-utility">
                            <i class="bx bx-like"></i> Hữu ích (15)
                        </span>
                    </div>
                </div>
            </div>
            <div class="review-item">
                <span class="customer-avatar-review">K</span>
                <div class="review-content">
                    <div class="review-header">
                        <div>
                            <span class="reviewer-name">Lê Viết Khanh</span>
                            <div class="rating-stars">
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                                <i class="bx bxs-star"></i>
                            </div>
                        </div>
                        <div class="review-actions">
                            <span class="status-tag">Đã duyệt</span>
                            <div class="review-action-icons">
                                <i class="bx bx-check"></i>
                                <i class="bx bx-show-alt action-icon"></i>
                            </div>
                        </div>
                    </div>
                    <p class="review-product">Nến thơm xương rồng</p>
                    <span class="review-item-date">5/12/2025</span>
                    <p class="review-text">Sản phẩm rất đẹp, chất liệu tốt.</p>
                    <div class="review-utility-container">
                        <span class="review-utility">
                            <i class="bx bx-like"></i> Hữu ích (12)
                        </span>
                        <button class="review-reply-action"><i class="bx bx-reply"></i>Phản hồi</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<div id="replyModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Phản hồi đánh giá</h3>
            <span class="close-modal" id="closeReply">&times;</span>
        </div>
        <div class="modal-body">
            <p><strong>Khách hàng:</strong> <span id="replyToName"></span></p>
            <textarea id="replyText" placeholder="Nhập nội dung phản hồi của bạn..."></textarea>
            <button id="sendReplyBtn" class="btn-save" style="margin-top: 15px;">Gửi phản hồi</button>
        </div>
    </div>
</div>
<div id="viewModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Chi tiết đánh giá</h3>
            <span class="close-modal" id="closeView">&times;</span>
        </div>
        <div id="viewDetailContent" class="modal-body">
        </div>
    </div>
</div>
</body>
</html>