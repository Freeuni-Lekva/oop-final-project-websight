package model.QuestionPackage;

import model.DBConnection;
import model.AnswerPackage.Answer;
import java.sql.*;
import java.util.ArrayList;

public abstract class Question implements Comparable {
    public static final int QTYPE_QR = 1;
    public static final int QTYPE_FB = 2;
    public static final int QTYPE_MC = 3;
    public static final int QTYPE_PR = 4;

    final int questionID;
    String questionText;
    final int questionType;
    final int questionNum;
    final int quizID;
    Answer answers;

    public Question(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
        this.questionID = questionId;
        this.questionType = questionType;
        this.questionText = questionText;
        this.quizID = quizId;
        this.questionNum = questionNum;
        addAnswers(db);
    }

    public abstract void addAnswers(DBConnection db) throws NumberFormatException, SQLException;

    public String showQuestionWithAnswers() {
        String html = "<p>" + questionText + "</p>";
        html += answers.getAnswersHTML();
        return html;
    }

    public String showQuestionText() {
        String html = "<h5>" + questionText + "</h5>";
        return html;
    }

    public String showAnswerOptions() {
        return answers.showAnswerOptionsHTML();
    }

    public int getQuestionID() {
        return questionID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getQuestionType() {
        return questionType;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public int getQuizID() {
        return quizID;
    }
    public int checkAnswers(ArrayList<String> guesses) {
        return answers.checkAnswers(guesses);
    }

    public ArrayList<String> getAnswerLocations() {
        return answers.getAnswerLocations();
    }

    public String showAnswer() {
        return answers.showAnswerHTML();
    }
    public int possiblePoints() {
        return answers.possiblePoints();
    }
    @Override
    public int compareTo(Object o) {
        Question other = (Question)o;
        return questionNum - other.questionNum;
    }
}