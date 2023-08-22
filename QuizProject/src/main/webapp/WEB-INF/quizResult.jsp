<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@ page import="model.*" %>
<%@ page import="model.QuestionPackage.*" %>
<%@ page import="model.AnswerPackage.*" %>

<%@ page import="java.util.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<tag:navbar session="<%= session %>" activeTab="" />
<% Quiz quiz = (Quiz) request.getAttribute("quiz");
    User user = (User) session.getAttribute("user");
    DBConnection db = (DBConnection) application.getAttribute("DBConnection"); %>
<html>
<head>
    <title><%= quiz.getQuizName() + " results" %></title>
    <style>
        .label { margin: 2px 2px; }
    </style>
</head>
<body>
<div class="container">
    <div class="jumbotron">
        <br>
        <%
            out.println("<div class='row'><div class='col-md-8 col-md-offset-2'>");
            out.println( "<h2 style='color:#428bca'>You just finished " + quiz.getQuizName() + "!</h3>");
            if (user != null) {
                if (quiz.isPracticeModeOn()) {
                    out.println( "<h3 style='color:#f0ad4e'>You've finished this quiz in practice mode. If you wish to have your quiz recorded, take it for real!</h3>" );
                }
                out.println( quiz.getResult(user.getUserID()));
            } else {
                out.println( "<h3 style='color:#d9534f'>For your quiz attempt to be counted, please log in.</h3>");
            }
            out.println( "<a class='btn btn-primary' href='quizSummary.jsp?quizID=" +
                    quiz.getQuizID() + "'>Quiz Summary Page</a>");
            out.println("</div></div>");
            out.println("<div class='row'><div class='col-md-8 col-md-offset-2'>");
            out.println( "<h2 style='color:#428bca'>Here are the quiz answers: </h2>");

            for (Question q : quiz.getAllQuestions()) {
                out.println(q.showQuestionText());
                out.println(q.showAnswerOptions());
            }
            out.println("</div></div>");
        %>
    </div>
</div>

</body>
</html>