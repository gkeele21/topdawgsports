<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<ul>
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'seedChallengeStandings'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/mm/seedChallengeStandings.htm">Standings</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'seedChallenge'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/mm/seedChallenge.htm">Roster</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${leagueTournament.status != 'UPCOMING'}">
        <c:if test="${pageName == 'seedChallengeAllPicks'}">
            <c:set var="pageClass" value="class=menuSelection" />
        </c:if>
        <li ${pageClass}><a href="/topdawgsports/mm/seedChallengeAllPicks.htm">Picks Breakdown</a></li>
        <c:set var="pageClass" value="" />
    </c:if>

    <c:if test="${pageName == 'masterBracket'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/mm/masterBracket.htm">Master College Bracket</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'seedChallengeRules'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/mm/seedChallengeRules.htm">Rules</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'gameSignup'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/gameSignup.htm?fsGameId=9">Sign Up</a></li>

</ul>