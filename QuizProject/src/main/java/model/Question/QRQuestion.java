package model.Question;

import model.Answer.QRAnswer;
import model.DBConnection;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class QRQuestion extends Question{

    public QRQuestion(int questionId, String questionText, int questionType, int questionNum, int quizId, DBConnection db) throws SQLException {
        super(questionId, questionText, questionType, questionNum, quizId, db);
    }

    @Override
    public void addAnswers(DBConnection db) throws NumberFormatException, SQLException {
        answers = new QRAnswer(questionID, db);
    }
}
