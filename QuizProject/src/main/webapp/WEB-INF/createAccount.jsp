<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<%
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <link href="./css/bootstrap.css" rel="stylesheet">
    <meta charset="UTF-8">
    <title>Create new account</title>
</head>
<body>
<tag:navbar session="<%= session %>" activeTab="home" />
<div class="container">
    <div class="jumbotron">
        <h1 style="color:#428bca">Create new account</h1>
        <% if (error != null) {
            out.println("<h4 style='color:#d9534f'>" + error + "</h4>");
        } %>
        <br>
        <div class="row center-block">
            <div class="col-md-10">
                <div class="thumbnail">
                    <div class="caption">
                        <h4>Enter proposed account information</h4>
                        <form class="form-horizontal" action="LoginServlet" method="post">
                            <div class="form-group">
                                <label for="userName" class="col-md-3 control-label">User name:</label>
                                <div class="col-md-4">
                                    <input id="userName" name="userName" placeholder="Desired user name" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="password" class="col-md-3 control-label">Password:</label>
                                <div class="col-md-4">
                                    <input id="password" type="password" name="password" placeholder="Desired password" />
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-2 col-md-9">
                                    <input name="origin" type="hidden" value="CreateAccount" />
                                    <button class="btn btn-primary" type="submit">Create account</button>
                                    or <a class='btn btn-default' href="homepage.jsp">Go to log in</a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>