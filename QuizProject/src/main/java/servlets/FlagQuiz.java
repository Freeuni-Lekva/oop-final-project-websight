package servlets;

import java.io.IOException;
import java.util.Set;
import javax.servlet.http.HttpSession;

import model.QuizFlag;
import model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;
import model.DBConnection;

public class FlagQuiz extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FlagQuiz() {super();}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            HttpSession ses = req.getSession();
            ServletContext cont = getServletContext();
            DBConnection conn = (DBConnection) cont.getAttribute("DBConnection");
            User user = (User) ses.getAttribute("user");
            String UName = user.getUserName();
            int UID = user.getUserID();
            int QID = Integer.parseInt( req.getParameter("quizID") );
            String QName = req.getParameter("quizName");
            Set<Integer> IDs = conn.getAdminUserIDsSet();
            for (Integer currAdminID : IDs) {
                QuizFlag.makeQuizFlag(QID, QName, UID, currAdminID, UName, conn);
            }

            req.setAttribute("flagNote", "Flagged");
            req.getRequestDispatcher("quizSummary.jsp?quizID=" + QID).forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
}
