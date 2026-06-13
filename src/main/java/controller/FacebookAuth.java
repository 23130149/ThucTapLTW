package controller;

import dao.CartDao;
import dao.UserDao;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/login-facebook")
public class FacebookAuth extends HttpServlet {

    private UserDao userDao;
    private CartDao cartDao;

    private static final String DEFAULT_APP_ID = "958762956904556";
    private static final String DEFAULT_APP_SECRET = "410f52cb6a34ed40a2ebf24d05c41c02";
    private static final String APP_ID_PARAM = "facebook.appId";
    private static final String APP_SECRET_PARAM = "facebook.appSecret";
    private static final String APP_ID_ENV = "FACEBOOK_APP_ID";
    private static final String APP_SECRET_ENV = "FACEBOOK_APP_SECRET";

    @Override
    public void init() {
        userDao = new UserDao();
        cartDao = new CartDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String error = request.getParameter("error");
        if (error != null) {
            HttpSession session = request.getSession();
            String description = request.getParameter("error_description");
            session.setAttribute("loginMessage", description == null || description.isBlank()
                    ? "Không thể đăng nhập Facebook. Vui lòng thử tài khoản khác."
                    : "Facebook từ chối đăng nhập: " + description);
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        String code = request.getParameter("code");

        if (code == null) {
            if (!isConfigured()) {
                request.getSession().setAttribute("loginMessage",
                        "Chưa cấu hình Facebook App ID/App Secret.");
                response.sendRedirect(request.getContextPath() + "/SignIn");
                return;
            }
            response.sendRedirect(buildLoginUrl(request));
            return;
        }

        try {
            String redirectUri = buildRedirectUri(request);
            String tokenUrl = "https://graph.facebook.com/v18.0/oauth/access_token"
                    + "?client_id=" + getAppId()
                    + "&redirect_uri=" + encode(redirectUri)
                    + "&client_secret=" + getAppSecret()
                    + "&code=" + encode(code);

            String tokenResponse = getResponse(tokenUrl);
            JsonObject tokenJson = JsonParser.parseString(tokenResponse).getAsJsonObject();
            if (!tokenJson.has("access_token")) {
                throw new IOException(readFacebookError(tokenJson, "Facebook không trả access token."));
            }

            String accessToken = tokenJson.get("access_token").getAsString();

            String userInfoUrl = "https://graph.facebook.com/me"
                    + "?fields=id,name,email"
                    + "&access_token=" + accessToken;

            String userResponse = getResponse(userInfoUrl);
            JsonObject userJson = JsonParser.parseString(userResponse).getAsJsonObject();
            if (!userJson.has("id")) {
                throw new IOException(readFacebookError(userJson, "Facebook không trả thông tin tài khoản."));
            }

            String facebookId = userJson.get("id").getAsString();
            String facebookName = userJson.has("name") && !userJson.get("name").isJsonNull()
                    ? userJson.get("name").getAsString()
                    : "";
            String email;

            if (userJson.has("email")) {
                email = userJson.get("email").getAsString();
            } else {
                email = facebookId + "@facebook.com";
            }

            if (email == null) {
                HttpSession session = request.getSession();
                session.setAttribute("loginMessage", "Tài khoản Facebook không cung cấp email.");
                response.sendRedirect(request.getContextPath() + "/SignIn");
                return;
            }

            User user = userDao.findByEmail(email);

            if (user == null) {
                userDao.insertGoogleUser(email, facebookId, facebookName);
                user = userDao.findByEmail(email);
            } else if ((user.getUserName() == null || user.getUserName().isBlank())
                    && userDao.updateUserNameIfBlank(user.getUserId(), facebookName)) {
                user.setUserName(facebookName);
            }

            HttpSession session = request.getSession();
            session.setAttribute("cart", cartDao.getCartByUserId(user.getUserId()));
            session.setAttribute("user", user);

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
                session.removeAttribute("redirectAfterLogin");

                if (isSafeRedirect(redirectAfterLogin)) {
                    while (redirectAfterLogin.startsWith("/")) {
                        redirectAfterLogin = redirectAfterLogin.substring(1);
                    }
                    response.sendRedirect(request.getContextPath() + "/" + redirectAfterLogin);
                } else {
                    response.sendRedirect(request.getContextPath() + "/Account");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("loginMessage", "Đăng nhập Facebook không thành công: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/SignIn");
        }
    }

    private String buildLoginUrl(HttpServletRequest request) {
        String redirectUri = buildRedirectUri(request);
        return "https://www.facebook.com/v18.0/dialog/oauth"
                + "?client_id=" + getAppId()
                + "&redirect_uri=" + encode(redirectUri)
                + "&scope=public_profile";
    }

    private String buildRedirectUri(HttpServletRequest request) {
        String scheme = firstNonBlank(request.getHeader("X-Forwarded-Proto"), request.getScheme());
        String host = firstNonBlank(request.getHeader("X-Forwarded-Host"), request.getServerName());
        if (!host.contains(":") && shouldAppendPort(scheme, request.getServerPort())) {
            host += ":" + request.getServerPort();
        }

        return scheme + "://"
                + host
                + request.getContextPath()
                + "/login-facebook";
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String getResponse(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        InputStream stream = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
        if (stream == null) {
            throw new IOException("Facebook không phản hồi dữ liệu.");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        String body = result.toString();
        if (conn.getResponseCode() >= 400) {
            try {
                JsonObject errorJson = JsonParser.parseString(body).getAsJsonObject();
                throw new IOException(readFacebookError(errorJson, "Facebook trả lỗi HTTP " + conn.getResponseCode()));
            } catch (IllegalStateException ignored) {
                throw new IOException("Facebook trả lỗi HTTP " + conn.getResponseCode());
            }
        }

        return body;
    }

    private boolean isConfigured() {
        return !getAppId().isBlank() && !getAppSecret().isBlank();
    }

    private String getAppId() {
        return getConfig(APP_ID_PARAM, APP_ID_ENV, DEFAULT_APP_ID);
    }

    private String getAppSecret() {
        return getConfig(APP_SECRET_PARAM, APP_SECRET_ENV, DEFAULT_APP_SECRET);
    }

    private String getConfig(String contextParam, String envName, String defaultValue) {
        String value = getServletContext().getInitParameter(contextParam);
        if (value == null || value.isBlank()) {
            value = System.getProperty(contextParam);
        }
        if (value == null || value.isBlank()) {
            value = System.getProperty(envName);
        }
        if (value == null || value.isBlank()) {
            value = System.getenv(envName);
        }
        if (value == null || value.isBlank()) {
            value = defaultValue;
        }
        return value == null ? "" : value.trim();
    }

    private boolean shouldAppendPort(String scheme, int port) {
        return port > 0
                && !("http".equalsIgnoreCase(scheme) && port == 80)
                && !("https".equalsIgnoreCase(scheme) && port == 443);
    }

    private String firstNonBlank(String first, String second) {
        return first != null && !first.isBlank() ? first.trim() : Objects.toString(second, "").trim();
    }

    private boolean isSafeRedirect(String redirectAfterLogin) {
        return redirectAfterLogin != null
                && !redirectAfterLogin.isBlank()
                && !redirectAfterLogin.startsWith("http://")
                && !redirectAfterLogin.startsWith("https://")
                && !redirectAfterLogin.startsWith("//");
    }

    private String readFacebookError(JsonObject json, String fallback) {
        if (json != null && json.has("error") && json.get("error").isJsonObject()) {
            JsonObject error = json.getAsJsonObject("error");
            if (error.has("message") && !error.get("message").isJsonNull()) {
                return error.get("message").getAsString();
            }
        }
        return fallback;
    }
}
