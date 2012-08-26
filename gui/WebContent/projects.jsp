<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>

<!--  check whoami and if not admin then redirect -->
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
			    <jsp:param name="pageName" value="MyProjectsPage" />
			</jsp:include>
		
				<c:import var="userDataXml" url="userData.xml"/>
				<x:parse doc="${userDataXml}" var="userData"/>

			    <table class="center">
			        <tr>
				        <th>Id</th>
				        <th>Name</th>
				        <th>Description</th>
				        <th>Tasks</th>
				        <th>Users Assigned</th>
				        <th>Edit</th>
				    </tr>
				    	  	
			<%
				// TODO: request relevant xml from server (+ pass coockie)
			%>
			
					<x:forEach select="$userData/TaskManagement/Projects/Project">
						<tr>
							<td><x:out select="Id"/></td>
							<td><x:out select="Name"/></td>
							<td><x:out select="Description"/></td>
							<td>
								<ul>
									<x:forEach select="Tasks/Task">
										<li>
											<x:out select="Name"/> (<x:out select="Status"/>)
										</li>
									</x:forEach>
								</ul>
							</td>
							<td>
								<ul>
									<x:forEach select="UsersAssigned/User">
										<li>
											<x:out select="Name"/>
										</li>
									</x:forEach>
								</ul>
							</td>
							<td>
								<input type="button" class="editButton" value="Edit" onclick="window.location.href='editTask.jsp?projectId=<x:out select="Id"/>'">
							</td>
						</tr>
					</x:forEach>
			    </table>
	  				  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>