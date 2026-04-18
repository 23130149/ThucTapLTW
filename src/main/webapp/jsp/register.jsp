<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng Ký</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dangky.css">
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
                <p id="nameMsg" class="msg"></p>

                <div class="input-field">
                    <input type="email" name="email" placeholder="Email" required>
                    <i class='bx bx-envelope'></i>
                </div>
                <p id="emailMsg" class="msg"></p>

                <div class="input-field">
                    <input type="password" name="password" placeholder="Mật khẩu" required>
                    <i class='bx bx-lock'></i>
                </div>
                <p id="passwordMsg" class="msg"></p>

                <div class="input-field">
                    <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu" required>
                    <i class='bx bx-lock'></i>
                </div>
                <p id="confirmMsg" class="msg"></p>
            </div>
            <button type="submit" class="btn">Đăng Ký</button>
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

        fullName.addEventListener("input", () => {
            const regex = /^([A-ZÀ-Ỹ][a-zà-ỹ]+)(\s[A-ZÀ-Ỹ][a-zà-ỹ]+)*$/;
            showMsg(nameMsg, "Viết hoa chữ cái đầu mỗi từ", regex.test(fullName.value.trim()));
        });
        email.addEventListener("input", () => {
            showMsg(emailMsg, "Email phải kết thúc bằng @gmail.com", email.value.endsWith("@gmail.com"));
        });
        password.addEventListener("input", () => {
            const v = password.value;
            const ok =
                /[A-Z]/.test(v) &&
                /[a-z]/.test(v) &&
                /\d/.test(v) &&
                /[^A-Za-z0-9]/.test(v) &&
                v.length >= 8;
            showMsg(passwordMsg, "Ít nhất 8 ký tự, đủ mạnh", ok);
        });
        confirmPassword.addEventListener("input", () => {
            showMsg(confirmMsg,
                "Mật khẩu xác nhận phải trùng",
                confirmPassword.value === password.value && confirmPassword.value !== "");
        });
        form.addEventListener("submit", e => {
            if (
                nameMsg.style.color !== "green" ||
                emailMsg.style.color !== "green" ||
                passwordMsg.style.color !== "green" ||
                confirmMsg.style.color !== "green"
            ) {
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
</body>
</html>
