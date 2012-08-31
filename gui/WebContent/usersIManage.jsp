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
	
		<div id="logoPanel">
			<x:if select="$whoAmI/WhoAmI/LoggedInAs!='NOT_LOGGED_IN'">
	            Hello <x:out select="$whoAmI/WhoAmI/LoggedInAs"/>!
	        </x:if>
	        
	        <!-- Generating proper menu -->
			<%if (whoAmIResponseInfo.responseString.contains("ADMIN")) {%>
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
			    <jsp:param name="pageName" value="UsersIManagePage" />
			</jsp:include>
		
			<%
			HttpRequestsManager.HttpResponseInfo usersAssignedResponse = null;
			if (!whoAmIResponseInfo.responseString.contains("NOT_LOGGED_IN")) {
				
				// Retrieve username value from DOM
		        Document whoAmIDoc = XmlUtilities.parseXmlStringToDocument(whoAmIResponseInfo.responseString);
		        NodeList whoAmINodes = whoAmIDoc.getElementsByTagName(XmlNamingConventions.WHO_AM_I_TAG);
		        Element userNameElem = (Element) whoAmINodes.item(0);
		        NodeList userNameNodeList = userNameElem.getElementsByTagName(XmlNamingConventions.LOGGED_IN_AS_TAG);
		        Element userNameValueElement = (Element) userNameNodeList.item(0);
		        
		        // Get user info (by username)
				String userParams = String.format(HttpConsts.USER_PARAMETER_NAME + "=%s", URLEncoder.encode(XmlUtilities.getCharacterDataFromElement(userNameValueElement), "UTF-8"));
				HttpRequestsManager.HttpResponseInfo userDataResponse = HttpRequestsManager.doGetToUserServlet(userParams, request.getCookies());
				userDataResponse.responseString.replaceAll("(\\r|\\n)", "");
	
				// Retrieve all users assigned
		        Document userDataDoc = XmlUtilities.parseXmlStringToDocument(userDataResponse.responseString);
		        NodeList userDataNodes = userDataDoc.getElementsByTagName("User");
		        Element usersAssignedElem = (Element) userDataNodes.item(0);
		        NodeList usersAssignedNodeList = usersAssignedElem.getElementsByTagName(XmlNamingConventions.USER_USERSIMANAGE_ELEMENT);
		        List<String> usersAssignedIds = new ArrayList<String>();
		        for (int i=0; i< usersAssignedNodeList.getLength(); ++i) {
			        Element usersAssignedValueElement = (Element) usersAssignedNodeList.item(i);
			        usersAssignedIds.add(XmlUtilities.getCharacterDataFromElement(usersAssignedValueElement));
		        }
		        
		        // Get users assigned info (by user id)
		        if (usersAssignedIds.size() >= 1) {
			        String usersAssignedParams = HttpConsts.USER_PARAMETER_NAME + "=" + usersAssignedIds.get(0);
			        for (int i=1; i<usersAssignedIds.size(); ++i) {
			        	usersAssignedParams += "," + usersAssignedIds.get(i);
			        }
			        usersAssignedResponse = HttpRequestsManager.doGetToUserServlet(usersAssignedParams, request.getCookies());
			        usersAssignedResponse.responseString.replaceAll("(\\r|\\n)", "");
		        }
			}
			%>
			
			<!--  Parsing the usersData xml -->
			<x:parse var="userData">
				<% out.print(usersAssignedResponse.responseString); %>
			</x:parse>
		
		    <table class="center">
		        <tr>
		        	<th>Id</th>
			        <th>First Name</th>
			        <th>Last Name</th>
			        <th>Nickname</th>
			        <th>User Type</th>
			        <th>Tasks</th>
			        <th>Edit</th>
			    </tr>
		
				<x:forEach select="$userData/Users/User">
					<tr>
						<td><x:out select="Id"/></td>
						<td><x:out select="FirstName"/></td>
						<td><x:out select="LastName"/></td>
						<td><x:out select="Nickname"/></td>
						<td><x:out select="Permission"/></td>
						<td><x:out select="Tasks"/></td>
						<td>
							<input type="button" class="editButton" value="Manage" onclick="window.location.href='editUser.jsp?userId=<x:out select="Id"/>'">
						</td>
					</tr>
				</x:forEach>
		    </table>
	  				  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>