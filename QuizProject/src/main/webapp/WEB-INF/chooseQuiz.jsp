<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<tag:navbar session="<%= session %>" activeTab="quizzes" />
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"><link href="./css/bootstrap.css" rel="stylesheet">
	<title>Take a Quiz!</title>
</head>
<body>

<div class="container">
	<h1>Choose a Quiz to try:</h1>
	<p></p>
	<p>Quizzes: </p>
	<p></p>
	<ul>
		<%@ page import="model.*" %>
		<%@ page import="java.util.*" %>
		<%@ page import="java.sql.*" %>
		<%
			DBConnection dbConnection = (DBConnection) application.getAttribute("DBConnection");
			ResultSet quizzes = dbConnection.getAllQuizzes();
			Map<Integer, String> quizMap = new HashMap<Integer, String>();
			session.setAttribute("quiz", null);
			try {
				quizzes.beforeFirst();
				while (quizzes.next()) {
					String quizName = quizzes.getString("quizName");
					int quizId = quizzes.getInt("quizID");
					quizMap.put(quizId, quizName);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			for (Integer quizId : quizMap.keySet()) {
				out.println("<li>");
				out.println("<input name='quizId' type='hidden' value='" + quizId + "'/>");
				out.println("<a href='QuizControllerServlet?id=" + quizId + "' >" + quizMap.get(quizId) + "</a>");
				out.println("</li>");
			}

		%>
	</ul>
</div>
</body>
</html>