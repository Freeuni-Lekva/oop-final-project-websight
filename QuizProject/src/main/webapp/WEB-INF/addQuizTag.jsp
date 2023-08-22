<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page import="model.*, java.sql.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <link href="./css/bootstrap.css" rel="stylesheet">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Add a tag to a Quiz</title>
</head>
<%
    DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
    int quizID = Integer.parseInt(request.getParameter("quizID"));

%>

<body>
<tag:navbar session="<%= session %>" activeTab="quizzes" />
<div class="container">
    <div class="jumbotron">
        <%
            if ( (String) request.getAttribute("alert") != null) {
                out.println("<h2 style='color:#d9534f'>" +
                        (String) request.getAttribute("alert") + "</h2><br>");
            }
        %>
        <h2 style="color:#428bca">Add a tag to the quiz here!</h2>
        <h2><small>Select the tag to add for this quiz, or, add your own at the bottom.</small></h2>
        <div class="row">
            <div class="col-md-10">
                <div class="thumbnail">
                    <br><br>
                    <form class="form-horizontal" role="form" action="AddTagServlet" method="post">
                        <div class="form-group">
                            <label for="tagAdd" class="col-md-3 control-label">Add this tag:</label>
                            <div class="col-md-4">
                                <select class="form-control" name="tagAdd" id="tagAdd">
                                    <option value="-1" selected>Select A Tag</option>
                                    <%
                                        ResultSet rs = dbConnection.getTagTypes();
                                        rs.beforeFirst();
                                        while(rs.next()) {
                                            out.println("<option value=\"" + rs.getInt("tagID") + "\">" + rs.getString("tagName") + "</option>");
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="newTag" class="col-md-3 control-label">[Optional] New Tag Name: </label>
                            <div class="col-md-4">
                                <input name="newTag" id="newTag" class="form-control">
                                <input name="quizID" type="hidden" value=<%= quizID %> >
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-md-offset-3 col-md-3">
                                <input class="btn btn-primary" type="submit" value="Add Tag">
                            </div>
                        </div>
                    </form>
                    <br><br>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>