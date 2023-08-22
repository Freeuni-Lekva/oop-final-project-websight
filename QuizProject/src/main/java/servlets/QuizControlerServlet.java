package servlets;

import model.DBConnection;
import model.Quiz;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


public class QuizControlerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String practice = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ServletContext context  = getServletContext();
            DBConnection connection = (DBConnection) context.getAttribute("DBConnection");
            HttpSession session     = request.getSession();
            Quiz currQuiz           = (Quiz) session.getAttribute("quiz");

            quizConf(request, session, connection, currQuiz);

            if (currQuiz.getSinglePage()) {
                forSinglePage(request, response, session, currQuiz);
            } else {
                forWholePage(request, response, session, currQuiz);
            }
        } catch (Exception e) {
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void quizConf(HttpServletRequest request, HttpSession session, DBConnection connection,  Quiz currQuiz) throws SQLException {
        if (currQuiz == null) {
            Integer quizID = Integer.parseInt(request.getParameter("quizID"));
            currQuiz = new Quiz(quizID, connection);
            session.setAttribute("quiz", currQuiz);
            currQuiz.startQuiz();
            practice = request.getParameter("practice");
            if (practice != null) {
                currQuiz.turnOnPracticeMode();
            }
        } else {
            if (currQuiz.getSinglePage()) {
                currQuiz.sendAllAnswers(request);
            } else {
                currQuiz.sendAnswers( request );
            }
        }
    }

    private void forSinglePage(HttpServletRequest request,  HttpServletResponse response, HttpSession session, Quiz currQuiz) throws ServletException, IOException {
        if ( currQuiz.hasMoreHTML() ) {
            String html = "<p>Single page quiz:</p>";
            while (currQuiz.hasMoreHTML()) {
                html += currQuiz.getNextHTML();
            }
            request.setAttribute("html", html);
            forwardToJSP(request, response, "quizLive.jsp");
        } else {
            session.setAttribute("quiz", null);
            request.setAttribute("quiz", currQuiz);
            forwardToJSP(request, response, "quizResult.jsp");
        }
    }

    private void forWholePage(HttpServletRequest request,  HttpServletResponse response, HttpSession session, Quiz currQuiz) throws ServletException, IOException {
        if (currQuiz.hasMoreHTML() ) {
            String html = currQuiz.getNextHTML();
            request.setAttribute("html", html);
            forwardToJSP(request, response, "quizLive.jsp");
        } else {
            session.setAttribute("quiz", null);
            request.setAttribute("quiz", currQuiz);
            forwardToJSP(request, response, "quizResults.jsp");
        }
    }

    private void forwardToJSP(HttpServletRequest request, HttpServletResponse response, String jspName) throws ServletException, IOException {
        String path = "WEB-INF/" + jspName;
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
    }
}
