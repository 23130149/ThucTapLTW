package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminProductController", value = "/admin/products")
public class AdminProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDao pDao = new ProductDao();

        List<Product> products = pDao.getListProduct();

        int totalProducts = pDao.getTotalProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / limit);
        int totalStock = pDao.getTotalStock();
        int outOfStock = pDao.countOutOfStock();
        double totalValue = pDao.getTotalValue();

        request.setAttribute("products", products);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalStock", totalStock);
        request.setAttribute("outOfStock", outOfStock);
        request.setAttribute("totalValue", totalValue);

        request.getRequestDispatcher("/jsp/adminjsp/Admin_SanPham.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}