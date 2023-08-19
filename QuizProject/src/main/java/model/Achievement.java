package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Achievement {
    public static final int AMATEUR = 1;
    public static final int PROLIFIC= 2;
    public static final int PRODIGIOUS = 3;
    public static final int QUIZ_MACHINE = 4;
    public static final int GREATEST = 5;
    public static final int PRACTICE= 6;
    public static final String[] achievementNames = new String[] {"", "Amateur Author",
            "Prolific Author", "Prodigious Author", "Quiz Machine",
            "I am the Greatest", "Practice Makes Perfect"};
    public static ArrayList<Integer> getAchievements(int userID, DBConnection connection) throws  SQLException {
        ResultSet rs = connection.getUserAchievements(userID);
        ArrayList<Integer> result = new ArrayList<>();
        while(rs.next()) {
            int achievementID = rs.getInt("achievementID");
            result.add(achievementID);
        }
        return result;
    }

    public static ArrayList<String> getAchievementNames(int userID, DBConnection connection) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Integer> IDs = getAchievements(userID, connection);
        for (int i = 0; i < IDs.size(); i++) {
            result.add(achievementNames[IDs.get(i)]);
        }
        return result;
    }
}

