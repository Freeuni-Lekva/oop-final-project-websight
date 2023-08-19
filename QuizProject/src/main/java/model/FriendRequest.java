package model;

import javax.servlet.http.HttpServletRequest;
import java.util.InputMismatchException;
import java.sql.SQLException;

public class FriendRequest extends Message {
    public FriendRequest(int from, int toUserID, String fromName) {
        super(FRIEND, toUserID, from);
        setSubject("Someone wants to be your friend!");
        setContent( generateFriendContent(from, fromName) +
                getAcceptButton(from) );
    }

    public static void makeFriendRequest(HttpServletRequest request, DBConnection connection) throws SQLException, InputMismatchException {
        String toUserName   = request.getParameter("toUserName");
        if ( !(connection.validUserName(toUserName)) ) throw new InputMismatchException();
        Integer to    = connection.getUserID( toUserName );
        Integer from  = Integer.parseInt(request.getParameter("fromUserID"));
        String fromName = connection.getUserName( from );
        FriendRequest req = new FriendRequest(from, to, fromName);
        connection.addMessage(req);
    }

    private static String
    generateFriendContent(Integer from, String fromName) {
        String res =
                "<h4>" + fromName + " wants to be your friend!</h4>" +
                        "User <a class='btn btn-default btn-xs' href='userpage.jsp?userID=" +
                        from + "'>" + fromName + "</a> has requested to be your " +
                        "friend! Check out their page or click below to accept the request<br><br>";
        return res;
    }

    private static String getAcceptButton(Integer fromUserID) {
        return "<form action='FriendRequestServlet' method='POST'>" +
                "<input name='nonActingUserID' value=" + fromUserID + " type='hidden' />" +
                "<input name='origin' value='messageLink' type='hidden' />" +
                "<input class='btn btn-default' type='submit' value='Accept' /><br>" +
                "</form>";
    }
    public static String getCreationHTML(Integer userID, DBConnection connection)
            throws SQLException {
        String userName = connection.getUserName(userID);
        StringBuilder html = new StringBuilder();

        html.append("<input name='fromUserID' type='hidden' value=" + userID + " />");
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
                        generateFriendContent(userID, userName) +
                        "<a class='disabled btn btn-default'>Accept</a><br>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>"
        );
        return html.toString();
    }
}