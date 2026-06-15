package util;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class VnpayUtil {

    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }

            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create VNPAY secure hash", e);
        }
    }

    public static String buildQuery(Map<String, String> params, boolean encodeValue) {
        StringBuilder query = new StringBuilder();

        for (Map.Entry<String, String> entry : new TreeMap<>(params).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value == null || value.isEmpty()) {
                continue;
            }

            if (query.length() > 0) {
                query.append("&");
            }

            query.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
            query.append("=");

            if (encodeValue) {
                query.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            } else {
                query.append(value);
            }
        }

        return query.toString();
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        if (ip == null || ip.trim().isEmpty()) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip == null || ip.trim().isEmpty() ? "127.0.0.1" : ip.trim();
    }

    public static String buildAbsoluteUrl(HttpServletRequest request, String path) {
        String scheme = firstHeaderValue(request.getHeader("X-Forwarded-Proto"));
        if (scheme == null || scheme.isBlank()) {
            scheme = request.getScheme();
        }

        String forwardedHost = firstHeaderValue(request.getHeader("X-Forwarded-Host"));
        String serverName = forwardedHost;
        int serverPort = request.getServerPort();
        boolean hostAlreadyHasPort = false;

        if (serverName == null || serverName.isBlank()) {
            serverName = request.getServerName();
        } else {
            hostAlreadyHasPort = serverName.contains(":");
        }

        String forwardedPort = firstHeaderValue(request.getHeader("X-Forwarded-Port"));
        if (forwardedPort != null && !forwardedPort.isBlank()) {
            try {
                serverPort = Integer.parseInt(forwardedPort);
            } catch (NumberFormatException ignored) {
            }
        }

        String contextPath = request.getContextPath();

        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && serverPort == 80)
                || ("https".equalsIgnoreCase(scheme) && serverPort == 443);

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (!hostAlreadyHasPort && !defaultPort && forwardedHost == null) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath == null ? "" : contextPath);
        url.append(path.startsWith("/") ? path : "/" + path);

        return url.toString();
    }

    private static String firstHeaderValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.split(",")[0].trim();
    }
}
