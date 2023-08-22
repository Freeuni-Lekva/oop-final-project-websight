<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page import="model.Message, model.User"%>
<!DOCTYPE html>
<html>
<%
	// Figure out the message type
	String messageType;
	Integer messageCode = Integer.parseInt( request.getParameter("type") );
	if ( messageCode.equals(Message.TYPE_NOTE) ) {
		messageType = "note";
	//needs additional types code
	} else {
		messageType = "message";
	}
	String error = (String) request.getAttribute("error");
	User user = (User) session.getAttribute("user");
%>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>New <%= messageType %></title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="messages" />
		<div class="container">
			<div class="jumbotron">
				<h2 style="color:#428bca">New <%= messageType %></h2>
				<% if (error != null) out.println(error); %>

				<form action="NewMessageServlet" method="post">
					<%= request.getAttribute("html")  %>
					<div class="row">
						<br>
				<%
					if (user != null && user.getUserID() != -1) {
						out.println("<input class='btn btn-default' type='submit' value='Submit'>");
					} else {
						out.println("<input class='disabled btn btn-default' type='submit' value='Submit'>");
					}
				%>
					</div>
		 		</form>
		 	</div>
		</div>
</body>
</html>