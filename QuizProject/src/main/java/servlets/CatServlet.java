package servlets;

import model.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class CatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public CatServlet() {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String quizIDText = (String) request.getAttribute("quizID");
        if (quizIDText == null) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("homepage.jsp");
            requestDispatcher.forward(request, response);
            return;
        }
        int quizID = Integer.parseInt(quizIDText);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
        requestDispatcher.forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");
        int catID = Integer.parseInt(request.getParameter("catAdd"));
        int quizID = Integer.parseInt(request.getParameter("quizID"));
        if (catID < 1) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
            requestDispatcher.forward(request, response);
            return;
        }
        try {
            dbConnection.addCat(quizID, catID);
        } catch (SQLException e) {
            e.printStackTrace();
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
            requestDispatcher.forward(request, response);
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("quizSummary.jsp?quizID=" + quizID);
        requestDispatcher.forward(request, response);

    }
}
