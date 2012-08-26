<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
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
        
		%> <h4> <%=myResponse %></h4> <%
	
	%>
	<div id="taskManagementContainer">
	
		<div id="logoPanel"></div>
		
		<jsp:include page="getMenu.jsp"/>
		
	  	<div id="mainPanel">
	  		<!-- check if user is logged in - if so, don't display getInfo -->
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="IndexPage" />
			</jsp:include>
	  			  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>