<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chỉnh sửa thông tin - Handmade House</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account.css">
  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>

<body>
<jsp:include page="/jsp/header.jsp"/>

<main class="about-us-container profile-edit-page">
  <h1>Chỉnh sửa thông tin cá nhân</h1>

  <div class="profile-edit-box">
    <div class="profile-edit-heading">
      <div>
        <h2>Hồ sơ của bạn</h2>
        <p>Cập nhật thông tin nhận diện để mua hàng và theo dõi đơn thuận tiện hơn.</p>
      </div>
      <span class="profile-edit-icon"><i class='bx bx-id-card'></i></span>
    </div>

    <c:if test="${not empty errorMessage}">
      <div class="form-alert form-alert-error">${errorMessage}</div>
    </c:if>

    <c:set var="selectedGender" value="${not empty param.gender ? param.gender : sessionScope.user.gender}" />

    <form action="${pageContext.request.contextPath}/Profile/Edit" method="post" id="profileEditForm" novalidate>
      <div class="form-row">
        <label for="userName">Họ và tên <span>*</span></label>
        <input type="text" id="userName" name="userName" maxlength="60"
               value="${not empty param.userName ? param.userName : sessionScope.user.userName}"
               data-rule="name" required>
        <small class="field-hint">Từ 2 đến 60 ký tự, chỉ gồm chữ cái và khoảng trắng.</small>
        <c:if test="${not empty errors.userName}">
          <small class="field-error server-error">${errors.userName}</small>
        </c:if>
      </div>

      <div class="form-row">
        <label for="email">Email</label>
        <c:choose>
          <c:when test="${empty sessionScope.user.email}">
            <input type="email" id="email" name="email" maxlength="120"
                   value="${param.email}"
                   placeholder="Bổ sung email cho tài khoản">
            <small class="field-hint">Tài khoản đăng nhập mạng xã hội thiếu email có thể bổ sung tại đây.</small>
          </c:when>
          <c:otherwise>
            <input type="email" id="email" name="email"
                   value="${sessionScope.user.email}" readonly>
            <small class="field-hint">Email đã xác thực nên tạm thời không chỉnh sửa trực tiếp.</small>
          </c:otherwise>
        </c:choose>
        <c:if test="${not empty errors.email}">
          <small class="field-error server-error">${errors.email}</small>
        </c:if>
      </div>

      <div class="form-row">
        <label for="phone">Số điện thoại</label>
        <input type="tel" id="phone" name="phone" maxlength="11"
               value="${not empty param.phone ? param.phone : sessionScope.user.phone}"
               placeholder="Ví dụ: 0944912685"
               data-rule="phone">
        <small class="field-hint">Bắt đầu bằng 0 và gồm 10 đến 11 số.</small>
        <c:if test="${not empty errors.phone}">
          <small class="field-error server-error">${errors.phone}</small>
        </c:if>
      </div>

      <div class="form-row two-col">
        <div>
          <label for="dateOfBirth">Ngày sinh</label>
          <input type="date" id="dateOfBirth" name="dateOfBirth"
                 value="${not empty param.dateOfBirth ? param.dateOfBirth : sessionScope.user.dateOfBirth}">
          <c:if test="${not empty errors.dateOfBirth}">
            <small class="field-error server-error">${errors.dateOfBirth}</small>
          </c:if>
        </div>

        <div>
          <label for="gender">Giới tính</label>
          <select id="gender" name="gender">
            <option value="">Chọn giới tính</option>
            <option value="MALE" ${selectedGender == 'MALE' ? 'selected' : ''}>Nam</option>
            <option value="FEMALE" ${selectedGender == 'FEMALE' ? 'selected' : ''}>Nữ</option>
            <option value="OTHER" ${selectedGender == 'OTHER' ? 'selected' : ''}>Khác</option>
          </select>
          <c:if test="${not empty errors.gender}">
            <small class="field-error server-error">${errors.gender}</small>
          </c:if>
        </div>
      </div>

      <div class="form-row">
        <label for="avatarUrl">Ảnh đại diện URL</label>
        <input type="url" id="avatarUrl" name="avatarUrl"
               value="${not empty param.avatarUrl ? param.avatarUrl : sessionScope.user.avatarUrl}"
               placeholder="https://..."
               data-rule="url">
        <c:if test="${not empty errors.avatarUrl}">
          <small class="field-error server-error">${errors.avatarUrl}</small>
        </c:if>
      </div>

      <div class="form-row">
        <label for="bio">Giới thiệu ngắn</label>
        <textarea id="bio" name="bio" maxlength="180" rows="3" placeholder="Một câu giới thiệu nhỏ về bạn...">${not empty param.bio ? param.bio : sessionScope.user.bio}</textarea>
        <small class="field-hint"><span id="bioCounter">0</span>/180 ký tự</small>
        <c:if test="${not empty errors.bio}">
          <small class="field-error server-error">${errors.bio}</small>
        </c:if>
      </div>

      <div class="profile-actions">
        <a href="${pageContext.request.contextPath}/Profile" class="btn-account-secondary">
          <i class='bx bx-x'></i>
          Hủy thay đổi
        </a>

        <button type="submit" class="btn-account-primary">
          <i class='bx bx-save'></i>
          Lưu thay đổi
        </button>
      </div>
    </form>
  </div>
</main>

<jsp:include page="/jsp/footer.jsp"/>

<script>
  document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("profileEditForm");
    const bio = document.getElementById("bio");
    const bioCounter = document.getElementById("bioCounter");

    function setError(input, message) {
      clearError(input);
      if (!message) return true;
      input.classList.add("is-invalid");
      const error = document.createElement("small");
      error.className = "field-error live-error";
      error.textContent = message;
      input.insertAdjacentElement("afterend", error);
      return false;
    }

    function clearError(input) {
      input.classList.remove("is-invalid");
      const next = input.nextElementSibling;
      if (next && next.classList.contains("live-error")) {
        next.remove();
      }
    }

    function validateField(input) {
      const value = input.value.trim();
      const rule = input.dataset.rule;

      if (rule === "name") {
        if (!value) return setError(input, "Vui lòng nhập họ và tên.");
        if (value.length < 2 || value.length > 60) return setError(input, "Họ và tên phải từ 2 đến 60 ký tự.");
        if (!/^[\p{L} ]+$/u.test(value)) return setError(input, "Họ và tên chỉ nên chứa chữ cái và khoảng trắng.");
      }

      if (rule === "phone" && value && !/^0\d{9,10}$/.test(value)) {
        return setError(input, "Số điện thoại phải bắt đầu bằng 0 và gồm 10 đến 11 số.");
      }

      if (rule === "url" && value && !/^https?:\/\/.+/.test(value)) {
        return setError(input, "URL phải bắt đầu bằng http:// hoặc https://.");
      }

      clearError(input);
      return true;
    }

    form.querySelectorAll("input[data-rule]").forEach(input => {
      input.addEventListener("blur", () => validateField(input));
      input.addEventListener("input", () => clearError(input));
    });

    function updateBioCounter() {
      bioCounter.textContent = bio.value.length;
    }

    if (bio && bioCounter) {
      updateBioCounter();
      bio.addEventListener("input", updateBioCounter);
    }

    form.addEventListener("submit", function (e) {
      let valid = true;
      form.querySelectorAll("input[data-rule]").forEach(input => {
        if (!validateField(input)) valid = false;
      });
      if (!valid) e.preventDefault();
    });
  });
</script>
</body>
</html>
