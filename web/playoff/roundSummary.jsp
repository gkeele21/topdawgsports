<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/ffPlayoff.css" media="screen" />
  </head>

<body>
    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div> <!-- main menu -->

        <div id="leftMenu">
            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div> <!-- left menu -->

        <div id="content">
            <div id="innerContent">

                <div id="roundSummary">
                    <div id="innerRoundSummary">

                        <table>
                            <tr class="rowTitle">
                                <td colspan="12">${displayName}</td>
                            </tr>
                            <tr>
                                <td colspan="5"></td>
                                <td colspan="7">Points by Position</td>
                            </tr>
                            <tr class="rowHeader">
                                <td>Round</td>
                                <td>Opponent</td>
                                <td>PF</td>
                                <td>PA</td>
                                <td>Result</td>
                                <td>QB</td>
                                <td>RB1</td>
                                <td>RB2</td>
                                <td>WR1</td>
                                <td>WR2</td>
                                <td>TE</td>
                                <td>K</td>
                            </tr>
                            
                            <tds:tableRows items="${gameResults}" var="game" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowData">

                                    <!-- Set Variables -->
                                    <c:set var="result" value="-"/>

                                    <c:choose>
                                        <c:when test="${fsTeamId == game.FSTeam1ID}">
                                            <c:set var="opponent" value="${game.bottomTeam.teamName}"/>
                                            <c:set var="pointsFor" value="${game.team1Pts}"/>
                                            <c:set var="pointsAgainst" value="${game.team2Pts}"/>
                                            <c:if test="${game.gameStatus == 3}">
                                                <c:if test="${game.team1Pts > game.team2Pts}">
                                                    <c:set var="result" value="W"/>
                                                </c:if>
                                                <c:if test="${game.team2Pts > game.team1Pts}">
                                                    <c:set var="result" value="L"/>
                                                </c:if>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="opponent" value="${game.topTeam.teamName}"/>
                                            <c:set var="pointsFor" value="${game.team2Pts}"/>
                                            <c:set var="pointsAgainst" value="${game.team1Pts}"/>
                                            <c:if test="${game.gameStatus == 3}">
                                                <c:if test="${game.team2Pts > game.team1Pts}">
                                                    <c:set var="result" value="W"/>
                                                </c:if>
                                                <c:if test="${game.team1Pts > game.team2Pts}">
                                                    <c:set var="result" value="L"/>
                                                </c:if>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>

                                    <tr ${highlightRow1} class="rowData">

                                        <td>${game.FSTournamentRound.roundNumber}</td>
                                        <td>${opponent}</td>
                                        <td>${pointsFor}</td>
                                        <td>${pointsAgainst}</td>
                                        <td>${result}</td>                                        
                                        
                                        <!-- Quarterback -->
                                        <c:set var="playerStats" value="${tds:getPlayerStats(fsTeamId, game.gameID, 1)}"/>
                                        <c:choose>
                                            
                                            <c:when test="${fn:length(playerStats) == 0}">
                                                <td>-</td>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="stat" items="${playerStats}">
                                                    <td title="${stat.player.firstName} ${stat.player.lastName}">${stat.fantasyPts}</td>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Running Back -->
                                        <c:set var="playerStats" value="${tds:getPlayerStats(fsTeamId, game.gameID, 2)}"/>
                                        <c:choose>

                                            <c:when test="${fn:length(playerStats) == 0}">
                                                <td>-</td>
                                                <td>-</td>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="stat" items="${playerStats}">
                                                    <td title="${stat.player.firstName} ${stat.player.lastName}">${stat.fantasyPts}</td>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Wide Receiver -->
                                        <c:set var="playerStats" value="${tds:getPlayerStats(fsTeamId, game.gameID, 3)}"/>
                                        <c:choose>

                                            <c:when test="${fn:length(playerStats) == 0}">
                                                <td>-</td>
                                                <td>-</td>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="stat" items="${playerStats}">
                                                    <td title="${stat.player.firstName} ${stat.player.lastName}">${stat.fantasyPts}</td>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Tight End -->
                                        <c:set var="playerStats" value="${tds:getPlayerStats(fsTeamId, game.gameID, 4)}"/>
                                        <c:choose>

                                            <c:when test="${fn:length(playerStats) == 0}">
                                                <td>-</td>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="stat" items="${playerStats}">
                                                    <td title="${stat.player.firstName} ${stat.player.lastName}">${stat.fantasyPts}</td>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Kicker -->
                                        <c:set var="playerStats" value="${tds:getPlayerStats(fsTeamId, game.gameID, 5)}"/>
                                        <c:choose>

                                            <c:when test="${fn:length(playerStats) == 0}">
                                                <td>-</td>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="stat" items="${playerStats}">
                                                    <td title="${stat.player.firstName} ${stat.player.lastName}">${stat.fantasyPts}</td>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>

                                    </tr>

                                </jsp:attribute>
                            </tds:tableRows>
                        </table>
                        
                    </div> <!-- inner RoundSummary -->
                </div> <!-- roundSummary -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

</body>
</html>