<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>

%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Quiz search results</title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="users" />
	<div class="container">
		<div class="jumbotron">
			<h2 style="color:#428bca">Select a quiz to go to its summary page!</h2>
			<h3>Search results:</h3>
<%
		DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
		String quizFilter = request.getParameter("quizFilter");
		int tagFilterValue = Integer.parseInt(request.getParameter("tagFilter"));
		int catFilterValue = Integer.parseInt(request.getParameter("catFilter"));
		String tagFilter = tagFilterValue == -1 ? null : String.valueOf(tagFilterValue);
		String catFilter = catFilterValue == -1 ? null : String.valueOf(catFilterValue);

		ResultSet quizzes = dbConnection.searchForQuiz(quizFilter, tagFilter, catFilter);
		Map<Integer, String> quizMap         = new HashMap<Integer, String>();
		Map<Integer, Integer> quizCreatorMap = new HashMap<Integer, Integer>();
		session.setAttribute("quiz", null);
		try {
			quizzes.beforeFirst();
			while (quizzes.next()) {
				String quizName   = quizzes.getString("quizName");
				int quizId        = quizzes.getInt("quizID");
				int creatorUserID = quizzes.getInt("quizCreatorUserID");
				quizMap.put(quizId, quizName);
				quizCreatorMap.put(quizId, creatorUserID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
		if (quizMap.size() > 0) {
			out.println("<table class='table table-hover table-condensed'>" +
						"<thead><tr><th>Quiz name</th><th>Creator</th></tr></thead><tbody>");

			for ( Integer quizId : quizMap.keySet() ) {
				String creatorUserName = dbConnection.getUserName( quizCreatorMap.get(quizId) );
				String quizName        = quizMap.get(quizId);
				out.println("<tr><td><a class='btn btn-primary' href='quizSummary.jsp?quizID=");
				out.println(quizId + "' >" + quizName + "</a></td>");
				out.println("<td><a class='btn btn-default' href='userpage.jsp?userID=");
				out.println(quizCreatorMap.get(quizId) + "' >" + creatorUserName + "</a></td></tr>");
			}
			out.println("</tbody></table>");

		} else {
			out.println("<h4><em>No quizzes met your search criteria</em>");
			out.println("<small>try another search <a class='btn btn-primary' ");
			out.println("href='quizSearch.jsp'>here</a></small></h4>");
		}
%>
		</div>
	</div>
</body>
</html>

