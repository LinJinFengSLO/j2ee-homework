<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
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
        String myResponse = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
        myResponse.replaceAll("(\\r|\\n)", "");
        
        // If user already logged in take him to home page
        if (!myResponse.contains("NOT_LOGGED_IN")) {
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
			    <jsp:param name="pageName" value="LoginPage" />
			</jsp:include>
			
	  		<br><br>
	  		
			<form name="input" action="http://localhost:8080/TaskManagement/security" method="put">
				<fieldset>
					<p><label for="email">Email</label><input type="text" name="email" /><br></p>
					<p><label for="pwd">Password</label><input type="password" name="password" /><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>