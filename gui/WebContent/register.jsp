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
	  		
			<form name="input" action="security" method="post">
				Email: <input type="text" name="email" />
				Password: <input type="password" name="pwd" />
				Repeat password: <input type="password" name="repeatpwd" />
				First name: <input type="text" name="firstname" /><br />
				Last name: <input type="text" name="lastname" />
				<input type="submit" value="Submit" />
			</form>

	  	</div>	<!-- mainPanel -->
	  	
	</div>	<!-- taskManagementContainer -->
	
</body>
</html>