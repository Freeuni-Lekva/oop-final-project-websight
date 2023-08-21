package model.Answer;

import model.DBConnection;

import java.sql.SQLException;
import java.util.List;

public class PRAnswer extends Answer{

    public PRAnswer(int questionId, DBConnection db)
            throws NumberFormatException, SQLException {
        super(questionId, db);
    }

    @Override
    public int checkAnswers(List<String> answers) {
        int score = 0;
        for (String answer : answers) {
            if (this.answers.contains(answer)) {
                score = possiblePoints();
            }
        }
        return score;
    }

    @Override
    public String getAnswersHTML() {
        String location = "answer-" + qsID;
        locations.add(location);
        String html = "<input type='text' name='" + location + "'/>";
        return html;
    }

    @Override
    public int possiblePoints() {
        return 1;
    }

}
