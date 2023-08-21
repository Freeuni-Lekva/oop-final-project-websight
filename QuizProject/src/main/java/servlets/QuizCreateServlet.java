import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/createServlet")
public class QuizCreateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,     HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet");
        RequestDispatcher rd = request.getRequestDispatcher("createQuiz.jsp");
        rd.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost");
    }
}
