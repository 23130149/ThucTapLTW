package service;

import jakarta.servlet.http.HttpSession;

import java.time.Instant;
import java.util.Random;

public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final int EXPIRE_SECONDS = 120;
    private static final int RESEND_SECONDS = 60;

    public static String generateOtp() {
        Random r = new Random();
        int otp = 100000 + r.nextInt(900000);
        return String.valueOf(otp);
    }

    public static void saveOtp(HttpSession session, String otp) {
        long now = Instant.now().getEpochSecond();
        session.setAttribute("OTP_CODE", otp);
        session.setAttribute("OTP_TIME", now);
        session.setAttribute("OTP_RESEND_AT", now + RESEND_SECONDS);
    }

    public static boolean verifyOtp(HttpSession session, String inputOtp) {

        String savedOtp = (String) session.getAttribute("OTP_CODE");
        Long savedTime = (Long) session.getAttribute("OTP_TIME");

        if (savedOtp == null || savedTime == null) return false;

        long now = Instant.now().getEpochSecond();

        if (now - savedTime > EXPIRE_SECONDS) {
            clearOtp(session);
            return false;
        }

        return savedOtp.equals(inputOtp);
    }

    public static long getResendRemain(HttpSession session) {
        Long resendAt = (Long) session.getAttribute("OTP_RESEND_AT");
        if (resendAt == null) return 0;

        long now = Instant.now().getEpochSecond();
        return Math.max(0, resendAt - now);
    }

    public static void clearOtp(HttpSession session) {
        session.removeAttribute("OTP_CODE");
        session.removeAttribute("OTP_TIME");
        session.removeAttribute("OTP_RESEND_AT");
    }
}
