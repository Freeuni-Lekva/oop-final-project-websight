package servlets;

import model.DBConnection;
import model.Quiz;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RatingQuiz extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RatingQuiz() { super(); }

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            DBConnection conn = (DBConnection) getServletContext().getAttribute("DBConnection");
            int QID = Integer.parseInt( request.getParameter("quizID") );
            int UID = Integer.parseInt( request.getParameter("userID") );
            int rat = Integer.parseInt( request.getParameter("rating") );
            Quiz quiz = new Quiz(QID, conn);
            Integer ratID = quiz.getUserRatingID(UID);
            if (ratID == null) conn.addQuizRating(QID, UID, rat);
            else conn.updateQuizRating(ratID, rat);
            request.getRequestDispatcher("quizSummary.jsp").forward(request, response);
        }  catch (Exception e) {
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
