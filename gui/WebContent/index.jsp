<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="javax.xml.parsers.DocumentBuilder" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory" %>


    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="header.jsp" %>
</head>
<body>

<%
	DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
    DocumentBuilder db =dbf.newDocumentBuilder();
    
    // TODO: should be replaced by a server-request (including recieved cookies) to get the xml file
    Document doc=db.parse("DummyIndex.xml");
    
    NodeList actionsList = doc.getElementsByTagName("Action");
	%>
	<jsp:include page="actionsMenu.jsp"/>
		<jsp:param name="numOfActions" value="<%=actionsList.getLength() %>" />
  	<%
  	for(int i=0;i<actionsList.getLength();i++)
    {
		NodeList nameNlc = doc.getElementsByTagName("name");
		Element nameElements = (Element)nameNlc.item(i);
		String nameTagValue = nameElements.getChildNodes().item(0).getNodeValue();
		
		NodeList linkNlc = doc.getElementsByTagName("author");
		Element linkElements=(Element)linkNlc.item(i);
		String linkTagValue=linkElements.getChildNodes().item(0).getNodeValue();
		%>
		<jsp:param name="action<%=i%>name" value="<%=nameTagValue%>"/>
		<jsp:param name="action<%=i%>link" value="<%=linkTagValue%>"/>
      	<%
		out.println("name :" + nameTagValue+"<br>");    
		out.println("link :" + linkTagValue+"<br>");     
    }
  	%>
  	</jsp:include>
 	
</body>
</html>