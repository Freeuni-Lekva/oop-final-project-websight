package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {

    private static final String username= DBInfo.username;
    private  static final String password= DBInfo.password;
    private static final String database_port= DBInfo.database_port;
    private  static final String database_name= DBInfo.database_name;
    private static final String users_table ="users";
    private  static final String quizzes_table="quizzes";
    private  static final String questions_table="questions";
    private  static final String answers_table="answers";
    private  static final String messages_table="quizzes";
    private  static final String friends_table="friends";
    private Connection connection;

    public DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection
                ( "jdbc:mysql://" + database_port, database_name, password);
        PreparedStatement statement = connection.prepareStatement("USE " + database_name);
        statement.execute();
    }

    public void close() throws SQLException {
        connection.close();
    }








}
