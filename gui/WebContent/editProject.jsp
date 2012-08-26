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
			    <jsp:param name="pageName" value="EditProjectPage" />
			</jsp:include>
			
	  		<br><br>
	  		
	  		<!--  update form action value -->
			<form name="input" action="security" method="put">
  	  	
			<%
				// TODO: request xml from server (+ pass coockie)
			%>			
				<fieldset>
					<p><label for="projectID">ID</label><input type="text" disabled="disabled" name="projectID" value="XXX"/><br></p>
					<p><label for="projectName">Name</label><input type="text" name="projectName" value="XXX"/><br></p>
					<p><label for="projectDescription">Description</label><input type="text" name="projectDescription=" value="XXX"/><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>		
				
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>