<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý sản phẩm</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_SanPham.css">
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
            <li class="active"><a href="${pageContext.request.contextPath}/admin/products"><i class="bx bx-package"></i>Sản phẩm</a></li>
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
        <h2>Quản lý sản phẩm</h2>
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
            <div class="stats-grid">
                <div class="stat-card stat-revenue">
                    <div class="stat-icon"><i class="bx bx-cube"></i></div>
                    <div class="stat-details">
                        <p class="title">Tổng sản phẩm</p>
                        <p class="value">${totalProducts}</p>
                    </div>
                </div>

                <div class="stat-card stat-new-orders">
                    <div class="stat-icon"><i class="bx bx-dollar-circle"></i></div>
                    <div class="stat-details">
                        <p class="title">Tổng giá trị hàng</p>
                        <p class="value">
                            <fmt:formatNumber value="${totalValue}" type="number"/>đ
                        </p>
                        <span class="stat-change positive"></span>
                    </div>
                </div>

                <div class="stat-card stat-customers">
                    <div class="stat-icon"><i class="bx bx-error-alt"></i></div>
                    <div class="stat-details">
                        <p class="title">Sản phẩm hết hàng</p>
                        <p class="value">${outOfStock}</p>
                    </div>
                </div>

                <div class="stat-card stat-customers">
                    <div class="stat-icon"><i class="bx bx-package"></i></div>
                    <div class="stat-details">
                        <p class="title">Tổng tồn kho</p>
                        <p class="value">${totalStock}</p>
                    </div>
                </div>
            </div>

            <form method="get"
                  action="${pageContext.request.contextPath}/admin/products"
                  class="search-filter-row"
                  id="filterForm">

                <div class="search-review-box">
                    <i class="bx bx-search"></i>
                    <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm..." value="${param.keyword}">
                </div>

                <select name="categoryId" class="filter-select">
                    <option value="">Tất cả danh mục</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.categoryId}" ${param.categoryId == cat.categoryId ? 'selected' : ''}>
                                ${cat.name}
                        </option>
                    </c:forEach>
                </select>

                <select name="status" class="filter-select">
                    <option value="">Tất cả trạng thái</option>
                    <option value="instock" ${param.status == 'instock' ? 'selected' : ''}>Còn hàng</option>
                    <option value="lowstock" ${param.status == 'lowstock' ? 'selected' : ''}>Sắp hết</option>
                    <option value="outofstock" ${param.status == 'outofstock' ? 'selected' : ''}>Hết hàng</option>
                </select>

                <select name="priceRange" class="filter-select">
                    <option value="">Tất cả giá</option>
                    <option value="0-100000" ${param.priceRange == '0-100000' ? 'selected' : ''}>Dưới 100k</option>
                    <option value="100000-300000" ${param.priceRange == '100000-300000' ? 'selected' : ''}>100k - 300k</option>
                    <option value="300000-500000" ${param.priceRange == '300000-500000' ? 'selected' : ''}>300k - 500k</option>
                    <option value="500000+" ${param.priceRange == '500000+' ? 'selected' : ''}>Trên 500k</option>
                </select>

                <c:if test="${fn:contains(sessionScope.permissionCodesText, ',MANAGE_PRODUCT,')}">
                    <button type="button" class="view-all-btn" id="openModalBtn">
                        <i class="bx bx-plus"></i>
                        Thêm sản phẩm
                    </button>
                </c:if>
            </form>

            <div class="order-table-container">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Mã sản phẩm</th>
                        <th>Tên sản phẩm</th>
                        <th class="img-col-header">Hình ảnh</th>
                        <th>Giá bán</th>
                        <th>Tồn kho</th>
                        <th>Đã bán</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:choose>
                        <c:when test="${empty products}">
                            <tr>
                                <td colspan="8" class="empty-table">Không có sản phẩm nào</td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td>#SP${p.productId}</td>
                                    <td>${p.productName}</td>

                                    <td class="img-col">
                                        <c:choose>
                                            <c:when test="${not empty p.imageUrl}">
                                                <c:choose>
                                                    <c:when test="${fn:startsWith(p.imageUrl, 'http://') or fn:startsWith(p.imageUrl, 'https://')}">
                                                        <img src="${p.imageUrl}" alt="${p.productName}" width="50">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${pageContext.request.contextPath}${p.imageUrl}" alt="${p.productName}" width="50">
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>

                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/images/no-image.png"
                                                     alt="No Image" width="50">
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td><fmt:formatNumber value="${p.productPrice}" type="number"/>đ</td>
                                    <td>${p.stockQuantity}</td>
                                    <td>${p.sold != null ? p.sold : 0}</td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${p.stockQuantity == 0}">
                                                <span class="status status-pending">Hết hàng</span>
                                            </c:when>
                                            <c:when test="${p.stockQuantity < 5}">
                                                <span class="status status-warning">Sắp hết</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status status-completed">Còn hàng</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:contains(sessionScope.permissionCodesText, ',MANAGE_PRODUCT,')}">
                                                <button class="action-icon" type="button"
                                                        data-id="${p.productId}"
                                                        data-name="${p.productName}"
                                                        data-price="${p.productPrice}"
                                                        data-stock="${p.stockQuantity}"
                                                        data-category="${p.categoryId}"
                                                        data-description="${p.productDescription}"
                                                        data-image="${p.imageUrl}"
                                                        onclick="openEditModal(this)">
                                                    <i class="bx bx-pencil"></i>
                                                </button>

                                                <form action="${pageContext.request.contextPath}/admin/products"
                                                      method="post"
                                                      style="display:inline">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="productId" value="${p.productId}">
                                                    <button type="submit"
                                                            class="action-icon"
                                                            onclick="return confirm('Xoá sản phẩm này?')">
                                                        <i class="bx bx-trash"></i>
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

                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}&keyword=${param.keyword}&categoryId=${param.categoryId}&status=${param.status}&priceRange=${param.priceRange}">
                            &laquo; Trước
                        </a>
                    </c:if>

                    <c:if test="${currentPage > 3}">
                        <a href="?page=1&keyword=${param.keyword}&categoryId=${param.categoryId}&status=${param.status}&priceRange=${param.priceRange}">1</a>
                        <c:if test="${currentPage > 4}">
                            <span class="pagination-dots">...</span>
                        </c:if>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:if test="${i >= currentPage - 2 && i <= currentPage + 2}">
                            <a href="?page=${i}&keyword=${param.keyword}&categoryId=${param.categoryId}&status=${param.status}&priceRange=${param.priceRange}"
                               class="${i == currentPage ? 'active-page' : ''}">
                                    ${i}
                            </a>
                        </c:if>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages - 2}">
                        <c:if test="${currentPage < totalPages - 3}">
                            <span class="pagination-dots">...</span>
                        </c:if>
                        <a href="?page=${totalPages}&keyword=${param.keyword}&categoryId=${param.categoryId}&status=${param.status}&priceRange=${param.priceRange}">
                                ${totalPages}
                        </a>
                    </c:if>

                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}&keyword=${param.keyword}&categoryId=${param.categoryId}&status=${param.status}&priceRange=${param.priceRange}">
                            Tiếp &raquo;
                        </a>
                    </c:if>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<c:if test="${not accessDenied}">
    <div id="productModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Thêm sản phẩm mới</h3>
                <span class="close-btn" onclick="closeModal()">&times;</span>
            </div>

            <form action="${pageContext.request.contextPath}/admin/products"
                  method="post"
                  id="productForm"
                  enctype="multipart/form-data">

                <input type="hidden" name="action" id="modalAction" value="add">
                <input type="hidden" name="productId" id="prodId">

                <div class="form-group">
                    <label>Tên sản phẩm <span class="required">*</span></label>
                    <input type="text" name="name" id="prodName" placeholder="Ví dụ: Móc khóa len cờ Việt Nam..." required>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Giá bán (VNĐ) <span class="required">*</span></label>
                        <input type="number" name="price" id="prodPrice" min="1" required>
                    </div>

                    <div class="form-group">
                        <label>Số lượng kho <span class="required">*</span></label>
                        <input type="number" name="stock" id="prodStock" min="0" required>
                    </div>
                </div>

                <div class="form-group">
                    <label>Danh mục</label>
                    <select name="categoryId" id="prodCategory">
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.categoryId}">
                                    ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>Mô tả sản phẩm</label>
                    <textarea name="description" id="prodDescription" rows="3" placeholder="Mô tả ngắn gọn về sản phẩm..."></textarea>
                </div>

                <div class="form-group">
                    <label>URL hình ảnh</label>
                    <input type="text" name="imageUrl" id="prodImageUrl" placeholder="https://... hoặc để trống nếu upload file">
                </div>

                <div class="form-group">
                    <label for="prodImageFile">Hoặc tải ảnh lên</label>
                    <input type="file"
                           name="imageFile"
                           id="prodImageFile"
                           accept="image/*"
                           onchange="previewImage(this)">

                    <div id="imagePreviewWrap" style="margin-top:8px; display:none;">
                        <img id="imagePreview"
                             src=""
                             alt="Preview"
                             style="max-width:120px; max-height:120px; border-radius:6px; border:1px solid #ddd;">
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeModal()">Hủy bỏ</button>
                    <button type="submit" class="btn-submit">Xác nhận Lưu</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        const contextPath = "${pageContext.request.contextPath}";

        document.querySelectorAll("#filterForm .filter-select").forEach(function (sel) {
            sel.addEventListener("change", function () {
                document.getElementById("filterForm").submit();
            });
        });

        const modal = document.getElementById("productModal");
        const productForm = document.getElementById("productForm");
        const modalTitle = document.querySelector(".modal-header h3");
        const openModalBtn = document.getElementById("openModalBtn");

        if (openModalBtn) {
            openModalBtn.onclick = function () {
                modal.style.display = "flex";
                productForm.reset();
                clearImagePreview();

                modalTitle.innerText = "Thêm sản phẩm mới";
                document.getElementById("modalAction").value = "add";
                document.getElementById("prodId").value = "";
            };
        }

        function closeModal() {
            modal.style.display = "none";
        }

        window.addEventListener("click", function (e) {
            if (e.target === modal) {
                closeModal();
            }
        });

        function getImageSrc(imageUrl) {
            if (!imageUrl) {
                return "";
            }

            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                return imageUrl;
            }

            if (imageUrl.startsWith("/")) {
                return contextPath + imageUrl;
            }

            return contextPath + "/" + imageUrl;
        }

        function openEditModal(btn) {
            modal.style.display = "flex";
            modalTitle.innerText = "Cập nhật sản phẩm";

            document.getElementById("modalAction").value = "update";
            document.getElementById("prodId").value = btn.dataset.id;
            document.getElementById("prodName").value = btn.dataset.name || "";
            document.getElementById("prodPrice").value = btn.dataset.price || "";
            document.getElementById("prodStock").value = btn.dataset.stock || "";
            document.getElementById("prodCategory").value = btn.dataset.category || "";
            document.getElementById("prodDescription").value = btn.dataset.description || "";
            document.getElementById("prodImageUrl").value = btn.dataset.image || "";

            if (btn.dataset.image) {
                document.getElementById("imagePreview").src = getImageSrc(btn.dataset.image);
                document.getElementById("imagePreviewWrap").style.display = "block";
            } else {
                clearImagePreview();
            }
        }

        function previewImage(input) {
            if (input.files && input.files[0]) {
                const reader = new FileReader();

                reader.onload = function (e) {
                    document.getElementById("imagePreview").src = e.target.result;
                    document.getElementById("imagePreviewWrap").style.display = "block";
                    document.getElementById("prodImageUrl").value = "";
                };

                reader.readAsDataURL(input.files[0]);
            }
        }

        function clearImagePreview() {
            document.getElementById("imagePreview").src = "";
            document.getElementById("imagePreviewWrap").style.display = "none";
        }

        productForm.addEventListener("submit", function (e) {
            const name = document.getElementById("prodName").value.trim();
            const price = parseFloat(document.getElementById("prodPrice").value);
            const stock = parseInt(document.getElementById("prodStock").value);

            if (name === "") {
                alert("Tên sản phẩm không được để trống!");
                e.preventDefault();
                return;
            }

            if (isNaN(price) || price <= 0) {
                alert("Giá sản phẩm phải lớn hơn 0!");
                e.preventDefault();
                return;
            }

            if (isNaN(stock) || stock < 0) {
                alert("Số lượng kho không hợp lệ!");
                e.preventDefault();
                return;
            }
        });

        <c:if test="${not empty editProduct}">
        window.addEventListener("DOMContentLoaded", function () {
            modal.style.display = "flex";
            modalTitle.innerText = "Cập nhật sản phẩm";

            document.getElementById("modalAction").value = "update";
            document.getElementById("prodId").value = "${editProduct.productId}";
            document.getElementById("prodName").value = `${editProduct.productName}`;
            document.getElementById("prodPrice").value = "${editProduct.productPrice}";
            document.getElementById("prodStock").value = "${editProduct.stockQuantity}";
            document.getElementById("prodCategory").value = "${editProduct.categoryId}";
            document.getElementById("prodDescription").value = `${editProduct.productDescription}`;
            document.getElementById("prodImageUrl").value = "${editProduct.imageUrl}";

            if ("${editProduct.imageUrl}" !== "") {
                document.getElementById("imagePreview").src = getImageSrc("${editProduct.imageUrl}");
                document.getElementById("imagePreviewWrap").style.display = "block";
            }
        });
        </c:if>

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