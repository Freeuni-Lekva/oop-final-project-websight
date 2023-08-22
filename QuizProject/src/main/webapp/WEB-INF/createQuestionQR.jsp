<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create a Question-Response Question!</title>
</head>
<body>
<tag:navbar session="<%= session %>" activeTab="quizzes" />
<div class="container">
    <div class="jumbotron">
        <h2 style="color:#428bca">Please write your question</h2>
        <h4>To create a question, kindly provide both the question itself and its corresponding answer.</h4>
        <h5 style="color:#d9534f">Please note that you won't have the ability to revise these later.</h5>
        <form class="form-horizontal" role="form" action="createServlet" method="post">
            <input name="origin" type="hidden"
                   value="CreateQuestionQR" >
            <div class="form-group">
                <label for="question" class="col-sm-3 control-label">Question:</label>
                <div class="col-sm-7">
                    <input name="question" id="question" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label for="response" class="col-sm-3 control-label">Response:</label>
                <div class="col-sm-7">
                    <input name="response" id="response" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-10">
                    <div class="checkbox">
                        <label>
                            <input name="otherResponsesCheck" type="checkbox" value="yes">
                            Are other responses allowed?
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="post" class="col-sm-3 control-label">Tilda (~) delimited list of other responses:</label>
                <div class="col-sm-7">
                    <input class="form-control" name="otherResponses">
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