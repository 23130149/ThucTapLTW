package service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {
    private static final String FROM_EMAIL = getConfig("SMTP_FROM_EMAIL", "noreply@handmadehousenlu.id.vn");
    private static final String APP_PASSWORD = getConfig("SMTP_APP_PASSWORD", "");

    public static void sendOtpEmail(String toEmail, String otp) {
        if (APP_PASSWORD.isBlank()) {
            throw new IllegalStateException("SMTP_APP_PASSWORD is not configured");
        }

        try {
            MimeMessage message = createMessage(toEmail, "Ma OTP xac thuc email");
            message.setText(
                    "Xin chao,\n\n"
                            + "Ma OTP cua ban la: " + otp + "\n"
                            + "Ma co hieu luc trong 2 phut.\n\n"
                            + "Neu ban khong yeu cau thao tac nay, hay bo qua email.\n\n"
                            + "Handmade House",
                    "UTF-8"
            );
            Transport.send(message);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot send OTP email", e);
        }
    }

    public static void sendContactReplyEmail(String toEmail, String subject, String contactSubject, String replyText) {
        if (toEmail == null || toEmail.isBlank()) {
            return;
        }

        String safeSubject = subject == null || subject.isBlank()
                ? "Handmade House da phan hoi lien he cua ban"
                : subject;
        String originalSubject = contactSubject == null || contactSubject.isBlank()
                ? "Lien he cua ban"
                : contactSubject;
        String body = "Xin chao,\n\n"
                + "Handmade House da phan hoi lien he: " + originalSubject + "\n\n"
                + "Noi dung phan hoi:\n" + replyText + "\n\n"
                + "Ban co the dang nhap Handmade House de xem lai lich su lien he va cac thong bao moi.\n\n"
                + "Handmade House";

        sendTextEmail(toEmail, safeSubject, body);
    }

    public static void sendTextEmail(String toEmail, String subject, String body) {
        if (APP_PASSWORD.isBlank()) {
            throw new IllegalStateException("SMTP_APP_PASSWORD is not configured");
        }

        try {
            MimeMessage message = createMessage(toEmail, subject);
            message.setText(body, "UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot send Handmade House email", e);
        }
    }

    private static MimeMessage createMessage(String toEmail, String subject) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                    }
                }
        );

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL, "Handmade House"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject, "UTF-8");
        return message;
    }

    private static String getConfig(String key, String defaultValue) {
        String propertyValue = System.getProperty(key);
        if (propertyValue != null && !propertyValue.trim().isEmpty()) {
            return propertyValue.trim();
        }
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }
        return defaultValue;
    }
}
