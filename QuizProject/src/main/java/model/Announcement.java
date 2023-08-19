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
}
