package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import model.QuestionPackage.FBQuestion;
import model.QuestionPackage.MCQuestion;
import model.QuestionPackage.PRQuestion;
import model.QuestionPackage.QRQuestion;
import model.QuestionPackage.Question;

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
        quizInfo.next();
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
    public Quiz(String quizName, int quizCreatorUserID, boolean singlePage, boolean randomOrder,
                boolean immediateCorrection, boolean practiceMode, DBConnection connection) throws SQLException {
        this.connection = connection;
        this.quizName = quizName;
        this.quizCreation = null;
        this.quizCreatorUserID = quizCreatorUserID;
        this.singlePage = singlePage;
        this.randomOrder = randomOrder;
        this.immediateCorrection = immediateCorrection;
        this.allowsPracticeMode = practiceMode;
        this.quizID = -1;
        questionList = new ArrayList<>();

        connection.addQuizToDB(this);

        populateQuestions();
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizName='" + quizName + '\'' +
                ", quizCreation=" + quizCreation +
                ", quizCreatorUserID=" + quizCreatorUserID +
                ", singlePage=" + singlePage +
                ", randomOrder=" + randomOrder +
                ", immediateCorrection=" + immediateCorrection +
                ", allowsPracticeMode=" + allowsPracticeMode +
                ", practiceModeOn=" + practiceModeOn +
                ", connection=" + connection +
                ", questionList=" + questionList +
                ", allQuestions=" + allQuestions +
                ", questionOrder=" + questionOrder +
                ", quizID=" + quizID +
                ", score=" + score +
                ", possibleScore=" + possibleScore +
                ", startTime=" + startTime +
                ", question=" + question +
                ", currQuestion=" + currQuestion +
                ", rand=" + rand +
                ", tempScore=" + tempScore +
                '}';
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

    private Question getQuestionObject(int questionID, String questionText, int questionType, int questionNum)
            throws SQLException {
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
        while (questions.next()) {
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
        } else if (!immediateCorrection) {
            result += "The responses will be shown once the quiz concludes.\n";
        }
        return result;
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

    public String getResult(int userID) throws SQLException {

        if (!connection.addQuizHistory(quizID, userID, score)) {
            throw new SQLException();
        }

        String result =
                "<h3 style='color:#d9534f'> You scored " + score + " out of a possible " + possibleScore + " points.</h3>";
        long end = System.currentTimeMillis();
        long elapsed = end - startTime;
        long hours = TimeUnit.MILLISECONDS.toHours(elapsed);
        long minutes =
                TimeUnit.MILLISECONDS.toMinutes(elapsed) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsed));
        long seconds =
                TimeUnit.MILLISECONDS.toSeconds(elapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed));
        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        result += "<h3 style='color:#d9534f'> You took " + time + " to complete the quiz.</h3>";
        return result;
    }

    public double getRating() throws SQLException {
        int numValues = 0;
        int num = 0;
        ResultSet ratings = connection.getRatings(quizID);
        while (ratings.next()) {
            num += ratings.getInt("ratingValue");
            numValues++;
        }
        return numValues == 0 ? 0 : num / (double) numValues;
    }

    public Integer getUserStanding(int userID) throws SQLException {
        int currUserID;
        Integer rating = null;

        ResultSet ratings = connection.getRatings(quizID);
        while (ratings.next()) {
            currUserID = ratings.getInt("userID");
            if (userID == currUserID) {
                rating = ratings.getInt("ratingValue");
            }
        }
        return rating;
    }

    public ArrayList<String> getTags() throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        ResultSet tags = connection.getTags(quizID);
        while (tags.next()) {
            result.add(tags.getString("tagName"));
        }
        return result;
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

    public void setQuizCreation(Date quizCreation) {
        this.quizCreation = quizCreation;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public Integer getUserRating(int userID) throws SQLException {
        int curr;
        Integer rat = null;
        ResultSet ratings = connection.getRatings(quizID);
        while (ratings.next()) {
            curr = ratings.getInt("userID");
            if (userID == curr) {
                rat = ratings.getInt("ratingValue");
            }
        }
        return rat;
    }

    public Integer getUserRatingID(int userID) throws SQLException {
        int curr;
        Integer ratID = null;
        ResultSet ratings = connection.getRatings(quizID);
        while (ratings.next()) {
            curr = ratings.getInt("userID");
            if (userID == curr) {
                ratID = ratings.getInt("ratingID");
            }
        }
        return ratID;
    }
}
