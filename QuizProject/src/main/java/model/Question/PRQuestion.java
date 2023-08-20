package model.Question;

import model.Answer.PRAnswer;
import model.DBConnection;

import java.sql.SQLException;

public class PRQuestion extends Question{
    public PRQuestion(int questionID, String questionText, int questionType, int questionNum, int quizID, DBConnection db) throws SQLException {
        super(questionID, questionText, questionType, questionNum, quizID, db);
    }

    @Override
    public void addAnswers(DBConnection db)
            throws NumberFormatException, SQLException {
        answers = new PRAnswer(questionID, db);
    }

    @Override
    public String showQuestionWithAnswers() {
        String html = "<p>Identify the picture:</p>";
        html += ("<p><img src='" + questionText + "' alt='Picture not found' width='200'/></p>");
        html += answers.getAnswersHTML();
        return html;
    }

    @Override
    public String showQuestionText() {
        String html = "<p>Identify the picture:</p>";
        html += ("<p><img src='" + questionText + "' alt='Picture not found' width='200'/></p>");
        return html;
    }
}
