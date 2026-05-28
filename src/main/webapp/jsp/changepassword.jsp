<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đổi mật khẩu - Handmade House</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/account.css">

    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">

    <style>
        .password-box {
            max-width: 520px;
            margin: 30px auto;
            background: #fff;
            padding: 30px;
            border-radius: 14px;
        }

        .form-row {
            margin-bottom: 14px;
        }

        .form-row label {
            font-weight: 600;
            display: block;
            margin-bottom: 6px;
        }

        .form-row input {
            width: 100%;
            padding: 11px 12px;
            border-radius: 8px;
            border: 1px solid #ddd;
        }

        .msg {
            font-size: 13px;
            margin-top: 4px;
        }

        .password-actions {
            display: flex;
            justify-content: space-between;
            margin-top: 22px;
        }

        .btn-save {
            background: #111;
            color: #fff;
            padding: 10px 22px;
            border-radius: 8px;
            border: none;
            cursor: pointer;
        }

        .btn-back {
            background: #eee;
            padding: 10px 18px;
            border-radius: 8px;
            text-decoration: none;
            color: #333;
        }

        .otp-note {
            font-size: 13px;
            color: #555;
            margin-bottom: 10px;
        }

        .resend {
            font-size: 13px;
            margin-top: 8px;
        }
    </style>
  <jsp:include page="/jsp/layout-assets.jsp"/>
</head>

<body>

<jsp:include page="/jsp/header.jsp"/>
<main class="about-us-container">

    <h1>Đổi mật khẩu</h1>

    <c:if test="${not empty error}">
        <p style="color:red; text-align:center; margin-bottom: 15px;">
                ${error}
        </p>
    </c:if>

    <div class="password-box">

        <c:if test="${step != 'OTP_SENT'}">
            <form action="${pageContext.request.contextPath}/ChangePassword"
                  method="post" id="step1Form">

                <input type="hidden" name="action" value="sendOtp">

                <div class="form-row">
                    <label>Mật khẩu hiện tại</label>
                    <input type="password" name="oldPassword" required>
                </div>

                <div class="password-actions">
                    <button type="submit" class="btn-save">
                        Gửi mã OTP
                    </button>

                    <a href="${pageContext.request.contextPath}/Account"
                       class="btn-back">
                        Quay lại
                    </a>
                </div>
            </form>
        </c:if>

        <!-- ================= STEP 2: NHẬP OTP + XÁC NHẬN ================= -->
        <c:if test="${step == 'OTP_SENT'}">
            <form action="${pageContext.request.contextPath}/ChangePassword"
                  method="post">

                <input type="hidden" name="action" value="confirm">

                <p class="otp-note">
                    📧 Mã OTP đã được gửi về email của bạn (hiệu lực 2 phút)
                </p>

                <div class="form-row">
                    <label>Mã OTP</label>
                    <input type="text" name="otp" required>
                </div>

                <div class="form-row">
                    <label>Mật khẩu mới</label>
                    <input type="password" name="newPassword" required>
                </div>

                <div class="form-row">
                    <label>Nhập lại mật khẩu mới</label>
                    <input type="password" name="confirmPassword" required>
                </div>

                <button type="submit" class="btn-save">
                    Xác nhận đổi mật khẩu
                </button>

                <div class="resend">
                    <c:choose>
                        <c:when test="${resendRemain > 0}">
                            Gửi lại OTP sau <b>${resendRemain}</b> giây
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/ChangePassword"
                                  method="post">
                                <input type="hidden" name="action" value="sendOtp">
                                <button type="submit" class="btn-back">
                                    Gửi lại OTP
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </form>
        </c:if>

    </div>
</main>

<jsp:include page="/jsp/footer.jsp"/>
<script>
    document.addEventListener("DOMContentLoaded", function () {

        const newPassword = document.getElementById("newPassword");
        const confirmPassword = document.getElementById("confirmPassword");

        if (!newPassword || !confirmPassword) return;

        const passwordMsg = document.getElementById("passwordMsg");
        const confirmMsg = document.getElementById("confirmMsg");

        function showMsg(el, msg, ok) {
            el.innerHTML = (ok ? "✔ " : "❌ ") + msg;
            el.style.color = ok ? "green" : "red";
        }

        newPassword.addEventListener("input", () => {
            const v = newPassword.value;
            const ok =
                /[A-Z]/.test(v) &&
                /[a-z]/.test(v) &&
                /\d/.test(v) &&
                /[^A-Za-z0-9]/.test(v) &&
                v.length >= 8;

            showMsg(
                passwordMsg,
                "Ít nhất 8 ký tự, gồm hoa, thường, số và ký tự đặc biệt",
                ok
            );
        });

        confirmPassword.addEventListener("input", () => {
            showMsg(
                confirmMsg,
                "Mật khẩu xác nhận phải trùng",
                confirmPassword.value === newPassword.value &&
                confirmPassword.value !== ""
            );
        });
    });
</script>
</body>
</html>
