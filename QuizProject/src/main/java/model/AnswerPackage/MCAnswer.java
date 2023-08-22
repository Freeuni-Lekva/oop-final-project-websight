package model.AnswerPackage;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import model.DBConnection;

public class MCAnswer extends Answer{
    String correctAnswer = "";

    public MCAnswer(int questionId, DBConnection db)
            throws NumberFormatException, SQLException {
        super(questionId, db);
        for (int i = 0; i < numAnswers; i++) {
            String answer = answers.get(i);
            if (!answer.isEmpty()) {
                if (answer.charAt(0) == '!') {
                    answer = answer.substring(1);
                    answers.set(i, answer);
                } else {
                    correctAnswer = answer;
                }
            }
        }
    }

    @Override
    public int checkAnswers(List<String> answers) {
        int score = 0;
        for (String answer : answers) {
            if (correctAnswer.equals(answer)) {
                score = possiblePoints();
            }
        }
        return score;
    }

    @Override
    public String getAnswersHTML() {
        String html = "";
        Collections.shuffle(answers);
        for (String answer : answers) {
            String location = "choices-" + qsID;
            locations.add(location);
            html += "<p><input type='radio' name='" + location + "' value='" + answer + "'>  " + answer + "</input></p>";
        }
        return html;
    }

    @Override
    public String showAnswerOptionsHTML() {
        String html = "<p>Correct answer is: ";
        html += correctAnswer;
        html += "</p>";
        return html;
    }

    @Override
    public String showAnswerHTML() {
        String html = "<p>" + correctAnswer + "</p>";
        return html;
    }

    @Override
    public int possiblePoints() {
        return 1;
    }
}
