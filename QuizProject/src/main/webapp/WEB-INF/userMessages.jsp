<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib  prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page import="/main/java.*,java.util.*,java.sql.*" %>
<%
	ArrayList<Message> messages;
	DBConnection connection;
	String title = "Guest messages";
	String userName = null;
	Integer userID  = null;

	connection        = (DBConnection) application.getAttribute("DBConnection");
	User user         = (User) session.getAttribute("user");
	messages          = (ArrayList<Message>) request.getAttribute("messages");
	String mailType   = (String) request.getAttribute("navtab"); // inbox vs sent
	String msgUpdate  = (String) request.getAttribute("messageUpdate"); // if new message just made

	if (messages == null) { messages = new ArrayList<Message>(); }
	if (user == null || user.getUserID() == -1) {
		msgUpdate = "Log in or make an account to view messages.";
	} else {
		userName = user.getUserName();
		userID   = user.getUserID();
		title = userName + "'s messages";
	}
%>
<html>
<head>
	<link href="./css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<script>
		jQuery(document).ready(function($) {
			$('#backButton').hide(); // no back button to start

			$(".clickableRow").hover(function() {
				$(this).css('cursor', 'pointer');
			}); // display hand on row mousevoer

			$(".clickableRow").click(function() {
				var idx        = $(this).attr("id");
				var msgHTML    = $(this).find('#' + idx).val();
				var msgID      = $(this).find('#messageID' + idx).val();
				var fromUserID = $(this).find('#fromUserID' + idx).val();

				$('#messageTable').hide();
				$('#messageHTML').html( msgHTML );
				$('#backButton').show();

				// Update DB to reflect that message was read
				// fromUserID is necessary so that a message is read only if the
				// current user is the rec
				$.get("UpdateReadMessageServlet?messageID=" + msgID +
					  "&fromUserID=" + fromUserID, function(responseIgnored) {});

	      	}); // Toggle messageTable off, messageHTML on

		});

	</script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><%= title %></title>
</head>
<body>
	<tag:navbar session="<%= session %>" activeTab="messages" />
	<!-- Display inbox vs sent based on navtab attr -->
	<div class="container">
		<div class="panel panel-primary">
			<div class='panel-heading'><span></span></div>
			<div class="panel-body">
				<div class="text-left">
					<%
					out.println("<h2 style='color:#428bca'>" + title + "</h2>");
					if ( !(msgUpdate == null) ) { // if message update happened, notify user
						out.println("<h5><span style='color:#d9534f;'>" + msgUpdate + "</span></h5>");
					}
					out.println("<br>");
					%>
				</div>
				<ul class="nav nav-tabs">
					<%
					if ( "inbox".equals(mailType) || mailType == null ) {
						out.println("<li class=\"active\"><a class=\"disabled\">Inbox</a></li>");
						out.println("<li><a href=\"ReadMessagesServlet?navtab=sent\">Sent</a></li>");

					} else {
						out.println("<li><a href=\"ReadMessagesServlet?navtab=inbox\">Inbox</a></li>");
						out.println("<li class=\"active\"><a class=\"disabled\">Sent</a></li>");
					}
					%>
					<li class="dropdown">
		   				<a class="dropdown-toggle" data-toggle="dropdown" href="#">
		   					New message<span class="caret"></span>
		   				</a>
					    <ul class="dropdown-menu">
					    	<li>
					    	<%= "<a href=\"NewMessageServlet?type=" + Message.TYPE_NOTE + "\">" %>
					    		Note</a>
					    	</li>
					    	<li>
					    	<%= "<a href=\"NewMessageServlet?type=" + Message.TYPE_FRIEND + "\">" %>
					    		FriendRequest</a>
					    	</li>
					    	<li>
					    	<%= "<a href=\"NewMessageServlet?type=" + Message.TYPE_CHALLENGE + "\">" %>
					    		Quiz challenge</a>
					    	</li>
					    	<%
					    		if ( user != null & user.getUserID() != -1 & user.isAdmin() ) {
					    			out.println(
					    			"<li role=\"presentation\" class=\"divider\"></li>" +
					    			"<li><a href=\"NewMessageServlet?type=" + Message.TYPE_ANNOUNCEMENT +
					    			"\">Announcement</a></li>"
					    			);
					    		}
					    	%>
					    </ul>
					</li>
				</ul>
				<div id="messageHTML"></div>
				<div id="backButton">
					<a href="ReadMessagesServlet?navtab=inbox" class="btn btn-primary">Back</a>
				</div>
				<table class="table table-hover" id="messageTable">
					<thead><tr>
						<th width="15%" style="color:#428bca">From</th>
						<th width="15%" style="color:#428bca">To</th>
						<th width="50%" style="color:#428bca">Preview</th>
						<th width="20%" style="color:#428bca">Date</th>
					</tr></thead>
					<tbody>
					<%
					for (int idx=0; idx < messages.size(); idx++) {
						out.println( messages.get(idx).displayAsTableRow(connection, idx, userID) );
					}
					%>
					</tbody>
				</table>
			</div>
			<div class='panel-footer'><span></span></div>
		</div>
	</div>
</body>
</html>