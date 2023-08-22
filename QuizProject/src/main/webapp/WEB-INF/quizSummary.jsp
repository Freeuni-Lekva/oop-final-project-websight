<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page import=" java.util.*, java.text.DecimalFormat, model.*" %>
<!DOCTYPE html>

<%
    DecimalFormat percent = new DecimalFormat("##0.#");
    DecimalFormat score = new DecimalFormat("#,##0.0#");
    DBConnection connection;
    Integer quizID     = null;
    Integer userRating = null;
    Integer userID     = null;
    String userType, userName;

    //Get DBConnection, user, and relevant quiz info
    connection = (DBConnection) application.getAttribute("DBConnection");
    User user  = (User) session.getAttribute("user");

    if (request.getParameterMap().containsKey("quizID")) { // error handling
        quizID     = Integer.parseInt( request.getParameter("quizID") );
    } else {
        request.getRequestDispatcher("error.jsp").forward(request, response);
        return;
    }

    Quiz quiz                    = new Quiz(quizID, connection);
    String quizName              = quiz.getQuizName();
    String quizSummary           = quiz.getQuizSummary();
    Integer creatorID            = quiz.getquizCreatoruserID();
    String creatorName           = connection.getUserName(creatorID);
    double numStars              = Math.floor( quiz.getRating() ); // no half stars
    Integer numQuestions         = quiz.getQuestionsNum();
    double avgScore              = quiz.getQuestionsNum()();
    boolean supportsPracticeMode = quiz.getPractiveMode();
    ArrayList<String> categories = quiz.getCategories();
    ArrayList<String> tags       = quiz.getTags();
    String flagNote = (String) request.getAttribute("flagNote"); // if user just flagged quiz

    if (user == null || user.getUserID() == -1) { // guest
        userID   = -1;
        userType = "guest";
        userName = "Guest";
    } else {
        userID     = user.getUserID();
        userName   = user.getUserName();
        userRating = quiz.getUserStanding(userID);

        if ( user.isAdmin() ) { userType = "admin"; }
        else { userType = "standard"; }
    }

    ArrayList<String> topScores    = QuizHistory.getTopScores(null, quizID, connection);
    ArrayList<String> recentScores = QuizHistory.getRecentScoresNoName(null, quizID, connection);
    ArrayList<String> userScores   = QuizHistory.getTopScores(userID, quizID, connection);
%>
<%! // Helper functions
    public final int NUM_SCORES = 10; // number of scores to display

    /*
     * Displays scores as a table
     */
    public String printPerformance(ArrayList<String> performanceRows) {
        StringBuilder html = new StringBuilder();

        if ( performanceRows.size() == 0 ) { // no scores
            return "<h4><em>No scores to display</em></h4>";
        }

        html.append(  // Table header
                "<table class='table table-hover table-condensed'>" +
                        "<thead><tr><th>#</th><th>Score</th><th>User</th></tr></thead><tbody>"
        );

        for (int i=1; i <= performanceRows.size(); i++) { // add scores as rows
            if (i > NUM_SCORES) { break; }
            html.append("<tr><td>" + i + "</td>" + performanceRows.get(i-1) + "</tr>");
        }

        html.append("</tbody></table>"); // close tags
        return html.toString();
    }

    /*
     * Displays quiz rating as glyphicon font stars
     */
    public String getAvgRating(double numStars) {
        StringBuilder html = new StringBuilder();
        html.append( getStars(numStars,   true)  );
        html.append( getStars(5-numStars, false) );
        return html.toString();
    }

    /*
     * Prints labels with the specified bootstrap label class, e.g., label-primary
     * Type should specify the type of label (categories or tags)
     */
    public String printLabels(ArrayList<String> labels, String labelClass, String type) {
        StringBuilder html = new StringBuilder();

        if ( labels.size() > 0 && labels.get(0) != null) {
            String label;
            for (int i=0; i < labels.size(); i++) {
                label = labels.get(i);
                html.append( "<span class='label " + labelClass + "'>" + label + "</span>");
                if ((i+1) % 5 == 0) { html.append("<br>"); }
            }
        } else {
            html.append("<h5 style='color:#d9534f'><em>No " + type + "</em></h5>");
        }
        return html.toString();
    }
    /*
     * Helper for generating the specified number of glyphicon stars, filled or
     * unfilled.
     */
    public String getStars(double number, boolean filled) {
        String type;
        if (filled) { type = "glyphicon-star"; }
        else        { type = "glyphicon-star-empty"; }

        StringBuilder html = new StringBuilder();
        for (int i=1; i <= number; i++) {
            html.append("<span class='glyphicon " + type + "'></span>");
        }
        return html.toString();
    }

    public String getQuizLinks(String userType, Integer quizID, String quizName,
                               boolean supportsPracticeMode, String flagNote) {
        StringBuilder html = new StringBuilder();
        if ( userType.equals("guest") ) {
            html.append(
                    "<div class='col-md-3'>" +
                            "<form action='quizControler'>" +
                            "<input type='hidden' name='quizID' value=" + quizID + " />" +
                            "<button class='disabled btn btn-primary btn-sm'>Login to take quiz</button>" +
                            "</form>" +
                            "</div>");
        }  else {
            if ( supportsPracticeMode ) { html.append(
                    "<div class='col-md-4'>" +
                            "<div class='input-group'>" +
                            "<form action='quizControler'>" +
                            "<input type='hidden' name='quizID' value=" + quizID + " />" +
                            "<button class='btn btn-primary btn-sm' type='submit'>Take Quiz</button>" +
                            "<button class='btn btn-success btn-sm' name='practice'" +
                            " title='Practice quiz' value='practice' type='submit'>Practice</button>" +
                            "</form></div></div>");
            } else { html.append(
                    "<div class='col-md-3'>" +
                            "<form action='quizControler'>" +
                            "<input type='hidden' name='quizID' value=" + quizID + " />" +
                            "<button class='btn btn-primary' type='submit'>Take Quiz</button>" +
                            "</form></div>");
            }
        } if ( userType.equals("standard") ) {
            html.append(
                    "<div class='col-md-1 pull-left'>" +
                            "<form action='flagQuizServlet' method='POST'>" +
                            "<input type='hidden' name='quizID' value=" + quizID + " />" +
                            "<input type='hidden' name='quizName' value='" + quizName + "' />");
            if (flagNote != null) {
                html.append(
                        "<button class='disabled btn btn-danger btn-sm' type='submit'>" +
                                "<span class='glyphicon glyphicon-flag'></span> " +
                                "<em>" + flagNote + "</em></button>" +
                                "</form></div>");
            } else {
                html.append(
                        "<button class='btn btn-danger btn-sm' type='submit'>" +
                                "<span class='glyphicon glyphicon-flag'></span> Flag" +
                                "</button>" +
                                "</form></div>");
            }
        } else if ( userType.equals("admin") ) {
            html.append(
                    "<div class='col-md-1  pull-left'>" +
                            "<form action='deleteQuizServlet' method='POST'>" +
                            "<input type='hidden' name='quizID' value=" + quizID + " />" +
                            "<button class='btn btn-danger btn-sm' 'type='submit'>" +
                            "<span title='Delete quiz' class='glyphicon glyphicon-trash'></span> Delete" +
                            "</button>" +
                            "</form>" +
                            "</div>");}
        return html.toString();
    }

    public String getRatingDropdown(String userType, int userID, int quizID, Integer userRating) {
        if ( userType.equals("guest") ) return ""; // guests cannot rate

        StringBuilder html = new StringBuilder();
        html.append(
                "<button type='button' class='btn btn-primary btn-md dropdown-toggle' data-toggle='dropdown' >" +
                        "<span class='caret' ></span>" +
                        "<span class='sr-only' >Toggle Dropdown</span>" +
                        "</button>" +
                        "<ul class='dropdown-menu' role='menu'>" +
                        "<li role='presentation' class='dropdown-header'>Rate quiz</li>" +
                        "<li>" +
                        "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=1'>" +
                        getStars(1, true) + "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=2'>" +
                        getStars(2, true) + "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=3'>" +
                        getStars(3, true) + "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=4'>" +
                        getStars(4, true) + "</a>" +
                        "</li>" +
                        "<li>" +
                        "<a href='RateQuizServlet?userID=" + userID + "&quizID=" + quizID + "&rating=5'>" +
                        getStars(5, true) + "</a>" +
                        "</li>" +
                        "</ul>"
        );

        return html.toString();
    }
%>
<html>
<head>
    <link href="./css/bootstrap.css" rel="stylesheet">
    <script type="text/javascript" src="./js/jquery.js"></script>
    <script type="text/javascript" src="./js/bootstrap.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <style>
        .label { margin: 2px 2px; }
    </style>
    <title><%= quizName %> summary</title>
</head>
<body>
<tag:navbar session="<%= session %>" activeTab="quizzes" />
<div class="container">
    <div class="jumbotron">
        <div class="row">
            <div class="col-md-7">
                <h1><%= quizName %>
                    <a class="btn btn-default btn-sm" <% out.println("href=userP    age.jsp?userID=" + creatorID ); %> >
                        by <%= creatorName %></a>
                </h1>
                <br>
                <div class="row">
                    <p>
                            <%= quizSummary %>
                        <br><span class="badge"><%= numQuestions %> questions</span>
                    <div class="row">
                        <%= getQuizLinks(userType, quizID, quizName, supportsPracticeMode, flagNote) %>
                    </div>
                    </p>
                </div>
            </div>
            <br><br>
            <div class="col-md-5">
                <dl class="dl-horizontal text-left">
                    <dt>average rating</dt>
                    <dd>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary">
                                <%= getAvgRating(numStars) %>
                            </button>
                            <%= getRatingDropdown( userType, userID, quizID, userRating ) %>
                        </div>
                        <% if (userRating != null) {
                            out.println("<br><span class='badge'>You rated " + userRating + " / 5</span>");
                        } %>

                    </dd>
                    <dt>average score</dt>
                    <dd>
                        <h3><span class="label label-primary">
      							<%= score.format(avgScore) %> out of <%= quiz.getPossibleScore() %>
      						</span>
                        </h3><br>
                    </dd>
                    <dt>categories</dt>
                    <dd><%= printLabels(categories, "label-warning", "categories") %></dd>
                    <% if (categories.size() < 1 || categories.get(0) == null) {
                        //Only 1 category is allowed
                        out.print("<br><dd><a href=addQuizCat.jsp?quizID=" + Integer.toString(quiz.getQuizID()) + " >Add to another category...</a>");
                    } //if
                    %>
                    <br><br>
                    <dt>tags</dt>
                    <dd><%= printLabels(tags, "label-success", "tags") %></dd><br>
                    <dd><a href=<% out.print("addQuizTag.jsp?quizID=" + Integer.toString(quiz.getQuizID())); %> >Add to another tag...</a>
                </dl>


            </div>
        </div>
        <br>
        <div class="row text-center">
            <div class="col-md-4">
                <div class="thumbnail">
                    <div class="caption">
                        <h3>Top scores</h3>
                        <%= printPerformance( topScores ) %>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="thumbnail">
                    <div class="caption">
                        <h3>Recent scores</h3>
                        <%= printPerformance( recentScores ) %>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="thumbnail">
                    <div class="caption">
                        <h3><%= userName + "'s scores" %></h3>
                        <%= printPerformance( userScores ) %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
