<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	// TODO: Pass coockie to the server and check if user is logged in - update below code accordingly
	String isLoggedIn = "true";
	String isAdmin = "false";
%>

<div id="menuPanel">

<%
	if (isLoggedIn == "false") {
		%>
		<input type="button" class="menuButton" value="Login" onclick="window.location.href='login.jsp'">
		<input type="button" class="menuButton" value="Register" onclick="window.location.href='register.jsp'">
	  	<%
	} else {
		%>
		<input type="button" class="menuButton" value="Home" onclick="window.location.href='index.jsp'">
		<input type="button" class="menuButton" value="My Tasks" onclick="window.location.href='myTasks.jsp'">
		<input type="button" class="menuButton" value="My Projects" onclick="window.location.href='myProjects.jsp'">
		<input type="button" class="menuButton" value="Users I Manage" onclick="window.location.href='usersIManage.jsp'">
		<input type="button" class="menuButton" value="Add New Task" onclick="window.location.href='addNewTask.jsp'">
	  	<%
		if (isAdmin == "true") {
			%>
			<input type="button" class="menuButton" value="Projects" onclick="window.location.href='projects.jsp'">
		  	<%
		} %>
		<input type="button" class="menuButton" value="My Profile" onclick="window.location.href='myProfile.jsp'">
		<input type="button" class="menuButton" value="Logout" onclick="window.location.href='logout.jsp'">
		<%
	} %>


</div>	<!-- menuPanel -->