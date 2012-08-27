<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>  

<%@ page import="java.io.ByteArrayInputStream, java.io.IOException, javax.xml.parsers.ParserConfigurationException, java.io.File" %>
<%@ page import="javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory, java.io.PrintWriter, java.io.BufferedWriter, java.io.StringReader" %>
<%@ page import="org.w3c.dom.Document, org.w3c.dom.NamedNodeMap, org.w3c.dom.Node, org.w3c.dom.NodeList, org.xml.sax.SAXException, org.xml.sax.InputSource" %>

<!DOCTYPE html>
<html>
<head>

<%@ include file="htmlCommonHeader.jsp" %>

</head>
<body>
	<%
		HttpURLConnection connection = null;	
        URL url = new URL("http://localhost:8080/TaskManagement/security");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        connection.getInputStream();
        String myResponse = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();

        final InputSource is= new InputSource(new StringReader(myResponse));
        ByteArrayInputStream stream = new ByteArrayInputStream(myResponse.getBytes());
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(stream);
        
		if (doc == null) {
			%> <br><br><br><br><h1> DOM is null </h1> <%
		}
        /*
        File xmlFile = new File("XmFile");
        BufferedWriter bw = null;
        Document doc = null;
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOMETAG>Content</SOMETAG>";
        try
        {
            PrintWriter w = new PrintWriter(xmlFile);
            bw = new BufferedWriter(w);
            bw.write(xml);
            bw.flush(); //<---- Make sure you flush the buffer contents into the file.
         
           //Now you should be ok to read the xml contents
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           DocumentBuilder builder = factory.newDocumentBuilder();
           doc = builder.parse(xmlFile);
         }
         catch (Exception e)
          {
               e.printStackTrace();
          }
            
        String path = xmlFile.getPath();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(myResponse.getBytes("UTF-8")));
        */
        //out.print(myResponse);
	%>	
		<x:parse doc="${doc}" var="userData"/>

	<div id="taskManagementContainer">
	
		<div id="logoPanel"></div>
		
		<jsp:include page="getMenu.jsp"/>
		
	  	<div id="mainPanel">
	  		<!-- check if user is logged in - if so, don't display getInfo -->
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="IndexPage" />
			</jsp:include>
	  			  	
	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>