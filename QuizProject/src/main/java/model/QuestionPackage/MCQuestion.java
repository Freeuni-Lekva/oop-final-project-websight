package model.Question;

import model.Answer.MCAnswer;
import model.DBConnection;

import java.sql.SQLException;

public class MCQuestion extends Question{
    public MCQuestion(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
        super(questionId, questionText, questionType, questionNum, quizId, db);
    }

    @Override
    public void addAnswers(DBConnection db)
            throws NumberFormatException, SQLException {
        answers = new MCAnswer(questionID, db);
    }
}
