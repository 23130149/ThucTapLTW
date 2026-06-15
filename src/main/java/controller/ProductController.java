package controller;

import dao.CategoryDao;
import dao.FavoriteDao;
import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Product;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "ProductController", value = "/product")
public class ProductController extends HttpServlet {
    private int parsePage(String rawPage) {
        try {
            return Math.max(Integer.parseInt(rawPage), 1);
        } catch (Exception e) {
            return 1;
        }
    }

    private String clean(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String materialLabel(String material) {
        return switch (material == null ? "" : material) {
            case "len", "len-crochet" -> "Len / crochet";
            case "nhua" -> "Nhựa / resin";
            case "sap-nen" -> "Sáp / nến thơm";
            case "go" -> "Gỗ";
            case "vai", "da", "vai-da" -> "Vải / da";
            case "gom-su" -> "Gốm / sứ";
            case "kim-loai" -> "Kim loại";
            default -> material;
        };
    }

    private String usageLabel(String usage) {
        return switch (usage == null ? "" : usage) {
            case "trang-tri", "trang-tri-nha" -> "Trang trí nhà cửa";
            case "thoi-trang", "phu-kien-ca-nhan" -> "Phụ kiện cá nhân";
            case "thu-gian-huong-thom" -> "Thư giãn / hương thơm";
            case "dien-thoai" -> "Ốp lưng điện thoại";
            case "thu-cung" -> "Đồ dùng thú cưng";
            case "qua-tang" -> "Quà tặng";
            default -> usage;
        };
    }

    private List<Integer> parseCategoryIds(HttpServletRequest request) {
        String[] values = request.getParameterValues("categoryId");
        Set<Integer> ids = new LinkedHashSet<>();
        if (values != null) {
            for (String value : values) {
                String cleaned = clean(value);
                if (cleaned == null) continue;
                try {
                    int id = Integer.parseInt(cleaned);
                    if (id > 0) ids.add(id);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return new ArrayList<>(ids);
    }

    private List<String> buildPagination(int currentPage, int totalPages) {
        List<String> items = new ArrayList<>();
        if (totalPages <= 7) {
            for (int i = 1; i <= totalPages; i++) items.add(String.valueOf(i));
            return items;
        }

        items.add("1");
        int start = Math.max(2, currentPage - 1);
        int end = Math.min(totalPages - 1, currentPage + 1);

        if (currentPage <= 3) {
            start = 2;
            end = 4;
        } else if (currentPage >= totalPages - 2) {
            start = totalPages - 3;
            end = totalPages - 1;
        }

        if (start > 2) items.add("...");
        for (int i = start; i <= end; i++) items.add(String.valueOf(i));
        if (end < totalPages - 1) items.add("...");
        items.add(String.valueOf(totalPages));
        return items;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String keyword = clean(request.getParameter("keyword"));
        String status = clean(request.getParameter("status"));
        String priceRange = clean(request.getParameter("priceRange"));
        String material = clean(request.getParameter("material"));
        String usage = clean(request.getParameter("usage"));
        String sort = clean(request.getParameter("sort"));
        List<Integer> categoryIds = parseCategoryIds(request);

        ProductDao productDao = new ProductDao();
        CategoryDao categoryDao = new CategoryDao();
        FavoriteDao favoriteDao = new FavoriteDao();

        int pageSize = 8;
        int currentPage = parsePage(request.getParameter("page"));
        int productCount = productDao.countFilteredProducts(keyword, categoryIds, status, priceRange, material, usage);
        int totalPages = Math.max(1, (int) Math.ceil((double) productCount / pageSize));

        if (currentPage > totalPages) currentPage = totalPages;

        List<Product> products = productDao.getFilteredProducts(keyword, categoryIds, status, priceRange, material, usage, sort, currentPage, pageSize);

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Set<Integer> favoriteIds = favoriteDao.getFavoriteProductIds(user.getUserId());
            products.forEach(product -> product.setFavorite(favoriteIds.contains(product.getProductId())));
        }

        request.setAttribute("productList", products);
        request.setAttribute("categoryList", categoryDao.getAllCategories());
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategoryIds", categoryIds);
        request.setAttribute("status", status);
        request.setAttribute("priceRange", priceRange);
        request.setAttribute("material", material);
        request.setAttribute("usage", usage);
        request.setAttribute("materialLabel", materialLabel(material));
        request.setAttribute("usageLabel", usageLabel(usage));
        request.setAttribute("sort", sort);
        request.setAttribute("productCount", productCount);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("currentPageString", String.valueOf(currentPage));
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("paginationItems", buildPagination(currentPage, totalPages));

        request.getRequestDispatcher("/jsp/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
