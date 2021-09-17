<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'leagueView'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/leagueView.htm">Standings</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'yourPlayers'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/yourPlayers.htm">Roster</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'faAcquirePlayer' || pageName == 'faDropPlayer' || pageName == 'faConfirm'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/faAcquirePlayer.htm">Free Agents</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'transactionRequests'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/transactionRequests.htm">Transaction Requests</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'transactions'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/transactions.htm">Transactions</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'draftResults'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/draftResults.htm">Draft Results</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'statLeaders'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/statLeaders.htm">Stat Leaders</a></li>
    <c:set var="pageClass" value="" />

    <c:if test="${pageName == 'dynastyRules'}">
        <c:set var="pageClass" value="class=menuSelection" />
    </c:if>
    <li ${pageClass}><a href="/topdawgsports/fantasy/dynastyRules.htm">Rules</a></li>
</ul>
