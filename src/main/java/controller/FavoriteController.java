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

    private int parsePage(String rawPage) {
        try {
            int page = Integer.parseInt(rawPage);
            return Math.max(page, 1);
        } catch (Exception e) {
            return 1;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        User user = (User) session.getAttribute("user");
        int pageSize = 8;
        int currentPage = parsePage(request.getParameter("page"));
        int favoriteCount = favoriteDao.countFavorites(user.getUserId());
        int totalPages = Math.max(1, (int) Math.ceil((double) favoriteCount / pageSize));

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        List<Product> favoriteProducts = favoriteDao.getFavoriteProducts(user.getUserId(), currentPage, pageSize);

        request.setAttribute("productList", favoriteProducts);
        request.setAttribute("favoriteCount", favoriteCount);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/jsp/favourite.jsp").forward(request, response);
    }
}
