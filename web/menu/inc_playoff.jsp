<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<ul>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'bracket'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/playoff/bracket.htm">Playoff Bracket</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'roster'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/playoff/roster.htm">Roster</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'rules'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/playoff/rules.htm">Rules</a></li>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'gameSignup'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/gameSignup.htm?fsGameId=10">Sign Up</a></li>
</ul>