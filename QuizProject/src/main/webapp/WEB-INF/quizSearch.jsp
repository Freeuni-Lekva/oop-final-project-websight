<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page import="model.*, java.sql.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Search quizzes</title>
</head>
<body>
<div class="container">
    <div class="jumbotron">
        <%
            if (request.getAttribute("alert") != null) {
                out.println("<h2 style='color:#d9534f'>" +
                        request.getAttribute("alert") + "</h2><br>");
            }
        %>
        <h2 style="color:#428bca">Search for available quizes here!</h2>
        <h2><small>You can search by name as well as by quiz categories and tags</small></h2>
        <div class="row">
            <div class="col-md-10">
                <div class="thumbnail">
                    <br><br>
                    <form class="form-horizontal" role="form" action="chooseQuizSearch.jsp" method="get">
                        <div class="form-group">
                            <label for="quizFilter" class="col-md-3 control-label">Quiz name filter:</label>
                            <div class="col-md-4">
                                <input name="quizFilter" id="quizFilter" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="catFilter" class="col-md-3 control-label">Optional category filter:</label>
                            <div class="col-md-4">
                                <select class="form-control" name="catFilter" id="catFilter">
                                    <option value="-1" selected>Any Category</option>
                                    <%
                                        DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
                                        ResultSet rs = dbConnection.getCategoryTypes();
                                        while(rs.next()) {
                                            out.println("<option value=\"" + rs.getInt("categoryID") + "\">" + rs.getString("categoryName") + "</option>");
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="tagFilter" class="col-md-3 control-label">Optional tag filter:</label>
                            <div class="col-md-4">
                                <select class="form-control" name="tagFilter" id="tagFilter">
                                    <option value="-1" selected>Any Tag</option>
                                    <%
                                        rs = dbConnection.getTagTypes();
                                        rs.beforeFirst();
                                        while(rs.next()) {
                                            out.println("<option value=\"" + rs.getInt("tagID") + "\">" + rs.getString("tagName") + "</option>");
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-offset-3 col-md-3">
                                <input class="btn btn-primary" type="submit" value="Search Quizzes">
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