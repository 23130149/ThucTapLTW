package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public final class GoogleTokenVerifier {
    private static final String TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";
    private static final String CLIENT_ID_PARAM = "google.clientId";
    private static final String CLIENT_ID_ENV = "GOOGLE_CLIENT_ID";
    private static final String DEFAULT_CLIENT_ID =
            "1027811499981-o189kbf29m7ucr73kr6npqq7v6t6u494.apps.googleusercontent.com";

    private GoogleTokenVerifier() {
    }

    public static JsonObject verify(String credential, ServletContext context) throws IOException {
        if (credential == null || credential.isBlank()) {
            throw new IOException("Google credential is missing.");
        }

        HttpURLConnection connection = (HttpURLConnection) URI.create(
                TOKEN_INFO_URL + URLEncoder.encode(credential, StandardCharsets.UTF_8)
        ).toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);

        int status = connection.getResponseCode();
        InputStream stream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
        if (stream == null) {
            throw new IOException("Google did not return token information.");
        }

        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

        JsonObject token = JsonParser.parseString(body.toString()).getAsJsonObject();
        if (status >= 400 || token.has("error_description")) {
            throw new IOException("Google token is invalid.");
        }

        String audience = getString(token, "aud");
        String issuer = getString(token, "iss");
        String email = getString(token, "email");
        String subject = getString(token, "sub");
        long expiresAt = parseLong(getString(token, "exp"));

        if (!getClientId(context).equals(audience)
                || (!"accounts.google.com".equals(issuer) && !"https://accounts.google.com".equals(issuer))
                || email.isBlank()
                || subject.isBlank()
                || expiresAt <= Instant.now().getEpochSecond()) {
            throw new IOException("Google token verification failed.");
        }

        return token;
    }

    public static String getClientId(ServletContext context) {
        String value = context == null ? null : context.getInitParameter(CLIENT_ID_PARAM);
        if (value == null || value.isBlank()) {
            value = System.getProperty(CLIENT_ID_PARAM);
        }
        if (value == null || value.isBlank()) {
            value = System.getProperty(CLIENT_ID_ENV);
        }
        if (value == null || value.isBlank()) {
            value = System.getenv(CLIENT_ID_ENV);
        }
        return value == null || value.isBlank() ? DEFAULT_CLIENT_ID : value.trim();
    }

    private static String getString(JsonObject json, String name) {
        return json.has(name) && !json.get(name).isJsonNull() ? json.get(name).getAsString() : "";
    }

    private static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
