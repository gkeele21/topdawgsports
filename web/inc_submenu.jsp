<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="/topdawgsports/css/subMenu.css" media="screen" />

<c:if test="${validUser != null && fsteam != null}" >
    <div id="submenu_container">
        <c:choose>
            <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 1}" >
                <ul id="subpmenu">
                    <li><a href="/topdawgsports/fantasy/leagueView.htm">Team Home</a></li>
                    <li><a href="/topdawgsports/fantasy/yourPlayers.htm">Your Players</a></li>

                    <c:set var="faLink" value="/topdawgsports/fantasy/transactionRequests.htm" />
                    <c:if test="${afterTransDeadline}">
                        <c:set var="faLink" value="/topdawgsports/fantasy/faAcquirePlayer.htm" />
                    </c:if>
                    <li><a href="${faLink}">Free Agents</a></li>
                    <li><a href="/topdawgsports/fantasy/teamWeekMatchups.htm">Your Matchups</a></li>
                    <li><a href="/topdawgsports/fantasy/keeperRules.htm">Rules</a></li>
                    <li><a href="/topdawgsports/fantasy/totalStatsQB.htm">Stats</a></li>
                </ul>
            </c:when>
            <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 2}" >
                <ul id="subpmenu">
                    <li><a href="/topdawgsports/sal/standings.htm">Team Home</a></li>
                    <li><a href="/topdawgsports/sal/yourPlayers.htm">Your Players</a></li>
                    <li><a href="/topdawgsports/sal/playersSelected.htm">Players Picked</a></li>
                    <li><a href="/topdawgsports/sal/rules.htm">Rules</a></li>
                </ul>
            </c:when>
        </c:choose>

    </div> <!-- menu_container -->
</c:if>



