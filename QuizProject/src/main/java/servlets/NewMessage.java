package servlets;

import model.DBConnection;
import model.Message;
import model.Note;
import model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;

public class NewMessage {
    public NewMessage() {
        super();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer messageIntType = null;
        Integer userID = null;
        DBConnection connection = null;

        try {
            ServletContext context = request.getServletContext();
            HttpSession session = request.getSession();
            connection = (DBConnection) context.getAttribute("DBConnection");
            User user = (User) session.getAttribute("user");
            userID = user.getUserID();
            String messageType = request.getParameter("type");
            String hasContent = request.getParameter("hasContent");

            if (userID == -1) {
                guestUser(request, response);

            } else if (messageType == null) {
                String html = dropDown(true);
                request.setAttribute("html", html);
                request.getRequestDispatcher("createMessage.jsp").forward(request, response);

            } else if (hasContent == null) { // know type, get content from user
                messageIntType = Integer.parseInt(messageType);
                messageContent(request, response, messageIntType, userID, connection);

            } else {
                messageIntType = Integer.parseInt(messageType);
                createMessage(request, response, userID, messageIntType, connection);
            }

        } catch (NullPointerException n) {
            guestUser(request, response);

        } catch (InputMismatchException badinput) {
            try {
                request.setAttribute("error",
                        "<h3 style='color:#d9534f'>Invalid input, please re-enter</h3>");
                messageContent(request, response, messageIntType, userID, connection);

            } catch (Exception e) {
                e.printStackTrace();
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void
    guestUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String html =
                "<h3 style='color:#d9534f'>You must log in or create an account to create a message!</h3>";
        request.setAttribute("html", html);
        request.getRequestDispatcher("createMessage.jsp").forward(request, response);
    }

    private void
    messageContent(HttpServletRequest request, HttpServletResponse response,
                   Integer messageType, Integer userID, DBConnection connection)
            throws ServletException, IOException, SQLException {

        String html = "";

        if (messageType == Message.NOTE) {
            html = Note.getCreationHTML(userID, request);
            // needs additional types code
        } else {
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("html", html);
        request.getRequestDispatcher("createMessage.jsp").forward(request, response);
    }


    private void
    createMessage(HttpServletRequest request, HttpServletResponse response,
                  Integer userID, Integer messageType, DBConnection connection)
            throws ServletException, IOException, SQLException, InputMismatchException {

        if (messageType == Message.NOTE) {
            Note.makeNote(request, connection);
            request.setAttribute("messageUpdate",
                    "Your note was sent successfully");

            //needs additional  types code
        } else {
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/ReadMessagesServlet").forward(request, response);
    }

    private String dropDown(boolean isAdmin) {
        StringBuilder dropdown = new StringBuilder();
        dropdown.append(
                "<div class=\"form-group\">" +
                        "<label for=\"1\">New message type</label><br>" +
                        "<div class=\"row\">" +
                        "<div class=\"col-md-4\">" +
                        "<select class=\"form-control\" id=\"1\" name=\"type\">" +
                        "<option value=" + Message.NOTE + ">Note</option>" +
                        "<option value=" + Message.FRIEND + ">Friend Request</option>" +
                        "<option value=" + Message.CHALLENGE + ">Quiz Challenge</option>"
        );

        if (isAdmin) { // Admin can also create announcements
            dropdown.append("<option value=" +
                    Message.ANNOUNCEMENT + ">Site Announcement</option>");
        }
        dropdown.append("</select></div></div>");

        return dropdown.toString();
    }
}
