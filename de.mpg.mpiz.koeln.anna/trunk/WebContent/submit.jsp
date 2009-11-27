<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="css/anna.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<f:view>

	<f:loadBundle basename="de.mpg.mpiz.koeln.anna.resources.resources"
		var="res" />

	<f:subview id="navView">
		<jsp:include page="navView.jsp" />
	</f:subview>

	<h:panelGroup layout="block" styleClass="annotate">

			<h:panelGroup layout="block" styleClass="annotate-intro">
				<h:outputText value="#{res[\'annotate.intro\']}"></h:outputText>
			</h:panelGroup>
		

		<h:panelGroup layout="block" styleClass="fileUpload">
			<h:panelGrid columns="2" width="100%">
				<h:outputLabel value="Upload FASTA file"></h:outputLabel>
				<h:panelGroup rendered="#{uploadBacking.myFile ne null}">
					<h:outputLabel value="File size: "></h:outputLabel>
					<h:outputLabel value="#{uploadBacking.fileSize}"></h:outputLabel>
				</h:panelGroup>
			</h:panelGrid>
			<h:form id="formFileUpload" enctype="multipart/form-data">
				<t:inputFileUpload id="fileupload" value="#{uploadBacking.myFile}"
					required="true" />
				<h:commandButton value="upload"></h:commandButton>
			</h:form>
		</h:panelGroup>

		<h:panelGroup layout="block" styleClass="speciesSelect">
		<h:outputLabel value="Select organism to train on"></h:outputLabel>
			<h:form>
				<h:selectOneMenu id="species" value="1"
					converter="trainingSpeciesConverter">
					<f:selectItems value="#{selectTrainingSpeciesBacking.list}" />
				</h:selectOneMenu>
			</h:form>
		</h:panelGroup>
		
		<h:panelGroup layout="block" styleClass="run">
		<h:form>
		<h:commandButton value="run!" action="#{runBacking.run}"></h:commandButton>
		</h:form>
		</h:panelGroup>
		
		<h:messages></h:messages>

	</h:panelGroup>


</f:view>
</body>
</html>