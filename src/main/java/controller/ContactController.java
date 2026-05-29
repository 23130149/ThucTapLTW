package controller;

import dao.ContactDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Contact;
import model.User;

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

        request.getRequestDispatcher("/jsp/contact.jsp")
                .forward(request, response);
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

        if (name == null || email == null || subject == null || message == null ||
                name.isBlank() || email.isBlank() || subject.isBlank() || message.isBlank()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            request.getRequestDispatcher("/jsp/contact.jsp").forward(request, response);
            return;
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            request.setAttribute("error", "Email không hợp lệ");
            request.getRequestDispatcher("/jsp/contact.jsp").forward(request, response);
            return;
        }

        if (phone != null && !phone.isBlank() && !phone.matches("^[0-9]{9,11}$")) {
            request.setAttribute("error", "Số điện thoại chỉ gồm 9 đến 11 chữ số");
            request.getRequestDispatcher("/jsp/contact.jsp").forward(request, response);
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
        request.getRequestDispatcher("/jsp/contact.jsp").forward(request, response);
    }
}
