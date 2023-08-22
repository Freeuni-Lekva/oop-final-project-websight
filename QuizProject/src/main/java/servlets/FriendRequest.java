package servlets;

import model.DBConnection;
import model.Message;
import model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class FriendRequest extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletContext context;
    private HttpSession session;
    private DBConnection connection;
    private User actingUser;
    private int actingUserID, nonActingUserID;
    private String origin, nonActingUserName;
    private Boolean areFriends;

    public FriendRequest() {
        super();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            context           = getServletContext();
            session           = request.getSession();
            connection        = (DBConnection) context.getAttribute("DBConnection");
            actingUser        = (User) session.getAttribute("user");
            actingUserID      = actingUser.getUserID();
            nonActingUserID   = Integer.parseInt( request.getParameter("nonActingUserID") );
            nonActingUserName = connection.getUserName(nonActingUserID);
            origin            = request.getParameter("origin");
            areFriends        = connection.friendship(actingUserID, nonActingUserID);

            // Direct based on origin / friend status
            if ( actingUserID == nonActingUserID) {
                goToFriendYourself(request, response);

            } else if ( areFriends ) {
                if ( "removeLink".equals(origin) ) {
                    goToRemoveFriend(request, response);

                } else {
                    goToAlreadyFriends(request, response);
                }

            } else if ( "messageLink".equals(origin) ) {
                goToMakeAcceptLink(request, response);

            } else if ( "accepted".equals(origin) ) {
                goToMakeFriend(request, response);

            } else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    private void goToFriendYourself(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String html =
                "<h3 style='color:#d9534f'>Sorry!<small> You cannot friend yourself!</small></h3>";
        request.setAttribute("html", html);
        request.getRequestDispatcher("friendRequest.jsp").forward(request, response);
    }

    private void goToAlreadyFriends(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String html =
                "<h3 style='color:#d9534f'>You are already friends with " + nonActingUserName + "!</h3>" +
                        "<div class='row'>" +
                        "<div class='col-md-10'>" +
                        "<h5>View their page " + getUserPageLink("here", nonActingUserID) + " or " +
                        getRemoveFriendLink("unfriend them", actingUserID, nonActingUserID) + "</h5>" +
                        "</div>" +
                        "</div>";

        request.setAttribute("html", html);
        request.getRequestDispatcher("friendRequest.jsp").forward(request, response);
    }

    private void goToMakeAcceptLink(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String html =
                "<h3 style='color:#d9534f'>" + nonActingUserName + " sent you a friend request!</h3>" +
                        "<div class='row'>" +
                        "<div class='col-md-10'>" +
                        "<h5>View their page " + getUserPageLink("here", nonActingUserID) + " or " +
                        getAcceptFriendLink("confirm request", actingUserID, nonActingUserID) +
                        "</h5>" +
                        "</div>" +
                        "</div>";

        request.setAttribute("html", html);
        request.getRequestDispatcher("friendRequest.jsp").forward(request, response);
    }

    private void goToMakeFriend(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        connection.friendship(nonActingUserID, actingUserID);
        String html =
                "<h3 style='color:#d9534f'>Congratulations! " +
                        "<small>you are now friends with " + nonActingUserName + "</small></h3>" +
                        "<div class='row'>" +
                        "<div class='col-md-10'>" +
                        "<h5>See what they've been up to " + getUserPageLink("here", nonActingUserID) +
                        " or " + getRemoveFriendLink("unfriend them", actingUserID, nonActingUserID) + "</h5>" +
                        "</div>" +
                        "</div>";

        request.setAttribute("html", html);
        request.getRequestDispatcher("friendRequest.jsp").forward(request, response);
    }

    private void goToRemoveFriend(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        connection.removeFriendship(nonActingUserID, actingUserID);
        String html =
                "<h3 style='color:#d9534f'>You are no longer friends with " + nonActingUserName + "</h3>" +
                        "<div class='row'>" +
                        "<div class='col-md-10'>" +
                        "<h5>You can still visit their page " + getUserPageLink("here", nonActingUserID) +
                        " or " + getMakeFriendRequestLink("re-friend them") + "</h5>" +
                        "</div>" +
                        "</div>";

        request.setAttribute("html", html);
        request.getRequestDispatcher("friendRequest.jsp").forward(request, response);
    }

    public static String getRemoveFriendLink(String text, int actingUserID, int nonActingUserID) {
        String form =
                "<a href='FriendRequestServlet?actingUserID=" + actingUserID +
                        "&nonActingUserID=" + nonActingUserID + "&origin=removeLink'"+
                        "class='btn btn-danger'>" + text + "</a>";
        return form;
    }

    public static String getAcceptFriendLink(String text, int actingUserID, int nonActingUserID) {
        String form =
                "<a href='FriendRequestServlet?actingUserID=" + actingUserID +
                        "&nonActingUserID=" + nonActingUserID + "&origin=accepted'"+
                        "class='btn btn-default'>" + text + "</a>";
        return form;
    }

    public static String getMakeFriendRequestLink(String text) {
        return  "<a class='btn btn-default' href='NewMessageServlet?type=" +
                Message.FRIEND + "'>" +  text + "</a>";


    }

    private String getUserPageLink(String text, int userID) {
        return "<a class='btn btn-default' href='userpage.jsp?userID=" +
                userID + "'>" + text + "</a>";
    }
}
