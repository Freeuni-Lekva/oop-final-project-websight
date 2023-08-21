package model;

import java.sql.SQLException;

public class QuizFlag extends Message {
    public QuizFlag(int toUserID, int fromUserID,
                    String fromUserName, String quizName, int quizID) {
        super(QUIZ_FLAG, toUserID, fromUserID);
        setSubject("Quiz " + quizName + " flagged by " + fromUserName);
        setContent(FContent(quizID, quizName, fromUserID, fromUserName));
    }

    private String FContent(int QID, String QName, int FID,
                                       String fromUserName) {
        return  "User <a class='btn btn-default btn-xs' href='userpage.jsp?userID=" +
                FID + "'>" + fromUserName + "</a> flagged the quiz " +
                "<a class='btn btn-default btn-xs' href='quizSummary.jsp?quizID=" +
                QID + "'>" + QName + "</a> as inappropriate. Take action by " +
                "following the link";
    }

    public static void makeQuizFlag(int QID, String QName, int FID,
                                    int TID, String FName, DBConnection conn) throws SQLException {
        QuizFlag qf = new QuizFlag(TID, FID, FName, QName, QID);
        conn.addMessage(qf);
    }
}