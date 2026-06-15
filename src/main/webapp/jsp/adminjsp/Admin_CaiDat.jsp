    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <!DOCTYPE html>
    <html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Admin - Cài đặt</title>
        <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin_CaiDat.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-font-standard.css">
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
                <li><a href="${pageContext.request.contextPath}/admin/reviews"><i class="bx bx-star"></i> Đánh giá</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/contacts"><i class="bx bx-envelope"></i> Liên hệ</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/banner"><i class="bx bx-image"></i>Banner</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/admin/setting"><i class="bx bx-cog"></i>Cài đặt</a></li>
            </ul>
        </nav>
        <div class="logout">
            <a href="${pageContext.request.contextPath}/home">
                <i class="bx bx-home-alt-2"></i> Trang chủ
            </a>
        </div>
    </aside>
    <main class="main-content">
        <header class="header">
            <h2>Cài đặt</h2>
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

                    <div class="user-details">
                        <span class="user-name">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.userName}">
                                    ${sessionScope.user.userName}
                                </c:when>
                                <c:otherwise>Admin Test</c:otherwise>
                            </c:choose>
                        </span>
                        <span class="user-role">Quản trị viên</span>
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

                <c:if test="${not empty sessionScope.settingMessage}">
                    <div class="admin-alert success">
                        <i class="bx bx-check-circle"></i>
                            ${sessionScope.settingMessage}
                    </div>
                    <c:remove var="settingMessage" scope="session"/>
                </c:if>
            <div class="setting-container">
                <c:if test="${canManageSetting}">
                <section class="settings-card">
                <div class="settings-card-header">
                    <div>
                        <h3>
                            <i class="bx bx-store-alt"></i>
                            Thông tin cửa hàng
                        </h3>
                        <p>Cập nhật thông tin cơ bản hiển thị cho cửa hàng.</p>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/admin/setting" method="post">
                    <input type="hidden" name="action" value="updateStore">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="storeName">Tên cửa hàng</label>
                            <input id="storeName" type="text" name="storeName"
                                   value="${fn:escapeXml(storeSetting.storeName)}" required>
                        </div>

                        <div class="form-group">
                            <label for="storeEmail">Email liên hệ</label>
                            <input id="storeEmail" type="email" name="storeEmail"
                                   value="${fn:escapeXml(storeSetting.storeEmail)}">
                        </div>

                        <div class="form-group">
                            <label for="storePhone">Số điện thoại</label>
                            <input id="storePhone" type="text" name="storePhone"
                                   value="${fn:escapeXml(storeSetting.storePhone)}">
                        </div>

                        <div class="form-group">
                            <label for="storeWebsite">Website</label>
                            <input id="storeWebsite" type="text" name="storeWebsite"
                                   value="${fn:escapeXml(storeSetting.storeWebsite)}">
                        </div>

                        <div class="form-group full-width">
                            <label for="storeAddress">Địa chỉ</label>
                            <textarea id="storeAddress" name="storeAddress" rows="3"><c:out value="${storeSetting.storeAddress}"/></textarea>
                        </div>
                    </div>

                    <div class="card-footer">
                        <c:if test="${canManageSetting}">
                            <button type="submit" class="btn-save">
                                <i class="bx bx-save"></i>
                                Lưu thông tin
                            </button>
                        </c:if>
                    </div>
                </form>
            </section>
            <section class="settings-card">
                <div class="settings-card-header">
                    <div>
                        <h3>
                            <i class="bx bx-shield-quarter"></i>
                            Phân quyền quản trị
                        </h3>
                        <p>Gán quyền truy cập từng chức năng cho từng tài khoản quản trị.</p>
                    </div>
                </div>
                <form method="get"
                      action="${pageContext.request.contextPath}/admin/setting"
                      class="admin-permission-search">
                    <div class="admin-search-box">
                        <i class="bx bx-search"></i>
                        <input type="text"
                               name="adminKeyword"
                               value="${adminKeyword}"
                               placeholder="Tìm admin theo tên, email hoặc số điện thoại...">
                    </div>

                    <a href="${pageContext.request.contextPath}/admin/setting" class="btn-reset-admin">
                        <i class="bx bx-refresh"></i>
                        Làm mới
                    </a>
                </form>

                <c:if test="${not empty adminKeyword}">
                    <p class="search-result-text">
                        Kết quả tìm kiếm cho: <strong>${adminKeyword}</strong>
                    </p>
                </c:if>
                    <div class="admin-permission-list">
                        <c:choose>
                            <c:when test="${empty admins}">
                                <div class="empty-admin-result">
                                    <i class="bx bx-user-x"></i>
                                    <p>Không tìm thấy tài khoản admin phù hợp.</p>
                                </div>
                            </c:when>

                            <c:otherwise>
                                <c:forEach var="admin" items="${admins}">
                                    <form class="admin-permission-card"
                                          action="${pageContext.request.contextPath}/admin/setting"
                                          method="post">

                                        <input type="hidden" name="action" value="updatePermission">
                                        <input type="hidden" name="adminId" value="${admin.userId}">
                                        <input type="hidden" name="adminKeyword" value="${adminKeyword}">

                                        <div class="admin-permission-header">
                                            <div class="admin-permission-info">
                            <span class="admin-avatar small">
                                <c:choose>
                                    <c:when test="${not empty admin.userName}">
                                        ${fn:substring(admin.userName, 0, 1)}
                                    </c:when>
                                    <c:otherwise>A</c:otherwise>
                                </c:choose>
                            </span>

                                                <div>
                                                    <strong>
                                                        <c:choose>
                                                            <c:when test="${not empty admin.userName}">
                                                                ${admin.userName}
                                                            </c:when>
                                                            <c:otherwise>Chưa cập nhật tên</c:otherwise>
                                                        </c:choose>
                                                    </strong>
                                                    <p>${admin.email}</p>
                                                </div>
                                            </div>

                                            <c:if test="${canManageSetting}">
                                                <button type="submit" class="btn-save small-btn">
                                                    <i class="bx bx-save"></i>
                                                    Lưu quyền
                                                </button>
                                            </c:if>
                                        </div>

                                        <c:set var="currentPermissions" value="${adminPermissionMap[admin.userId]}"/>

                                        <div class="permission-grid">
                                            <c:forEach var="permission" items="${permissions}">
                                                <label class="permission-option">
                                                    <input type="checkbox"
                                                           name="permissions"
                                                           value="${permission.permissionCode}"
                                                           <c:if test="${fn:contains(currentPermissions, permission.permissionCode)}">checked</c:if>>

                                                    <span>
                                                        <strong>${permission.permissionName}</strong>
                                                        <small>${permission.description}</small>
                                                    </span>
                                                </label>
                                            </c:forEach>
                                        </div>
                                    </form>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
            </section>
                </c:if>
                <section class="settings-card">
                    <div class="settings-card-header">
                        <div>
                            <h3>
                                <i class="bx bx-lock-alt"></i>
                                Bảo mật tài khoản
                            </h3>
                            <p>Đổi mật khẩu quản trị viên đang đăng nhập.</p>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/admin/setting" method="post">
                        <input type="hidden" name="action" value="changePassword">
                        <input type="hidden" name="adminKeyword" value="${adminKeyword}">

                        <div class="form-grid">
                            <div class="form-group full-width">
                                <label for="currentPassword">Mật khẩu hiện tại</label>
                                <input id="currentPassword"
                                       type="password"
                                       name="currentPassword"
                                       placeholder="Nhập mật khẩu hiện tại">
                            </div>

                            <div class="form-group">
                                <label for="newPassword">Mật khẩu mới</label>
                                <input id="newPassword"
                                       type="password"
                                       name="newPassword"
                                       placeholder="Nhập mật khẩu mới">
                            </div>

                            <div class="form-group">
                                <label for="confirmPassword">Xác nhận mật khẩu mới</label>
                                <input id="confirmPassword"
                                       type="password"
                                       name="confirmPassword"
                                       placeholder="Nhập lại mật khẩu mới">
                            </div>
                        </div>

                        <div class="card-footer">
                            <button type="submit" class="btn-password">
                                <i class="bx bx-lock-alt"></i>
                                Đổi mật khẩu
                            </button>
                        </div>
                    </form>
                </section>
        </div>
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
    <script defer src="${pageContext.request.contextPath}/js/password-toggle.js?v=20260615-1"></script>
    </body>
    </html>
