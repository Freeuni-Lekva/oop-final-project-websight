package servlets;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@WebServlet("/LogoutServlet")
public class LogOut extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogOut() {super();}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession s = req.getSession();
        Cookie[] cs = req.getCookies();
        for (int i = 0; i < cs.length; i++) {
            Cookie c = cs[i];
            if (c.getName().equals(LogIn.COOKIE_NAME)) {
                c.setMaxAge(0);
                c.setValue(null);
                res.addCookie(c);
            }
        }
        s.invalidate();
        res.sendRedirect("homepage.jsp");
    }
}