package model;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.InputMismatchException;

public class Challenge extends Message {

    public Challenge(int toUserID, int fromUserID,
                     String fromUserName, String quizName, int quizID) {
        super(CHALLENGE, toUserID, fromUserID);
        String content =
                generateChallenge(fromUserID, fromUserName, quizID, quizName);

        setSubject("Someone wants to test your quiz skills!");
        setContent( content );
    }

    public static void makeChallenge(HttpServletRequest request, DBConnection connection)
            throws SQLException, InputMismatchException {

        String toUserName   = request.getParameter("toUserName");
        String quizName     = request.getParameter("quizName");
        if ( !(connection.validUserName(toUserName)) ||
                !(connection.validQuizName(quizName)) ) {
            throw new InputMismatchException();
        }

        Integer toUserID    = connection.getUserID( toUserName );
        Integer fromUserID  = Integer.parseInt(request.getParameter("fromUserID"));
        String fromUserName = connection.getUserName(fromUserID);
        Integer quizID      = connection.getQuizID(quizName);

        Challenge newChallenge =
                new Challenge(toUserID, fromUserID, fromUserName, quizName, quizID);
        connection.addMessage(newChallenge);
    }

    private static String
    generateChallenge(int fromUserID, String fromUserName, int quizID, String quizName) {
        String content =
                "<h4>Someone challenged you to a quiz!</h4> User <a class='btn " +
                        "btn-default btn-xs' href='userPage.jsp?userID=" + fromUserID + "'>" +
                        fromUserName + "</a> suggested you take the quiz " +
                        "<a class='btn btn-default btn-xs' href='quizSummary.jsp?quizID=" +
                        quizID + "'>" + quizName + "</a> Follow the link to find out more!<br>";

        return content;
    }

    public static String getCreationHTML(Integer userID, String quizName) {
        StringBuilder html = new StringBuilder();

        html.append("<input name='fromUserID' type='hidden' value=" + userID + " />");
        html.append("<input name='type' type='hidden' value=" + CHALLENGE + " />");
        html.append("<input name='hasContent' type='hidden' value='true' />");

        html.append(
                "<div class='row'><br>" +
                        "<div class='form-group'>" +
                        "<label for='2' class='col-sm-2 control-label'>Quiz</label>" +
                        "<div class='col-sm-4'>" +
                        "<input id='2' type='text' class='form-control'");

        if (quizName == null) {
            html.append("name='quizName' placeholder='Quiz name'>");
        } else {
            html.append("name='quizName' readonly value=" +
                    quizName + " placeholder=" + quizName + " >");
        }
        html.append(
                "</div>" +
                        "</div>" +
                        "</div>" +
                        "<div class='row'>" +
                        "<div class='form-group'>" +
                        "<label for='1' class='col-sm-2 control-label'>Friend to challenge</label>" +
                        "<div class='col-sm-4'>" +
                        "<input id='1' type='text' class='form-control'" +
                        "name='toUserName' placeholder='Username'>" +
                        "</div>" +
                        "</div>" +
                        "</div>"
        );
        return html.toString();
    }
}
