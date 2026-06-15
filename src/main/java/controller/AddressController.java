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
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }
        String deleteId = request.getParameter("delete");
        if (deleteId != null) {
            try {
                addressDao.deleteByIdAndUserId(Integer.parseInt(deleteId), user.getUserId());
            } catch (NumberFormatException ignored) {
            }
            response.sendRedirect(request.getContextPath() + "/Address");
            return;
        }

        List<UserAddress> addresses =
                addressDao.findByUserId(user.getUserId());

        request.setAttribute("addresses", addresses);

        String editId = request.getParameter("edit");
        if (editId != null) {
            UserAddress editAddress = null;
            try {
                editAddress = addressDao.findByIdAndUserId(Integer.parseInt(editId), user.getUserId());
            } catch (NumberFormatException ignored) {
            }
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
        User user = session == null ? null : (User) session.getAttribute("user");

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
        String country = normalize(request.getParameter("country"));
        String province = normalize(request.getParameter("province"));
        String district = normalize(request.getParameter("district"));
        String ward = normalize(request.getParameter("ward"));
        String street = normalize(request.getParameter("street"));

        if (province.isBlank() || district.isBlank() || street.isBlank()) {
            request.getSession().setAttribute("addressError",
                    "Vui lòng nhập đầy đủ tỉnh/thành phố, quận/huyện và địa chỉ chi tiết.");
            response.sendRedirect(getRedirectUrl(request, addressId));
            return;
        }

        if (!ward.isBlank() && !district.isBlank()) {
            district = ward + ", " + district;
        }

        address.setCountry(country.isBlank() ? "Việt Nam" : country);
        address.setProvince(province);
        address.setDistrict(district);
        address.setStreet(street);
        address.setProvinceId(parseInteger(request.getParameter("provinceId")));
        address.setDistrictId(parseInteger(request.getParameter("districtId")));
        address.setWardCode(normalize(request.getParameter("wardCode")));

        if (addressId == 0) {
            addressDao.insert(address);
        } else {
            addressDao.update(address);
        }

        request.getSession().setAttribute("toastMessage",
                addressId == 0 ? "Đã thêm địa chỉ nhận hàng." : "Đã cập nhật địa chỉ nhận hàng.");
        request.getSession().setAttribute("toastType", "hh-toast-success");
        request.getSession().setAttribute("toastIcon", "bx-map");
        response.sendRedirect(getRedirectUrl(request, 0));
    }

    private String getRedirectUrl(HttpServletRequest request, int editAddressId) {
        String returnTo = normalize(request.getParameter("returnTo"));
        if ("payment".equalsIgnoreCase(returnTo)) {
            return request.getContextPath() + "/payment";
        }

        return request.getContextPath() + "/Address"
                + (editAddressId > 0 ? "?edit=" + editAddressId : "");
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", " ");
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
