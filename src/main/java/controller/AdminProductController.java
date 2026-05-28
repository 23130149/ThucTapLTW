package controller;

import dao.CategoryDao;
import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.File;
import java.io.IOException;
import java.util.List;

@MultipartConfig
@WebServlet(name = "AdminProductController", value = "/admin/products")
public class AdminProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDao pDao = new ProductDao();
        CategoryDao cDao = new CategoryDao();

        String keyword = request.getParameter("keyword");
        String categoryId = request.getParameter("categoryId");
        String status = request.getParameter("status");
        String priceRange = request.getParameter("priceRange");

        String editIdParam = request.getParameter("editId");
        Product editProduct = null;

        if (editIdParam != null && !editIdParam.isBlank()) {
            try {
                int editId = Integer.parseInt(editIdParam);
                editProduct = pDao.getProductById(editId);
            } catch (NumberFormatException ignored) {
            }
        }

        int pageSize = 5;
        int currentPage = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            }
        }    catch (NumberFormatException e) {
                currentPage = 1;
        }

        List<Product> products = pDao.getFilteredProducts(keyword, categoryId, status, priceRange, currentPage, pageSize);

        int totalProducts = pDao.countFilteredProducts(keyword, categoryId, status, priceRange);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int totalStock = pDao.getTotalStock();
        int outOfStock = pDao.countOutOfStock();
        double totalValue = pDao.getTotalValue();

        request.setAttribute("categories",    cDao.getAllCategories());
        request.setAttribute("products", products);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalStock", totalStock);
        request.setAttribute("outOfStock", outOfStock);
        request.setAttribute("totalValue", totalValue);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("editProduct", editProduct);

        request.getRequestDispatcher("/jsp/adminjsp/Admin_SanPham.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDao pDao = new ProductDao();
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            int price = 0, stock = 0, categoryId = 0;
            try { price = Integer.parseInt(request.getParameter("price")); } catch (NumberFormatException ignored) {}
            try { stock = Integer.parseInt(request.getParameter("stock")); } catch (NumberFormatException ignored) {}
            try { categoryId = Integer.parseInt(request.getParameter("categoryId")); } catch (NumberFormatException ignored) {}
            String imageUrl = resolveImageUrl(request);
            Product p = new Product();
            p.setProductName(name);
            p.setProductPrice(price);
            p.setStockQuantity(stock);
            p.setCategoryId(categoryId);
            p.setProductDescription(description);
            p.setImageUrl(imageUrl);
            pDao.insertProduct(p);
        }
        else if ("update".equals(action)) {
            String name = request.getParameter("name");
            int productId = 0, price = 0, stock = 0, categoryId = 0;
            try { productId = Integer.parseInt(request.getParameter("productId")); } catch (NumberFormatException ignored) {}
            try { price = Integer.parseInt(request.getParameter("price")); } catch (NumberFormatException ignored) {}
            try { stock = Integer.parseInt(request.getParameter("stock")); } catch (NumberFormatException ignored) {}
            try { categoryId = Integer.parseInt(request.getParameter("categoryId")); } catch (NumberFormatException ignored) {}
            String description = request.getParameter("description");
            String imageUrl = resolveImageUrl(request);
            Product p = new Product();
            p.setProductId(productId);
            p.setProductName(name);
            p.setProductPrice(price);
            p.setStockQuantity(stock);
            p.setCategoryId(categoryId);
            p.setProductDescription(description);
            p.setImageUrl(imageUrl);
            pDao.updateProduct(p);
        }
        else if ("delete".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                pDao.deleteProduct(productId);
            } catch (NumberFormatException ignored) {}
        }
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
    private String resolveImageUrl(HttpServletRequest request) throws IOException, ServletException {
        String imageUrl = request.getParameter("imageUrl");
        Part filePart = request.getPart("imageFile");

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            String uploadDir = getServletContext().getRealPath("/uploads/products/");

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String savedName = System.currentTimeMillis() + "_" + fileName;
            filePart.write(uploadDir + File.separator + savedName);

            imageUrl = "/uploads/products/" + savedName;
        }

        return imageUrl;
    }
}