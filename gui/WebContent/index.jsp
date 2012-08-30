<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="constants.XmlNamingConventions" %>
<%@ page import="utilities.HttpRequestsManager" %>

<!DOCTYPE html>
<html>
<head>

<%@ include file="htmlCommonHeader.jsp" %>

</head>
<body>

	<%
		HttpRequestsManager.HttpResponseInfo whoAmIResponseInfo = HttpRequestsManager.doGetToSecurityServlet(request.getCookies());
		whoAmIResponseInfo.responseString.replaceAll("(\\r|\\n)", "");		
	%>
	
	<!--  Parsing the WhoAmI xml -->
	<x:parse var="whoAmI">
		<% out.print(whoAmIResponseInfo.responseString); %>
	</x:parse>
	

	<div id="taskManagementContainer">
	
		<div id="logoPanel">	
			<x:if select="$whoAmI/WhoAmI/LoggedInAs!='NOT_LOGGED_IN'">
	            Hello <x:out select="$whoAmI/WhoAmI/LoggedInAs"/>!
	        </x:if>
		  	<x:if select="$whoAmI/WhoAmI/LoggedInAs='NOT_LOGGED_IN'">
	            Hello guest, please log in or register.
	        </x:if>
	        
		  	<!-- Generating proper menu -->
		  	<% if (whoAmIResponseInfo.responseString.contains("NOT_LOGGED_IN")) {%>
		  		<jsp:include page="getMenu.jsp">
		  			<jsp:param name="isLoggedIn" value="false" />
				    <jsp:param name="isAdmin" value="false" />
				</jsp:include>
			<%} else { 
				if (whoAmIResponseInfo.responseString.contains("ADMIN")) {%>
					<jsp:include page="getMenu.jsp">
						<jsp:param name="isLoggedIn" value="true" />
				    	<jsp:param name="isAdmin" value="true" />
				   	</jsp:include>
				<%} else { %>
					<jsp:include page="getMenu.jsp">
						<jsp:param name="isLoggedIn" value="true" />
						<jsp:param name="isAdmin" value="false" />
					</jsp:include>
				<%}
			}%>
		</div> <!-- logoPanel -->
				
	  	<div id="mainPanel">
			<!-- Loading proper content -->
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="IndexPage" />
			</jsp:include>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>