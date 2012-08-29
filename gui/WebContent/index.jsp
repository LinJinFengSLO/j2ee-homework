<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>  

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
	%>
	<x:parse var="whoAmI">
		<% out.print(myResponse); %>
	</x:parse>
	
	<br><br><br><br>
	<h1> Hello <x:out select="$whoAmI/WhoAmI/LoggedInAs"/></h1>

	<div id="taskManagementContainer">
	
		<div id="logoPanel"></div>
				
	  	<div id="mainPanel">
	  	
		  	<jsp:include page="getMenu.jsp">
			    <jsp:param name="userName" value='<x:out select="$whoAmI/WhoAmI/LoggedInAs"/>' />
			    <jsp:param name="role" value='<x:out select="$whoAmI/WhoAmI/Role"/>' />
			</jsp:include>
	  	
	  		<!-- check if user is logged in - if so, don't display getInfo -->
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="IndexPage" />
			</jsp:include>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>