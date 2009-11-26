<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

	<h:form>
		<h:panelGroup layout="block" styleClass="sidePane">
			<h:panelGroup layout="block" styleClass="sidePaneElement">
				<h:commandLink value="Home" action="home"></h:commandLink>
			</h:panelGroup>
			<h:panelGroup layout="block" styleClass="sidePaneElement">
				<h:commandLink value="Annotate!" action="submit"></h:commandLink>
			</h:panelGroup>
			<h:panelGroup layout="block" styleClass="sidePaneElement">
				<h:commandLink value="Run locally" action="local"></h:commandLink>
			</h:panelGroup>
		</h:panelGroup>
	</h:form>
