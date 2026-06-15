package controller;

import dao.ContactDao;
import dao.NotificationDao;
import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Contact;
import service.EmailService;
import util.AjaxUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminContactController", value = "/admin/contacts")
public class AdminContactController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ContactDao ctDao = new ContactDao();
        OrderDao oDao = new OrderDao();

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");

        keyword = keyword == null ? "" : keyword.trim();
        status = status == null ? "" : status.trim();

        String detailIdRaw = request.getParameter("detailId");
        Contact selectedContact = null;

        if (detailIdRaw != null && !detailIdRaw.isBlank()) {
            try {
                int detailId = Integer.parseInt(detailIdRaw);
                selectedContact = ctDao.findById(detailId);
            } catch (NumberFormatException ignored) {
            }
        }

        List<Contact> contacts = ctDao.findContacts(keyword, status);

        request.setAttribute("contacts", contacts);
        request.setAttribute("selectedContact", selectedContact);
        request.setAttribute("totalContacts", ctDao.count());
        request.setAttribute("newContacts", ctDao.countByStatus("NEW"));
        request.setAttribute("processingContacts", ctDao.countByStatus("PROCESSING"));
        request.setAttribute("doneContacts", ctDao.countByStatus("DONE"));
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentStatus", status);
        request.setAttribute("notificationCount", oDao.countAdminNotifications());
        request.setAttribute("latestNotifications", oDao.getLatestAdminNotifications(20));

        request.getRequestDispatcher("/jsp/adminjsp/Admin_LienHe.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        ContactDao contactDao = new ContactDao();
        NotificationDao notificationDao = new NotificationDao();
        String action = request.getParameter("action");
        boolean success = false;
        String message = null;

        if ("updateStatus".equals(action)) {
            try {
                int contactId = Integer.parseInt(request.getParameter("contactId"));
                String status = normalizeStatus(request.getParameter("status"));
                success = contactDao.updateStatus(contactId, status);
            } catch (NumberFormatException ignored) {
                message = "Liên hệ không hợp lệ.";
            }
        } else if ("reply".equals(action)) {
            try {
                int contactId = Integer.parseInt(request.getParameter("contactId"));
                String reply = request.getParameter("reply");

                if (reply != null && !reply.trim().isEmpty()) {
                    String cleanReply = reply.trim();
                    success = contactDao.replyContact(contactId, cleanReply);
                    if (success) {
                        Contact contact = contactDao.findById(contactId);
                        if (contact != null) {
                            Integer receiverUserId = contact.getUserId();
                            if ((receiverUserId == null || receiverUserId <= 0)
                                    && contact.getContactEmail() != null && !contact.getContactEmail().isBlank()) {
                                receiverUserId = notificationDao.findUserIdByEmail(contact.getContactEmail());
                            }
                            if (receiverUserId != null && receiverUserId > 0) {
                                notificationDao.addOrRefreshSafe(
                                        receiverUserId,
                                        "CONTACT_REPLY",
                                        "Admin đã phản hồi liên hệ của bạn",
                                        "Liên hệ: " + safeText(contact.getSubject()),
                                        "/contact?detailId=" + contactId + "#contact-" + contactId,
                                        "CONTACT",
                                        contactId
                                );
                            }
                            EmailService.sendContactReplyEmail(
                                    contact.getContactEmail(),
                                    "Handmade House phản hồi liên hệ của bạn",
                                    contact.getSubject(),
                                    cleanReply
                            );
                        }
                    }
                } else {
                    message = "Vui lòng nhập nội dung phản hồi.";
                }
            } catch (NumberFormatException ignored) {
                message = "Liên hệ không hợp lệ.";
            }
        } else if ("delete".equals(action)) {
            try {
                int contactId = Integer.parseInt(request.getParameter("contactId"));
                success = contactDao.delete(contactId);
            } catch (NumberFormatException ignored) {
                message = "Liên hệ không hợp lệ.";
            }
        } else {
            message = "Thao tác liên hệ không hợp lệ.";
        }
        if (!success && message == null) {
            message = "Không tìm thấy liên hệ cần cập nhật.";
        }
        if (AjaxUtil.wantsJson(request)) {
            Map<String, Object> payload = success
                    ? AjaxUtil.ok("Đã cập nhật liên hệ.")
                    : AjaxUtil.error(message);
            payload.put("action", action);
            payload.put("contactId", request.getParameter("contactId"));
            payload.put("status", request.getParameter("status"));
            AjaxUtil.writeJson(response, payload);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/admin/contacts");
    }

    private String safeText(String value) {
        if (value == null || value.isBlank()) {
            return "Không có tiêu đề";
        }
        return value.trim();
    }

    private String normalizeStatus(String status) {
        if (status == null) return "NEW";

        return switch (status.trim()) {
            case "PROCESSING" -> "PROCESSING";
            case "DONE" -> "DONE";
            default -> "NEW";
        };
    }
}
