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
    private static final String FROM_EMAIL = "tranhoangquan839@gmail.com";

    private static final String APP_PASSWORD = "culzbbvlffixlkdi";

    public static void sendOtpEmail(String toEmail, String otp) {

        System.out.println("DEBUG: sendOtpEmail() called, to = " + toEmail);

        try {
            Properties pr = new Properties();
            pr.put("mail.smtp.auth", "true");
            pr.put("mail.smtp.starttls.enable", "true");
            pr.put("mail.smtp.host", "smtp.gmail.com");
            pr.put("mail.smtp.port", "587");

            Session session = Session.getInstance(
                    pr,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                        }
                    }
            );
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "Handmade House"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("Mã OTP xác thực email");
            message.setText(
                    "Xin chào,\n\n" +
                            "Mã OTP của bạn là: " + otp + "\n" +
                            "Mã có hiệu lực trong 2 phút.\n\n" +
                            "Nếu bạn không yêu cầu thao tác này, hãy bỏ qua email.\n\n" +
                            "— Handmade House",
                    "UTF-8"
            );
            Transport.send(message);
            System.out.println("Đã gửi OTP tới: " + toEmail);
        } catch (Exception e) {
            System.err.println("LỖI gửi email OTP");
            e.printStackTrace();
        }
    }
}
