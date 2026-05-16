package controller;

import dao.UserAddressDao;
import model.User;
import model.UserAddress;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/Address")
public class AddressController extends HttpServlet {

    private final UserAddressDao addressDao = new UserAddressDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }
        String deleteId = request.getParameter("delete");
        if (deleteId != null) {
            addressDao.deleteById(Integer.parseInt(deleteId));
            response.sendRedirect(request.getContextPath() + "/Address");
            return;
        }

        List<UserAddress> addresses =
                addressDao.findByUserId(user.getUserId());

        request.setAttribute("addresses", addresses);

        String editId = request.getParameter("edit");
        if (editId != null) {
            UserAddress editAddress =
                    addressDao.findById(Integer.parseInt(editId));
            request.setAttribute("address", editAddress);
        } else {
            request.setAttribute("address", new UserAddress());
        }

        request.getRequestDispatcher("jsp/address.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        int addressId = 0;
        try {
            addressId = Integer.parseInt(
                    request.getParameter("userAddressId"));
        } catch (Exception ignored) {}

        UserAddress address = new UserAddress();
        address.setUserAddressId(addressId);
        address.setUserId(user.getUserId());
        address.setCountry(request.getParameter("country"));
        address.setProvince(request.getParameter("province"));
        address.setDistrict(request.getParameter("district"));
        address.setStreet(request.getParameter("street"));

        if (addressId == 0) {
            addressDao.insert(address);
        } else {
            addressDao.update(address);
        }

        response.sendRedirect(request.getContextPath() + "/Address");
    }
}
