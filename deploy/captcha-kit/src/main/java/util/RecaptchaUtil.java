package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public final class RecaptchaUtil {
    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String SITE_KEY_CONTEXT_PARAM = "recaptcha.siteKey";
    private static final String SECRET_KEY_CONTEXT_PARAM = "recaptcha.secretKey";
    private static final String SITE_KEY_ENV = "RECAPTCHA_SITE_KEY";
    private static final String SECRET_KEY_ENV = "RECAPTCHA_SECRET_KEY";
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private RecaptchaUtil() {
    }

    public static String getSiteKey(ServletContext context) {
        return getConfig(context, SITE_KEY_CONTEXT_PARAM, SITE_KEY_ENV);
    }

    public static boolean isConfigured(ServletContext context) {
        return !getSiteKey(context).isBlank() && !getSecretKey(context).isBlank();
    }

    public static boolean verify(HttpServletRequest request, ServletContext context) {
        if (!isConfigured(context)) {
            return true;
        }

        String token = request.getParameter("g-recaptcha-response");
        String secret = getSecretKey(context);

        if (token == null || token.isBlank()) {
            return false;
        }

        String body = "secret=" + urlEncode(secret)
                + "&response=" + urlEncode(token)
                + "&remoteip=" + urlEncode(request.getRemoteAddr());

        HttpRequest verifyRequest = HttpRequest.newBuilder(URI.create(VERIFY_URL))
                .timeout(Duration.ofSeconds(8))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(verifyRequest, HttpResponse.BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            return json.has("success") && json.get("success").getAsBoolean();
        } catch (IOException | InterruptedException | IllegalStateException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }

    private static String getSecretKey(ServletContext context) {
        return getConfig(context, SECRET_KEY_CONTEXT_PARAM, SECRET_KEY_ENV);
    }

    private static String getConfig(ServletContext context, String contextParam, String envName) {
        String value = context == null ? null : context.getInitParameter(contextParam);
        if (value == null || value.isBlank()) {
            value = System.getProperty(contextParam);
        }
        if (value == null || value.isBlank()) {
            value = System.getProperty(envName);
        }
        if (value == null || value.isBlank()) {
            value = System.getenv(envName);
        }
        return value == null ? "" : value.trim();
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
