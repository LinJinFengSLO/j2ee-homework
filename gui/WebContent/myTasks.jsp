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
		
				<c:import var="PageInfo" url="myTasks.xml"/>
				<x:parse doc="${PageInfo}" var="infoXml"/>

			    <table class="center">
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

				    	  	
			<%
				// TODO: request xml from server (+ pass coockie)
			%>
				
					<c:import var="MyTasks" url="myTasks.xml"/>
					<x:parse doc="${MyTasks}" var="myTasksXml"/>

					<x:forEach select="$myTasksXml/TaskManagement/Tasks/Task">
						<tr>
							<td><x:out select="TaskId"/></td>
							<td><x:out select="Description"/></td>
							<td><x:out select="CreationDate"/></td>
							<td><x:out select="DueDate"/></td>
							<td>
								<ul>
									<x:forEach select="PriorTasks/Task">
										<li>
											<x:out select="TaskName"/>
										</li>
									</x:forEach>
								</ul>
							</td>
							<td>
								<ul>
									<x:forEach select="UsersAssigned/User">
										<li>
											<x:out select="UserName"/>
											<x:out select="IsLate"/>
										</li>
									</x:forEach>
								</ul>
							</td>
							<td><x:out select="Status"/>&nbsp;
							<x:out select="IsLate"/>
							</td>
							<td>
								<input type="button" class="editButton" value="Edit" onclick="window.location.href='EditTask.jsp?taskId=<x:out select="TaskId"/>'">
							</td>
						</tr>
					</x:forEach>
			    </table>
	  				  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>