package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class Message implements Comparable<Message> {


    private static final int PREVIEW = 10;

    public static final int NOTE = 0;
    public static final int FRIEND = 1;
    public static final int CHALLENGE = 2;
    public static final int ANNOUNCEMENT = 3;
    public static final int QUIZ_FLAG = 4;

    protected int type, from, to;
    protected boolean read;
    protected Integer messageID;
    protected String subject, content;
    protected Date date;

    public Message(int type, int to, int from) {
        this.type = type;
        this.from = from;
        this.to = to;
        messageID = null;
        subject = null;
        content = null;
        read = false;
        date = new Date();
    }

    public Message(Integer messageID, int type, int to,
                   int from, String subject, String content,
                   Date date, Boolean read) {
        this.messageID = messageID;
        this.type = type;
        this.to = to;
        this.from = from;
        this.read = read;
        this.subject = subject;
        this.content = content;
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public Integer getID() {
        return messageID;
    }

    public int getToUserID() {
        return to;
    }

    public int getFromUserID() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public boolean setReadStatus() {
        return read;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void addContent(String content) {
        this.content = content;
    }

    public static ArrayList<Message>
    loadMessages(ResultSet rs, Set<Integer> validTypes,
                 Integer validToUserID, Integer validFromUserID) throws SQLException {

        ArrayList<Message> messages = new ArrayList<Message>();

        Message message;
        int messageType, toUserID, fromUserID;
        Boolean messageRead, toUserDeleted, fromUserDeleted;
        Integer messageID;
        String subject, content;
        Date date;

        while (rs.next()) {
            messageID = (Integer) rs.getObject("messageID");
            messageType = (Integer) rs.getObject("type");
            toUserID = (Integer) rs.getObject("to");
            fromUserID = (Integer) rs.getObject("from");
            subject = (String) rs.getObject("subject");
            content = (String) rs.getObject("content");
            date = (Date) rs.getObject("date");
            messageRead = (Boolean) rs.getObject("read");
            toUserDeleted = (Boolean) rs.getObject("toUserDeleted");
            fromUserDeleted = (Boolean) rs.getObject("fromUserDeleted");

            // Construct a Message only if it passes possible filters
            if ((validToUserID != null && toUserID != validToUserID) ||
                    (validToUserID != null && toUserDeleted)) {
                continue; // not TO this user, or TO user deleted
            }
            if ((validFromUserID != null && fromUserID != validFromUserID) ||
                    (validFromUserID != null && fromUserDeleted)) {
                continue; // not FROM this user, or FROM user deleted
            }
            if (validTypes != null && !validTypes.contains(messageType)) {
                continue; // not correct type
            }
            message = new Message(messageID, messageType, toUserID,
                    fromUserID, subject, content, date,
                    messageRead);

            messages.add(message);
        }

        return messages;
    }

    public String printDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public String printShortDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @Override
    public int compareTo(Message o) {
        return -date.compareTo(o.date);
    }

    public String display(DBConnection connection) throws SQLException {

        StringBuilder html = new StringBuilder();
        html.append(
                "<div class='row'><br>" +
                        "<div class='col-md-8'>" +
                        "<h6>Sent " + printDate() + "</h6>" +
                        "<div class='panel panel-default'>" +
                        "<div class='panel-body' >" +
                        "<dl class='dl-horizontal'>" +
                        "<dt>Sent from</dt> <dd>" + connection.getUserName(from) + "</dd>" +
                        "<dt>Sent to</dt>   <dd>" + connection.getUserName(to) + "</dd>" +
                        "<dt>Subject</dt>   <dd>" + subject + "</dd>" +
                        "<dt>Content</dt>   <dd>" + content + "</dd>" +
                        "</dl> " +
                        "</div>" +
                        "<div class='panel-footer text-right'>"
        );
        if (type == NOTE) {
            html.append(
                    "<form class='btn-group btn-group-sm' action='NewMessageServlet' method='POST'>" +
                            "<input name='type' value=" + NOTE + " type='hidden'>" +
                            "<input name='toUserName' value=" + connection.getUserName(from) + " type='hidden'>" +
                            "<input name='subject'  value='Re: " + subject + "' type='hidden'>" +
                            "<button class='btn btn-default' type='submit'>Reply</button>" +
                            "</form>"
            );
        }
        html.append(
                "<form class='btn-group btn-group-sm' action='DeleteMessageServlet' method='POST'>" +
                        "<input name='messageID' value=" + messageID + " type='hidden'>" +
                        "<button class='btn btn-danger' type='submit'>Delete</button>" +
                        "</form>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>"
        );
        return html.toString();
    }

    public String displayRow(DBConnection connection, int index, int requestingUserID)
            throws SQLException {
        // Determine how long the subject / content preview will be
        String contentNoHTML = content.replaceAll("\\<[^>]*>", "");
        int lenSubject = Math.min(subject.length(), PREVIEW);
        int lenContent = Math.min(contentNoHTML.length(), PREVIEW - lenSubject);

        boolean userIsReceiver = requestingUserID == to;

        StringBuilder html = new StringBuilder();
        html.append(
                "<tr class=\"clickableRow\" id=" + index + " >" +
                        "<input type='hidden' id=" + index + " value=\"" + this.display(connection) + "\" />" +
                        "<input type='hidden' id=\"messageID" + index + "\" value=\"" + messageID + "\" />" +
                        "<input type='hidden' id=\"fromUserID" + index + "\" value=\"" + from + "\" />" +
                        "<td>" + strong(connection.getUserName(from), userIsReceiver) + "</td>" +
                        "<td>" + strong(connection.getUserName(to), userIsReceiver) + "</td>" +
                        "<td>" + strong(subject.substring(0, lenSubject), userIsReceiver) +
                        " - " + contentNoHTML.substring(0, lenContent) + "... </td>" +
                        "<td>" + strong(printShortDate(), userIsReceiver) + "</td>" +
                        "</tr>"
        );
        return html.toString();
    }

    private String strong(String text, boolean makeBold) {
        if (!read && makeBold) {
            return "<strong>" + text + "</strong>";
        }
        return text;
    }

    public String alert(DBConnection connection) throws SQLException {
        StringBuilder html = new StringBuilder();
        html.append(
                "<div class='alert alert-danger alert-dismissable' >" +
                        "<button type='button' class='close' data-dismiss='alert' >x</button>" +
                        "<h4 class='alert-heading'>Admin: " + connection.getUserName(from) + "</h4>" +
                        content +
                        "</div>");
        return html.toString();
    }

    public String compact(DBConnection connection) throws SQLException {
        StringBuilder html = new StringBuilder();
        html.append(
                "<li class='list-group-item'>" +
                        "<h4 class='list-group-item-heading'>" + connection.getUserName(from) +
                        "<small>    " + printShortDate() + "</small></h4>" +
                        "<h5 style='color:DimGray'>" + subject + "</h5>   " +
                        "</li>");
        return html.toString();
    }
}
