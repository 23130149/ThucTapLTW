package controller;

import dao.UserDao;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/login-facebook")
public class FacebookAuth extends HttpServlet {

    private UserDao userDao;

    private final String APP_ID = "958762956904556";
    private final String APP_SECRET = "410f52cb6a34ed40a2ebf24d05c41c02";
    private final String REDIRECT_URI = "http://localhost:8080/projectwar/login-facebook";

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        if (code == null) {
            response.sendRedirect(request.getContextPath() + "/SignIn");
            return;
        }

        try {
            String tokenUrl = "https://graph.facebook.com/v18.0/oauth/access_token"
                    + "?client_id=" + APP_ID
                    + "&redirect_uri=" + REDIRECT_URI
                    + "&client_secret=" + APP_SECRET
                    + "&code=" + code;

            String tokenResponse = getResponse(tokenUrl);
            JsonObject tokenJson = JsonParser.parseString(tokenResponse).getAsJsonObject();

            String accessToken = tokenJson.get("access_token").getAsString();

            String userInfoUrl = "https://graph.facebook.com/me"
                    + "?fields=id,name,email"
                    + "&access_token=" + accessToken;

            String userResponse = getResponse(userInfoUrl);
            JsonObject userJson = JsonParser.parseString(userResponse).getAsJsonObject();

            String facebookId = userJson.get("id").getAsString();
            String email;

            if (userJson.has("email")) {
                email = userJson.get("email").getAsString();
            } else {
                email = facebookId + "@facebook.com";
            }

            if (email == null) {
                response.getWriter().println("Facebook account does not provide email!");
                return;
            }

            User user = userDao.findByEmail(email);

            if (user == null) {
                userDao.insertGoogleUser(email, facebookId);
                user = userDao.findByEmail(email);
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/Account");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login Facebook failed!");
        }
    }

    private String getResponse(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        return result.toString();
    }
}