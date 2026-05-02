package controller.cart;

import cart.Cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import service.ProductService;

import java.io.IOException;

@WebServlet(name = "AddCart", value = "/Add-Cart")
public class AddCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        String qRaw = request.getParameter("quantity");
        if(idRaw == null || idRaw.trim().isEmpty()){
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }
int id;
        try {
id =Integer.parseInt(idRaw.trim());
if (id <=0)throw new NumberFormatException("invalid id");
        }catch (NumberFormatException e){
            response.sendRedirect(request.getContextPath() + "/product");
            return;
        }
int quantity = 1 ;
if(qRaw != null && !qRaw.trim().isEmpty()) {
    try {
        quantity = Math.max(1, Integer.parseInt(qRaw.trim()));
    } catch (NumberFormatException i) {
        quantity = 1;
    }
    ProductService ps = new ProductService();
    Product p = ps.getProductById(id);
    if(p == null){
        response.sendRedirect(request.getContextPath() + "/product");
        return;
    }
HttpSession session =request.getSession();
    Cart cart = (Cart)session.getAttribute("cart");
if (cart==null){
    cart = new Cart();
}
cart.addProduct(p,quantity);
session.setAttribute("cart",cart);
session.setAttribute("cartMessage","Đã thêm sản phẩm vào giỏ hàng");
if("1".equals(request.getParameter("buyNow"))){
    response.sendRedirect((request.getContextPath() + "/payment"));
return;
}
String referer = request.getHeader("Referer");
response.sendRedirect(referer !=null ? referer :request.getContextPath() + "/product");
}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}