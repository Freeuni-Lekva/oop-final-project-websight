package DAO;

import model.Answer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AnswerDAO {
    private Connection connection;

    public AnswerDAO(Connection connection) {
        this.connection = connection;
    }

    public Answer getAnswerById(int answerId){

    }

    public List<Answer> getAnswers(){
        return null;
    }

    private Answer convertToAnswer(ResultSet resultSet) throws SQLException {
        Answer answer = new Answer();
        answer.setAnswerId(resultSet.getInt("answerID"));
        answer.setText(resultSet.getString(""));
        return answer;
    }
}
