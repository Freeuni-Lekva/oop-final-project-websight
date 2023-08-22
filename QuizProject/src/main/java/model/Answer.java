package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Answer {
    private final ArrayList<String> answers;
    private final int questionId;
    private int numAnswers;

    public Answer(int questionId, DBConnection connection) throws SQLException {
        this.questionId = questionId;
        answers = new ArrayList<>();
        ResultSet rs = connection.getQuizAnswer(questionId);
        rs.last();
        numAnswers = rs.getRow();
        rs.next();

        for (int i = 0; i < numAnswers; i++) {
            answers.add(rs.getString("answer"));
            rs.next();
        }
    }

    public String showAnswer() {
        if (answers == null) return null;
        if (answers.size() == 0) {
            return "<p></p>";
        }
        String html = "<p>" + answers.get(0) + "</p>";
        return html;
    }

    public String showAnswers() {
        String html = "";
        if (answers.size() == 1) {
            html += "<h5>Correct answer is: ";
        } else if (answers.size() > 1) {
            html += "<h5>Correct answers are: ";
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
