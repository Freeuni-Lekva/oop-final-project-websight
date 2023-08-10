<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Error</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="quizzes" />
		<div class="container">
			<div class="jumbotron">
				<h2 style="color:#d9534f">Sorry!<small> Something went wrong!</small></h2>
			</div>
		</div>
</body>
</html>