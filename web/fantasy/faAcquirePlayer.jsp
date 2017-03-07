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

                <div id="leagueRoster">
                    <div id="innerLeagueRoster">

                        <!-- Roster -->
                        <table>
                            <tds:tableRows items="${teamRoster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="8">${fsteam.FSLeague.leagueName} Roster</td>
                                    </tr>
                                    <tr>
                                        <td colspan="8">for Week #${fantasyCurrentWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
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
                                    <c:set var="game" value="${tds:getGame(fantasyCurrentWeek.seasonWeekID,roster.player.team.teamID)}" />
                                    <c:set var="opp" value="" />
                                    <c:if test="${game != null}">
                                        <c:set var="opp" value="${tds:getOpponentString(game,roster.player.team.teamID)}" />
                                    </c:if>
                                    <tr ${highlightRow1} class="rowData">
                                        <td><c:out value="${roster.activeState}" /></td>
                                        <td><c:out value="${roster.starterState}" /></td>
                                        <td><c:out value="${roster.player.position.positionName}" /></td>
                                        <td><c:out value="${roster.player.team.abbreviation}" /></td>
                                        <td><tds:player player="${roster.player}" displayStatsLink="true" displayInjury="false" /></td>
                                        <td><fmt:formatNumber value="${roster.totalFootballStats.fantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                        <td><fmt:formatNumber value="${roster.totalFootballStats.avgFantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                        <td><c:out value="${opp}" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="8">You have no roster for this week.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>
                    </div> <!-- inner League Roster -->
                </div> <!-- league roster -->

                <div id="freeAgents">
                    <div id="innerFreeAgents">

                        <div id="position">
                            <label>Choose a Position :</label>
                            <a href="faAcquirePlayer.htm?pos=QB">QB</a>
                            <a href="faAcquirePlayer.htm?pos=RB">RB</a>
                            <a href="faAcquirePlayer.htm?pos=WR">WR</a>
                            <a href="faAcquirePlayer.htm?pos=TE">TE</a>
                            <a href="faAcquirePlayer.htm?pos=PK">PK</a>
                        </div>

                        <table>
                            <tds:tableRows items="${freeAgents}" var="player" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2" displayRows="35" startingRowNum="${startingRowNum}">
                                <jsp:attribute name="rowInfo">
                                    
                                </jsp:attribute>
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="8">Free Agents</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">                                        
                                        <td>#</td>                                        
                                        <td>Pos</td>
                                        <td>Team</td>
                                        <td colspan="2">Player</td>
                                        <td>Total FP</td>
                                        <td>Avg. FP</td>
                                        <td>Opp</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <c:set var="game" value="${tds:getGame(fantasyCurrentWeek.seasonWeekID,player.team.teamID)}" />
                                    <c:set var="opp" value="" />
                                    <c:if test="${game != null}">
                                        <c:set var="opp" value="${tds:getOpponentString(game,player.team.teamID)}" />
                                    </c:if>
                                    <tr ${highlightRow1} class="rowData">                                
                                        <td>${currentRowOverall1}.</td>
                                        <td><c:out value="${player.position.positionName}" /></td>                                        
                                        <td><c:out value="${player.team.abbreviation}" /></td>
                                        <td>
                                            <c:if test="${!afterStartersDeadline && (!game.gameHasStarted || game.isByeWeek)}">
                                                <a href="faDropPlayer.htm?pu=${player.playerID}">
                                                    <img class="plusMinus" src="/topdawgsports/images/plus.png" />
                                                </a>
                                            </c:if>
                                        </td>
                                        <td class="playerName"><tds:player player="${player}" displayStatsLink="true" displayInjury="false" /></td>
                                        <td><fmt:formatNumber value="${player.totalFootballStats.fantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                        <td><fmt:formatNumber value="${player.totalFootballStats.avgFantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                        <td><c:out value="${opp}" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="8">There are no free agents available for this week.</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowNavigation">
                                    <tr>
                                        <td colspan="8"><hr /></td>
                                    </tr>
                                    <tr class="rowData2">
                                        <td><tds:navFirst link="faAcquirePlayer.htm">FIRST</tds:navFirst></td>
                                        <td colspan="2"><tds:navPrev link="faAcquirePlayer.htm"><< PREV</tds:navPrev></td>
                                        <td colspan="2">${fromRows1} to ${toRows1} of ${totalRows1}</td>
                                        <td colspan="2"><tds:navNext link="faAcquirePlayer.htm">NEXT >></tds:navNext></td>
                                        <td><tds:navLast link="faAcquirePlayer.htm">LAST</tds:navLast></td>
                                    </tr>
                                </jsp:attribute>

                            </tds:tableRows>
                        </table>

                    </div> <!-- innner Free Agents -->
                </div> <!-- Free Agents -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>
