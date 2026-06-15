package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LegalPageController", urlPatterns = {"/privacy", "/data-deletion", "/terms"})
public class LegalPageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/data-deletion".equals(path)) {
            request.getRequestDispatcher("/jsp/data-deletion.jsp").forward(request, response);
            return;
        }

        if ("/terms".equals(path)) {
            request.getRequestDispatcher("/jsp/terms.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/jsp/privacy.jsp").forward(request, response);
    }
}
