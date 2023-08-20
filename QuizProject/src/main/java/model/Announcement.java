package model;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;

public class Announcement extends Message {

    public Announcement(Integer toUserID, Integer fromUserID, String announcement) {
        super(ANNOUNCEMENT, toUserID, fromUserID);
        setSubject("IMPORTANT: ADMIN ANNOUNCEMENT");
        setContent(announcement);
    }

    public static int
    makeAnnouncements(HttpServletRequest request, DBConnection connection)
            throws SQLException, InputMismatchException {

        String announcement = (String)  request.getParameter("content");
        Integer fromUserID  = Integer.parseInt( request.getParameter("fromUserID") );
        if (announcement == null || announcement.length() == 0) {
            throw new InputMismatchException(); // invalid input
        }

        int count = 0;
        int currUserID;
        Announcement newAnnouncement;
        ResultSet allUsers = connection.getAllUsers();
        while ( allUsers.next() ) {
            currUserID = (Integer) allUsers.getObject("userID");
            newAnnouncement = new Announcement(currUserID, fromUserID, announcement);
            connection.addMessage(newAnnouncement);
            count++;
        }

        return count;
    }

    public static String getCreationHTML(Integer userID) {
        StringBuilder html = new StringBuilder();

        html.append("<input name='fromUserID' type='hidden' value=" + userID + " />");
        html.append("<input name='type' type='hidden' value=" + Message.ANNOUNCEMENT + " />");
        html.append("<input name='hasContent' type='hidden' value='true' />");

        html.append(
                "<div class='row'><br>" +
                        "<div class='form-group'>" +
                        "<label for='1' class='col-sm-2 control-label'>Announcement</label>" +
                        "<div class='col-sm-7'>" +
                        "<textarea class='form-control' id='1' name='content' " +
                        "placeholder='Announcement' rows='5'></textarea>" +
                        "</div>" +
                        "</div>" +
                        "</div>"
        );
        return html.toString();
    }
}
