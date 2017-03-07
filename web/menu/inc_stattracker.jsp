<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<ul>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'golferHome'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/stattracker/golferHome.htm">Golfer Home</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'eventInfo'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/stattracker/eventInfo.htm">Event Information</a></li>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'tournamentResults'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/stattracker/eventResults.htm">Event Results</a></li>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'enterScores'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/stattracker/enterScores.htm">Enter Hole Scores</a></li>
    <c:set var="pageClass" value="" />

</ul>