<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.io.DataOutputStream" %>
<%@ page import="java.io.DataInputStream" %>
<%@ page import="constants.*" %>
<%@ page import="utilities.HttpRequestsManager" %>

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
		if (!whoAmIResponseInfo.responseString.contains("NOT_LOGGED_IN")) {
			response.sendRedirect("index.jsp");
		}
		
		// Send login request if needed
		if ((request.getParameter("doLogin")!= null) && (request.getParameter("doLogin").equals("true")) ) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			if ((email!=null) && (password!=null)) {
			    // Build request body
		        String body =
		        "email=" + URLEncoder.encode(email, "UTF-8") +
		        "&password=" + URLEncoder.encode(password, "UTF-8") +
		        "&action=" + URLEncoder.encode("login", "UTF-8");
			
		        HttpRequestsManager.HttpResponseInfo loginResponseInfo = HttpRequestsManager.doPostToSecurityServlet(body, request.getCookies());
		        if (loginResponseInfo != null) {
			if (loginResponseInfo.cookies != null) {
				for (int i=0; i< loginResponseInfo.cookies.length; ++i) {
			        		response.addCookie(loginResponseInfo.cookies[i]);
				}
			}
			response.sendRedirect(HttpConsts.SUCCESSFUL_LOGIN_REDIRECT_URL);
		        }
		        else {
	%> <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br> login failed! <%
		        }
			}
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
					<input type="hidden" name="doLogin" value="true">
					<p><label for="email">Email</label><input type="text" name="email" /><br></p>
					<p><label for="pwd">Password</label><input type="password" name="password" /><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>