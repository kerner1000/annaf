<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="css/anna.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Anna</title>
</head>
<body>
<f:view>

	<f:loadBundle basename="de.mpg.mpiz.koeln.anna.resources.resources"
		var="res" />

	<f:subview id="navView">
	<jsp:include page="navView.jsp" />
	</f:subview>
	
	<h:panelGroup layout="block" styleClass="home">
	
	<h:panelGroup layout="block" styleClass="welcome">
	<h:outputLabel value="Welcome to Anna!"></h:outputLabel>
	</h:panelGroup>
	
	<h:panelGroup styleClass="introduction">
	<h:outputText  value="#{res[\'home.introduction\']}"></h:outputText>
	</h:panelGroup>
			
	</h:panelGroup>
	
</f:view>
</body>
</html>