<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<footer class="hh-footer">
    <div class="hh-footer-inner">
        <div class="hh-footer-brand">
            <h3><c:out value="${empty applicationScope.storeSetting.storeName ? 'Handmade House' : applicationScope.storeSetting.storeName}"/></h3>
            <p>Không gian nhỏ cho những sản phẩm thủ công tinh tế, dễ thương và gần gũi.</p>
            <div class="hh-socials">
                <a href="https://facebook.com" target="_blank" rel="noopener" aria-label="Facebook"><i class="bx bxl-facebook"></i></a>
                <a href="https://instagram.com" target="_blank" rel="noopener" aria-label="Instagram"><i class="bx bxl-instagram"></i></a>
                <a href="https://tiktok.com" target="_blank" rel="noopener" aria-label="TikTok"><i class="bx bxl-tiktok"></i></a>
            </div>
        </div>
        <div class="hh-footer-col">
            <h4>Khám phá</h4>
            <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>
            <a href="${pageContext.request.contextPath}/jsp/blog.jsp">Blog</a>
            <a href="${pageContext.request.contextPath}/favorite">Yêu thích</a>
        </div>
        <div class="hh-footer-col">
            <h4>Hỗ trợ</h4>
            <a href="${pageContext.request.contextPath}/contact">Liên hệ</a>
            <a href="${pageContext.request.contextPath}/terms">Điều khoản dịch vụ</a>
            <a href="${pageContext.request.contextPath}/Account">Tài khoản</a>
            <a href="${pageContext.request.contextPath}/privacy">Chính sách quyền riêng tư</a>
            <a href="${pageContext.request.contextPath}/data-deletion">Xóa dữ liệu người dùng</a>
        </div>
        <div class="hh-footer-col">
            <h4>Thông tin</h4>
            <p><c:out value="${empty applicationScope.storeSetting.storeAddress ? 'Khu phố 6, Linh Trung, Thủ Đức, TP.HCM' : applicationScope.storeSetting.storeAddress}"/></p>
            <p><c:out value="${empty applicationScope.storeSetting.storePhone ? '0944912685' : applicationScope.storeSetting.storePhone}"/></p>
            <p><c:out value="${empty applicationScope.storeSetting.storeEmail ? 'handmadehouse23@handmade.vn' : applicationScope.storeSetting.storeEmail}"/></p>
        </div>
    </div>
    <div class="hh-footer-bottom">© 2025 Handmade House</div>
</footer>
<script src="${pageContext.request.contextPath}/js/search-suggest.js"></script>
