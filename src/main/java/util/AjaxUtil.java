package util;

import cart.Cart;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AjaxUtil {
    private static final Gson GSON = new Gson();

    private AjaxUtil() {
    }

    public static boolean wantsJson(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String requestedWith = request.getHeader("X-Requested-With");
        return (accept != null && accept.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(requestedWith)
                || "1".equals(request.getParameter("ajax"));
    }

    public static void writeJson(HttpServletResponse response, Map<String, ?> payload) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.getWriter().write(GSON.toJson(payload));
    }

    public static void writeJson(HttpServletResponse response, int status, Map<String, ?> payload) throws IOException {
        response.setStatus(status);
        writeJson(response, payload);
    }

    public static Map<String, Object> ok(String message) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("success", true);
        payload.put("message", message);
        return payload;
    }

    public static Map<String, Object> error(String message) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("success", false);
        payload.put("message", message);
        return payload;
    }

    public static Map<String, Object> cartSummary(Cart cart) {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalQuantity", cart == null ? 0 : cart.getTotalQuantity());
        summary.put("totalPrice", cart == null ? 0 : cart.getTotalPrice());
        summary.put("empty", cart == null || cart.getData() == null || cart.getData().isEmpty());
        return summary;
    }
}
