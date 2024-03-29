<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="constants.XmlNamingConventions" %>

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
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
	        for (int i=0; i<cookies.length; ++i)
	        	connection.addRequestProperty("Cookie", cookies[i].toString());
        }
        connection.setRequestMethod("GET");
        connection.connect();
        connection.getInputStream();
        String whoAmIResponse = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
        whoAmIResponse.replaceAll("(\\r|\\n)", "");

	%>
	
	<!--  Parsing the WhoAmI xml -->
	<x:parse var="whoAmI">
		<% out.print(whoAmIResponse); %>
	</x:parse>
	

	<div id="taskManagementContainer">
	
		<div id="logoPanel">	
			<x:if select="$whoAmI/WhoAmI/LoggedInAs!='NOT_LOGGED_IN'">
	            Hello <x:out select="$whoAmI/WhoAmI/LoggedInAs"/>!
	        </x:if>
		  	<x:if select="$whoAmI/WhoAmI/LoggedInAs='NOT_LOGGED_IN'">
	            Hello guest, please log in or register.
	        </x:if>
	        
		  	<!-- Generating proper menu -->
		  	<% if (whoAmIResponse.contains("NOT_LOGGED_IN")) {%>
		  		<jsp:include page="getMenu.jsp">
		  			<jsp:param name="isLoggedIn" value="false" />
				    <jsp:param name="isAdmin" value="false" />
				</jsp:include>
			<%} else { 
				if (whoAmIResponse.contains("ADMIN")) {%>
					<jsp:include page="getMenu.jsp">
						<jsp:param name="isLoggedIn" value="true" />
				    	<jsp:param name="isAdmin" value="true" />
				   	</jsp:include>
				<%} else { %>
					<jsp:include page="getMenu.jsp">
						<jsp:param name="isLoggedIn" value="true" />
						<jsp:param name="isAdmin" value="false" />
					</jsp:include>
				<%}
			}%>
		</div> <!-- logoPanel -->
				
	  	<div id="mainPanel">

		<h3>Upload File to load tasks:<h3/><br/><br/>
		<form action="http://localhost:8080/TaskManagement/task" method="post" enctype="multipart/form-data">
		    <input type="hidden" name="action" value="tasksFromXml"/>
		    <input type="file" name="filestring" />
		    <input type="submit" />
		</form>


	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>