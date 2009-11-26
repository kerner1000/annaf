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
	<f:subview id="navView">
		<jsp:include page="navView.jsp" />
	</f:subview>

	<h:panelGroup layout="block" styleClass="annotate">

		<h:panelGroup layout="block">
			<h:form id="formFileUpload" enctype="multipart/form-data">
				<h:outputLabel value="Upload FASTA file"></h:outputLabel>
				<t:inputFileUpload id="fileupload" value="#{uploadBacking.myFile}"
					required="true" />
				<h:commandButton value="upload"></h:commandButton>
				<h:outputLabel value="File size: "></h:outputLabel>
				<h:outputLabel value="#{uploadBacking.fileSize}"></h:outputLabel>
				<h:outputLabel value=" File name: "></h:outputLabel>
				<h:outputLabel value="#{uploadBacking.myFile}"></h:outputLabel>
			</h:form>
		</h:panelGroup>

		<h:panelGroup layout="block">
			<h:form>
				<t:selectOneMenu id="speciesSelect"
					binding="#{selectTrainingSpeciesBacking.list}"
					title="Select Species to train on">
					<f:selectItem id="si1" itemLabel="Thums Up" itemValue="11" />
					<f:selectItem id="si2" itemLabel="Limca" itemValue="22" />
				</t:selectOneMenu>
			</h:form>
		</h:panelGroup>

	</h:panelGroup>


</f:view>
</body>
</html>