package model.Question;

import model.Answer.FBAnswer;
import model.DBConnection;

import java.sql.SQLException;

public class FBQuestion extends Question{
    public FBQuestion(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
        super(questionId, questionText, questionType, questionNum, quizId, db);
    }

    @Override
    public void addAnswers(DBConnection db)
            throws NumberFormatException, SQLException {
        answers = new FBAnswer(questionID, db);
    }
}
