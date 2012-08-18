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

			    <table border="1">
			        <tr>
				        <th>Name</th>
				        <th>Description</th>
				        <th>Creation Date</th>
				        <th>Due Date</th>
				        <th>Prior Tasks</th>
				        <th>Users Assigned</th>
				        <th>Status</th>
				        <th>Edit</th>
				    </tr>
			    <!--
					<c:forEach var="person" items="${people.people}" varStatus="rowCounter">
						<c:choose>
							<c:when test="${rowCounter.count % 2 == 0}">
								<c:set var="rowStyle" scope="page" value="odd"/>
				          	</c:when>
				          	<c:otherwise>
				            	<c:set var="rowStyle" scope="page" value="even"/>
				          	</c:otherwise>
						</c:choose>
						<tr class="${rowStyle}">
							<td>${person.name}</td>
							<td>${person.age}</td>
							<td>${person.height}</td>
						</tr>
			      </c:forEach>
			    -->
			    </table>


	  				  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>