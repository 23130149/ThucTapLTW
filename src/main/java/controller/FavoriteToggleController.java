package controller;

import dao.FavoriteDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.AjaxUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "FavoriteToggleController", value = "/favorite-toggle")
public class FavoriteToggleController extends HttpServlet {
    private FavoriteDao favoriteDao;

    @Override
    public void init() {
        favoriteDao = new FavoriteDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Vui lòng đăng nhập để dùng danh sách yêu thích."));
                return;
            }
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        int productId;
        try {
            String productIdRaw = request.getParameter("productId");
            if (productIdRaw == null || productIdRaw.isBlank()) {
                productIdRaw = request.getParameter("id");
            }
            productId = Integer.parseInt(productIdRaw);
            if (productId <= 0) throw new NumberFormatException("Invalid product id");
        } catch (NumberFormatException e) {
            if (AjaxUtil.wantsJson(request)) {
                AjaxUtil.writeJson(response, AjaxUtil.error("Sản phẩm không hợp lệ."));
                return;
            }
            redirectBack(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        boolean added = favoriteDao.toggleFavorite(user.getUserId(), productId);
        if (AjaxUtil.wantsJson(request)) {
            Map<String, Object> payload = AjaxUtil.ok(added ? "Đã thêm vào yêu thích." : "Đã bỏ khỏi yêu thích.");
            payload.put("productId", productId);
            payload.put("favorite", added);
            AjaxUtil.writeJson(response, payload);
            return;
        }

        session.setAttribute("toastMessage", added ? "Đã thêm vào sản phẩm yêu thích" : "Đã bỏ khỏi sản phẩm yêu thích");
        session.setAttribute("toastType", "hh-toast-favorite");
        session.setAttribute("toastIcon", added ? "bx-heart" : "bx-heart-circle");
        redirectBack(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void redirectBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            response.sendRedirect(referer);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/product");
    }
}
