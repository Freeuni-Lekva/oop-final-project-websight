package servlets;

import model.DBConnection;
import model.HashingPassword;
import model.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String o = req.getParameter("origin");
        DBConnection conn = (DBConnection) this.getServletContext().getAttribute("DBConnection");
        String UN = req.getParameter("userName");
        String pas = req.getParameter("password");
        int ID = -1;
        if (o.equals("CreateAccount")) {
            try {
                if (conn.nameInUse(UN) ) {
                    req.setAttribute("error", "Sorry! That username already exits!");
                    req.getRequestDispatcher("CreateAccount.jsp").forward(req, res);
                    return;
                }
                String passwordHash;
                passwordHash = HashingPassword.createHash(pas);
                ID = conn.createUser(UN, passwordHash);
            } catch (SQLException e) {
                req.setAttribute("error", "Cannot create account with provided information");
                goToFail(req, res);
                return;
            }
            catch (NoSuchAlgorithmException ignore) { }
            catch (InvalidKeySpecException ignore) { }

        }
        else if (o.equals("Login")) {
            try {
                String dbPassword = conn.getPassword(UN);
                if (HashingPassword.PasswordValidation(pas, dbPassword)) {
                    ID = conn.getUserID(UN);
                }
                else {
                    req.setAttribute("error", "Invalid password, please try again.");
                    goToFail(req, res);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                req.setAttribute("error", "An error occured, please try again.");
                goToFail(req, res);
                return;
            }
            catch (NoSuchAlgorithmException ignore) { }
            catch (InvalidKeySpecException ignore) { }
        }
        else {
            goToFail(req, res);
            return;
        }
        User user = new User(ID, UN, conn);
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("user", user);
        Cookie userCookie = new Cookie(COOKIE_NAME, HashingPassword.createCookie());
        try {
            conn.createCookie(ID, userCookie.getValue());
        } catch (SQLException e) {
            e.printStackTrace();
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("homepage.jsp");
            requestDispatcher.forward(req, res);
            return;
        }
        userCookie.setMaxAge(365 * 24 * 60 * 60);
        res.addCookie(userCookie);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("userPage.jsp?userID=" + ID);
        requestDispatcher.forward(req, res);
    }

    static void goToFail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("homepage.jsp");
        requestDispatcher.forward(request, response);
    }
}