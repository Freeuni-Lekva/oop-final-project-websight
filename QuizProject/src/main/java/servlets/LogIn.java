package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LogIn extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final String COOKIE_NAME = "userCookie";

    public LogIn() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher RD = request.getRequestDispatcher("homepage.jsp");
        RD.forward(request, response);
    }
}