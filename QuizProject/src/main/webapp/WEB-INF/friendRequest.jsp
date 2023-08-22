<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page import="model.*" %>
<!DOCTYPE html>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Friend request</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="users" />
	<div class="container">
		<div class="jumbotron">
			<div class="row">
				<div class="col-md-10">
					<%
						String html = (String) request.getAttribute("html");
						if (html != null) {
							out.println(html);
						} else {
							response.sendRedirect("homepage.jsp");
						}
					%>
				</div>
			</div>
		</div>
	</div>
</body>
</html>