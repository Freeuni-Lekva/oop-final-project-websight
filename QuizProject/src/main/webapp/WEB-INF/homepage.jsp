<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="main.java.model.User" %>
<%@ page import="main.java.servlets.LogIn" %>
<%@ page import="main.java.model.DBConnection" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<%
	String error = (String) request.getAttribute("error");

	DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
	User user = (User) session.getAttribute("user");
	if (user != null && user.getUserID() != -1) {
		Integer userID  = user.getUserID();
		String userpage = "userpage.jsp?userID=" + userID;
		response.sendRedirect(userpage);
		return;
	}

	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (int i = 0; i < cookies.length; i++) {
			Cookie c = cookies[i];
			if (c.getName().equals(LoginServlet.COOKIE_NAME)) {
				int userID = dbConnection.getUserIDFromCookie(c.getValue());
				if ( userID != -1) {
					String userName = dbConnection.getUserName(userID);
					user = new User(userID, userName, dbConnection);
					session.setAttribute("user", user);
					String userpage = "userpage.jsp?userID=" + userID;
					response.sendRedirect(userpage);
					return;
				}
			}
		}
	}
%>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta charset="UTF-8">
	<title>Welcome to quizorama!</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="home" />
	<div class="container">
		<div class="jumbotron">
			<h1 style="color:#428bca">Welcome to quizorama!</h1>
			<% if (error != null) {
				out.println("<h4 style='color:#d9534f'>" + error + "</h4>");
			} %>
			<br>
			<div class="row center-block">
				<div class="col-md-10">
					<div class="thumbnail">
						<div class="caption">
							<h4>Please log in</h4>
							<form class="form-horizontal" action="LoginServlet" method="post">
								<div class="form-group">
									<label for="userName" class="col-md-2 control-label">User name:</label>
									<div class="col-md-4">
										<input id="userName" name="userName" placeholder="Enter user name" />
									</div>
								</div>
								<div class="form-group">
									<label for="password" class="col-md-2 control-label">Password:</label>
									<div class="col-md-4">
										<input id="password" type="password" name="password" placeholder="Enter password" />
									</div>
								</div>
								<div class="form-group">
									<div class="col-md-offset-2 col-md-9">
										<input name="origin" type="hidden" value="Login" />
										<button class="btn btn-primary" type="submit">Log in</button>
										or <a class='btn btn-default' href="CreateAccount.jsp">Go to create account</a>
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