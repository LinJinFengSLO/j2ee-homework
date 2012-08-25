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
			    <jsp:param name="pageName" value="AdminPage" />
			</jsp:include>
			
	  		<br><br>
	  		
			<form name="input" action="security" method="put">
  	  	
			<%
				// TODO: request xml from server (+ pass coockie)
			%>			
				<fieldset>
					<p><label for="taskName">Name</label><input type="text" name="taskName" value="XXX"/><br></p>
					<p><label for="taskDescription">Description</label><input type="text" name="taskDescription" value="XXX"/><br></p>
					<p><label for="dueDate">Due Date</label><input type="text" name="dueDate" value="XXX" /><br></p>
					<p><label for="project">Project</label>
									<select>
										<option value="projectName">project1</option>
									</select><br></p>
					<p class="submit"><input type="submit" value="Create Task" /></p>
				</fieldset>		
				
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>