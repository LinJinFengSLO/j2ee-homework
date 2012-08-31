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
			    <jsp:param name="pageName" value="MyTasksPage" />
			</jsp:include>
		
		
			<%
			HttpRequestsManager.HttpResponseInfo tasksDataResponse = null;
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
	
				// Retrieve all users tasks
		        Document userDataDoc = XmlUtilities.parseXmlStringToDocument(userDataResponse.responseString);
		        NodeList userDataNodes = userDataDoc.getElementsByTagName("User");
		        Element tasksElem = (Element) userDataNodes.item(0);
		        NodeList tasksNodeList = tasksElem.getElementsByTagName(XmlNamingConventions.USER_TASKS_ELEMENT);
		        List<String> tasksIds = new ArrayList<String>();
		        for (int i=0; i<tasksNodeList.getLength(); ++i) {
			        Element tasksValueElement = (Element) tasksNodeList.item(i);
			        tasksIds.add(XmlUtilities.getCharacterDataFromElement(tasksValueElement));
		        }
		        
		        // Get task info (by task id)
		        if (tasksIds.size() >= 1) {
			        String tasksParams = HttpConsts.TASK_PARAMETER_NAME + "=" + tasksIds.get(0);
			        for (int i=1; i<tasksIds.size(); ++i) {
			        	tasksParams += "," + tasksIds.get(i);
			        }
					tasksDataResponse = HttpRequestsManager.doGetToTaskServlet(tasksParams, request.getCookies());
					tasksDataResponse.responseString.replaceAll("(\\r|\\n)", "");
		        }
			}
			%>
			
			<!--  Parsing the tasksData xml -->
			<x:parse var="taskData">
				<% out.print(tasksDataResponse.responseString); %>
			</x:parse>
			
		    <table class="center">
		        <tr>
		        	<th>Task Id</th>
			        <th>Task Name</th>
			        <th>Task Description</th>
			        <th>Creation Date</th>
			        <th>Due Date</th>
			        <th>Prior Tasks</th>
			        <th>Users Assigned</th>
			        <th>Status</th>
			        <th>Edit</th>
			    </tr>

				<x:forEach select="$taskData/Tasks/Task">
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
						<td><x:out select="UsersAssigned"/></td>
						<td><x:out select="Status"/>&nbsp;
						<x:out select="IsLate"/>
						</td>
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