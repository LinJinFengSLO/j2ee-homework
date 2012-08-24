<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>

<% // TODO: make XmlNamingConventions a local class in GUI project and import it %>

<%
	String pageName = request.getParameter("pageName");
%>
	<c:import var="PageInfo" url="info.xml"/>
	<x:parse doc="${PageInfo}" var="infoXml"/>
<%
	if(pageName.equals("IndexPage")) { %>
		<h2><x:out select="$infoXml/TaskManagement/IndexPage/Title"/></h2><br>
		<x:forEach select="$infoXml/TaskManagement/IndexPage/TextLine" var="textLine"><br>
		   <x:out select="$textLine"/>
		</x:forEach> <%
	}

	if(pageName.equals("LoginPage")) { %>
	<h2><x:out select="$infoXml/TaskManagement/LoginPage/Title"/></h2><br>
	<x:forEach select="$infoXml/TaskManagement/LoginPage/TextLine" var="textLine"><br>
	   <x:out select="$textLine"/>
	</x:forEach> <%
	}

	if(pageName.equals("RegisterPage")) { %>
	<h2><x:out select="$infoXml/TaskManagement/RegisterPage/Title"/></h2><br>
	<x:forEach select="$infoXml/TaskManagement/RegisterPage/TextLine" var="textLine"><br>
	   <x:out select="$textLine"/>
	</x:forEach> <%
	}

	if(pageName.equals("MyTasksPage")) { %>
	<h2><x:out select="$infoXml/TaskManagement/MyTasksPage/Title"/></h2><br>
	<x:forEach select="$infoXml/TaskManagement/MyTasksPage/TextLine" var="textLine"><br>
	   <x:out select="$textLine"/>
	</x:forEach> <%
	}
	
	if(pageName.equals("MyProjectsPage")) { %>
	<h2><x:out select="$infoXml/TaskManagement/MyProjectsPage/Title"/></h2><br>
	<x:forEach select="$infoXml/TaskManagement/MyProjectsPage/TextLine" var="textLine"><br>
	   <x:out select="$textLine"/>
	</x:forEach> <%
	}
	
	if(pageName.equals("UsersIManagePage")) { %>
	<h2><x:out select="$infoXml/TaskManagement/UsersIManagePage/Title"/></h2><br>
	<x:forEach select="$infoXml/TaskManagement/UsersIManagePage/TextLine" var="textLine"><br>
	   <x:out select="$textLine"/>
	</x:forEach> <%
	}

	if(pageName.equals("MyProfilePage")) { %>
	<h2><x:out select="$infoXml/TaskManagement/MyProfilePage/Title"/></h2><br>
	<x:forEach select="$infoXml/TaskManagement/MyProfilePage/TextLine" var="textLine"><br>
	   <x:out select="$textLine"/>
	</x:forEach> <%
	}
%>