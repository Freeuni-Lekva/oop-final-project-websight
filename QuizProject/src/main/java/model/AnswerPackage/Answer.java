package model.AnswerPackage;

import model.DBConnection;

import java.sql.*;
import java.util.*;

public abstract class Answer {
    protected List<String> answers;
    protected final int qsID;
    protected int numAnswers;
    private int[] answerIds;
    protected ArrayList<String> locations;

    public Answer(int qsID, DBConnection db) throws NumberFormatException, SQLException {
        this.qsID = qsID;
        answers = new ArrayList<String>();
        ResultSet rs = db.getAnswerByQuestionIDFromDB(qsID);
        rs.last();
        numAnswers = rs.getRow();
        answerIds = new int[numAnswers];
        locations = new ArrayList<String>();
        rs.first();

        for (int i = 0; i < numAnswers; i++) {
            answerIds[i] = rs.getInt("answerId");
            answers.add(rs.getString("answer"));
            rs.next();
        }
    }

    public abstract int checkAnswers(List<String> answers);

    public abstract String getAnswersHTML();

    public abstract int possiblePoints();

    public ArrayList<String> getAnswerLocations() {
        return locations;
    }

    public String showAnswerHTML() {
        if (answers == null) return null;
        if (answers.size() == 0) {
            return "<p></p>";
        }
        String html = "<p>" + answers.get(0) + "</p>";
        return html;
    }

    public String showAnswerOptionsHTML() {
        String html = "";
        if (answers.size() > 1) {
            html += "<h5>Correct answers are: ";
        } else if (answers.size() == 1) {
            html += "<h5>Correct answer is: ";
        }
        boolean first = true;
        for (String answer : answers) {
            if (!first) {
                html += ", ";
            }
            html += answer;
            first = false;
        }
        html += "</h5>";
        return html;
    }
}
