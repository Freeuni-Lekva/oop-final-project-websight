package model;

import javax.servlet.http.HttpServletRequest;
import java.util.InputMismatchException;
import java.sql.SQLException;

public class FriendRequest extends Message {
    public FriendRequest(int from, int to, String fromName) {
        super(FRIEND, to, from);
        setSubject("You have a friend request!");
        setContent(FContent(from, fromName) + getAccept(from) );
    }

    public static void friendRequest(HttpServletRequest req, DBConnection con) throws SQLException, InputMismatchException {
        String toUserName = req.getParameter("toUserName");
        if (!(con.validUserName(toUserName)) ) throw new InputMismatchException();
        int from  = Integer.parseInt(req.getParameter("fromUserID"));
        int to = con.getUserID(toUserName);
        String fromName = con.getUserName(from);
        FriendRequest res = new FriendRequest(from, to, fromName);
        con.addMessage(res);
    }

    private static String FContent(Integer from, String fromName) {
        String res =    "<h4>" + fromName + " sent you a friend request!</h4>" +
                        "User <a class='btn btn-default btn-xs' href='userpage.jsp?userID=" +
                        from + "'>" + fromName + "</a> wants to be your " +
                        "friend! Accept the request or check their profile <br><br>";
        return res;
    }

    private static String getAccept(Integer from) {
        return "<form action='FriendRequestServlet' method='POST'>" +
                "<input name='nonActingUserID' value=" + from + " type='hidden' />" +
                "<input name='origin' value='messageLink' type='hidden' />" +
                "<input class='btn btn-default' type='submit' value='Accept' /><br>" +
                "</form>";
    }
    public static String getCreationHTML(Integer UID, DBConnection con)
            throws SQLException {
        String userName = con.getUserName(UID);
        StringBuilder html = new StringBuilder();

        html.append("<input name='fromUserID' type='hidden' value=" + UID + " />");
        html.append("<input name='type' type='hidden' value=" + Message.FRIEND + " />");
        html.append("<input name='hasContent' type='hidden' value='true' />");
        html.append(
                "<div class='row'><br>" +
                        "<div class='form-group'>" +
                        "<label for='1' class='col-sm-2 control-label'>User to friend</label>" +
                        "<div class='col-sm-7'>" +
                        "<input id='1' type='text' class='form-control'" +
                        "name='toUserName' placeholder='Username'>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "<div class='row'><br>" +
                        "<label class='col-sm-2'>Preview</label>" +
                        "<div class='col-sm-7'>" +
                        "<div class='panel panel-default'>" +
                        "<div class='panel-body' >" +
                        FContent(UID, userName) +
                        "<a class='disabled btn btn-default'>Accept</a><br>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>"
        );
        return html.toString();
    }
}