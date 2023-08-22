package model;

import model.QuestionPackage.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Quiz {

    private static final int QUESTION_RESPONSE = 1;
    private static final int FILL_BLANK = 2;
    private static final int MULTIPLE_CHOICE = 3;
    private static final int PICTURE_RESPONSE = 4;

    private String quizName;
    private Date quizCreation;
    private int quizCreatorUserID;
    private boolean singlePage;
    private boolean randomOrder;
    private boolean immediateCorrection;
    private boolean allowsPracticeMode;
    private boolean practiceModeOn;


    private DBConnection connection;
    private ArrayList<Question> questionList;
    private ArrayList<Question> allQuestions;

    private ArrayList<Question> questionOrder;
    private int quizID;
    private int score;
    private int possibleScore;
    private long startTime;
    private boolean question;
    private Question currQuestion;
    private Random rand = new Random();
    private int tempScore = 0;



    public Quiz(int quizID, DBConnection connection) throws SQLException {
        this.connection = connection;
        this.quizID = quizID;
        ResultSet quizInfo = connection.getQuizFromDB(quizID);
        quizInfo.first();
        quizName = quizInfo.getString("quizName");
        quizCreation = quizInfo.getDate("quizCreation");
        quizCreatorUserID = quizInfo.getInt("quizCreatoruserID");
        singlePage = quizInfo.getBoolean("singlePage?");
        randomOrder = quizInfo.getBoolean("randomOrder?");
        immediateCorrection = quizInfo.getBoolean("immediateCorrection?");
        if (singlePage) {
            immediateCorrection = false;
        }
        questionList = new ArrayList<Question>();
        allQuestions = new ArrayList<Question>();

        ResultSet questions = connection.getQuestionsByQuizIDFromDB(quizID);
        questions.beforeFirst();
        while (questions.next()) {
            int questionID = questions.getInt("questionID");
            String questionText = questions.getString("question");
            int questionType = questions.getInt("questionTypeID");
            int questionNum = questions.getInt("questionNumber");
            Question currQuestion = getQuestionObject(questionID, questionText, questionType, questionNum);
            questionList.add(currQuestion);
            allQuestions.add(currQuestion);
        }
    }

    /*Use it if is create Quiz**/
    public Quiz(String quizName, int quizCreatorUserID, boolean singlePage, boolean randomOrder, boolean immediateCorrection, boolean practiceMode, DBConnection connection) throws SQLException {
        this.connection = connection;
        this.quizName = quizName;
        this.quizCreation = null;
        this.quizCreatorUserID = quizCreatorUserID;
        this.singlePage = singlePage;
        this.randomOrder = randomOrder;
        this.immediateCorrection = immediateCorrection;
        this.allowsPracticeMode = practiceMode;
        this.quizID = -1;
        questionList = new ArrayList<Question>();

        connection.addQuizToDB(this);

        populateQuestions();
    }

    private void populateQuestions() throws SQLException {
        ResultSet questions = connection.getQuestionsByQuizIDFromDB(quizID);
        while (questions.next()) {
            int questionID = questions.getInt("questionID");
            String questionText = questions.getString("question");
            int questionType = questions.getInt("questionType");
            int questionNum = questions.getInt("questionNum");
            Question currQuestion = getQuestionObject(questionID, questionText, questionType, questionNum);
            questionList.add(currQuestion);
        }
    }

    private Question getQuestionObject(int questionID, String questionText, int questionType, int questionNum) throws SQLException {
        Question q = null;
        switch (questionType) {
            case QUESTION_RESPONSE:
                q = new QRQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
                break;
            case FILL_BLANK:
                q = new FBQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
                break;
            case MULTIPLE_CHOICE:
                q = new MCQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
                break;
            case PICTURE_RESPONSE:
                q = new PRQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
                break;
            default:
                q = new QRQuestion(questionID, questionText, questionType, questionNum, this.quizID, this.connection);
        }
        return q;
    }

    public void startQuiz() {
        score = 0;
        possibleScore = 0;
        startTime = System.currentTimeMillis();
        question = true;
    }

    public boolean hasMoreHTML() {
        return !(question && questionList.size() == 0);
    }

    public String getNextHTML() {
        if (immediateCorrection) {
            boolean tempQuest = question;
            question = !question;
            if (tempQuest) {
                currQuestion = getNextQuestion();
                return currQuestion.showQuestionWithAnswers();
            } else {
                String html = "";
                int points = score - tempScore;
                if (points > 1) {
                    html += "<p>That is correct! You got " + points + " points.</p>";
                } else if (points == 1) {
                    html += "<p>That is correct! You got " + points + " point.</p>";
                } else {
                    html += "<p>That is incorrect.</p>";
                }
                html += currQuestion.showAnswerOptions();
                return html;
            }
        } else {
            currQuestion = getNextQuestion();
            return currQuestion.showQuestionWithAnswers();
        }
    }

    private Question getNextQuestion() {
        int index = randomOrder ? rand.nextInt(questionList.size()) : 0;
        Question result = questionList.get(index);
        questionList.remove(index);
        return result;
    }

    public void sendAllAnswers(HttpServletRequest request) {
        for (Question q : allQuestions) {
            ArrayList<String> locations = q.getAnswerLocations();
            ArrayList<String> submittedAnswers = new ArrayList<String>();
            for (int i = 0; i < locations.size(); i++) {
                submittedAnswers.add(request.getParameter(locations.get(i)));
            }
            int qScore = q.checkAnswers(submittedAnswers);
            score += qScore;
            possibleScore += q.possiblePoints();
        }
    }

    public void sendAnswers(HttpServletRequest request) {
        if (!immediateCorrection || !question) {
            ArrayList<String> locations = currQuestion.getAnswerLocations();
            ArrayList<String> submittedAnswers = new ArrayList<String>();
            for (int i = 0; i < locations.size(); i++) {
                submittedAnswers.add(request.getParameter(locations.get(i)));
            }
            int qScore = currQuestion.checkAnswers(submittedAnswers);
            tempScore = score;
            score += qScore;
            possibleScore += currQuestion.possiblePoints();
        }
    }

    public ArrayList<String> getCategories() throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        ResultSet categories = connection.getCategories(quizID);
        while (categories.next()) {
            result.add(categories.getString("categoryName"));
        }
        return result;
    }

    public int getQuestionsNum() throws SQLException {
        int num = 0;
        ResultSet questions = connection.getQuestionsByQuizIDFromDB(quizID);
        while ( questions.next() ) {
            num++;
        }
        return num;
    }

    public String getQuizSummary() {
        String result = quizName + " was created on " + quizCreation + ".\n"; //COULD ADD WHO CREATED IT
        result += "There are " + questionList.size() + " questions.\n";
        if (singlePage) {
            result += "All of the questions will be on the same page.\n";
        }
        if (randomOrder) {
            result += "The questions will be in random order mode.\n";
        }
        if (immediateCorrection && !singlePage) {
            result += "The answers will be displayed after question.\n";
        } else if (!immediateCorrection){
            result += "The responses will be shown once the quiz concludes.\n";
        }
        return result;
    }



    public void setQuizCreation(Date quizCreation) {
        this.quizCreation = quizCreation;
    }

    public int getNextQuestionNum() {
        return questionList.size() + 1;
    }

    public void addQuestion(Question question) {
        questionList.add(question);
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public int getScore() {
        return score;
    }

    public int getPossibleScore() {
        return possibleScore;
    }

    public boolean turnOnPracticeMode() {
        if (allowsPracticeMode) {
            practiceModeOn = true;
        } else {
            practiceModeOn = false;
        }
        return practiceModeOn;
    }

    public boolean isPracticeModeOn() {
        return practiceModeOn;
    }

    public int getMaxPoints() {
        int total = 0;
        for (Question q : questionList) {
            total += q.possiblePoints();
        }
        return total;
    }

    public ArrayList<Question> getAllQuestions() {
        return allQuestions;
    }

    public String getQuizName() {
        return quizName;
    }
    public int getquizCreatoruserID() {
        return quizCreatorUserID;
    }
    public boolean getSinglePage() {
        return singlePage;
    }
    public boolean getRandomOrder() {
        return randomOrder;
    }
    public boolean getImmediateCorrection() {
        return immediateCorrection;
    }
    public boolean getPractiveMode() {
        return allowsPracticeMode;
    }
    public Date getQuizCreation() {
        return quizCreation;
    }
    public int getQuizID() {
        return quizID;
    }
    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }
}
