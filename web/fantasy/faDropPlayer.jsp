<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script type="text/javascript" src="../js/script.js" ></script>
  </head>
  <body>

    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div><!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

                <div id="freeAgents">
                    <div id="innerFreeAgents">

                        <!-- PU Player -->
                        <table>
                            <tr class="rowTitle">
                                <td colspan="6">Free Agent</td>
                            </tr>
                            <tr class="rowHeader">
                                <td>Pos</td>
                                <td>Team</td>
                                <td>Player</td>
                                <td>Total FP</td>
                                <td>Avg. FP</td>
                                <td>Opp</td>
                            </tr>
                            <tr class="rowData">
                                <c:set var="game" value="${tds:getGame(fantasyCurrentWeek.seasonWeekID,puPlayer.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,puPlayer.team.teamID)}" />

                                <td><c:out value="${puPlayer.position.positionName}" /></td>
                                <td><c:out value="${puPlayer.team.abbreviation}" /></td>
                                <td><tds:player player="${puPlayer}" displayStatsLink="true" displayInjury="false" /></td>
                                <td><fmt:formatNumber value="${puPlayer.totalFootballStats.fantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                <td><fmt:formatNumber value="${puPlayer.totalFootballStats.avgFantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                <td><c:out value="${opp}" /></td>
                            </tr>
                        </table>

                        <!-- Roster -->
                        <table>
                            <tds:tableRows items="${teamRoster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="9">${fsteam.FSLeague.leagueName} Roster</td>
                                    </tr>
                                    <tr>
                                        <td colspan="9">for Week #${fantasyCurrentWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>Action</td>
                                        <td>Active</td>
                                        <td>Status</td>
                                        <td>Pos</td>
                                        <td>Team</td>
                                        <td>Player</td>
                                        <td>Total FP</td>
                                        <td>Avg. FP</td>
                                        <td>Opp</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <!-- Skip this roster player if it's the same because they're being brought off IR -->
                                    <c:if test="${puPlayer.playerID != roster.player.playerID}">
                                        <c:set var="game" value="${tds:getGame(fantasyCurrentWeek.seasonWeekID,roster.player.team.teamID)}" />
                                        <c:set var="opp" value="${tds:getOpponentString(game,roster.player.team.teamID)}" />
                                        <tr ${highlightRow1} class="rowData">
                                            <td>
                                                <c:if test="${!game.gameHasStarted || game.isByeWeek}">
                                                    <c:if test="${roster.activeState == 'active'}">
                                                        <a href="faConfirm.htm?drop=${roster.ID}&dropType=drop">drop</a>
                                                        &#160;
                                                        <c:if test="${fsteam.FSLeague.includeIR == 1}">
                                                            |&#160;<a href="faConfirm.htm?drop=${roster.ID}&dropType=onir">onir</a>&#160;
                                                            |&#160;<a href="faConfirm.htm?drop=${roster.ID}&dropType=onir-covid">onir-covid</a>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>
                                            </td>
                                            <td><c:out value="${roster.activeState}" /></td>
                                            <td><c:out value="${roster.starterState}" /></td>
                                            <td><c:out value="${roster.player.position.positionName}" /></td>
                                            <td><c:out value="${roster.player.team.abbreviation}" /></td>
                                            <td><tds:player player="${roster.player}" displayStatsLink="true" displayInjury="false" /></td>
                                            <td><fmt:formatNumber value="${roster.totalFootballStats.fantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                            <td><fmt:formatNumber value="${roster.totalFootballStats.avgFantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                            <td><c:out value="${opp}" /></td>
                                        </tr>
                                    </c:if>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="9">You have no roster for this week.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>

                    </div> <!-- inner free agents -->
                </div> <!-- free agent -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>
