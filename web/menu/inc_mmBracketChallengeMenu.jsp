<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<ul>
    
    <c:set var="pageClass" value="" />
    
    <c:if test="${pageName == 'bracketChallengeStandings'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/mm/bracketChallengeStandings.htm">Standings</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'bracketChallenge' || pageName == 'bracketChallengePicks'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/mm/bracketSelection.htm">Bracket</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'masterBracket'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/mm/masterBracket.htm">Master College Bracket</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'bracketChallengeRules'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/mm/bracketChallengeRules.htm">Rules</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'gameSignup'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/gameSignup.htm?fsGameId=8">Sign Up</a></li>

</ul>