<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Crate a new Quiz</title>
</head>
<body>
    <h4>Here's the area where you're able to generate a new quiz.</h4>
    <h3>Let's initiate the process of crafting your quiz – kindly fill out the form below to commence.</h3>
        <form action="QuizCreateServlet" method="post">
        <div class="form-group">
            <input name="origin" type="hidden" value="CreateQuiz" >
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
                        "Shuffle question sequence?"
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-md-10">
                <div class="checkbox">
                    <label>
                        <input name="quizParams" value="onePage" type="checkbox">
                        Display all questions on a single page?
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-md-10">
                <div class="checkbox">
                    <label>
                        <input name="quizParams" value="practiceMode" type="checkbox">
                        Can be taken in practice mode?
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-md-10">
                <div class="checkbox">
                    <label>
                        <input name="quizParams" value="immediateCorrection" type="checkbox">
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
