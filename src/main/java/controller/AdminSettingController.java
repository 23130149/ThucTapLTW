package controller;

import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AdminSettingController", value = "/admin/setting")
public class AdminSettingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao oDao = new OrderDao();
        request.setAttribute("notificationCount", oDao.countAdminNotifications());

        request.getRequestDispatcher("/jsp/adminjsp/Admin_CaiDat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("storeName");
        String storeEmail = request.getParameter("storeEmail");
        String storePhone = request.getParameter("storePhone");
        String storeWebsite = request.getParameter("storeWebsite");
        String storeAddress = request.getParameter("storeAddress");

        request.getSession().setAttribute("settingMessage", "Lưu cài đặt thành công");

        response.sendRedirect(request.getContextPath() + "/admin/setting");
    }
}