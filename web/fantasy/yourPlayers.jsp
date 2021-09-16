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

                        <form action="yourPlayers.do" method="post">

                            <!-- Active Roster -->
                            <table>
                                <tds:tableRows items="${activeRoster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
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
                                            <%--<td>Active</td>--%>
                                            <td>Pos</td>
                                            <td>Team</td>
                                            <td>Player</td>
                                            <td>Total FP</td>
                                            <td>Avg. FP</td>
                                            <td>Opp</td>
                                            <td>Game Date</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr ${highlightRow1} class="rowData">
                                            <c:set var="game" value="${tds:getGame(fantasyCurrentWeek.seasonWeekID,roster.player.team.teamID)}" />
                                            <%--<td>
                                                <c:choose>
                                                    <c:when test="${!afterStartersDeadline && (!game.gameHasStarted || game.isByeWeek)}">
                                                        <c:choose>
                                                            <c:when test="${roster.activeState eq 'active'}">
                                                                <select name="selActiveState">
                                                                    <option selected="true" value="${roster.ID}_active">Active</option>
                                                                    <option value="${roster.ID}_inactive">Inactive</option>
                                                                </select>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <select name="selActiveState">
                                                                    <option selected="true" value="${roster.ID}_inactive">Inactive</option>
                                                                    <option value="${roster.ID}_active">Active</option>
                                                                </select>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="hidden" name="hfState" value="${roster.ID}_active" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>--%>
                                            <td><c:out value="${roster.player.position.positionName}" /></td>
                                            <td><c:out value="${roster.player.team.abbreviation}" /></td>
                                            <td><tds:player player="${roster.player}" displayStatsLink="true" displayInjury="false" /></td>
                                            <td><fmt:formatNumber value="${roster.player.totalFootballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                            <td><fmt:formatNumber value="${roster.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                            <td><c:out value="${tds:getOpponentString(game,roster.player.team.teamID)}" /></td>
                                            <td>
                                                <fmt:parseDate  value="${game.gameDate}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="gameDate" />
                                                <fmt:formatDate value="${gameDate}" pattern="E h:mm a" timeZone="America/Denver" />
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="9">You have no active roster for this week.</td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>

                            <c:if test="${!empty inactiveRoster}">
                                <!-- InActive Roster -->
                                <table>
                                    <tds:tableRows items="${inactiveRoster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                        <jsp:attribute name="rowTitle">
                                        <tr class="rowTitle">
                                            <td colspan="8">Inactive Players</td>
                                        </tr>
                                    </jsp:attribute>
                                        <jsp:attribute name="rowHeader">
                                            <tr class="rowHeader">
                                                <%--<td>Active</td>--%>
                                                <td>Pos</td>
                                                <td>Team</td>
                                                <td>Player</td>
                                                <td>Total FP</td>
                                                <td>Avg. FP</td>
                                                <td>Opp</td>
                                                <td>Game Date</td>
                                            </tr>
                                        </jsp:attribute>
                                        <jsp:attribute name="rowData">
                                            <c:set var="game" value="${tds:getGame(fantasyCurrentWeek.seasonWeekID,roster.player.team.teamID)}" />
                                            <tr ${highlightRow1} class="rowData">
                                                <%--<td>
                                                    <c:choose>
                                                        <c:when test="${!afterStartersDeadline && (!game.gameHasStarted || game.isByeWeek)}">
                                                            <c:choose>
                                                                <c:when test="${roster.activeState eq 'active'}">
                                                                    <select name="selActiveState">
                                                                        <option selected="true" value="${roster.ID}_active">Active</option>
                                                                        <option value="${roster.ID}_inactive">Inactive</option>
                                                                    </select>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <select name="selActiveState">
                                                                        <option selected="true" value="${roster.ID}_inactive">Inactive</option>
                                                                        <option value="${roster.ID}_active">Active</option>
                                                                    </select>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" name="hfState" value="${roster.ID}_inactive" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>--%>
                                                <td><c:out value="${roster.player.position.positionName}" /></td>
                                                <td><c:out value="${roster.player.team.abbreviation}" /></td>
                                                <td><tds:player player="${roster.player}" displayStatsLink="true" displayInjury="false" /></td>
                                                <td><fmt:formatNumber value="${roster.player.totalFootballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                <td><fmt:formatNumber value="${roster.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                <td><c:out value="${tds:getOpponentString(game,roster.player.team.teamID)}" /></td>
                                                <td>
                                                    <fmt:parseDate  value="${game.gameDate}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="gameDate" />
                                                    <fmt:formatDate value="${gameDate}" pattern="E h:mm a" timeZone="America/Denver" />
                                                </td>
                                            </tr>
                                        </jsp:attribute>
                                        <jsp:attribute name="rowEmpty">
                                            <tr>
                                                <td colspan="8">You have no inactive roster for this week.</td>
                                            </tr>
                                        </jsp:attribute>
                                    </tds:tableRows>
                                </table>
                            </c:if>

                            <!-- Submit Button -->
                            <%--<c:if test="${!afterStartersDeadline}">
                                <input type="image" src="../images/submit.png" />
                            </c:if>--%>
                        </form>

                        <!-- IR Roster -->
                        <c:if test="${!empty irRoster}">
                            <table>
                                <tds:tableRows items="${irRoster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowTitle">
                                        <tr class="rowTitle">
                                            <td colspan="8">Players on IR</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <td>Pos</td>
                                            <td>Team</td>
                                            <td>Player</td>                                            
                                            <td>Added Week</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <c:set var="weekOnIR" value="${tds:getWeekPutOnIR(roster.FSTeam.FSLeagueID,roster.FSTeam.FSTeamID, roster.playerID)}" />
                                        <tr ${highlightRow1} class="rowData">
                                            <td><c:out value="${roster.player.position.positionName}" /></td>
                                            <td><c:out value="${roster.player.team.abbreviation}" /></td>
                                            <td><tds:player player="${roster.player}" displayStatsLink="true" displayInjury="true" /></td>
                                            <td><c:out value="${weekOnIR.FSSeasonWeekNo}" /></td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>
                        </c:if>
                    </div> <!-- inner League Roster -->
                </div> <!-- league Roster -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
