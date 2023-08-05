package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

        connection = DriverManager.getConnection
                ("jdbc:mysql://" + database_port, username, password);
        PreparedStatement statement = connection.prepareStatement("USE " + database_name);
        statement.execute();
    }

    public void close() throws SQLException {
        connection.close();
    }


}
