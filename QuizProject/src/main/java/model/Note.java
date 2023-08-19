package model;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.InputMismatchException;

public class Note extends Message {

    public Note(Integer toUserID, Integer fromUserID, String subject, String content) {
        super(NOTE, toUserID, fromUserID);

        setSubject(subject);
        setContent(content);
    }

    public static void makeNote(HttpServletRequest request, DBConnection connection)
            throws SQLException, InputMismatchException {

        String toUserName = request.getParameter("toUserName");
        if (!(connection.validUserName(toUserName))) {
            throw new InputMismatchException();
        }
        Integer toUserID = connection.getUserID(toUserName);
        Integer fromUserID = Integer.parseInt(request.getParameter("fromUserID"));
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        Note newNote = new Note(toUserID, fromUserID, subject, content);
        connection.addMessage(newNote);
    }

    public static String getCreationHTML(Integer userID, HttpServletRequest request) {
        String subject = request.getParameter("subject");
        String toUserName = request.getParameter("toUserName");
        StringBuilder html = new StringBuilder();

        html.append("<input name=\"fromUserID\" type=\"hidden\" value=" + userID + " />");
        html.append("<input name=\"type\" type=\"hidden\" value=" + Message.NOTE + " />");
        html.append("<input name=\"hasContent\" type=\"hidden\" value=\"true\" />");
        html.append(
                "<div class=\"row\"><br>" +
                        "<div class=\"form-group\">" +
                        "<label for=\"1\" class=\"col-sm-2 control-label\">To user</label>" +
                        "<div class=\"col-sm-7\">" +
                        "<input id=\"1\" type=\"text\" class=\"form-control\" name=\"toUserName\"");
        if (toUserName != null) {
            html.append("value=" + toUserName + " >");
        } else {
            html.append("placeholder=\"Username\" >");
        }
        html.append(
                "</div>" +
                        "</div>" +
                        "</div>" +
                        "<div class=\"row\">" +
                        "<div class=\"form-group\">" +
                        "<label for=\"2\" class=\"col-sm-2 control-label\">Subject</label>" +
                        "<div class=\"col-sm-7\">" +
                        "<input type=\"text\" id=\"2\" class=\"form-control\" name=\"subject\" ");
        if (subject != null) {
            html.append("value='" + subject + "' >");
        } else {
            html.append("placeholder=\"Subject\">");
        }
        html.append(
                "</div>" +
                        "</div>" +
                        "</div>" +
                        "<div class=\"row\">" +
                        "<div class=\"form-group\">" +
                        "<label for=\"3\" class=\"col-sm-2 control-label\">Content</label>" +
                        "<div class=\"col-sm-7\">" +
                        "<textarea class=\"form-control\" id=\"3\" name=\"content\" " +
                        "rows=\"5\"></textarea>" +
                        "</div>" +
                        "</div>" +
                        "</div>"
        );
        return html.toString();
    }

}
