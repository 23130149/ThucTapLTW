<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_DanhGia.css?v=3">
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
            <li class="active"><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/banner"><i class="bx bx-image"></i>Banner</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
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
        <h2>Quản lý đánh giá</h2>
        <div class="user-info">
            <div class="notification-wrapper">
                <a href="javascript:void(0)" class="notification-btn">
                    <i class="bx bx-bell"></i>
                    <c:if test="${notificationCount > 0}">
                        <span class="notification-count">${notificationCount}</span>
                    </c:if>
                </a>
                <div class="notification-dropdown">
                    <h4>Thông báo Admin</h4>

                    <c:choose>
                        <c:when test="${empty latestNotifications}">
                            <p class="empty-notification">Không có thông báo mới</p>
                        </c:when>

                        <c:otherwise>
                            <c:forEach items="${latestNotifications}" var="n">
                                <a href="${pageContext.request.contextPath}${n.url}" class="notification-item">
                                    <span class="notification-type">${n.type}</span>
                                    <p>${n.message}</p>
                                </a>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/admin/setting" class="profile-admin">
                <span class="admin-avatar">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.userName}">
                            ${fn:substring(sessionScope.user.userName, 0, 1)}
                        </c:when>
                        <c:otherwise>A</c:otherwise>
                    </c:choose>
                </span>
                <div>
                    <p class="user-name">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user.userName}">
                                ${sessionScope.user.userName}
                            </c:when>
                            <c:otherwise>Admin</c:otherwise>
                        </c:choose>
                    </p>
                    <small class="user-role">Quản trị viên</small>
                </div>
            </a>
        </div>
    </header>
    <c:choose>
    <c:when test="${accessDenied}">
        <div class="admin-alert error">
            <i class="bx bx-error-circle"></i>
                ${accessDeniedMessage}
        </div>
    </c:when>

    <c:otherwise>
    <c:if test="${not empty sessionScope.reviewMessage}">
        <div class="admin-alert success">
                ${sessionScope.reviewMessage}
        </div>
        <c:remove var="reviewMessage" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.reviewError}">
        <div class="admin-alert error">
                ${sessionScope.reviewError}
        </div>
        <c:remove var="reviewError" scope="session"/>
    </c:if>
    <section class="stats-grid">
        <div class="stat-card stat-total">
            <div class="stat-icon"><i class="bx bx-message-square-detail"></i></div>
            <div class="stat-details">
                <p class="title">Tổng đánh giá</p>
                <p class="value">${totalReviews}</p>
            </div>
        </div>

        <div class="stat-card stat-rating">
            <div class="stat-icon"><i class="bx bxs-star"></i></div>
            <div class="stat-details">
                <p class="title">Đánh giá trung bình</p>
                <p class="value">
                    <fmt:formatNumber value="${averageRating}" minFractionDigits="1" maxFractionDigits="1"/>
                </p>
            </div>
        </div>

        <div class="stat-card stat-pending">
            <div class="stat-icon"><i class="bx bx-time-five"></i></div>
            <div class="stat-details">
                <p class="title">Chờ duyệt</p>
                <p class="value">${pendingCount}</p>
            </div>
        </div>

        <div class="stat-card stat-five">
            <div class="stat-icon"><i class="bx bx-trending-up"></i></div>
            <div class="stat-details">
                <p class="title">Tỷ lệ 5 sao</p>
                <p class="value">${fiveStarRate}%</p>
            </div>
        </div>
    </section>

    <section class="review-panel">
        <div class="panel-header">
            <div>
                <h3>Danh sách đánh giá</h3>
                <p>Quản lý đánh giá của khách hàng về sản phẩm</p>
            </div>
        </div>

        <div class="rating-breakdown-card">
            <h3>Biểu đồ số sao</h3>

            <c:forEach var="star" begin="1" end="5">
                <c:set var="realStar" value="${6 - star}"/>

                <c:choose>
                    <c:when test="${realStar == 5}">
                        <c:set var="count" value="${fiveStarCount}"/>
                    </c:when>
                    <c:when test="${realStar == 4}">
                        <c:set var="count" value="${fourStarCount}"/>
                    </c:when>
                    <c:when test="${realStar == 3}">
                        <c:set var="count" value="${threeStarCount}"/>
                    </c:when>
                    <c:when test="${realStar == 2}">
                        <c:set var="count" value="${twoStarCount}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="count" value="${oneStarCount}"/>
                    </c:otherwise>
                </c:choose>

                <c:set var="percent" value="${totalReviews == 0 ? 0 : count * 100 / totalReviews}"/>

                <div class="rating-bar-item">
                    <span class="rating-star-label">
                        ${realStar}<i class="bx bxs-star"></i>
                    </span>

                    <div class="progress-bar-container">
                        <div class="progress-bar" style="width: ${percent}%"></div>
                    </div>

                    <span class="rating-count">${count} đánh giá</span>
                </div>
            </c:forEach>
        </div>

            <form method="get" action="${pageContext.request.contextPath}/admin/reviews" class="search-filter-row">
                <div class="search-review-box">
                    <i class="bx bx-search"></i>
                    <input type="text"
                           name="keyword"
                           value="${fn:escapeXml(keyword)}"
                           placeholder="Tìm kiếm đánh giá...">
                </div>

                <select name="rating" class="filter-select" onchange="this.form.submit()">
                    <option value="" ${empty currentRating ? 'selected' : ''}>Tất cả số sao</option>
                    <option value="5" ${currentRating == 5 ? 'selected' : ''}>5 sao</option>
                    <option value="4" ${currentRating == 4 ? 'selected' : ''}>4 sao</option>
                    <option value="3" ${currentRating == 3 ? 'selected' : ''}>3 sao</option>
                    <option value="2" ${currentRating == 2 ? 'selected' : ''}>2 sao</option>
                    <option value="1" ${currentRating == 1 ? 'selected' : ''}>1 sao</option>
                </select>

                <select name="status" class="filter-select" onchange="this.form.submit()">
                    <option value="" ${empty currentStatus ? 'selected' : ''}>Tất cả trạng thái</option>
                    <option value="PENDING" ${currentStatus == 'PENDING' ? 'selected' : ''}>Chờ duyệt</option>
                    <option value="APPROVED" ${currentStatus == 'APPROVED' ? 'selected' : ''}>Đã duyệt</option>
                    <option value="HIDDEN" ${currentStatus == 'HIDDEN' ? 'selected' : ''}>Đã ẩn</option>
                </select>
            </form>
        <div class="review-list-container">
            <c:choose>
                <c:when test="${empty reviews}">
                    <div class="empty-state">
                        <i class="bx bx-message-square-x"></i>
                        <p>Chưa có dữ liệu đánh giá.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <c:forEach var="review" items="${reviews}">
                        <div class="review-item">
                        <span class="customer-avatar-review">
                            <c:choose>
                                <c:when test="${not empty review.userName}">
                                    ${fn:substring(review.userName, 0, 1)}
                                </c:when>
                                <c:otherwise>K</c:otherwise>
                            </c:choose>
                        </span>

                            <div class="review-content">
                                <div class="review-header">
                                    <div>
                                        <span class="reviewer-name">${review.userName}</span>

                                        <div class="rating-stars">
                                            <c:forEach var="i" begin="1" end="5">
                                                <c:choose>
                                                    <c:when test="${i <= review.rating}">
                                                        <i class="bx bxs-star"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="bx bx-star"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <span class="status-tag status-${fn:toLowerCase(review.status)}">
                                    <c:choose>
                                        <c:when test="${review.status == 'PENDING'}">Chờ duyệt</c:when>
                                        <c:when test="${review.status == 'APPROVED'}">Đã duyệt</c:when>
                                        <c:when test="${review.status == 'HIDDEN'}">Đã ẩn</c:when>
                                        <c:otherwise>${review.status}</c:otherwise>
                                    </c:choose>
                                </span>
                                </div>

                                <p class="review-product">
                                    Sản phẩm: <c:out value="${review.productName}"/>
                                </p>

                                <span class="review-item-date">
                                        ${review.createAt}
                                </span>

                                <p class="review-text">
                                    <c:out value="${review.comment}"/>
                                </p>

                                <c:if test="${not empty review.shopReply}">
                                    <div class="shop-response">
                                        <p class="response-title">
                                            <i class="bx bx-reply"></i>
                                            Phản hồi từ shop:
                                        </p>

                                        <p class="response-text">
                                            <c:out value="${review.shopReply}"/>
                                        </p>
                                    </div>
                                </c:if>

                                <div class="review-extra">
                                    <span>
                                        <i class="bx bx-like"></i>
                                        Hữu ích: ${review.helpfulCount}
                                    </span>
                                </div>

                                <c:choose>
                                    <c:when test="${fn:contains(sessionScope.permissionCodesText, ',MANAGE_REVIEW,')}">
                                        <div class="review-action-row">
                                            <c:if test="${review.status == 'PENDING' || review.status == 'HIDDEN'}">
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/reviews"
                                                      class="review-status-form">
                                                    <input type="hidden" name="action" value="approve">
                                                    <input type="hidden" name="reviewId" value="${review.reviewId}">
                                                    <input type="hidden" name="keyword" value="${fn:escapeXml(keyword)}">
                                                    <input type="hidden" name="rating" value="${currentRating}">
                                                    <input type="hidden" name="status" value="${currentStatus}">
                                                    <button type="submit" class="review-action-btn approve-btn">
                                                        <i class="bx bx-check-circle"></i>
                                                        Duyệt hiển thị
                                                    </button>
                                                </form>
                                            </c:if>

                                            <c:if test="${review.status == 'PENDING' || review.status == 'APPROVED'}">
                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/reviews"
                                                      class="review-status-form">
                                                    <input type="hidden" name="action" value="hide">
                                                    <input type="hidden" name="reviewId" value="${review.reviewId}">
                                                    <input type="hidden" name="keyword" value="${fn:escapeXml(keyword)}">
                                                    <input type="hidden" name="rating" value="${currentRating}">
                                                    <input type="hidden" name="status" value="${currentStatus}">
                                                    <button type="submit" class="review-action-btn hide-btn">
                                                        <i class="bx bx-hide"></i>
                                                        Ẩn đánh giá
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>

                                        <form method="post"
                                              action="${pageContext.request.contextPath}/admin/reviews"
                                              class="reply-inline-form">

                                            <input type="hidden" name="action" value="reply">
                                            <input type="hidden" name="reviewId" value="${review.reviewId}">
                                            <input type="hidden" name="keyword" value="${fn:escapeXml(keyword)}">
                                            <input type="hidden" name="rating" value="${currentRating}">
                                            <input type="hidden" name="status" value="${currentStatus}">

                                            <textarea name="replyText"
                                                      placeholder="Nhập phản hồi cho khách hàng..."
                                                      required><c:out value="${review.shopReply}"/></textarea>

                                            <button type="submit" class="reply-btn">
                                                <i class="bx bx-send"></i>
                                                Lưu phản hồi
                                            </button>
                                        </form>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="no-permission-text">Chỉ xem</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
    </c:otherwise>
    </c:choose>
</main>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const wrappers = document.querySelectorAll(".notification-wrapper");

        wrappers.forEach(function (wrapper) {
            const button = wrapper.querySelector(".notification-btn");
            const dropdown = wrapper.querySelector(".notification-dropdown");

            button.addEventListener("click", function (event) {
                event.preventDefault();
                event.stopPropagation();

                wrappers.forEach(function (item) {
                    if (item !== wrapper) {
                        item.classList.remove("active");
                    }
                });

                wrapper.classList.toggle("active");
            });

            dropdown.addEventListener("click", function (event) {
                event.stopPropagation();
            });
        });

        document.addEventListener("click", function () {
            wrappers.forEach(function (wrapper) {
                wrapper.classList.remove("active");
            });
        });
    });
</script>
</body>
</html>
