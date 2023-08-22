package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class QuizHistory {
    private static final int TOP = 3;
    private static final int RECENT = 3;
    private static final int POP = 3;


    public static ArrayList<Standing> getStandingsByQuiz(int quizID, DBConnection connection) throws SQLException {
        ArrayList<Standing> result = new ArrayList<Standing>();
        ResultSet Standings = connection.getRatings(quizID);
        while(Standings.next()) {
            int userID = Standings.getInt("userID");
            int StandingValue = Standings.getInt("StandingValue");
            String review = Standings.getString("StandingReview");
            Standing currentStanding = new Standing(quizID, userID, StandingValue, review);
            result.add(currentStanding);
        }
        return result;
    }

    public static ArrayList<Standing> getStandingsByUser(int userID, DBConnection connection) throws SQLException {
        ArrayList<Standing> result = new ArrayList<Standing>();
        ResultSet Standings = connection.getRatingsByUserID(userID);
        while(Standings.next()) {
            int quizID = Standings.getInt("quizID");
            int StandingValue = Standings.getInt("StandingValue");
            String review = Standings.getString("StandingReview");
            Standing currentStanding = new Standing(quizID, userID, StandingValue, review);
            result.add(currentStanding);
        }
        return result;
    }
    public static ArrayList<Score> getHistories(Integer userID, Integer quizID, DBConnection connection) throws SQLException {
        ArrayList<Score> result = new ArrayList<Score>();
        ResultSet histories = (userID == null) ? connection.getHistories(quizID) : connection.getHistoriesByUserID(userID);
        while (histories.next()) {
            int currQuizID = histories.getInt("quizID");
            userID = histories.getInt("userID");
            if (quizID == null || (quizID != null && quizID == currQuizID)) {
                int score = histories.getInt("score");
                java.util.Date date = (java.util.Date) histories.getObject("dateTaken");
                Score currentScore = new Score(currQuizID, userID, date,score);
                result.add(currentScore);
            }
        }
        return result;
    }

    public static ArrayList<String> getRecentScores(Integer userID, Integer quizID, DBConnection connection) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        if (userID == null && quizID == null) return result;
        ArrayList<Score> recentScores = new ArrayList<Score>();
        ArrayList<Score> scores = getHistories(userID, quizID, connection);

        for (int i = 0; i < scores.size(); i++) {
            int recentScoreIndex = 0;
            while(recentScoreIndex < recentScores.size() &&
                    recentScores.get(recentScoreIndex).getDate().compareTo(scores.get(i).getDate()) > 0) {
                recentScoreIndex++;
            }
            recentScores.add(recentScoreIndex , scores.get(i));
        }
        for (int i = 0; i < recentScores.size(); i++) {
            if (i >= RECENT) break;
            int score = recentScores.get(i).getScore();
            userID = recentScores.get(i).getUID();
            int currQuizID  = recentScores.get(i).getQID();
            String quizName = connection.getQuizName(currQuizID);
            String format =
                    "<td>" + score + "</td><td><a class='btn btn-primary btn-xs' href='quizSummary.jsp?quizID=" +
                            currQuizID + "'>" + quizName + "</a></td>" +
                            "<td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=" +
                            userID + "'>" + connection.getUserName(userID) + "</a></td>";
            result.add(format);
        }
        return result;
    }
    public static ArrayList<String> getRecentScoresNoName(Integer userID, Integer quizID, DBConnection connection) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        if (userID == null && quizID == null) return result;
        ArrayList<Score> recentScores = new ArrayList<Score>();
        ArrayList<Score> scores = getHistories(userID, quizID, connection);
        for (int i = 0; i < scores.size(); i++) {
            int recentScoreIndex = 0;
            while(recentScoreIndex < recentScores.size() &&
                    recentScores.get(recentScoreIndex).getDate().compareTo(scores.get(i).getDate()) > 0) {
                recentScoreIndex++;
            }
            recentScores.add(recentScoreIndex , scores.get(i));
        }
        for (int i = 0; i < recentScores.size(); i++) {
            if (i >= RECENT) break;
            int score = recentScores.get(i).getScore();
            userID = recentScores.get(i).getUID();
            String format =
                    "<td>" + score + "</td>" +
                            "<td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=" +
                            userID + "'>" + connection.getUserName(userID) + "</a></td>";
            result.add(format);
        }
        return result;
    }


    /* Gets the NUM_TOP_SCORES number of recent scores associated with this user
     * ID. Returns an arrayList of the string format specified by Chris.
     */
    public static ArrayList<String> getTopScores(Integer userID, Integer quizID, DBConnection connection) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Score> topScores = new ArrayList<Score>();
        ArrayList<Score> scores = getHistories(userID, quizID, connection);
        for (int i = 0; i < scores.size(); i++) {
            int topScoreIndex = 0;
            while(topScoreIndex < topScores.size() && topScores.get(topScoreIndex).getScore() > scores.get(i).getScore()) {
                topScoreIndex++;
            }
            topScores.add(topScoreIndex ,scores.get(i));
        }
        for (int i = 0; i < topScores.size(); i++) {
            if (i >=TOP) break;
            int score = topScores.get(i).getScore();
            userID = topScores.get(i).getUID();
            String format = "<td>" + score + "</td><td><a class='btn btn-default btn-xs' href='userpage.jsp?userID="
                    + userID + "'>" + connection.getUserName(userID) + "</a></td>";
            result.add(format);
        }
        return result;
    }
    public static ArrayList<CreationQuiz> getQuizzes(Integer userID, DBConnection connection) throws SQLException {
        ArrayList<CreationQuiz> result = new ArrayList<>();
        ResultSet quizzes = (userID == null) ? connection.getAllQuizzes() : connection.getQuizzesCreatedByUserIDFromDB(userID);
        while (quizzes.next()) {
            int quizID = quizzes.getInt("quizID");
            userID = quizzes.getInt("quizCreatoruserID");
            java.util.Date createDate = (java.util.Date) quizzes.getObject("quizCreation");
            String quizName = quizzes.getString("quizName");
            CreationQuiz currentCreation = new CreationQuiz(quizID, userID, createDate, quizName);
            result.add(currentCreation);
        }
        return result;
    }

    public static ArrayList<String> getRecentQuizCreations(Integer userID, DBConnection connection) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<CreationQuiz> recent = new ArrayList<>();
        ArrayList<CreationQuiz> all = getQuizzes(userID, connection);
        for (int i = 0; i < all.size(); i++) {
            int recentIndex = 0;
            while(recentIndex < recent.size() &&
                    recent.get(recentIndex).getCreateDate().compareTo(all.get(i).getCreateDate()) > 0) {
                recentIndex++;
            }
            recent.add(recentIndex , all.get(i));
        }
        for (int i = 0; i < recent.size(); i++) {
            if (i >= RECENT) break;
            CreationQuiz curr = recent.get(i);
            String format =
                    "<td><a class='btn btn-primary btn-xs' href='quizSummary.jsp?quizID=" +
                            curr.getQuizID() + "'>" + curr.getQuizName() + "</a></td>" +
                            "<td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=" +
                            curr.getUserID() + "'>" + connection.getUserName(curr.getUserID()) + "</a></td>";
            result.add(format);
        }
        return result;
    }
    /* Returns an HTML printable string of the most popular quizzes in the DB.
     */
    public static ArrayList<String> getPopularQuizzes(DBConnection connection) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        ResultSet allStandings = connection.getAllRatings();
        HashMap<Integer, Integer> popQuizzes = new HashMap<Integer, Integer>();
        while(allStandings.next()) {
            int quizID = allStandings.getInt("quizID");
            if (popQuizzes.containsKey(quizID)) continue;
            int avgStanding = getStanding(quizID, connection);
            popQuizzes.put(quizID, avgStanding);
        }
        ArrayList<Integer> popList = new ArrayList<Integer>();
        Set<Integer> keySet = popQuizzes.keySet();
        for (Integer quizID : keySet) {
            int popIndex = 0;
            while(popIndex < popList.size() &&
                    popList.get(popIndex) > popQuizzes.get(quizID)) {
                popIndex++;
            }
            popList.add(popIndex , quizID);
        }
        for (int i = 0; i < popList.size(); i++) {
            if (i >= POP) break;
            int quizID = popList.get(i);
            ResultSet quizInfo = connection.getQuizInfo(quizID);
            quizInfo.next();
            String format =
                    "<td><a class='btn btn-primary btn-xs' href='quizSummary.jsp?quizID=" +
                            quizInfo.getInt("quizID") + "'>" + quizInfo.getString("quizName") + "</a></td>" +
                            "<td><a class='btn btn-default btn-xs' href='userpage.jsp?userID=" +
                            quizInfo.getInt("quizCreatoruserID") + "'>" +
                            connection.getUserName(quizInfo.getInt("quizCreatoruserID")) + "</a></td>";
            result.add(format);
        }
        return result;
    }

    public static ArrayList<String> getCategories(int quizID, DBConnection connection) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        ResultSet categories = connection.getCategories(quizID);
        while (categories.next()) {
            result.add(categories.getString("categoryName"));
        }
        return result;
    }
    public static ArrayList<String> getTags(int quizID, DBConnection connection) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        ResultSet tags = connection.getTags(quizID);
        while(tags.next()) {
            result.add(tags.getString("tagName"));
        }
        return result;
    }
    public static int getStanding(int quizID, DBConnection connection) throws SQLException{
        int numValues = 0;
        int total = 0;
        ResultSet Standings = connection.getRatings(quizID);
        while (Standings.next()) {
            total += Standings.getInt("StandingValue");
            numValues++;
        }
        return numValues == 0 ? 0 : total / numValues;
    }
}
