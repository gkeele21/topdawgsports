<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<ul>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'teamInfo'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/golf/teamInfo.htm">Golf Home</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'choosePlayers'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/golf/choosePlayers.htm">Choose Players</a></li>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'leaderboard'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/golf/leaderboard.htm">Leaderboard</a></li>
    <c:set var="pageClass" value="" />
    
</ul>