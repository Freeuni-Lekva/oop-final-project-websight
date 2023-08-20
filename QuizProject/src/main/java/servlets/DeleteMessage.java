package servlets;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import model.DBConnection;
import java.sql.ResultSet;

@WebServlet("/DeleteMessage")
public class DeleteMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteMessage() {super();}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {}

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            ServletContext cont = getServletContext();
            DBConnection conn = (DBConnection) cont.getAttribute("DBConnection");
            Integer userID = ((User) session.getAttribute("user")).getUserID();
            Integer messageID = Integer.parseInt(req.getParameter("messageID"));
            ResultSet results = conn.getMessage(messageID);
            results.first();
            Integer toUserID = (Integer) results.getObject("toUserID");
            Integer fromUserID = (Integer) results.getObject("fromUserID");
            Boolean fromUserDeleted = (Boolean) results.getObject("fromUserDeleted");
            Boolean toUserDeleted = (Boolean) results.getObject("toUserDeleted");

            if (toUserID.equals(fromUserID)) conn.deleteMessage(messageID);
            else if (toUserID.equals(userID)) {
                if (fromUserDeleted) conn.deleteMessage(messageID);
                else conn.toUserDeleteMessage(messageID);
            } else {
                if (toUserDeleted) conn.deleteMessage(messageID);
                else conn.fromUserDeleteMessage(messageID);
            }
            req.setAttribute("navtab", "inbox");
            req.setAttribute("messageUpdate", "Message deleted successfully");
            req.getRequestDispatcher("/ReadMessagesServlet").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}