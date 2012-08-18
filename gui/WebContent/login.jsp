<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
  
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
			    <jsp:param name="pageName" value="LoginPage" />
			</jsp:include>
			
	  		<br><br>
	  		
			<form name="input" action="security" method="put">
			
				<fieldset>
					<p><label for="email">Email</label><input type="text" name="email" /><br></p>
					<p><label for="pwd">Password</label><input type="password" name="pwd" /><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>
				
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>