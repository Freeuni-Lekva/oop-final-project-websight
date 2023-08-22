package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

        connection = DriverManager.getConnection("jdbc:mysql://localhost:" + database_port+"/"+database_name+"?characterEncoding=UTF8&user="+username+"&password="+password);
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

    public ResultSet getQuizAnswer(int questionId) throws SQLException {
        String select = "SELECT * FROM " + answers_table + " WHERE questionID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, questionId);
        return sql.executeQuery();
    }

    public boolean addMessage(Message message) throws SQLException {
        String set = "INSERT INTO " + messages_table +
                "(messageType, toUserID, fromUserID, subject, content, date," +
                " messageRead, toUserDeleted, fromUserDeleted)" +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        PreparedStatement sql = connection.prepareStatement(set);

        sql.setInt(1, message.getType());
        sql.setInt(2, message.getToUserID());
        sql.setInt(3, message.getFromUserID());
        sql.setString(4, message.getSubject());
        sql.setString(5, message.getContent());
        sql.setTimestamp(6, new java.sql.Timestamp(message.getDate().getTime()));
        sql.setBoolean(7, message.getReadStatus());
        sql.setBoolean(8, false);
        sql.setBoolean(9, false);
        return sql.execute();
    }
    public boolean validQuizName(String quizName) throws SQLException {
        String quizNameGet = "SELECT COUNT(*) FROM " + quizzes_table + " WHERE quizName = ?";
        PreparedStatement sql = connection.prepareStatement(quizNameGet);
        sql.setString(1, quizName);
        ResultSet rs = sql.executeQuery();
        rs.first();
        return rs.getInt(1) > 0;
    }

    public int getQuizID(String quizName) throws SQLException {
        String userIDGet = "SELECT quizID FROM " + quizzes_table + " WHERE quizName = ?";
        PreparedStatement sql = connection.prepareStatement(userIDGet);
        sql.setString(1, quizName);
        ResultSet rs = sql.executeQuery();
        rs.first();
        return rs.getInt(1);
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
    public ResultSet getQuizInfo(int quizID) throws SQLException {
        String select = "SELECT * FROM " + quizzes_table + " WHERE quizID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, quizID);
        return sql.executeQuery();
    }

    public ResultSet getQuizQuestions (int quizID) throws SQLException {
        String select = "SELECT * FROM " + questions_table + " WHERE quizID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, quizID);
        return sql.executeQuery();
    }
    public ResultSet getQuestionAnswers (int questionID) throws SQLException {
        String select = "SELECT * FROM " + answers_table + " WHERE questionID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, questionID);
        return sql.executeQuery();
    }
    public boolean deleteQuiz(int quizID) throws SQLException {
        String delete =
                "DELETE " +
                        "quizzes, history, scores,ratings, tags, categories, " +
                        "answers,questions FROM " +
                        "quizzes LEFT JOIN history     on quizzes.quizID = history.quizID" +
                        " LEFT JOIN scores          on quizzes.quizID = scores.quizID" +
                        " LEFT JOIN ratings on quizzes.quizID = ratings.quizID" +
                        " LEFT JOIN tags        on quizzes.quizID = tags.quizID" +
                        " LEFT JOIN categories  on quizzes.quizID = cCategories.quizID" +
                        " LEFT JOIN answers     on quizzes.quizID = answers.quizID" +
                        " LEFT JOIN questions   on quizzes.quizID = questions.quizID" +
                        " WHERE quizzes.quizID = ?";

        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, quizID);
        return sql.execute();
    }

    public ResultSet getMessage(int messageID) throws SQLException {
        String select = "SELECT * FROM " + messages_table + " WHERE messageID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, messageID);
        return sql.executeQuery();
    }

    public String getPassword(int userID) throws SQLException {
        String passwordGet = "SELECT password FORM users WHERE userID = ?";
        PreparedStatement sql = connection.prepareStatement(passwordGet);
        sql.setInt(1, userID);
        ResultSet rs = sql.executeQuery();
        if (!rs.first())
            return "";
        return rs.getString(1);
    }

    public boolean deleteMessage(Integer messageID) throws SQLException {
        String delete = "DELETE FROM " + messages_table + " WHERE messageID = ?";

        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, messageID);

        return sql.execute();
    }
    public boolean readMessage (Integer messageID) throws SQLException {
        String set = "UPDATE " + messages_table + " SET messageRead = ?"
                + " WHERE messageID = ?";

        PreparedStatement sql = connection.prepareStatement(set);
        sql.setBoolean(1, true);
        sql.setInt(2, messageID);

        return sql.execute();
    }

    public ResultSet getHistories(int quizID) throws SQLException {
        String select = "SELECT * FROM " + quiz_history_table + " WHERE quizID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, quizID);
        return sql.executeQuery();
    }

    public ResultSet getTagTypes() throws SQLException {
        String select = "SELECT * FROM " + tags_table + ";";
        PreparedStatement sql = connection.prepareStatement(select);
        return sql.executeQuery();
    }
    public ResultSet getTags(int quizID) throws SQLException {
        String select = "SELECT a.quizID, a.quizName, c.tagName FROM " + quizzes_table + " a LEFT JOIN "
                + quiz_tags_table + " b ON a.quizID = b.quizID LEFT JOIN "
                + tags_table + " c ON b.tagID = c.tagID WHERE a.quizID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, quizID);
        return sql.executeQuery();
    }
    public ResultSet getCategoryTypes() throws SQLException {
        String select = "SELECT * FROM " + categories_table + ";";
        PreparedStatement sql = connection.prepareStatement(select);
        return sql.executeQuery();
    }

    public ResultSet getRatings(int quizID) throws SQLException {
        String select = "SELECT * FROM " + user_ratings_table + " WHERE quizID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, quizID);
        return sql.executeQuery();
    }
    public ResultSet getRatingsByUserID(int userID) throws SQLException {
        String select = "SELECT * FROM " + user_ratings_table + " WHERE userID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, userID);
        return sql.executeQuery();
    }

    public ResultSet getHistoriesByUserID(int userID) throws SQLException {
        String select = "SELECT * FROM " + quiz_history_table + " WHERE userID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, userID);
        return sql.executeQuery();
    }
    public boolean addQuizRating(int quizID, int userID, int rating)
            throws SQLException {
        String set = "INSERT INTO " + user_ratings_table +
                " (userID, quizID, ratingValue) VALUES ( ?, ?, ?)";

        PreparedStatement sql = connection.prepareStatement(set);
        sql.setInt(1, userID);
        sql.setInt(2, quizID);
        sql.setInt(3, rating);

        return sql.execute();
    }

    public ResultSet getAllRatings() throws SQLException {
        String select = "SELECT * FROM " + user_ratings_table;
        PreparedStatement sql = connection.prepareStatement(select);
        return sql.executeQuery();
    }

    public boolean updateQuizRating(int ratingID, int rating)
            throws SQLException {
        // Updates the ratingValue of the rating with ID ratingID
        String update = "UPDATE " + user_ratings_table +
                " SET ratingValue = ? WHERE ratingID = ?";

        PreparedStatement sql = connection.prepareStatement(update);
        sql.setInt(1, rating);
        sql.setInt(2, ratingID);

        return sql.execute();
    }

    public boolean setAchievement(int userID, int achievementID, String description) throws SQLException{
        ResultSet genKey = null;
        String insert = "insert into " + user_achievements_table + " (userID, achievementID) VALUES (?, ?);";
        PreparedStatement sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setInt(1, userID);
        sql.setInt(2, achievementID);
        int affectedRows = sql.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating achievement failed, no rows affected.");
        }

        genKey = sql.getGeneratedKeys();
        if (!genKey.first())
            throw new SQLException("Creating achievement failed, no gen key obtained.");
        return true;
    }

    public boolean addTag(int quizID, int tagID) throws SQLException {
        String insert = "insert into " + quiz_tags_table + " (quizID, tagID) VALUES (?, ?);";
        PreparedStatement sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setInt(1, quizID);
        sql.setInt(2, tagID);
        int affectedRows = sql.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating achievement failed, no rows affected.");
        }

        ResultSet genKey = sql.getGeneratedKeys();
        if (!genKey.first())
            throw new SQLException("Creating achievement failed, no gen key obtained.");
        return true;
    }
    public int addNewTag(String newTag) throws SQLException {
        String insert = "insert into " + tags_table + " (tagName) VALUES (?);";
        PreparedStatement sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setString(1, newTag);
        int affectedRows = sql.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating achievement failed, no rows affected.");
        } //if

        ResultSet genKey = sql.getGeneratedKeys();
        if (!genKey.first())
            throw new SQLException("Creating achievement failed, no gen key obtained.");
        return genKey.getInt(1);
    }
    public boolean toUserDeleteMessage(Integer messageID) throws SQLException {
        String update = "UPDATE " + messages_table+ " SET toUserDeleted = ? " +
                "WHERE messageID = ?";

        PreparedStatement sql = connection.prepareStatement(update);
        sql.setBoolean(1, true);
        sql.setInt(2, messageID);

        return sql.execute();
    }
    public boolean fromUserDeleteMessage(Integer messageID) throws SQLException {
        String update = "UPDATE " + messages_table + " SET fromUserDeleted = ? " +
                "WHERE messageID = ?";

        PreparedStatement sql = connection.prepareStatement(update);
        sql.setBoolean(1, true);
        sql.setInt(2, messageID);

        return sql.execute();
    }

    public String getQuizName(int quizID) throws SQLException {
        String userIDGet = "SELECT quizName FROM " + quizzes_table + " WHERE quizID = ?";
        PreparedStatement sql = connection.prepareStatement(userIDGet);
        sql.setInt(1, quizID);
        ResultSet rs = sql.executeQuery();
        rs.first();
        return rs.getString(1);
    }
    public ResultSet searchForQuiz(String quizFilter, String tagFilter, String catFilter) throws SQLException {
        String tagAddOn = "";
        String catAddOn = "";
        if (tagFilter == null || tagFilter.equals("")) {
            tagFilter = "%";
            tagAddOn = "or b.tagID is null ";
        } //if
        if (catFilter == null || catFilter.equals("")) {
            catFilter = "%";
            catAddOn = "or c.categoryID is null ";
        } //if
        if (quizFilter == null || quizFilter.equals("")) {
            quizFilter = "%";
        } //if
        else {
            quizFilter = "%" + quizFilter + "%";
        } //else

        String select = "SELECT * FROM " + quizzes_table + " a LEFT JOIN " + quiz_tags_table + " b"
                + " ON a.quizID = b.quizID LEFT JOIN " + quiz_categories_table + " c ON a.quizID = c.quizID "
                + " WHERE ( a.quizName like ? ) AND (b.tagID like ? " + tagAddOn + " ) AND ( c.categoryID like ? "
                + catAddOn + " );";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, quizFilter);
        sql.setString(2, tagFilter);
        sql.setString(3, catFilter);
        return sql.executeQuery();
    }
    public ResultSet getAllQuizzes() throws SQLException {
        String select = "SELECT * FROM " + quizzes_table + ";";
        PreparedStatement sql = connection.prepareStatement(select);
        return sql.executeQuery();
    }
    public ArrayList<Message> getRecentMessages(int userID) throws SQLException{
        ResultSet rs = getUserMessages(userID);
        Set<Integer> validTypes = new HashSet<Integer>() {
            private static final long serialVersionUID = -5488924326330841157L;
            {
                add(Message.NOTE);
                add(Message.CHALLENGE);
                add(Message.FRIEND);
            }};
        ArrayList<Message> messages = Message.loadMessages(rs, validTypes, userID, null);
        ArrayList<Message> recent = new ArrayList<Message>();
        ArrayList<Message> result = new ArrayList<Message>();
        for (int i = 0; i < messages.size(); i++) {
            int recentIndex = 0;
            while(recentIndex < recent.size() &&
                    recent.get(recentIndex).getDate().compareTo(messages.get(i).getDate()) > 0) {
                recentIndex++;
            }
            recent.add(recentIndex , messages.get(i));
        }
        for (int i = 0; i < recent.size(); i++) {
            if (i >= 3) break;
            result.add(recent.get(i));
        }
        return result;
    }

    public ResultSet getAnswerInfo(int questionId) throws SQLException {
        String select = "SELECT * FROM "  + answers_table + " WHERE questionID = ?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, questionId);
        return sql.executeQuery();
    }
    public boolean addCat(int quizID, int catID) throws SQLException {
        String insert = "insert into " + quiz_categories_table + " (quizID, categoryID) VALUES (?, ?);";
        PreparedStatement sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setInt(1, quizID);
        sql.setInt(2, catID);
        int affectedRows = sql.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating achievement failed, no rows affected.");
        }

        ResultSet genKey = sql.getGeneratedKeys();
        if (!genKey.first())
            throw new SQLException("Creating achievement failed, no gen key obtained.");
        return true;
    }

    public boolean addQuizHistory(int quizID, int userID, int score) throws SQLException {
        String set = "INSERT INTO " + quiz_history_table +
                " (quizID, userID, dateTaken, score, `completed?`, timeStamp) VALUES ( ?, ?, ?, ?, ?, ? )";
        PreparedStatement sql = connection.prepareStatement(set);
        java.util.Date utilDate = new java.util.Date();
        Date date = new Date(utilDate.getTime());
        sql.setInt(1, quizID);
        sql.setInt(2, userID);
        sql.setDate(3, date);
        sql.setInt(4, score);
        sql.setInt(5,1); //Completed = true
        sql.setDate(6, date);
        int success = sql.executeUpdate();
        boolean worked = false;
        if (success > 0) {
            worked = true;
        }
        return worked;
    }

    public Set<Integer> getAdminUserIDsSet() throws SQLException {
        Set<Integer> adminIDs = new HashSet<Integer>();
        Integer currAdminID;
        ResultSet rs;

        String adminGet     = "SELECT userID FROM types INNER JOIN userTypes" +
                " using (typeID) WHERE typeID = ?";

        PreparedStatement sql = connection.prepareStatement(adminGet);
        sql.setInt(1, User.ADMIN);
        rs = sql.executeQuery();
        while ( rs.next() ) {
            currAdminID = ( (Number) rs.getObject(1) ).intValue();
            adminIDs.add(currAdminID);
        }
        return adminIDs;
    }


    public boolean createCookie(int userID, String cookie) throws SQLException {
        String update = "UPDATE " + users_table + " SET cookie = ? WHERE userID = ?;";
        PreparedStatement sql = connection.prepareStatement(update, Statement.RETURN_GENERATED_KEYS);
        sql.setString(1, cookie);
        sql.setInt(2, userID);
        int affectedRows = sql.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating achievement failed, no rows affected.");
        }

        return true;
    }

    public int getUserIDFromCookie(String cookieIn) throws SQLException {

        String select = "SELECT * FROM " + users_table + " WHERE cookie = ?;";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, cookieIn);
        ResultSet userRow = sql.executeQuery();
        if (!userRow.first()) {

            return -1;
        }
        return userRow.getInt("userID");
    }

    public int createUser(String userName, String password) throws SQLException {
        ResultSet genKey = null;
        String insert = "insert into " + users_table + " (userName, password) VALUES (?, ?);";
        PreparedStatement sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setString(1, userName);
        sql.setString(2, password);
        int affectedRows = sql.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        genKey = sql.getGeneratedKeys();
        if (!genKey.first())
            throw new SQLException("Creating user failed, no gen key obtained.");
        int userID = genKey.getInt(1);

        System.out.println("Created the user, about to insert userType");
        insert = "insert into " + user_types_table + " (userID, typeID) VALUES (?, ?);";
        sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setInt(1, userID);
        sql.setInt(2, User.USER);
        affectedRows = sql.executeUpdate();
        System.out.println("Inserted the userType.");
        if (affectedRows == 0) {
            throw new SQLException("Creating userType failed, no rows affected.");
        }

        return userID;
    }

}
