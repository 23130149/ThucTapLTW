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

        String keyword = request.getParameter("keyword");
        String categoryId = request.getParameter("categoryId");
        String status = request.getParameter("status");
        String priceRange = request.getParameter("priceRange");

        int currentPage = 1;
        int pageSize = 5;

        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (Exception e) {
                currentPage = 1;
            }
        }

        List<Product> products = pDao.getListProduct();

        int totalProducts = pDao.getTotalProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int totalStock = pDao.getTotalStock();
        int outOfStock = pDao.countOutOfStock();
        double totalValue = pDao.getTotalValue();

        request.setAttribute("products", products);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalStock", totalStock);
        request.setAttribute("outOfStock", outOfStock);
        request.setAttribute("totalValue", totalValue);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/jsp/adminjsp/Admin_SanPham.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDao pDao = new ProductDao();

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String description = request.getParameter("description");
            Product p = new Product();
            p.setProductName(name);
            p.setProductPrice(price);
            p.setStockQuantity(stock);
            p.setCategoryId(categoryId);

            p.setProductDescription(description);

            pDao.insertProduct(p);
        }
        else if ("update".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String description = request.getParameter("description");
            Product p = new Product();
            p.setProductId(productId);
            p.setProductName(name);
            p.setProductPrice(price);
            p.setStockQuantity(stock);
            p.setCategoryId(categoryId);
            p.setProductDescription(description);
            pDao.updateProduct(p);
        }
        else if ("delete".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            pDao.deleteProduct(productId);
        }
        response.sendRedirect(
                request.getContextPath() + "/admin/products"
        );
    }
}