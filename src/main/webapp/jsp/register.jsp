<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng Ký</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dangky.css?v=2">
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">


</head>

<body>
<div class="wrapper">
    <c:if test="${empty step}">
        <form action="${pageContext.request.contextPath}/Register"
              method="post"
              id="registerForm">
            <input type="hidden" name="action" value="sendOtp">
            <h1>Đăng Ký</h1>
            <c:if test="${not empty error}">
                <p style="color:red;text-align:center">${error}</p>
            </c:if>
            <div class="input-box">
                <div class="input-field">
                    <input type="text" name="fullName" placeholder="Họ Tên" required>
                    <i class='bx bx-user'></i>
                </div>
                <p id="nameMsg" class="msg rule-msg">Ít nhất 2 ký tự, không chứa số hoặc ký tự đặc biệt.</p>

                <div class="input-field">
                    <input type="email" name="email" placeholder="Email" required>
                    <i class='bx bx-envelope'></i>
                </div>
                <p id="emailMsg" class="msg rule-msg">Sử dụng địa chỉ Gmail hợp lệ.</p>

                <div class="input-field">
                    <input type="password" name="password" placeholder="Mật khẩu" required>
                    <i class='bx bx-lock'></i>
                </div>
                <p id="passwordMsg" class="msg rule-msg">Ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.</p>

                <div class="input-field">
                    <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu" required>
                    <i class='bx bx-lock'></i>
                </div>
                <p id="confirmMsg" class="msg rule-msg">Nhập lại đúng mật khẩu phía trên.</p>
            </div>
            <c:if test="${recaptchaConfigured}">
                <div class="captcha-box">
                    <div class="g-recaptcha" data-sitekey="${recaptchaSiteKey}"></div>
                </div>
            </c:if>
            <button type="submit" class="btn">Đăng Ký</button>
            <div class="login-link">
                <p>
                    Đã có tài khoản?
                    <a href="${pageContext.request.contextPath}/SignIn">Đăng Nhập</a>
                </p>
            </div>
        </form>
    </c:if>
    <c:if test="${step eq 'OTP_SENT'}">

        <form action="${pageContext.request.contextPath}/Register"
              method="post"
              class="otp-form">

            <input type="hidden" name="action" value="confirmOtp">

            <h1 class="otp-title">Xác nhận OTP</h1>
            <c:if test="${not empty error}">
                <p style="color:red; text-align:center; margin-bottom:10px;">
                        ${error}
                </p>
            </c:if>
            <p class="otp-desc">
                Mã OTP đã được gửi đến email của bạn
            </p>
            <div class="otp-timer">
                OTP hết hạn sau <span id="time">120</span>s
            </div>

            <div class="otp-input-box">
                <input type="text" name="otp" maxlength="6" placeholder="••••••" required>
            </div>
            <div class="otp-actions">
                <button type="submit" class="btn">Xác nhận</button>

                <button type="button"
                        id="resendBtn"
                        class="resend-btn"
                        disabled>
                    Gửi lại OTP (<span id="resendTime">30</span>s)
                </button>
            </div>
        </form>
        <form action="${pageContext.request.contextPath}/Register"
              method="post"
              id="resendForm">
            <input type="hidden" name="action" value="sendOtp">
            <c:if test="${recaptchaConfigured}">
                <div class="captcha-box resend-captcha">
                    <div class="g-recaptcha" data-sitekey="${recaptchaSiteKey}"></div>
                </div>
            </c:if>
        </form>
    </c:if>
</div>
<script>
    const form = document.getElementById("registerForm");
    if (form) {
        const fullName = form.fullName;
        const email = form.email;
        const password = form.password;
        const confirmPassword = form.confirmPassword;

        const nameMsg = document.getElementById("nameMsg");
        const emailMsg = document.getElementById("emailMsg");
        const passwordMsg = document.getElementById("passwordMsg");
        const confirmMsg = document.getElementById("confirmMsg");

        function showMsg(el, msg, ok) {
            el.innerHTML = (ok ? "✔ " : "❌ ") + msg;
            el.style.color = ok ? "green" : "red";
        }

        function validateName() {
            const value = fullName.value.trim().replace(/\s+/g, " ");
            const ok = value.length >= 2 && value.length <= 100 && /^[\p{L} .'-]+$/u.test(value);
            showMsg(nameMsg, "Ít nhất 2 ký tự, không chứa số hoặc ký tự đặc biệt", ok);
            return ok;
        }

        function validateEmail() {
            const ok = /^[^\s@]+@gmail\.com$/i.test(email.value.trim());
            showMsg(emailMsg, "Email phải kết thúc bằng @gmail.com", ok);
            return ok;
        }

        function validatePassword() {
            const v = password.value;
            const ok =
                /[A-Z]/.test(v) &&
                /[a-z]/.test(v) &&
                /\d/.test(v) &&
                /[^A-Za-z0-9]/.test(v) &&
                v.length >= 8;
            showMsg(passwordMsg, "Ít nhất 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt", ok);
            return ok;
        }

        function validateConfirmPassword() {
            const ok = confirmPassword.value === password.value && confirmPassword.value !== "";
            showMsg(confirmMsg, "Mật khẩu xác nhận phải trùng khớp", ok);
            return ok;
        }

        fullName.addEventListener("input", validateName);
        email.addEventListener("input", validateEmail);
        password.addEventListener("input", function () {
            validatePassword();
            if (confirmPassword.value) validateConfirmPassword();
        });
        confirmPassword.addEventListener("input", validateConfirmPassword);

        form.addEventListener("submit", e => {
            const validationResults = [
                validateName(),
                validateEmail(),
                validatePassword(),
                validateConfirmPassword()
            ];
            if (!validationResults.every(Boolean)) {
                e.preventDefault();
                alert("Vui lòng nhập đúng thông tin!");
            }
        });
    }
    let timeLeft = 120;
    let resendLeft = 30;

    const timeEl = document.getElementById("time");
    const resendBtn = document.getElementById("resendBtn");
    const resendTimeEl = document.getElementById("resendTime");

    if (timeEl) {
        const timer = setInterval(() => {
            timeLeft--;
            timeEl.innerText = timeLeft;
            if (timeLeft <= 0) clearInterval(timer);
        }, 1000);
    }
    if (resendBtn) {
        const resendTimer = setInterval(() => {
            resendLeft--;
            resendTimeEl.innerText = resendLeft;
            if (resendLeft <= 0) {
                resendBtn.disabled = false;
                resendBtn.innerText = "Gửi lại OTP";
                clearInterval(resendTimer);
            }
        }, 1000);

        resendBtn.addEventListener("click", () => {
            document.getElementById("resendForm").submit();
        });
    }
</script>
<c:if test="${recaptchaConfigured}">
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
</c:if>
<script defer src="${pageContext.request.contextPath}/js/password-toggle.js?v=20260615-1"></script>
</body>
</html>
