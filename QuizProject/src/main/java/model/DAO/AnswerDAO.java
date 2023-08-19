package model.DAO;

import model.Answer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO {
    private Connection connection;

    public AnswerDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addAnswerToDB(Answer answer) throws SQLException{
        String query = "INSERT INTO answers (answerID, text)" +
                "VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, answer.getAnswerId());
        statement.setString(2, answer.getText());
        return statement.execute();
    }
    public Answer getAnswerByIDFromDB(int answerId) throws SQLException {
        String query = "SELECT * FROM answers WHERE answerID = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, answerId);
        ResultSet result = statement.executeQuery();

        while (result.next()){
            return convertToAnswer(result);
        }

        return null;
    }

    public List<Answer> getAnswersFromDB() throws SQLException {
        List<Answer> answers = new ArrayList<>();
        Statement statement = connection.createStatement();
        String query = "SELCT * FROM answers;";
        ResultSet result = statement.executeQuery(query);
        while (result.next()){
            answers.add(convertToAnswer(result));
        }
        return answers;
    }

    private Answer convertToAnswer(ResultSet resultSet) throws SQLException {
        Answer answer = new Answer();
        answer.setAnswerId(resultSet.getInt("answerID"));
        answer.setText(resultSet.getString("text"));
        return answer;
    }
}
