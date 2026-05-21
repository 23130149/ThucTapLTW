package controller;

import dao.FavoriteDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

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
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(request.getParameter("productId"));
            if (productId <= 0) throw new NumberFormatException("Invalid product id");
        } catch (NumberFormatException e) {
            redirectBack(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        boolean added = favoriteDao.toggleFavorite(user.getUserId(), productId);
        session.setAttribute("favoriteMessage", added ? "Đã thêm vào sản phẩm yêu thích" : "Đã bỏ khỏi sản phẩm yêu thích");

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
