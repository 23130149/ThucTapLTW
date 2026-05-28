<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="headerKeyword" value="${not empty param.keyword ? param.keyword : keyword}" />
<header class="hh-header">
    <c:if test="${not empty sessionScope.favoriteMessage}">
        <div class="hh-toast hh-toast-favorite">
            <i class="bx bx-check-circle"></i>
            <span>${sessionScope.favoriteMessage}</span>
        </div>
        <c:remove var="favoriteMessage" scope="session"/>
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
