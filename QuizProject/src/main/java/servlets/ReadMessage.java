package servlets;

import model.DBConnection;
import model.User;
import model.Message;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Collections;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class ReadMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReadMessage() { super(); }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            HttpSession ses = req.getSession();
            ServletContext cont = getServletContext();
            DBConnection conn = (DBConnection) cont.getAttribute("DBConnection");
            String tmp = req.getParameter("navtab");
            User user = (User) ses.getAttribute("user");
            Set<Integer> valids = new HashSet<Integer>() {{
                add(Message.NOTE);
                add(Message.FRIEND);
                add(Message.CHALLENGE);
            }};
            if ( user.isAdmin() ) {
                valids.add(Message.ANNOUNCEMENT);
                valids.add(Message.QUIZ_FLAG);
            }
            ResultSet all = conn.getUserMessages(user.getUserID());
            ArrayList<Message> f;
            if (("inbox").equals(tmp) || tmp == null) {
                req.setAttribute("navtab", "inbox");
                f = Message.loadMessages(all, valids, user.getUserID(), null);
            } else if (("sent").equals(tmp)){
                req.setAttribute("navtab", "sent");
                f = Message.loadMessages(all, valids, null, user.getUserID());
            } else throw new Exception();
            Collections.sort(f);
            req.setAttribute("messages", f);
            req.getRequestDispatcher("userMessages.jsp").forward(req, res);
        } catch (NullPointerException nullUser) {
            req.getRequestDispatcher("userMessages.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(req, response);
    }
}
