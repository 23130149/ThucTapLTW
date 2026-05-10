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
            <span class="notification-badge">
                <i class="bx bx-bell"></i>
            </span>
            <div class="profile-admin">
                <span class="admin-avatar">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.userName}">
                            ${fn:substring(sessionScope.user.userName,0,1)}
                        </c:when>
                        <c:otherwise>A</c:otherwise>
                    </c:choose>
                </span>
                <div>
                    <p class="user-name">${sessionScope.user.userName}</p>
                    <small class="user-role">Quản trị viên</small>
                </div>
            </div>
        </div>
    </header>
    <div class="stats-grid">
        <div class="stat-card stat-revenue">
            <div class="stat-icon"><i class="bx bx-cube"></i></div>
            <div class="stat-details">
                <p class="title">Tổng sản phẩm</p>
                <p class="value">${totalProducts}</p>
                <span class="stat-change positive"></span>
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
                <span class="stat-change positive"></span>
            </div>
        </div>
        <div class="stat-card stat-customers">
            <div class="stat-icon"><i class="bx bx-package"></i></div>
            <div class="stat-details">
                <p class="title">Tổng tồn kho</p>
                <p class="value">${totalStock}</p>
                <span class="stat-change positive"></span>
            </div>
        </div>
    </div>
    <form method="get"
          action="${pageContext.request.contextPath}/admin/products"
          class="search-filter-row">
        <div class="search-review-box">
            <i class="bx bx-search"></i>
            <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm...">
        </div>
        <select name="categoryId" class="filter-select">
            <option value="">Tất cả danh mục</option>
            <option value="1">Móc khóa</option>
            <option value="2">Vòng tay</option>
            <option value="3">Nến thơm</option>
            <option value="4">Ốp lưng</option>
            <option value="5">Thời trang</option>
            <option value="6">Len-Crochet</option>
            <option value="7">Đồ trang trí</option>
            <option value="8">Thú cưng</option>
        </select>
        <select name="status" class="filter-select">
            <option value="">Tất cả trạng thái</option>
            <option value="instock">Còn hàng</option>
            <option value="lowstock">Sắp hết</option>
            <option value="outstock">Hết hàng</option>
        </select>
        <select name="priceRange" class="filter-select">
            <option value="">Tất cả giá</option>
            <option value="0-100000">
                Dưới 100k
            </option>
            <option value="100000-300000">
                100k - 300k
            </option>
            <option value="300000-500000">
                300k - 500k
            </option>
            <option value="500000+">
                Trên 500k
            </option>
        </select>
        <button type="button" class="view-all-btn" id="openModalBtn"><i class="bx bx-plus"></i>Thêm sản phẩm</button>
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
            <c:forEach var="p" items="${products}">
            <tr>
                <td>#SP${p.productId}</td>
                <td>${p.productName}</td>
                <td class="img-col">
                    <c:choose>
                        <c:when test="${not empty p.imageUrl}">
                            <img src="${p.imageUrl}" alt="${p.productName}" width="50">
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
                    <button class="action-icon" type="button"
                            data-id="${p.productId}"
                            data-name="${p.productName}"
                            data-price="${p.productPrice}"
                            data-stock="${p.stockQuantity}"
                            data-category="${p.categoryId}"
                            data-description="${p.productDescription}"
                            data-image="${p.imageUrl}"
                            onclick="openEditModal(this)"> <i class="bx bx-pencil"></i>
                    </button>
                    <form action="${pageContext.request.contextPath}/admin/products"
                          method="post" style="display:inline">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="productId" value="${p.productId}">
                        <button type="submit" class="action-icon" onclick="return confirm('Xoá sản phẩm này?')">
                            <i class="bx bx-trash"></i>
                        </button>
                    </form>
                </td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}">Previous</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="?page=${i}"
                   class="${i == currentPage ? 'active-page' : ''}">
                        ${i}
                </a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}">Next</a>
            </c:if>
        </div>
    </div>
</main>
<div id="productModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Thêm sản phẩm mới</h3>
            <span class="close-btn" onclick="closeModal()">&times;</span>
        </div>

        <form action="${pageContext.request.contextPath}/admin/products" method="post" id="productForm">

            <input type="hidden" name="action" id="modalAction" value="add">
            <input type="hidden" name="productId" id="prodId">

            <div class="form-group">
                <label>Tên sản phẩm</label>
                <input type="text" name="name" id="prodName" placeholder="Ví dụ: Móc khóa len cờ Việt Nam..." required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Giá bán (VNĐ)</label>
                    <input type="number" name="price" id="prodPrice" required>
                </div>
                <div class="form-group">
                    <label>Số lượng kho</label>
                    <input type="number" name="stock" id="prodStock" required>
                </div>
            </div>

            <div class="form-group">
                <label>Danh mục</label>
                <select name="categoryId" id="prodCategory">
                    <option value="1">Móc khóa</option>
                    <option value="2">Vòng tay</option>
                    <option value="3">Nến thơm</option>
                    <option value="4">Ốp lưng</option>
                    <option value="5">Thời trang</option>
                    <option value="6">Len-Crochet</option>
                    <option value="7">Đồ trang trí</option>
                    <option value="8">Thú cưng</option>
                </select>
            </div>
            <div class="form-group">
                <label>Mô tả sản phẩm</label>
                <textarea name="description" id="prodDescription" rows="3" placeholder="Mô tả ngắn gọn về sản phẩm..."></textarea>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeModal()">Hủy bỏ</button>
                <button type="submit" class="btn-submit">Xác nhận Lưu</button>
            </div>
        </form>
    </div>
</div>
<script>
    const filters = document.querySelectorAll(".filter-select");
    filters.forEach(filter => {
        filter.addEventListener("change", function () {
            this.form.submit();
        });
    });
    const modal = document.getElementById("productModal");
    const productForm = document.getElementById("productForm");
    const modalTitle = document.querySelector(".modal-header h3");
    document.getElementById("openModalBtn").onclick = function () {
        modal.style.display = "flex";
        productForm.reset();
        modalTitle.innerText = "Thêm sản phẩm mới";
        document.getElementById("modalAction").value = "add";
        document.getElementById("prodId").value = "";
    };
    function closeModal() {
        modal.style.display = "none";
    }
    function openEditModal(btn) {
        modal.style.display = "flex";
        modalTitle.innerText = "Cập nhật sản phẩm";
        document.getElementById("modalAction").value = "update";
        document.getElementById("prodId").value =
            btn.dataset.id;
        document.getElementById("prodName").value =
            btn.dataset.name;
        document.getElementById("prodPrice").value =
            btn.dataset.price;
        document.getElementById("prodStock").value =
            btn.dataset.stock;
        document.getElementById("prodCategory").value =
            btn.dataset.category;
        document.getElementById("prodDescription").value =
            btn.dataset.description;
    }
    window.onclick = function (event) {
        if (event.target === modal) {
            closeModal();
        }
    };
    productForm.addEventListener("submit", function (e) {
        const name =
            document.getElementById("prodName").value.trim();
        const price =
            document.getElementById("prodPrice").value;
        const stock =
            document.getElementById("prodStock").value;
        if (name === "") {
            alert("Tên sản phẩm không được để trống!");
            e.preventDefault()
            return;
        }
        if (price <= 0) {
            alert("Giá sản phẩm phải lớn hơn 0!");
            e.preventDefault();
            return;
        }
        if (stock < 0) {
            alert("Số lượng kho không hợp lệ!");
            e.preventDefault();
            return;
        }
    });
</script>
</body>
</html>