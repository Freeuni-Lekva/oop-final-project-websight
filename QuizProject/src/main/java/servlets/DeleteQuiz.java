package servlets;

import model.DBConnection;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteQuiz extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteQuiz() {super();}

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            ServletContext cont  = getServletContext();
            DBConnection conn = (DBConnection) cont.getAttribute("DBConnection");
            int QID = Integer.parseInt( req.getParameter("quizID") );
            String QName = conn.getQuizName(QID);
            conn.deleteQuiz(QID);
            req.setAttribute("alert", "Quiz " + QName + " was deleted");
            req.getRequestDispatcher("quizSearch.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
}
