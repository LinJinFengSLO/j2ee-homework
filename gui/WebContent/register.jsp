<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>

<!DOCTYPE html>
<html>
<head>

<%@ include file="htmlCommonHeader.jsp" %>

</head>
<body>

	<%
		HttpURLConnection connection = null;	
        URL url = new URL("http://localhost:8080/TaskManagement/security");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        connection.getInputStream();
        String whoAmIResponse = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
        whoAmIResponse.replaceAll("(\\r|\\n)", "");
        
        // If user already logged in take him to home page
        if (!whoAmIResponse.contains("NOT_LOGGED_IN")) {
        	response.sendRedirect("index.jsp");
        }
	%>

	<div id="taskManagementContainer">
	
		<div id="logoPanel">
		 	Hello guest, please log in or register.
			<!-- Generating proper menu -->
	  		<jsp:include page="getMenu.jsp">
	  			<jsp:param name="isLoggedIn" value="false" />
			    <jsp:param name="isAdmin" value="false" />
			</jsp:include>
		</div>
				
	  	<div id="mainPanel">
	  	
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="RegisterPage" />
			</jsp:include>
		
			<br><br>
			
			<form name="input" action="http://localhost:8080/TaskManagement/security" method="post">
				<fieldset>
					<input type="hidden" name="actionName" value="register">
					<input type="hidden" name="successTargetLink" value="http://localhost:8080/TaskManagement_UI/index.jsp">
					<input type="hidden" name="failureTargetLink" value="http://localhost:8080/TaskManagement_UI/register.jsp">
					<p><label for="email">Email</label><input type="text" name="email" /><br></p>
					<p><label for="password1">Password</label><input type="password" name="password1" /><br></p>
					<p><label for="password2">Repeat Password</label><input type="password" name="password2" /><br></p>
					<p><label for="firstname">First name</label><input type="text" name="firstname" /><br></p>
					<p><label for="lastname">Last name</label><input type="text" name="lastname" /><br></p>
					<p><label for="nickname">nick name</label><input type="text" name="nickname" /><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>