package model;

import java.sql.*;
import java.util.ArrayList;

public class DBConnection {

    private String username = DBInfo.username;
    private String password = DBInfo.password;
    private String database_port = DBInfo.database_port;
    private String database_name = DBInfo.database_name;
    private String users_table = "users";
    private String quizzes_table = "quizzes";
    private String questions_table = "questions";
    private String answers_table = "answers";
    private String messages_table = "messages";
    private String friends_table = "friends";
    private String user_achievements_table = "achievements";
    private String user_types_table = "userTypes";
    private String user_ratings_table = "ratings";
    private String tags_table = "tags";
    private String quiz_history_table = "history";
    private String quiz_tags_table = "quizTags";
    private String categories_table = "categories";
    private String quiz_categories_table = "quizCategories";


    private Connection connection;

    public DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection("jdbc:mysql://" + database_port, username, password);
        PreparedStatement statement = connection.prepareStatement("USE " + database_name);
        statement.execute();
    } // try/catch

    public void close() throws SQLException {
        connection.close();
    } // try/catch

    public ResultSet getUser(int userID) throws SQLException {
        String str = "select * from " + users_table + " where userID = ?;";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setInt(1, userID);
        return statement.executeQuery();
    }

    public ResultSet getAllUsers() throws SQLException {
        String str = "select * from " + users_table + " ;";
        PreparedStatement statement = connection.prepareStatement(str);
        ResultSet rs = statement.executeQuery();
        return rs;
    }

    public int getUserID(String userName) throws SQLException {
        String str = "SELECT userID FROM users WHERE userName = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setString(1, userName);
        ResultSet rs = statement.executeQuery();
        rs.first();
        return rs.getInt(1);
    }

    public String getUserName(int userID) throws SQLException {
        String str = "SELECT userName FROM users WHERE userID = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setInt(1, userID);
        ResultSet rs = statement.executeQuery();
        rs.first();
        return rs.getString(1);
    }

    public String getPassword(String userName) throws SQLException {
        String str = "SELECT userID FROM users WHERE userName = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setString(1, userName);
        ResultSet rs = statement.executeQuery();
        rs.first();
        int userID = rs.getInt(1);

        String passStr = "SELECT password FROM users WHERE userID = ?";
        statement = connection.prepareStatement(passStr);
        statement.setInt(1, userID);
        rs = statement.executeQuery();
        if (!rs.first())
            return "";
        return rs.getString(1);
    }

    public int getUserType(int userID) throws SQLException {
        String str = "SELECT userType FROM users WHERE userID = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setInt(1, userID);
        ResultSet rs = statement.executeQuery();
        rs.first();
        return rs.getInt(1);
    }

    public boolean setUserName(int userID, String userName) throws SQLException {
        String str = "UPDATE " + users_table + " SET userName = ? "
                + "WHERE userID = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setInt(2, userID);
        statement.setString(1, userName);
        return statement.execute();
    }

    public boolean setPassword(int userID, String password) throws SQLException {
        String str = "UPDATE " + users_table + " SET password = ? "
                + "WHERE userID = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setInt(2, userID);
        statement.setString(1, password);
        return statement.execute();
    }

    public boolean validUserName(String userName) throws SQLException {
        String str = "SELECT COUNT(*) FROM " + users_table + " WHERE userName = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setString(1, userName);
        ResultSet rs = statement.executeQuery();
        rs.first();
        return rs.getInt(1) > 0;
    }

    public ResultSet searchUser(String userName) throws SQLException {
        String str = "SELECT * FROM " + users_table + " WHERE userName like ?;";
        PreparedStatement statement = connection.prepareStatement(str);
        userName = "%" + userName + "%";
        statement.setString(1, userName);
        return statement.executeQuery();
    }

    public ResultSet getUserMessages(int userID) throws SQLException {
        String str = "SELECT * FROM " + messages_table + " WHERE toUserID = ? OR fromUserID = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setInt(1, userID);
        statement.setInt(2, userID);
        return statement.executeQuery();
    }

    public boolean nameInUse(String userName) throws SQLException {
        String str = "SELECT COUNT(*) from " + users_table + " WHERE LOWER(userName) LIKE LOWER(?)";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setString(1, userName);
        ResultSet rs = statement.executeQuery();
        rs.first();
        return rs.getInt(1) > 0;
    }

    public boolean friendship(int userID1, int userID2) throws SQLException {
        String str = "INSERT INTO " + friends_table + " (userID, friendID) VALUES (?, ?)";
        PreparedStatement statement1 = connection.prepareStatement(str);
        PreparedStatement statement2 = connection.prepareStatement(str);
        statement1.setInt(1, userID1);
        statement1.setInt(2, userID2);
        statement2.setInt(1, userID2);
        statement2.setInt(2, userID1);
        return statement1.execute() & statement2.execute();
    }

    public boolean removeFriendship(int userID1, int userID2) throws SQLException {
        String str = "DELETE FROM " + friends_table + " WHERE userID = ?";
        PreparedStatement statement1 = connection.prepareStatement(str);
        PreparedStatement statement2 = connection.prepareStatement(str);
        statement1.setInt(1, userID1);
        statement2.setInt(1, userID2);
        return statement1.execute() & statement2.execute();
    }

    public boolean checkFriendship(int userID1, int userID2) throws SQLException {
        String str = "SELECT COUNT(*) FROM " + friends_table + " WHERE userID = ?";
        PreparedStatement friend1 = connection.prepareStatement(str);
        PreparedStatement friend2 = connection.prepareStatement(str);
        friend1.setInt(1, userID1);
        friend2.setInt(1, userID2);
        ResultSet rs1 = friend1.executeQuery();
        ResultSet rs2 = friend2.executeQuery();
        rs1.first();
        rs2.first();
        return (rs1.getInt(1) + rs2.getInt(1)) == 2;
    }

    public ArrayList<Integer> getFriends(int userID) throws SQLException {
        ArrayList<Integer> result = new ArrayList<Integer>();
        ResultSet rs = getAllUsers();
        while (rs.next()) {
            int friendID = rs.getInt("userID");
            if (friendID != userID && checkFriendship(userID, friendID)) {
                result.add(friendID);
            }
        }
        return result;
    }

    public ResultSet getUserAchievements(int userID) throws SQLException {
        String str = "SELECT * FROM " + user_achievements_table + " WHERE userID = ?";
        PreparedStatement statement = connection.prepareStatement(str);
        statement.setInt(1, userID);
        ResultSet rs = statement.executeQuery();
        return rs;
    }

    public ResultSet getAnswerByQuestionIDFromDB(int questionId) throws SQLException {
        String select = "SELECT * FROM " + questions_table + " WHERE questionID = ?";
        PreparedStatement statement = connection.prepareStatement(select);
        statement.setInt(1, questionId);
        return statement.executeQuery();
    }
    public ResultSet getQuizzesCreatedByUserIDFromDB(int userID) throws SQLException {
        String select = "SELECT * FROM " + quizzes_table + " WHERE quizCreatoruserID = ?";
        PreparedStatement statement = connection.prepareStatement(select);
        statement.setInt(1, userID);
        return statement.executeQuery();
    }

    public ResultSet getQuestionsByQuizIDFromDB (int quizID) throws SQLException {
        String select = "SELECT * FROM " + questions_table + " WHERE quizID = ?";
        PreparedStatement statement = connection.prepareStatement(select);
        statement.setInt(1, quizID);
        return statement.executeQuery();
    }

    public ResultSet getQuizFromDB (int quizID) throws SQLException {
        String select = "SELECT * FROM " + quizzes_table + " WHERE quizID = ?";
        PreparedStatement statement = connection.prepareStatement(select);
        statement.setInt(1, quizID);
        return statement.executeQuery();
    }

    public ResultSet getCategories(int quizID) throws SQLException {
        //returns the category name
        String select = "SELECT a.quizID, a.quizName, c.categoryName FROM " + quizzes_table + " a LEFT JOIN "
                + quiz_categories_table + " b ON a.quizID = b.quizID LEFT JOIN "
                + categories_table + " c ON b.categoryID = c.categoryID WHERE a.quizID = ?";
        PreparedStatement statement = connection.prepareStatement(select);
        statement.setInt(1, quizID);
        return statement.executeQuery();
    }

    public int addQuizToDB(Quiz quiz) throws SQLException {
        ResultSet resultSet = null;
        String insert = "insert into " + quizzes_table + " (quizName, quizCreatorUserID, `singlePage?`, `randomOrder?`, `immediateCorrection?`, `practiceMode?`) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, quiz.getQuizName());
        statement.setInt(2, quiz.getquizCreatoruserID());
        statement.setBoolean(3, quiz.getSinglePage());
        statement.setBoolean(4, quiz.getRandomOrder());
        statement.setBoolean(5, quiz.getImmediateCorrection());
        statement.setBoolean(6, quiz.getPractiveMode());
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("The attempt to include the quiz was unsuccessful, resulting in no changes to the rows.");
        }

        resultSet = statement.getGeneratedKeys();
        if (!resultSet.first())
            throw new SQLException("The effort to add the quiz was not successful, as no generation key was acquired.");

        quiz.setQuizID(resultSet.getInt(1)); //Add the quizID to this new quiz that we have
        return resultSet.getInt(1);
    }

    public int addQuestionToDB(String questionText, int qType,
                           int nextQuestionNum, int quizID) throws SQLException {
        String insert = "insert into " + questions_table + " (quizID, questionTypeID, question, questionNumber) VALUES (?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, quizID);
        statement.setInt(2, qType);
        statement.setString(3, questionText);
        statement.setInt(4, nextQuestionNum);
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Adding question failed, no rows affected.");
        }

        ResultSet genKey = statement.getGeneratedKeys();
        if (!genKey.first())
            throw new SQLException("The attempt to add an answer was unsuccessful, as no generation key was acquired.");
        return genKey.getInt(1);
    }
    public int addAnswerToDB(String answerText, int quizID, int questionID) throws SQLException {
        ResultSet genKey = null;
        String insert = "insert into " + answers_table + " (questionID, quizID, answer) VALUES (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, questionID);
        statement.setInt(2, quizID);
        statement.setString(3, answerText);
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0)
            throw new SQLException("Adding answer failed, no rows affected.");

        genKey = statement.getGeneratedKeys();
        if (!genKey.first())
            throw new SQLException("The attempt to add an answer was unsuccessful, as no generation key was acquired.");
        return genKey.getInt(1);
    }
}
