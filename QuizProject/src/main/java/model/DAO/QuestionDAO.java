package model.DAO;


import model.Answer;
import model.Question;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class QuestionDao {
    private static final int QUESTION_RESPONSE = 1;
    private static final int FILL_BLANK = 2;
    private static final int MULTIPLE_CHOICE = 3;
    private static final int PICTURE_RESPONSE = 4;
    private Connection connection;


    public QuestionDao(Connection connection) {
        this.connection = connection;
    }


    public Question getQuestionByIDFromDB(int questionsID) throws SQLException {
        String query = "SELECT * FROM questions where questionID = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, questionsID);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            return convertToQuestion(result);
        }


        return null;
    }


    public List<Question> getQuestionsFromDB() throws SQLException {
        List<Question> questions = new ArrayList<>();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM questions;";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            questions.add(convertToQuestion(resultSet));
        }


        return questions;
    }


    public List<Question> getQuestionsFromDB(int quizID) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE quizID = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, quizID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            questions.add(convertToQuestion(resultSet));
        }


        return questions;
    }


    private Question convertToQuestion(ResultSet result) throws SQLException {
        Question question = new Question();
        Answer correctAnswer;


        int type = result.getInt("questionType");
        question.setQuestionID(result.getInt("questionID"));
        question.setQuestionText(result.getString("text"));
        question.setQuizID(result.getInt("quizID"));
        question.setQuestionType(type);


        if (type == MULTIPLE_CHOICE){
            List<Answer> probableAnswers = new ArrayList<Answer>();
            int firstProbableAnswerIndex = result.getInt("firstProbableAnswerID");
            int secondProbableAnswerIndex = result.getInt("secondProbableAnswerID");
            int thirdProbableAnswerIndex = result.getInt("thirdProbableAnswerID");
            AnswerDAO answerDAO = new AnswerDAO(connection);
            correctAnswer = answerDAO.getAnswerByIDFromDB(firstProbableAnswerIndex);
            probableAnswers.add(correctAnswer);
            probableAnswers.add(answerDAO.getAnswerByIDFromDB(secondProbableAnswerIndex));
            probableAnswers.add(answerDAO.getAnswerByIDFromDB(thirdProbableAnswerIndex));
        }


        return question;
    }
}

