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
        String whoAmIResponse = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
        whoAmIResponse.replaceAll("(\\r|\\n)", "");
        
        // If user isn't logged in take him to home page
        if (whoAmIResponse.contains("NOT_LOGGED_IN")) {
        	response.sendRedirect("index.jsp");
        }
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
	        
	        <!-- Generating proper menu -->
			<%if (whoAmIResponse.contains("ADMIN")) {%>
				<jsp:include page="getMenu.jsp">
					<jsp:param name="isLoggedIn" value="true" />
			    	<jsp:param name="isAdmin" value="true" />
			   	</jsp:include>
			<%} else { %>
				<jsp:include page="getMenu.jsp">
					<jsp:param name="isLoggedIn" value="true" />
					<jsp:param name="isAdmin" value="false" />
				</jsp:include>
			<%}%>
		</div>
		
	  	<div id="mainPanel">
	  	
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="MyTasksPage" />
			</jsp:include>
		
		
			<%
				connection = null;	
		        url = new URL("http://localhost:8080/TaskManagement/user");
		        connection = (HttpURLConnection) url.openConnection();
		        connection.setRequestMethod("GET");
		        connection.connect();
		        connection.getInputStream();
		        String myResponse = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
		        myResponse.replaceAll("(\\r|\\n)", "");
		        
		        // If user isn't logged in take him to home page
		        if (myResponse.contains("NOT_LOGGED_IN")) {
		        	response.sendRedirect("index.jsp");
		        }
			%>
			
			<!--  Parsing the WhoAmI xml -->
			<x:parse var="userData">
				<% out.print(myResponse); %>
			</x:parse>
		
		
		
				<c:import var="userDataXml" url="userData.xml"/>
				<x:parse doc="${userDataXml}" var="userData"/>

			    <table class="center">
			        <tr>
			        	<th>Id</th>
				        <th>Name</th>
				        <th>Description</th>
				        <th>Creation Date</th>
				        <th>Due Date</th>
				        <th>Prior Tasks</th>
				        <th>Users Assigned</th>
				        <th>Status</th>
				        <th>Project</th>
				        <th>Edit</th>
				    </tr>
				    	  	
			<%
				// TODO: request xml from server (+ pass coockie)
			%>
			
					<x:forEach select="$userData/TaskManagement/Tasks/Task">
						<tr>
							<td><x:out select="Id"/></td>
							<td><x:out select="Name"/></td>
							<td><x:out select="Description"/></td>
							<td><x:out select="CreationDate"/></td>
							<td><x:out select="DueDate"/></td>
							<td>
								<ul>
									<x:forEach select="PriorTasks/Task">
										<li>
											<x:out select="Name"/>
										</li>
									</x:forEach>
								</ul>
							</td>
							<td>
								<ul>
									<x:forEach select="UsersAssigned/User">
										<li>
											<x:out select="Name"/>
											<x:out select="IsLate"/>
										</li>
									</x:forEach>
								</ul>
							</td>
							<td><x:out select="Status"/>&nbsp;
							<x:out select="IsLate"/>
							</td>
							<td><x:out select="ProjectName"/></td>
							<td>
								<input type="button" class="editButton" value="Edit" onclick="window.location.href='editTask.jsp?taskId=<x:out select="Id"/>'">
							</td>
						</tr>
					</x:forEach>
			    </table>
	  				  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>