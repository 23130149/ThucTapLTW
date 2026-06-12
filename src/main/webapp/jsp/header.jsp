<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="headerKeyword" value="${not empty param.keyword ? param.keyword : keyword}" />
<header class="hh-header">
    <c:if test="${not empty sessionScope.toastMessage}">
        <div class="hh-toast ${empty sessionScope.toastType ? 'hh-toast-success' : sessionScope.toastType}">
            <i class="bx ${empty sessionScope.toastIcon ? 'bx-check-circle' : sessionScope.toastIcon}"></i>
            <span>${sessionScope.toastMessage}</span>
        </div>
        <c:remove var="toastMessage" scope="session"/>
        <c:remove var="toastType" scope="session"/>
        <c:remove var="toastIcon" scope="session"/>
    </c:if>
    <c:if test="${empty sessionScope.user}">
        <div class="hh-login-modal ${sessionScope.showLoginModal ? 'show' : ''}" id="hhLoginModal" aria-hidden="${sessionScope.showLoginModal ? 'false' : 'true'}">
            <div class="hh-login-dialog" role="dialog" aria-modal="true" aria-labelledby="hhLoginTitle">
                <button type="button" class="hh-login-close" aria-label="Đóng thông báo">
                    <i class="bx bx-x"></i>
                </button>
                <div class="hh-login-icon">
                    <i class="bx bx-cart-add"></i>
                </div>
                <h3 id="hhLoginTitle">Vui lòng đăng nhập</h3>
                <p>Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.</p>
                <a class="hh-login-button" href="${pageContext.request.contextPath}/SignIn?redirect=${sessionScope.redirectAfterLogin}">Đăng nhập</a>
            </div>
        </div>
        <c:remove var="showLoginModal" scope="session"/>
    </c:if>
    <div class="hh-header-main">
        <div class="hh-header-inner">
            <a class="hh-logo" href="${pageContext.request.contextPath}/home">
                <span>Handmade</span>
                <b>House</b>
            </a>
            <form class="hh-search-form" action="${pageContext.request.contextPath}/product" method="get" autocomplete="off">
                <input class="hh-search-input" type="text" name="keyword" value="${headerKeyword}" placeholder="Tìm sản phẩm" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false">
                <button class="hh-search-button" type="submit" aria-label="Tìm kiếm"><i class="bx bx-search-alt-2"></i></button>
            </form>
            <div class="hh-actions">
                <a class="hh-action" href="${pageContext.request.contextPath}/favorite" aria-label="Yêu thích"><i class="bx bx-heart"></i></a>
                <a class="hh-action" href="${pageContext.request.contextPath}/cart" aria-label="Giỏ hàng">
                    <i class="bx bx-cart"></i>
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                        <span class="hh-cart-count">${sessionScope.cart.totalQuantity}</span>
                    </c:if>
                </a>
                <a class="hh-action" href="${pageContext.request.contextPath}/Account" aria-label="Tài khoản"><i class="bx bx-user"></i></a>
            </div>
        </div>
    </div>
    <nav class="hh-nav">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>
        <a href="${pageContext.request.contextPath}/jsp/blog.jsp">Blog</a>
        <a href="${pageContext.request.contextPath}/contact">Liên hệ</a>
    </nav>
</header>
<script>window.APP_CONTEXT='${pageContext.request.contextPath}';</script>
<c:if test="${empty sessionScope.user}">
    <script>
        (function () {
            const modal = document.getElementById('hhLoginModal');
            if (!modal) return;

            const loginLink = modal.querySelector('.hh-login-button');
            const closeButtons = modal.querySelectorAll('.hh-login-close');
            const contextPath = window.APP_CONTEXT || '';

            function currentRedirect() {
                let current = window.location.pathname + window.location.search;
                if (contextPath && current.startsWith(contextPath)) {
                    current = current.substring(contextPath.length);
                }
                current = current.replace(/^\/+/, '');
                return current || 'home';
            }

            function syncLoginLink() {
                loginLink.href = contextPath + '/SignIn?redirect=' + encodeURIComponent(currentRedirect());
            }

            function openLoginModal(event) {
                event.preventDefault();
                syncLoginLink();
                modal.classList.add('show');
                modal.setAttribute('aria-hidden', 'false');
            }

            function closeLoginModal() {
                modal.classList.remove('show');
                modal.setAttribute('aria-hidden', 'true');
            }

            if (modal.classList.contains('show')) {
                syncLoginLink();
            }

            document.querySelectorAll('a[href*="/Add-Cart"]').forEach(function (link) {
                link.addEventListener('click', openLoginModal);
            });

            document.querySelectorAll('form[action$="/Add-Cart"]').forEach(function (form) {
                form.addEventListener('submit', openLoginModal);
            });

            closeButtons.forEach(function (button) {
                button.addEventListener('click', closeLoginModal);
            });

            modal.addEventListener('click', function (event) {
                if (event.target === modal) {
                    closeLoginModal();
                }
            });

            document.addEventListener('keydown', function (event) {
                if (event.key === 'Escape') {
                    closeLoginModal();
                }
            });
        })();
    </script>
</c:if>