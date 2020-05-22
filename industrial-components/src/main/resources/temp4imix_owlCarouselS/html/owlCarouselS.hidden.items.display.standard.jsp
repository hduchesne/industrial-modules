<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>

<c:set var="items" value="${jcr:getChildrenOfType(currentNode, 'temp4int:owlcarouselItemS')}"/>

<c:forEach items="${items}" var="item" varStatus="status">

    <template:module node="${item}" nodeTypes="temp4int:owlcarouselItemS">
        <template:param name="currentStatus" value="${status.first?' active':''}"/>
    </template:module>

</c:forEach>