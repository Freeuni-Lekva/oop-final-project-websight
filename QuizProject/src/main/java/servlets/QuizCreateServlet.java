package servlets;

import model.Achievement;
import model.DBConnection;
import model.QuestionPackage.*;
import model.Quiz;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class QuizCreateServlet extends HttpServlet {
    private static final String RANDOM = "random";
    private static final String ONEPAGE = "onePage";
    private static final String PRACTICE = "practiceMode";
    private static final String IMMEDIATECORR = "immediateCorrection";
    private static final String Q_QR = "questionResponse";
    private static final String Q_FB = "fillInBlank";
    private static final String Q_MC = "multipleChoice";
    private static final String Q_PR = "pictureResponse";
    @Override
    protected void doGet(HttpServletRequest request,     HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/createQuiz.jsp");
        rd.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String origin = request.getParameter("origin");
        if (origin.equals("CreateQuiz")){
            createQuizPage(request, response);
        } else if (origin.equals("NextQuestion")){
            nextQuestionPage(request, response);
        } else if (origin.equals("CreateQuestionQR")) {
            createQRQuestionPage(request,response);
        } else if (origin.equals("CreateQuestionFB")){
            createFBQuestionPage(request,response);
        } else if(origin.equals("CreateQuestionMC")){
            createMCQuestionPage(request,response);
        } else if (origin.equals("CreateQuestionPR")){
            createPRQuestionPage(request, response);
        } else {
            forwardToJSP(request,response,"CreateQuiz");
        }
    }

    private void createQuizPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Quiz quiz = null;
        try {
            quiz = newQuiz(request);
        } catch (SQLException e) {
            forwardToJSP(request, response, "error.jsp");
            return ;
        }
        if (quiz == null){
            forwardToJSP(request, response, "error.jsp");
            return ;
        }
        request.getSession().setAttribute("Quiz", quiz);
        nextQuestion(request, response, true);
    }

    private void nextQuestionPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionType = request.getParameter("questionType");
        if (questionType.equals(Q_QR)) {
            forwardToJSP(request, response, "CreateQuizQR.jsp");
        }
        else if (questionType.equals(Q_FB)) {
            forwardToJSP(request, response, "CreateQuizFB.jsp");
        }
        else if (questionType.equals(Q_MC)) {
            forwardToJSP(request, response, "CreateQuizMC.jsp");
        }
        else if (questionType.equals(Q_PR)) {
            forwardToJSP(request, response, "CreateQuizPR.jsp");
        }
        else {
            Quiz quiz = (Quiz) request.getSession().getAttribute("Quiz");
            String quizName = quiz.getQuizName();
            request.getSession().removeAttribute("quiz");
            User user = (User) request.getSession().getAttribute("user");
            DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");
            try {
                Achievement.getAchivment(user.getUserID(), dbConnection);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                forwardToJSP(request, response, "CreateQuizPR.jsp");
                return;
            }
            request.setAttribute("alert", "Your quiz " + quizName + " was created successfully!");
            forwardToJSP(request, response, "quizSearch.jsp");
        }
    }

    private void createQRQuestionPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionText = request.getParameter("question");
        Question question = null;
        try {
            question = createQuestion(request, questionText, Question.QTYPE_QR);
        } catch (SQLException e) {
            e.printStackTrace();
            forwardToJSP(request, response, "CreateQuiz.jsp");
            return;
        }

        String answerText = request.getParameter("response");
        if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
            answerText = answerText + "~" + request.getParameter("otherResponses");
        }
        try {
            createAnswer(request, answerText, question);
        } catch (SQLException e) {
            forwardToJSP(request, response, "CreateQuiz.jsp");
            return;
        }

        nextQuestion(request, response, false);
    }


    private void createFBQuestionPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionPreText = request.getParameter("pre");
        String questionPostText = request.getParameter("post");
        String questionText = questionPreText + " _________ " + questionPostText;

        Question question = null;
        try {
            question = createQuestion(request, questionText, Question.QTYPE_FB);
        } catch (SQLException e) {
            e.printStackTrace();
            forwardToJSP(request,response,"CreateQuiz.jsp");
            return;
        }

        String answerText = request.getParameter("blank");
        if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
            answerText = answerText + "~" + request.getParameter("otherResponses");
        }

        try {
            createAnswer(request, answerText, question);
        } catch (SQLException e) {
            forwardToJSP(request,response,"CreateQuiz.jsp");
            return;
        }

        nextQuestion(request, response, false);
    }

    private void createMCQuestionPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionText = request.getParameter("question");
        Question question = null;
        try {
            question = createQuestion(request, questionText, Question.QTYPE_MC);
        } catch (SQLException e) {
            e.printStackTrace();
            forwardToJSP(request,response,"CreateQuiz.jsp");
            return;
        }
        String answerText = request.getParameter("mc_correct") +
                "~!" + request.getParameter("mc1") +
                "~!" + request.getParameter("mc2") +
                "~!" + request.getParameter("mc3");
        try {
            createAnswer(request, answerText, question);
        } catch (SQLException e) {
            e.printStackTrace();
            forwardToJSP(request,response,"CreateQuiz.jsp");
            return;
        }
        nextQuestion(request, response, false);
    }

    private void createPRQuestionPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String questionText = request.getParameter("question");
        Question question = null;
        try {
            question = createQuestion(request, questionText, Question.QTYPE_PR);
        } catch (SQLException e) {
            e.printStackTrace();
            forwardToJSP(request,response,"CreateQuiz.jsp");
            return;
        }
        String answerText = request.getParameter("response");
        if (request.getParameter("otherResponsesCheck") != null && request.getParameter("otherResponsesCheck").equals("yes")) {
            answerText = answerText + "~" + request.getParameter("otherResponses");
        }

        try {
            createAnswer(request, answerText, question);
        } catch (SQLException e) {
            e.printStackTrace();
            forwardToJSP(request,response,"CreateQuiz.jsp");
            return;
        }
        nextQuestion(request, response, false);
    }

    private void forwardToJSP(HttpServletRequest request, HttpServletResponse response, String jspName) throws ServletException, IOException {
        String path = "WEB-INF/" + jspName;
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
    }

    private Quiz newQuiz(HttpServletRequest request) throws SQLException {
        String quizName = (String) request.getParameter("quizName");
        ServletContext servletContext = this.getServletContext();
        DBConnection dbConnection = (DBConnection) servletContext.getAttribute("DBConnection");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || dbConnection == null) {
            return null;
        }
        int userID = user.getUserID();
        String[] checkBoxes = request.getParameterValues("quizParams");
        boolean singlePage = false;
        boolean randomOrder = false;
        boolean immediateCorrection = false;
        boolean practiceMode = false;
        if (checkBoxes != null && checkBoxes.length > 0) {
            for (int i = 0; i < checkBoxes.length; i++ ) {
                if (checkBoxes[i].equals(RANDOM)) {
                    randomOrder = true;
                }
                else if (checkBoxes[i].equals(ONEPAGE)) {
                    singlePage = true;
                }
                else if (checkBoxes[i].equals(PRACTICE)) {
                    practiceMode = true;
                }
                else if (checkBoxes[i].equals(IMMEDIATECORR)){
                    immediateCorrection = true;
                }
            }
        }
        return new Quiz(quizName, userID, singlePage,randomOrder,immediateCorrection, practiceMode, dbConnection);
    }

    private void nextQuestion(HttpServletRequest request, HttpServletResponse response, boolean isFirst) throws ServletException, IOException {
        StringBuilder options = new StringBuilder();
        if (!isFirst) {
            options.append("<option value=\"done\">No More Questions!</option>");
        }
        options.append(
                "<option value=\"" + Q_QR + "\">Question Response Question</option>" +
                        "<option value=\"" + Q_MC + "\">Multiple Choice Question</option>" +
                        "<option value=\"" + Q_FB + "\">Fill-In-The-Blank Question</option>" +
                        "<option value=\"" + Q_PR + "\">Picture Response Question</option>");
        request.setAttribute("options", options.toString());
        request.getRequestDispatcher("askForNextQuestion.jsp").forward(request, response);
    }

    private void createAnswer(HttpServletRequest request, String answerText, Question question) throws SQLException {
        DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");

        Quiz quiz = (Quiz) request.getSession().getAttribute("Quiz");

        String[] multiAnswers = answerText.split("~");
        for (String thisAnswer : multiAnswers) {
            dbConnection.addAnswerToDB(thisAnswer,  quiz.getQuizID(), question.getQuestionID());
        }
        question.addAnswers(dbConnection);
    }

    private Question createQuestion(HttpServletRequest request, String questionText, int qType) throws SQLException {
        DBConnection dbConnection = (DBConnection) this.getServletContext().getAttribute("DBConnection");
        Quiz quiz = (Quiz) request.getSession().getAttribute("Quiz");
        int questionID = dbConnection.addQuestionToDB(questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID());
        Question question = null;
        if (qType == Question.QTYPE_QR )
            question = new QRQuestion(questionID, questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
        else if( qType == Question.QTYPE_FB)
            question = new FBQuestion(questionID, questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
        else if( qType == Question.QTYPE_MC)
            question = new MCQuestion(questionID, questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
        else if( qType == Question.QTYPE_PR)
            question = new PRQuestion(questionID, questionText, qType, quiz.getNextQuestionNum(), quiz.getQuizID(), dbConnection);
        else
            return null;
        quiz.addQuestion(question);
        return question;
    }
}
