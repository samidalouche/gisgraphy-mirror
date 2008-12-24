<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <meta name="heading" content="<fmt:message key='mainMenu.heading'/>"/>
    <meta name="menu" content="MainMenu"/>
</head>

<p><fmt:message key="mainMenu.message"/></p>

<%
com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextSearchEngine fulltextSearchEngine = (com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextSearchEngine) org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()).getBean("fullTextSearchEngine");
%>

<div class="separator"></div>

<% if (!fulltextSearchEngine.isAlive()) { %>
   <div class="tip redtip"> <fmt:message key="import.fulltextEngineNotReachable" ><fmt:param><%= fulltextSearchEngine.getURL() %></fmt:param></fmt:message></div>
<% } else {%>
<div class="tip greentip"> <fmt:message key="import.fulltextEngineReachable"/> ! </div>
<%  }%>
<br/>
<ul class="glassList">
    <li>
        <a href="<c:url value='/editProfile.html'/>"><fmt:message key="menu.user"/></a>
    </li>
    <li>
        <a href="<c:url value='/uploadFile.html' />"><fmt:message key="menu.selectFile"/></a>
    </li>

</ul>