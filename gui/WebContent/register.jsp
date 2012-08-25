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
			    <jsp:param name="pageName" value="RegisterPage" />
			</jsp:include>
		
			<br><br>
			
			<form name="input" action="../TaskManagement/security" method="post">
			
				<fieldset>
					<p><label for="email">Email</label><input type="text" name="email" /><br></p>
					<p><label for="password1">Password</label><input type="password" name="password1" /><br></p>
					<p><label for="password2">Repeat Password</label><input type="password" name="password2" /><br></p>
					<p><label for="firstname">First name</label><input type="text" name="firstname" /><br></p>
					<p><label for="lastname">Last name</label><input type="text" name="lastname" /><br></p>
					<p><label for="nickname">nick name</label><input type="text" name="nickname" /><br></p>
					<p class="submit"><input type="submit" value="Submit" /></p>
				</fieldset>
				
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>