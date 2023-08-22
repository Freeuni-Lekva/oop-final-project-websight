<%@ tag language="java" description="Site-wide navbar" pageEncoding="ISO-8859-1"%>
<%@ attribute name="session" type="javax.servlet.http.HttpSession"
	rtexprvalue="true" required="true" %>
<%@ attribute name="activeTab" rtexprvalue="true" required="true" %>
<%@ tag import="main.java.model.User" %>
<%
	String userName = "Guest";
	Integer userID  = -1;
	User user       = null;

	if (session != null) {
		user = (User) session.getAttribute("user");
	}
	if (user != null) {
		userName = user.getUserName();
		userID   = user.getUserID();
	}
%>
<%!
	public String getAnchor(String text, String href, boolean active) {
		StringBuilder html = new StringBuilder();
		if (active) { html.append("<li class='active'>"); }
		else        { html.append("<li>"); }

		html.append("<a href='" + href + "'>" + text + "</a></li>");
		return html.toString();
	}


	public String getLoginStatus(String userName, int userID) {
		String html =
			"<p class='navbar-text navbar-right'>Logged in as " + userName +
			" <a href='LogoutServlet' class='navbar-btn btn-danger btn-sm'>Log out</a></p>";
		return html;
	}


	public String getGuestButtons() {
		String html =
			"<div class='navbar-right'>" +
			  "<a class='btn navbar-btn btn-primary btn-sm' href='homepage.jsp'>Log in</a>" +
			  "<a class='btn navbar-btn btn-default btn-sm' href='createAccount.jsp'>Sign up</a>" +
			"</div>";

		return html;
	}

	public String getQuizzesDropdown(String userName, String text, boolean active) {
		if ( userName.equals("Guest") || userName.equals("") ) {
			return getAnchor("Quizzes", "quizSearch.jsp", active);
		}
		StringBuilder html = new StringBuilder();
		if (active) { html.append("<li class='active dropdown'>"); }
		else        { html.append("<li class='dropdown'>"); }

		html.append(
		 "<a href='#' class='dropdown-toggle' data-toggle='dropdown'>" +
           text + " <b class='caret'></b></a>" +
           "<ul class='dropdown-menu'>" +
      	     getAnchor("Search quizzes", "quizSearch.jsp", false) +
       	     getAnchor("Create new quiz!", "CreateQuiz.jsp", false) +
	      "</ul>" +
		"</li>");
		return html.toString();
	}
%>

<div class="container">
	<div class="navbar navbar-default" role="navigation">
		<div class="container-fluid">
			<!-- This is used to toggle navbar display only when screen size is very small -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#quizoramaNav">
					<span class="sr-only">Toggle button</span>
					<span class="icon-bar" role="presentation"></span>
					<span class="icon-bar" role="presentation"></span>
					<span class="icon-bar" role="presentation"></span>
				</button>
				<a href="#" class="navbar-brand" style="color:#428bca;font-weight:bold">quizorama</a>
			</div>

			<!-- Actual navbar contents -->
			<div class="collapse navbar-collapse" id="quizoramaNav">
				<ul class="nav navbar-nav">
				<% // Must figure out which tab is active
					if ("home".equals( activeTab )) {
						out.println( getAnchor("Home", "homepage.jsp", true) );
					} else {
						out.println( getAnchor("Home", "homepage.jsp", false) );
					} if ("quizzes".equals( activeTab )) {
						out.println( getQuizzesDropdown(userName, "Quizzes", true) );
					} else {
						out.println( getQuizzesDropdown(userName, "Quizzes", false) );
					} if ("users".equals( activeTab )) {
						out.println( getAnchor("Users", "userSearch.jsp", true) );
					} else {
						out.println( getAnchor("Users", "userSearch.jsp", false) );
					} if ("messages".equals( activeTab )) {
						out.println( getAnchor("Messages", "ReadMessagesServlet?navtab=inbox", true) + "</ul>");
					} else {
						out.println( getAnchor("Messages", "ReadMessagesServlet?navtab=inbox", false) + "</ul>");
					} if (userID == -1 || userName == "") {
						out.println( getGuestButtons() );
					} else {
						out.println( getLoginStatus(userName, userID) );
					}
				%>
			</div>
		</div>
	</div>

</div>