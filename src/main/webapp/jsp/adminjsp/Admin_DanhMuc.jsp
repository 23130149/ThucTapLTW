<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Danh mục</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/Admin_DanhMuc.css">
</head>
<body>
<aside class="sliderbar">
    <div class="slidebar-header">
        <h2 class="logo">Handmade House</h2>
    </div>
    <nav class="slidebar-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="bx bx-chart"></i>Tổng quan</a></li>
            <li class="active"><a href="${pageContext.request.contextPath}/admin/category"><i class="bx bx-category"></i>Danh mục</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products"><i class="bx bx-package"></i>Sản phẩm</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/orders"><i class="bx bx-receipt"></i>Đơn hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"><i class="bx bx-group"></i>Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
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
        <h2>Quản lý danh mục</h2>
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
            <div class="category-summary-grid">
                <div class="summary-card total-category">
                    <div class="card-icon"><i class="bx bx-category"></i></div>
                    <p>Tổng danh mục</p>
                    <h3>${totalCategory}</h3>
                </div>

                <div class="summary-card total-product">
                    <div class="card-icon"><i class="bx bx-package"></i></div>
                    <p>Tổng sản phẩm</p>
                    <h3>${totalProduct}</h3>
                </div>

                <div class="summary-card avg-category">
                    <div class="card-icon"><i class="bx bx-bar-chart"></i></div>
                    <p>Trung bình mỗi danh mục</p>
                    <h3>${avgCategory}</h3>
                </div>
            </div>

            <div class="customer-search-filter-row">
                <form class="search-customer-box" method="get" action="${pageContext.request.contextPath}/admin/category">
                    <i class="bx bx-search"></i>
                    <input type="text" name="keyword" value="${keyword}" placeholder="Tìm kiếm danh mục...">
                </form>

                <c:if test="${fn:contains(sessionScope.permissionCodesText, ',MANAGE_CATEGORY,')}">
                    <button type="button" class="btn-add" onclick="openAddCategoryModal()">
                        <i class="bx bx-plus"></i>
                        Thêm danh mục
                    </button>
                </c:if>
            </div>

            <div class="order-table-container">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Tên danh mục</th>
                        <th>Sản phẩm</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:choose>
                        <c:when test="${empty categories}">
                            <tr>
                                <td colspan="3" class="empty-table">Không có danh mục nào</td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="category" items="${categories}">
                                <tr>
                                    <td>${category.name}</td>
                                    <td>${category.productCount}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:contains(sessionScope.permissionCodesText, ',MANAGE_CATEGORY,')}">
                                                <button type="button"
                                                        class="action-btn edit-category-btn"
                                                        data-id="${category.categoryId}"
                                                        data-name="${category.name}"
                                                        data-image="${category.imageUrl}">
                                                    <i class="bx bx-edit action-icon"></i>
                                                </button>

                                                <form method="post"
                                                      action="${pageContext.request.contextPath}/admin/category"
                                                      style="display:inline;"
                                                      onsubmit="return confirm('Bạn có chắc muốn xóa danh mục này không?')">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="categoryId" value="${category.categoryId}">
                                                    <button type="submit" class="action-btn">
                                                        <i class="bx bx-trash action-icon"></i>
                                                    </button>
                                                </form>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="no-permission-text">Chỉ xem</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<c:if test="${not accessDenied}">
    <div class="modal-overlay" id="addCategoryModal">
        <div class="category-modal">
            <div class="modal-header">
                <h3>Thêm danh mục</h3>
                <button type="button" class="close-modal-btn" onclick="closeAddCategoryModal()">
                    <i class="bx bx-x"></i>
                </button>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/admin/category">
                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label>Tên danh mục</label>
                    <input type="text" name="name" placeholder="Nhập tên danh mục..." required>
                </div>

                <div class="form-group">
                    <label>Ảnh danh mục</label>
                    <input type="text" name="imageUrl" placeholder="Nhập đường dẫn ảnh...">
                </div>

                <div class="modal-actions">
                    <button type="button" class="cancel-btn" onclick="closeAddCategoryModal()">Hủy</button>
                    <button type="submit" class="save-btn">Lưu danh mục</button>
                </div>
            </form>
        </div>
    </div>

    <div class="modal-overlay" id="editCategoryModal">
        <div class="category-modal">
            <div class="modal-header">
                <h3>Chỉnh sửa danh mục</h3>
                <button type="button" class="close-modal-btn" onclick="closeEditCategoryModal()">
                    <i class="bx bx-x"></i>
                </button>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/admin/category">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="categoryId" id="editCategoryId">

                <div class="form-group">
                    <label>Tên danh mục</label>
                    <input type="text" name="name" id="editCategoryName" placeholder="Nhập tên danh mục..." required>
                </div>

                <div class="form-group">
                    <label>Ảnh danh mục</label>
                    <input type="text" name="imageUrl" id="editCategoryImageUrl" placeholder="Nhập đường dẫn ảnh...">
                </div>

                <div class="modal-actions">
                    <button type="button" class="cancel-btn" onclick="closeEditCategoryModal()">Hủy</button>
                    <button type="submit" class="save-btn">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openAddCategoryModal() {
            document.getElementById("addCategoryModal").classList.add("show");
        }

        function closeAddCategoryModal() {
            document.getElementById("addCategoryModal").classList.remove("show");
        }

        function openEditCategoryModal(id, name, imageUrl) {
            document.getElementById("editCategoryId").value = id;
            document.getElementById("editCategoryName").value = name;
            document.getElementById("editCategoryImageUrl").value = imageUrl || "";
            document.getElementById("editCategoryModal").classList.add("show");
        }

        function closeEditCategoryModal() {
            document.getElementById("editCategoryModal").classList.remove("show");
        }

        document.querySelectorAll(".edit-category-btn").forEach(function (button) {
            button.addEventListener("click", function () {
                openEditCategoryModal(
                    this.dataset.id,
                    this.dataset.name,
                    this.dataset.image
                );
            });
        });

        document.getElementById("addCategoryModal").addEventListener("click", function (event) {
            if (event.target === this) {
                closeAddCategoryModal();
            }
        });

        document.getElementById("editCategoryModal").addEventListener("click", function (event) {
            if (event.target === this) {
                closeEditCategoryModal();
            }
        });
    </script>
</c:if>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const wrappers = document.querySelectorAll(".notification-wrapper");

        wrappers.forEach(function (wrapper) {
            const button = wrapper.querySelector(".notification-btn");
            const dropdown = wrapper.querySelector(".notification-dropdown");

            if (!button || !dropdown) {
                return;
            }

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
