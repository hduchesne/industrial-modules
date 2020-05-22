<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>

<c:set var="heading" value="${currentNode.properties['temp4i:heading'].string}"/>
<c:set var="iconClass" value="${currentNode.properties['temp4i:iconClass'].string}"/>

<template:include view="hidden.generateLink"/>

<a href="${moduleMap.linkUrl}" class="link-thumbnail">
    <h3>${heading}</h3>
    <span class="${iconClass}"></span>
    <template:include view="image">
        <template:param name="class" value="img-fluid"/>
    </template:include>
</a>
