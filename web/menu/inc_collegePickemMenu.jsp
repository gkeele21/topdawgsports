<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'weeklySchedule'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/collegepickem/weeklySchedule.htm">Weekly Schedule</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'standings'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/collegepickem/standings.htm">Standings</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'rules'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/collegepickem/rules.htm">Rules</a></li>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'gameSignup'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/gameSignup.htm?fsGameId=7">Sign Up</a></li>
</ul>