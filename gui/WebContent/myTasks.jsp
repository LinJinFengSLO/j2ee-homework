<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>

<% // TODO: make XmlNamingConventions a local class in GUI project and import it (%page import="") %>

<!DOCTYPE html>
<html>
<head>

<%@ include file="htmlCommonHeader.jsp" %>

</head>
<body>

	<div id="taskManagementContainer">
	
		<div id="logoPanel"></div>
		
		<jsp:include page="getMenu.jsp"/>
		
	  	<div id="mainPanel">
	  	
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="MyTasksPage" />
			</jsp:include>

	  	
			<%
				// TODO: request xml from server (pass coockie)
			%>
			
				<c:import var="PageInfo" url="myTasks.xml"/>
				<x:parse doc="${PageInfo}" var="infoXml"/>

				<h2><x:out select="$infoXml/TaskManagement/RegisterPage/Title"/></h2><br>
				<x:forEach select="$infoXml/TaskManagement/RegisterPage/TextLine" var="textLine"><br>
				   <x:out select="$textLine"/>
				</x:forEach> 
	  				  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>