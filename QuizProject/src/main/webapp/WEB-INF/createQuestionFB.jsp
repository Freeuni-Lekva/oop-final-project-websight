<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <link href="./css/bootstrap.css" rel="stylesheet">
    <script type="text/javascript" src="./js/jquery.js"></script>
    <script type="text/javascript" src="./js/bootstrap.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Create a new Fill-in-the-Blank Question!</title>
</head>
<body>
<tag:navbar session="<%= session %>" activeTab="quizzes" />
<div class="container">
    <div class="jumbotron">
        <h2 style="color:#428bca">Please write your question</h2>
        <h4>Please fill what comes before the blank, the blank, and what comes after</h4>
        <h5 style="color:#d9534f">You will not be able to go back and edit these!</h5>
        <form class="form-horizontal" role="form" action="QuizCreateServlet" method="post">
            <input name="origin" type="hidden"
                   value="CreateQuestionFB" >
            <div class="form-group">
                <label for="pre" class="col-sm-4 control-label">Pre-blank:</label>
                <div class="col-sm-5">
                    <input name="pre" id="pre" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label for="blank" class="col-sm-4 control-label">Blank:</label>
                <div class="col-sm-5">
                    <input name="blank" id="blank" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label for="post" class="col-sm-4 control-label">Post-blank:</label>
                <div class="col-sm-5">
                    <input name="post" id="post" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-10">
                    <div class="checkbox">
                        <label>
                            <input name="otherResponsesCheck" type="checkbox" value="yes">
                            Are other responses allowed?
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="post" class="col-sm-4 control-label">Tilda (~) delimited list of other responses:</label>
                <div class="col-sm-5">
                    <input class="form-control" name="otherResponses">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-7">
                    <input class="btn btn-primary" type="submit" value="Create question">
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>