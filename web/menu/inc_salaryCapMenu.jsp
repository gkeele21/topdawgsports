<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul>    
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'yourPlayers'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/sal/yourPlayers.htm">Roster</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'standings'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/sal/standings.htm">Standings</a></li>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'statLeaders'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/statLeaders.htm">Stat Leaders</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'rules'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/sal/rules.htm">Rules</a></li>
    <c:set var="pageClass" value="" />

</ul>