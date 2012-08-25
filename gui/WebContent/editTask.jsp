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
			    <jsp:param name="pageName" value="EditTaskPage" />
			</jsp:include>
			
	  		<br><br>
	  		
			<form name="input" action="security" method="put">
  	  	
			<%
				// TODO: request xml from server (+ pass coockie)
			%>			
				<fieldset>
					<p><label for="taskID">ID</label><input type="text" disabled="disabled" name="taskID" value="XXX"/><br></p>
					<p><label for="taskName">Name</label><input type="text" name="taskName" value="XXX"/><br></p>
					<p><label for="taskDescription">Description</label><input type="text" name="taskDescription" value="XXX"/><br></p>
					<p><label for="creationDate">Creation Date</label><input type="text" disabled="disabled" name="creationDate" value="XXX" /><br></p>
					<p><label for="dueDate">Due Date</label><input type="text" name="dueDate" value="XXX" /><br></p>
					<p><label for="status">Status</label>
									<select>
										<option value="NOT_STARTED">NOT_STARTED</option>
										<option value="ON_GOING" selected="selected">ON_GOING</option>
										<option value="COMPLETED">COMPLETED</option>
									</select><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>		
				
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>