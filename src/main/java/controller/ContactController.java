package controller;

import dao.ContactDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Contact;
import model.User;
import util.RecaptchaUtil;

import java.io.IOException;

@WebServlet(name = "ContactController", value = {"/Contact", "/contact"})
public class ContactController extends HttpServlet {
    private ContactDao contactDao;

    @Override
    public void init() {
        contactDao = new ContactDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        prepareRecaptcha(request);
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user != null) {
            request.setAttribute("myContacts", contactDao.findByUserId(user.getUserId()));
            request.setAttribute("selectedContactId", parseInt(request.getParameter("detailId"), 0));
        }
        request.getRequestDispatcher("/jsp/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        if (!RecaptchaUtil.verify(request, getServletContext())) {
            forwardWithError(request, response, name, email, phone, subject, message,
                    "Vui lòng xác nhận bạn không phải robot.");
            return;
        }

        if (name == null || email == null || subject == null || message == null
                || name.isBlank() || email.isBlank() || subject.isBlank() || message.isBlank()) {
            forwardWithError(request, response, name, email, phone, subject, message,
                    "Vui lòng nhập đầy đủ thông tin bắt buộc");
            return;
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            forwardWithError(request, response, name, email, phone, subject, message,
                    "Email không hợp lệ");
            return;
        }

        if (phone != null && !phone.isBlank() && !phone.matches("^[0-9]{9,11}$")) {
            forwardWithError(request, response, name, email, phone, subject, message,
                    "Số điện thoại chỉ gồm 9 đến 11 chữ số");
            return;
        }

        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        Contact contact = new Contact();
        contact.setContactName(name);
        contact.setContactEmail(email);
        contact.setPhone(phone);
        contact.setSubject(subject);
        contact.setMessage(message);
        contact.setUserId(user != null ? user.getUserId() : null);

        contactDao.insert(contact);

        request.setAttribute("success", "Gửi tin nhắn thành công! Chúng tôi sẽ phản hồi sớm.");
        if (user != null) {
            request.setAttribute("myContacts", contactDao.findByUserId(user.getUserId()));
        }
        prepareRecaptcha(request);
        request.getRequestDispatcher("/jsp/contact.jsp").forward(request, response);
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String name, String email, String phone, String subject, String message,
                                  String error) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("subject", subject);
        request.setAttribute("message", message);
        prepareRecaptcha(request);
        request.getRequestDispatcher("/jsp/contact.jsp").forward(request, response);
    }


    private int parseInt(String raw, int fallback) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception e) {
            return fallback;
        }
    }

    private void prepareRecaptcha(HttpServletRequest request) {
        request.setAttribute("recaptchaSiteKey", RecaptchaUtil.getSiteKey(getServletContext()));
        request.setAttribute("recaptchaConfigured", RecaptchaUtil.isConfigured(getServletContext()));
    }
}
