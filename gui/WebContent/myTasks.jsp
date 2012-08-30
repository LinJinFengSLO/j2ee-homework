<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory, javax.xml.parsers.DocumentBuilder, org.w3c.dom.Document " %>
<%@ page import = "org.w3c.dom.Element, java.io.ByteArrayInputStream " %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="java.io.StringReader" %>
<%@ page import="org.w3c.dom.NodeList" %>
<%@ page import="org.w3c.dom.Node" %>
<%@ page import="constants.XmlNamingConventions" %>
<%@ page import="utilities.HttpRequestsManager" %>

<%@ page import="utilities.XmlUtilities " %>


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
		whoAmIResponseInfo.responseString.replaceAll("(\\r|\\n)", "");		
		if (!whoAmIResponseInfo.responseString.contains("NOT_LOGGED_IN")) {
			response.sendRedirect("index.jsp");
		}
        
        /*
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            NodeList name = element.getElementsByTagName("LoggedInAs");
            Element line = (Element) name.item(0);
            System.out.println("Logged In As: " + XmlUtilities.getCharacterDataFromElement(line));
          }*/
        
        
        /*
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        Document myDoc = domBuilder.parse(whoAmIResponse);


        // If user isn't logged in take him to home page
        if (whoAmIResponse.contains("NOT_LOGGED_IN")) {
        	response.sendRedirect("index.jsp");
        }
        */
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
				HttpRequestsManager.HttpResponseInfo userDataResponse = HttpRequestsManager.doGetToUserServlet(request.getCookies());
				userDataResponse.responseString.replaceAll("(\\r|\\n)", "");
		        
		        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		        InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(userDataResponse.responseString));
		        

		        Document doc = db.parse(is);
		        NodeList nodes = doc.getElementsByTagName("User");
		        
		        Element element1 = (Element) nodes.item(0);
		        NodeList name1 = element1.getElementsByTagName("Nickname");
		        Element line1 = (Element) name1.item(0);
		        System.out.println("Nickname: " + XmlUtilities.getCharacterDataFromElement(line1));
		        
		        Element element2 = (Element) nodes.item(0);
		        NodeList name2 = element2.getElementsByTagName("Email");
		        Element line2 = (Element) name2.item(0);
		        System.out.println("Email: " + XmlUtilities.getCharacterDataFromElement(line2));
		        
			%>


	  				  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>