<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Sổ địa chỉ - Handmade House</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/account.css">
  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>

<body>
<jsp:include page="/jsp/header.jsp"/>

<main class="about-us-container">
  <h1>Sổ địa chỉ</h1>

  <div class="account-info">
    <i class='bx bxs-user-circle'></i>
    <h3>${sessionScope.user.userName}</h3>
    <p>${sessionScope.user.email}</p>
  </div>

  <ul class="account-menu">
    <li>
      <a href="${pageContext.request.contextPath}/Account">
        <i class='bx bx-home'></i>
        <span>Tổng quan</span>
      </a>
    </li>
    <li>
      <a href="${pageContext.request.contextPath}/Profile">
        <i class='bx bx-edit-alt'></i>
        <span>Thông tin cá nhân</span>
      </a>
    </li>
    <li>
      <a href="${pageContext.request.contextPath}/ChangePassword">
        <i class='bx bx-lock-alt'></i>
        <span>Đổi mật khẩu</span>
      </a>
    </li>
    <li class="active">
      <a href="${pageContext.request.contextPath}/Address">
        <i class='bx bx-map'></i>
        <span>Sổ địa chỉ</span>
      </a>
    </li>
  </ul>

  <div class="recent-orders-box address-box">
    <div class="address-header">
      <h2>Danh sách địa chỉ</h2>

      <c:if test="${address.userAddressId == 0}">
        <button type="button" class="btn-add-address" id="btnAddAddress">
          <i class='bx bx-plus'></i>
          <span>Thêm địa chỉ mới</span>
        </button>
      </c:if>
    </div>

    <c:choose>
      <c:when test="${empty addresses}">
        <p class="empty-address">Bạn chưa có địa chỉ nào.</p>
      </c:when>

      <c:otherwise>
        <div class="address-list">
          <c:forEach var="addr" items="${addresses}">
            <div class="address-card">
              <div class="address-card-icon">
                <i class='bx bx-map'></i>
              </div>

              <div class="address-card-content">
                <h4>${addr.street}</h4>
                <p>${addr.district}, ${addr.province}</p>
                <p>${addr.country}</p>
              </div>

              <div class="address-card-actions">
                <a href="${pageContext.request.contextPath}/Address?edit=${addr.userAddressId}"
                   title="Sửa địa chỉ">
                  <i class='bx bx-edit'></i>
                </a>

                <a href="${pageContext.request.contextPath}/Address?delete=${addr.userAddressId}"
                   title="Xóa địa chỉ"
                   onclick="return confirm('Bạn có chắc muốn xóa địa chỉ này?')">
                  <i class='bx bx-trash'></i>
                </a>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>

    <div id="addressFormBox"
         class="address-form-box ${address.userAddressId > 0 ? 'show' : ''}">
      <h3>
        <c:choose>
          <c:when test="${address.userAddressId > 0}">
            Sửa địa chỉ
          </c:when>
          <c:otherwise>
            Thêm địa chỉ mới
          </c:otherwise>
        </c:choose>
      </h3>

      <form action="${pageContext.request.contextPath}/Address" method="post">
        <input type="hidden" name="userAddressId" value="${address.userAddressId}" />

        <div class="form-grid">
          <div class="form-group">
            <label>Quốc gia</label>
            <input type="text" name="country" value="${empty address.country ? 'Việt Nam' : address.country}" required>
          </div>

          <div class="form-group">
            <label>Tỉnh/Thành phố</label>
            <select name="province" id="provinceSelect" data-current="${address.province}" required>
              <option value="">Chọn Tỉnh/Thành phố</option>
            </select>
          </div>

          <div class="form-group">
            <label>Quận/Huyện</label>
            <select name="district" id="districtSelect" data-current="${address.district}" required disabled>
              <option value="">Chọn Quận/Huyện</option>
            </select>
          </div>

          <div class="form-group">
            <label>Phường/Xã</label>
            <select name="ward" id="wardSelect" required disabled>
              <option value="">Chọn Phường/Xã</option>
            </select>
          </div>

          <div class="form-group form-group-full">
            <label>Đường/Số nhà</label>
            <input type="text" name="street" value="${address.street}" placeholder="Ví dụ: 12 Linh Trung" required>
          </div>
        </div>

        <div class="address-actions">
          <button type="submit" class="btn-save">Lưu địa chỉ</button>

          <a href="${pageContext.request.contextPath}/Address" class="btn-cancel">
            Hủy
          </a>
        </div>
      </form>
    </div>
  </div>
</main>

<jsp:include page="/jsp/footer.jsp"/>

<script>
  document.addEventListener("DOMContentLoaded", function () {
    const btnAddAddress = document.getElementById("btnAddAddress");
    const addressFormBox = document.getElementById("addressFormBox");

    if (btnAddAddress && addressFormBox) {
      btnAddAddress.addEventListener("click", function () {
        addressFormBox.classList.add("show");
        btnAddAddress.style.display = "none";
      });
    }

    const addressData = {
      "TP. Hồ Chí Minh": {
        "Thủ Đức": ["Linh Trung", "Linh Tây", "Linh Chiểu", "Hiệp Bình Chánh"],
        "Quận 1": ["Bến Nghé", "Bến Thành", "Nguyễn Thái Bình"],
        "Quận 7": ["Tân Phong", "Tân Phú", "Phú Mỹ"],
        "Bình Thạnh": ["Phường 1", "Phường 2", "Phường 25"]
      },
      "Đồng Nai": {
        "Biên Hòa": ["Tân Phong", "Trảng Dài", "Long Bình", "Tam Hiệp"],
        "Long Thành": ["Long Thành", "An Phước", "Phước Thái"],
        "Nhơn Trạch": ["Phú Hội", "Long Tân", "Phước Thiền"]
      },
      "Bình Dương": {
        "Thủ Dầu Một": ["Phú Cường", "Hiệp Thành", "Phú Hòa"],
        "Dĩ An": ["Dĩ An", "An Bình", "Tân Đông Hiệp"],
        "Thuận An": ["Lái Thiêu", "Bình Hòa", "An Phú"]
      },
      "Hà Nội": {
        "Ba Đình": ["Phúc Xá", "Trúc Bạch", "Đội Cấn"],
        "Cầu Giấy": ["Dịch Vọng", "Quan Hoa", "Yên Hòa"],
        "Đống Đa": ["Cát Linh", "Văn Chương", "Láng Hạ"]
      },
      "Đà Nẵng": {
        "Hải Châu": ["Hải Châu I", "Hải Châu II", "Thạch Thang"],
        "Thanh Khê": ["Tam Thuận", "Xuân Hà", "Chính Gián"],
        "Sơn Trà": ["An Hải Bắc", "An Hải Đông", "Phước Mỹ"]
      },
      "Cần Thơ": {
        "Ninh Kiều": ["Cái Khế", "An Hòa", "Tân An"],
        "Bình Thủy": ["Bình Thủy", "Trà An", "Long Hòa"],
        "Cái Răng": ["Lê Bình", "Hưng Phú", "Ba Láng"]
      }
    };

    const provinceSelect = document.getElementById("provinceSelect");
    const districtSelect = document.getElementById("districtSelect");
    const wardSelect = document.getElementById("wardSelect");

    function fillSelect(select, items, placeholder) {
      select.innerHTML = '<option value="">' + placeholder + '</option>';
      items.forEach(item => {
        const option = document.createElement("option");
        option.value = item;
        option.textContent = item;
        select.appendChild(option);
      });
    }

    function fillDistricts() {
      const province = provinceSelect.value;
      const districts = province ? Object.keys(addressData[province] || {}) : [];
      fillSelect(districtSelect, districts, "Chọn Quận/Huyện");
      fillSelect(wardSelect, [], "Chọn Phường/Xã");
      districtSelect.disabled = districts.length === 0;
      wardSelect.disabled = true;
    }

    function fillWards() {
      const province = provinceSelect.value;
      const district = districtSelect.value;
      const wards = province && district ? (addressData[province][district] || []) : [];
      fillSelect(wardSelect, wards, "Chọn Phường/Xã");
      wardSelect.disabled = wards.length === 0;
    }

    if (provinceSelect && districtSelect && wardSelect) {
      fillSelect(provinceSelect, Object.keys(addressData), "Chọn Tỉnh/Thành phố");

      const currentProvince = provinceSelect.dataset.current;
      if (currentProvince && addressData[currentProvince]) {
        provinceSelect.value = currentProvince;
        fillDistricts();
      }

      provinceSelect.addEventListener("change", fillDistricts);
      districtSelect.addEventListener("change", fillWards);
    }
  });
</script>

<script>
  window.APP_CONTEXT = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
</body>
</html>
