package controller.cart;

import cart.Cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ViewCart", value = "/cart")
public class ViewCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart =getorCreateCart(request);
        request.setAttribute("cart",cart);
request.getRequestDispatcher("/jsp/cart.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Post is not supported for /cart");
    }
private Cart getorCreateCart(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Cart cart = (session !=null) ? (Cart) session.getAttribute("cart") :null;

        if( cart == null){
            session = request.getSession(true);
        cart = new Cart();
        session.setAttribute("cart",cart);
        }
return cart;
}
}