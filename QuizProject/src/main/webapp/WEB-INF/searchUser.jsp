<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="/main/java.*, java.sql.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Search for users</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="users" />
	<div class="container">
		<div class="jumbotron">
			<h2 style="color:#428bca">Search for other users here</h2>
			<h2><small>You may find users based on their user name</small></h2>
			<div class="row">
				<div class="col-md-8">
					<div class="thumbnail">
						<br><br>
						<form class="form-horizontal" role="form" action="chooseUserSearch.jsp" method="get">
							<div class="form-group">
								<label for="userNameFilter" class="col-md-3 control-label">User name filter:</label>
								<div class="col-md-5">
									<input name="userNameFilter" id="userNameFilter" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<div class="col-md-offset-3 col-md-3">
									<input class="btn btn-primary" type="submit" value="Search users">
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