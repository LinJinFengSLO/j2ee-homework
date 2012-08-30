<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.io.DataOutputStream" %>
<%@ page import="java.io.DataInputStream" %>

<!DOCTYPE html>
<html>
<head>

<%@ include file="htmlCommonHeader.jsp" %>

</head>
<body>

	<%
	
	if ((request.getParameter("doIt")!= null) && (request.getParameter("doIt").equals("true")) ) {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		if ((email!=null) && (password!=null)) {
		
		       // Build request body
	        String body =
	        "email=" + URLEncoder.encode(email, "UTF-8") +
	        "&password=" + URLEncoder.encode(password, "UTF-8") +
	        "&action=" +	URLEncoder.encode("login", "UTF-8");
		       
			

	        
			HttpURLConnection connection = null;	
	        URL url = new URL("http://localhost:8080/TaskManagement/security");
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        
	        

	        
	        

	        
	        DataOutputStream outStream = null;
	        DataInputStream inStream = null;
	         // Create I/O streams
	         outStream = new DataOutputStream(connection.getOutputStream());
	         connection.connect();
		        outStream.writeBytes(body);
		        outStream.flush();
		        outStream.close();
		        
	        inStream = new DataInputStream(connection.getInputStream());
	          // Send request

	        String cookie = connection.getHeaderField("Set-Cookie");
	        if (cookie != null) {
	           	cookie = cookie.substring(0, cookie.indexOf(';'));
	        	System.out.println("cookie: " + cookie);	
	        	String delimiter = "=";
	        	String[] temp;
	        	temp = cookie.split(delimiter);
	        	Cookie loginCookie = new Cookie(temp[0],temp[1]);
	           
	           loginCookie.set
	           //loginCookie.setValue(newValue)
	           //loginCookie.set
	           //response.addCookie(arg0)
	        }
		}
		
		
	}
		
		
		
		
		HttpURLConnection connection = null;	
        URL url = new URL("http://localhost:8080/TaskManagement/security");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        connection.getInputStream();
        String whoAmIResponse = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
        whoAmIResponse.replaceAll("(\\r|\\n)", "");
        
        // If user already logged in take him to home page
        if (!whoAmIResponse.contains("NOT_LOGGED_IN")) {
        	response.sendRedirect("index.jsp");
        }
	%>

	<div id="taskManagementContainer">
	
		<div id="logoPanel">
		 	Hello guest, please log in or register.
			<!-- Generating proper menu -->
	  		<jsp:include page="getMenu.jsp">
	  			<jsp:param name="isLoggedIn" value="false" />
			    <jsp:param name="isAdmin" value="false" />
			</jsp:include>
		</div>
				
	  	<div id="mainPanel">
	  	
		  	<jsp:include page="getInfo.jsp">
			    <jsp:param name="pageName" value="LoginPage" />
			</jsp:include>
			
	  		<br><br>
	  		
			<form name="input" action="" method="post" enctype="application/x-www-form-urlencoded">
				<fieldset>
					<input type="hidden" name="action" value="login">
					<input type="hidden" name="doIt" value="true">
					<input type="hidden" name="successTargetLink" value="http://localhost:8080/TaskManagement_UI/index.jsp">
					<input type="hidden" name="failureTargetLink" value="http://localhost:8080/TaskManagement_UI/login.jsp">
					<p><label for="email">Email</label><input type="text" name="email" /><br></p>
					<p><label for="pwd">Password</label><input type="password" name="password" /><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>