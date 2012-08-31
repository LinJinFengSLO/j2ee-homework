<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory, javax.xml.parsers.DocumentBuilder, org.w3c.dom.Document " %>
<%@ page import = "org.w3c.dom.Element, java.io.ByteArrayInputStream " %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL, java.net.URLEncoder" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="java.io.StringReader" %>
<%@ page import="org.w3c.dom.NodeList" %>
<%@ page import="org.w3c.dom.Node" %>
<%@ page import="java.util.List, java.util.ArrayList" %>
<%@ page import="constants.*" %>
<%@ page import="utilities.*" %>

<!DOCTYPE html>
<html>
<head>

<%@ include file="htmlCommonHeader.jsp" %>

</head>
<body>

	<%
		// If user already logged in take him to home page
		HttpRequestsManager.HttpResponseInfo whoAmIResponseInfo = HttpRequestsManager.doGetToSecurityServlet(request.getCookies());
		whoAmIResponseInfo.responseString.replaceAll("(\\r|\\n)", "");
		if (whoAmIResponseInfo.responseString.contains("NOT_LOGGED_IN")) {
			response.sendRedirect("index.jsp");
		}
	%>
	
	<!--  Parsing the WhoAmI xml -->
	<x:parse var="whoAmI">
		<% out.print(whoAmIResponseInfo.responseString); %>
	</x:parse>
	
	<div id="taskManagementContainer">
	
		<div id="logoPanel"></div>
		
		<jsp:include page="getMenu.jsp"/>
		
	  	<div id="mainPanel">
	  	
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="AddNewTaskPage" />
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