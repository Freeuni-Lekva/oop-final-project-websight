package model.DAO;

import model.Question;
import model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuizDAO {
    private Connection connection;

    public QuizDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addQuizToDB(Quiz quiz) throws SQLException {
        String query = "INSERT INTO quizzes (quizID, authorID, title, description, date, isRandomOrder, isOnePage, isImmediateCorrection)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, quiz.getQuizID());
        statement.setInt(2, quiz.getAuthorID());
        statement.setString(3, quiz.getTitle());
        statement.setString(4,quiz.getDescription());
        statement.setDate(5, new java.sql.Date(quiz.getCreationDate().getTime()));
        statement.setBoolean(6, quiz.isRandomOrderQuestions());
        statement.setBoolean(7, quiz.isOnePage());
        statement.setBoolean(8, quiz.isImmediateCorrection());

        for(Question question : quiz.getQuestions()){
            QuestionDAO qsDAO = new QuestionDAO(connection);
            qsDAO.addQuestionToDB(question);
        }
        return statement.execute();
    }

    public Quiz getQuizByIDFromDB(int quizID) throws SQLException {
        String query = "SELECT * FROM quizzes where quizID = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, quizID);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            return convertToQuiz(result);
        }

        return null;
    }

    public ArrayList<Quiz> getQuizzesByTitleFromDB(String title) throws SQLException {
        if (title != null) {
            return getQuizListByParamIndex("authorID", -1, title);
        } else  {
            return null;
        }
    }
    public ArrayList<Quiz> getQuizzesByAuthorIDFromDB(int authorID) throws SQLException {
        return getQuizListByParamIndex("authorID", authorID, null);
    }

    private ArrayList<Quiz> getQuizListByParamIndex(String paramNameInTable, int ParamIndexValueINT, String ParamIndexValueSTR) throws SQLException {
        ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
        String query = "SELECT * FROM"+
                paramNameInTable  + "where authorID = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        if (ParamIndexValueSTR == null){
            statement.setInt(1, ParamIndexValueINT);
        } else {
            statement.setString(1, ParamIndexValueSTR);
        }
        ResultSet result = statement.executeQuery();
        while (result.next()){
            quizzes.add(convertToQuiz(result));
        }

        return quizzes;
    }

    private Quiz convertToQuiz(ResultSet result) throws SQLException {
        Quiz quiz = new Quiz();
        quiz.setQuizID(result.getInt("quizID"));
        quiz.setAuthorID(result.getInt("authorID"));
        quiz.setTitle(result.getString("title"));
        quiz.setDescription(result.getString("description"));
        quiz.setCreationDate(new java.util.Date(result.getDate("creationDate").getTime()));
        quiz.setRandomOrderQuestions(result.getBoolean("isRandomOrder"));
        quiz.setOnePage(result.getBoolean("isOnePage"));
        quiz.setImmediateCorrection(result.getBoolean("isImmediateCorrection"));
        QuestionDAO qsDAO = new QuestionDAO(connection);
        quiz.setQuestions(qsDAO.getQuestionsByQuizIDFromDB(quiz.getQuizID()));

        return quiz;
    }
}
