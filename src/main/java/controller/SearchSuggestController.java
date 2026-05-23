package controller;

import com.google.gson.Gson;
import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchSuggestController", value = "/search-suggest")
public class SearchSuggestController extends HttpServlet {
    private final ProductDao productDao = new ProductDao();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String keyword = request.getParameter("keyword");
        List<Product> suggestions = productDao.searchSuggestions(keyword, 4);

        response.getWriter().write(gson.toJson(suggestions));
    }
}
