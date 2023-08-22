<%@ page language="java" contentType="text/html"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create a new Question-Response Question!</title>
</head>
<body>
<tag:navbar session="<%= session %>" activeTab="quizzes" />
<div class="container">
    <div class="jumbotron">
        <h2 style="color:#428bca">Please write your question</h2>
        <h4>To formulate a question, please enter both the question and the potential answers.</h4>
        <h5 style="color:#d9534f">You will not be able to go back and edit these!</h5>
        <form class="form-horizontal" role="form" action="createServlet" method="post">
            <input name="origin" type="hidden"
                   value="CreateQuestionMC" >
            <div class="form-group">
                <label for="question" class="col-sm-3 control-label">Question:</label>
                <div class="col-sm-7">
                    <input name="question" id="question" class="form-control">
                </div>
            </div>
            <div class="form-group has-success">
                <label for="answer" class="col-sm-3 control-label">Correct Answer:</label>
                <div class="col-sm-7">
                    <input name="mc_correct" id="answer" class="form-control">
                </div>
            </div>
            <div class="form-group has-error">
                <label for="mc1" class="col-sm-3 control-label">Incorrect choice #1:</label>
                <div class="col-sm-7">
                    <input name="mc1" id="mc1" class="form-control">
                </div>
            </div>
            <div class="form-group has-error"">
            <label for="mc2" class="col-sm-3 control-label">Incorrect choice #2:</label>
            <div class="col-sm-7">
                <input name="mc2" id="mc2" class="form-control">
            </div>
    </div>
    <div class="form-group has-error"">
    <label for="mc3" class="col-sm-3 control-label">Incorrect choice #3:</label>
    <div class="col-sm-7">
        <input name="mc3" id="mc3" class="form-control">
    </div>
</div>
<div class="form-group">
    <div class="col-sm-offset-3 col-sm-7">
        <input class="btn btn-primary" type="submit" value="Create question">
    </div>
</div>
</form>
</div>
</div>
</body>
</html>