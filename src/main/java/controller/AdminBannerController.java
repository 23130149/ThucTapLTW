package controller;

import dao.BannerDao;
import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Banner;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "AdminBannerController", value = "/admin/banner")
public class AdminBannerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BannerDao bDao = new BannerDao();
        OrderDao oDao = new OrderDao();

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String editIdRaw = request.getParameter("editId");

        keyword = keyword == null ? "" : keyword.trim();
        status = status == null ? "" : status.trim();

        Banner selectedBanner = null;

        if (editIdRaw != null && !editIdRaw.isBlank()) {
            try {
                selectedBanner = bDao.getBannerById(Integer.parseInt(editIdRaw));
            } catch (NumberFormatException ignored) {
            }
        }

        List<Banner> banners = bDao.getAdminBanners(keyword, status);

        request.setAttribute("banners", banners);
        request.setAttribute("selectedBanner", selectedBanner);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentStatus", status);
        request.setAttribute("totalBanners", bDao.countAllBanners());
        request.setAttribute("activeBanners", bDao.countBannersByStatus("ACTIVE"));
        request.setAttribute("inactiveBanners", bDao.countBannersByStatus("INACTIVE"));
        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(20));

        request.getRequestDispatcher("/jsp/adminjsp/Admin_Banner.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        BannerDao bannerDao = new BannerDao();
        String action = request.getParameter("action");

        String message;

        try {
            if ("add".equals(action)) {
                Banner banner = readBannerFromRequest(request);
                boolean success = bannerDao.insert(banner);
                message = success ? "Thêm banner thành công." : "Không thể thêm banner.";
            } else if ("update".equals(action)) {
                Banner banner = readBannerFromRequest(request);
                banner.setBannerId(Integer.parseInt(request.getParameter("bannerId")));
                boolean success = bannerDao.update(banner);
                message = success ? "Cập nhật banner thành công." : "Không thể cập nhật banner.";
            } else if ("toggleStatus".equals(action)) {
                int bannerId = Integer.parseInt(request.getParameter("bannerId"));
                String status = normalizeStatus(request.getParameter("status"));
                boolean success = bannerDao.updateStatus(bannerId, status);
                message = success ? "Cập nhật trạng thái banner thành công." : "Không thể cập nhật trạng thái banner.";
            } else if ("delete".equals(action)) {
                int bannerId = Integer.parseInt(request.getParameter("bannerId"));
                boolean success = bannerDao.delete(bannerId);
                message = success ? "Xóa banner thành công." : "Không thể xóa banner.";
            } else {
                message = "Thao tác không hợp lệ.";
            }
        } catch (Exception e) {
            message = "Dữ liệu banner không hợp lệ.";
        }

        request.getSession().setAttribute("adminBannerMessage", message);

        String keyword = request.getParameter("keyword");
        String currentStatus = request.getParameter("currentStatus");

        String redirectUrl = request.getContextPath() + "/admin/banner";
        String queryString = buildQuery(keyword, currentStatus);

        response.sendRedirect(redirectUrl + queryString);
    }

    private Banner readBannerFromRequest(HttpServletRequest request) {
        Banner banner = new Banner();

        banner.setTitleLine1(clean(request.getParameter("titleLine1")));
        banner.setTitleLine2(clean(request.getParameter("titleLine2")));
        banner.setSubtitle(clean(request.getParameter("subtitle")));
        banner.setImageUrl(clean(request.getParameter("imageUrl")));
        banner.setTargetUrl(clean(request.getParameter("targetUrl")));
        banner.setStatus(normalizeStatus(request.getParameter("status")));
        banner.setSortOrder(parseSortOrder(request.getParameter("sortOrder")));

        return banner;
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeStatus(String status) {
        if ("INACTIVE".equals(status)) {
            return "INACTIVE";
        }
        return "ACTIVE";
    }

    private int parseSortOrder(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception e) {
            return 0;
        }
    }

    private String buildQuery(String keyword, String status) {
        StringBuilder query = new StringBuilder();

        keyword = keyword == null ? "" : keyword.trim();
        status = status == null ? "" : status.trim();

        if (!keyword.isEmpty()) {
            query.append("?keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }

        if (!status.isEmpty()) {
            query.append(query.length() == 0 ? "?" : "&");
            query.append("status=").append(URLEncoder.encode(status, StandardCharsets.UTF_8));
        }

        return query.toString();
    }
}