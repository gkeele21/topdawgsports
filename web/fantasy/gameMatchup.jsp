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

                <div id="gameBreakdown">
                    <div id="innerGameBreakdown">

                        <table>
                            <tr class="rowTitle">
                                <td colspan="8">Game Breakdown</td>
                            </tr>
                            <tr>
                                <td>
                                    <c:if test="${matchup.FSSeasonWeek.FSSeasonWeekNo > 1}">
                                        <a class="standardLink" href="gameMatchup.htm?wk=1&game=${matchup.gameNo}"><<</a>
                                    </c:if>
                                    <c:if test="${matchup.FSSeasonWeek.FSSeasonWeekNo > 1}">
                                        <a class="standardLink" href="gameMatchup.htm?wk=${matchup.FSSeasonWeek.FSSeasonWeekNo - 1}&game=${matchup.gameNo}"><</a>
                                    </c:if>
                                </td>
                                <td>
                                    <h2>Week #${matchup.FSSeasonWeek.FSSeasonWeekNo}</h2>
                                </td>
                                <td>
                                    <c:if test="${matchup.FSSeasonWeek.FSSeasonWeekNo < fantasyCurrentWeek.FSSeasonWeekNo}">
                                        <a class="standardLink" href="gameMatchup.htm?wk=${matchup.FSSeasonWeek.FSSeasonWeekNo + 1}&game=${matchup.gameNo}">></a>
                                    </c:if>
                                    <c:if test="${matchup.FSSeasonWeek.FSSeasonWeekNo < fantasyCurrentWeek.FSSeasonWeekNo}">
                                        <a class="standardLink" href="gameMatchup.htm?wk=${fantasyCurrentWeek.FSSeasonWeekNo - 1}&game=${matchup.gameNo}">>></a>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <c:if test="${matchup.gameNo > 1}">
                                        <a class="standardLink" href="gameMatchup.htm?game=1&wk=${matchup.FSSeasonWeek.FSSeasonWeekNo}"><<</a>
                                    </c:if>
                                    <c:if test="${matchup.gameNo > 1}">
                                        <a class="standardLink" href="gameMatchup.htm?game=${matchup.gameNo - 1}&wk=${matchup.FSSeasonWeek.FSSeasonWeekNo}"><</a>
                                    </c:if>
                                </td>
                                <td>
                                    <h3>Game #${matchup.gameNo}</h3>
                                </td>
                                <td>
                                    <c:if test="${matchup.gameNo < numgames}">
                                        <a class="standardLink" href="gameMatchup.htm?game=${matchup.gameNo + 1}&wk=${matchup.FSSeasonWeek.FSSeasonWeekNo}">></a>
                                    </c:if>
                                    <c:if test="${matchup.gameNo < numgames}">
                                        <a class="standardLink" href="gameMatchup.htm?game=${numgames}&wk=${matchup.FSSeasonWeek.FSSeasonWeekNo}">>></a>
                                    </c:if>
                                </td>
                            </tr>
                            <tr class="rowHeader">
                                <td>Team 1</td>
                                <td>vs</td>
                                <td>Team 2</td>
                            </tr>
                            <tr class="rowData2">
                                <td>${matchup.FSTeam1.teamName} : <fmt:formatNumber value="${matchup.team1Pts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                <td></td>
                                <td>${matchup.FSTeam2.teamName} : <fmt:formatNumber value="${matchup.team2Pts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                            </tr>
                            <tr>
                                <td>
                                    <!-- Team 1 -->
                                    <table>
                                        <tds:tableRows items="${team1roster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                            <jsp:attribute name="rowTitle">
                                                <tr class="rowTitle">
                                                    <td colspan="8">${matchup.FSTeam1.teamName}</td>
                                                </tr>
                                            </jsp:attribute>
                                            <jsp:attribute name="rowHeader">
                                                <tr class="rowHeader">
                                                    <td>Pos</td>
                                                    <td>Team</td>
                                                    <td>Player</td>
                                                    <td>Total FP</td>
                                                </tr>
                                            </jsp:attribute>
                                            <jsp:attribute name="rowData">
                                                <c:set var="highClass" value="rowData2" />
                                                <c:if test="${roster.activeState == 'inactive'}">
                                                    <c:set var="highClass" value="rowData2" />
                                                </c:if>
                                                <c:if test="${roster.starterState == 'starter'}">
                                                    <c:set var="highClass" value="rowData" />
                                                </c:if>
                                                <tr class="${highClass}">
                                                    <td><c:out value="${roster.player.position.positionName}" /></td>
                                                    <td><c:out value="${roster.player.team.abbreviation}" /></td>
                                                    <td><tds:player player="${roster.player}" displayStatsLink="true" displayInjury="false" /></td>
                                                    <td><fmt:formatNumber value="${roster.footballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                </tr>
                                            </jsp:attribute>
                                            <jsp:attribute name="rowEmpty">
                                                <tr>
                                                    <td colspan="8">This team has no active roster for this week.</td>
                                                </tr>
                                            </jsp:attribute>
                                        </tds:tableRows>
                                    </table>
                                    <!-- End Team 1 -->
                                </td>
                                <td></td>
                                <td>
                                    <!-- Team 2 -->
                                    <table>
                                        <tds:tableRows items="${team2roster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                            <jsp:attribute name="rowTitle">
                                                <tr class="rowTitle">
                                                    <td colspan="8">${matchup.FSTeam2.teamName}</td>
                                                </tr>
                                            </jsp:attribute>
                                            <jsp:attribute name="rowHeader">
                                                <tr class="rowHeader">
                                                    <td>Pos</td>
                                                    <td>Team</td>
                                                    <td>Player</td>
                                                    <td>Total FP</td>
                                                </tr>
                                            </jsp:attribute>
                                            <jsp:attribute name="rowData">
                                                <c:set var="highClass" value="rowData2" />
                                                <c:if test="${roster.activeState == 'inactive'}">
                                                    <c:set var="highClass" value="rowData2" />
                                                </c:if>
                                                <c:if test="${roster.starterState == 'starter'}">
                                                    <c:set var="highClass" value="rowData" />
                                                </c:if>
                                                <tr class="${highClass}">
                                                    <td><c:out value="${roster.player.position.positionName}" /></td>
                                                    <td><c:out value="${roster.player.team.abbreviation}" /></td>
                                                    <td><tds:player player="${roster.player}" displayStatsLink="true" displayInjury="false" /></td>
                                                    <td><fmt:formatNumber value="${roster.footballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                </tr>
                                            </jsp:attribute>
                                            <jsp:attribute name="rowEmpty">
                                                <tr>
                                                    <td colspan="8">This team has no active roster for this week.</td>
                                                </tr>
                                            </jsp:attribute>
                                        </tds:tableRows>
                                    </table>
                                    <!-- End Team 2 -->
                                </td>
                            </tr>
                        </table>
                            
                    </div> <!-- inner game breakdown -->
                </div> <!-- game breakdown -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>
