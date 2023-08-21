<%--
  Created by IntelliJ IDEA.
  User: paco
  Date: 20.08.23
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Crate a new Quiz</title>
</head>
<body>
    <h4>This is the space where you can create a fresh quiz.</h4>
    <h3>Let's get your quiz creation underway – please complete the form below to begin.</h3>
        <form action="QuizCreateServlet" method="post">
        <div class="form-group">
            <input name="origin" type="hidden" value="CreateQuiz.jsp" >
            <label for="quizName" class="col-sm-2 control-label">Quiz Name:</label>
            <div class="col-md-10">
                <input name="quizName" id="quizName">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-md-10">
                <div class="checkbox">
                    <label>
                        <input name="quizParams" value="random" type="checkbox">
                        Randomize question order?
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-md-10">
                <div class="checkbox">
                    <label>
                        <input name="quizParams" value="one-page" type="checkbox">
                        Show all questions on one page?
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-md-10">
                <div class="checkbox">
                    <label>
                        <input name="quizParams" value="practice-mode" type="checkbox">
                        Can be taken in practice mode?
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-md-10">
                <div class="checkbox">
                    <label>
                        <input name="quizParams" value="immediate-correction" type="checkbox">
                        Should answers be displayed immediately after question?
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-md-10">
                <input class="btn btn-primary" type="submit" value="Create Quiz">
            </div>
        </div>
    </form>
</body>
</html>
