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
			    <jsp:param name="pageName" value="MyProfilePage" />
			</jsp:include>
			
	  		<br><br>
	  		
			<form name="input" action="security" method="put">

				<c:import var="userDataXml" url="userData.xml"/>
				<x:parse doc="${userDataXml}" var="userData"/>
  	  	
			<%
				// TODO: request xml from server (+ pass coockie)
			%>			
				<fieldset>
					<p><label for="email">Email (ID)</label><input type="text" disabled="disabled" name="email" value="<x:out select="$userData/TaskManagement/Email"/>"/><br></p>
					<p><label for="nickname">Nickname</label><input type="text" name="nickname" value="<x:out select="$userData/TaskManagement/Nickname"/>"/><br></p>
					<p><label for="firstname">First Name</label><input type="text" name="firstname" value="<x:out select="$userData/TaskManagement/FirstName"/>"/><br></p>
					<p><label for="lastname">Last Name</label><input type="text" name="lastname" value="<x:out select="$userData/TaskManagement/LastName"/>" /><br></p>
					<p><label for="type">Type</label><input type="text" name="type" disabled="disabled" value="<x:out select="$userData/TaskManagement/Type"/>" /><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>