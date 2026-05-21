package controller;

import dao.FavoriteDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "FavoriteController", value = "/favorite")
public class FavoriteController extends HttpServlet {
    private FavoriteDao favoriteDao;

    @Override
    public void init() {
        favoriteDao = new FavoriteDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        User user = (User) session.getAttribute("user");
        List<Product> favoriteProducts = favoriteDao.getFavoriteProducts(user.getUserId());

        request.setAttribute("productList", favoriteProducts);
        request.setAttribute("favoriteCount", favoriteProducts.size());
        request.getRequestDispatcher("/jsp/favourite.jsp").forward(request, response);
    }
}
