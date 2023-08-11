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


    public int addQuiz(Quiz quiz) throws SQLException {

        ResultSet genKey = null;
        String insert = "INSERT INTO" + quizzes_table +
                "(quizz_id, title, description, author_id, creation_time, is_random, is_one_page, is_immediate_correction)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement stm = connection.prepareStatement(insert);
        stm.setInt(1, quiz.getQuizId());
        stm.setString(2, quiz.getTitle());
        stm.setString(3, quiz.getDescription());
        stm.setInt(4, quiz.getAuthorId());
        stm.setDate(5, new java.sql.Date(quiz.getCreationDate().getTime()));
        stm.setBoolean(6, quiz.isRandomOrderQuestions());
        stm.setBoolean(7, quiz.isOnePage());
        stm.setBoolean(8, quiz.isImmediateCorrection());

         stm.executeUpdate();

        return 0;
    }

}
