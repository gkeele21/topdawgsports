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
        </div> <!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div> <!-- left Menu -->

        <div id="content">
            <div id="innerContent">
                
                <div id="leagueStandings">                    
                    <div id="innerLeagueStandings">

                        <!-- Initialize Variable -->
                        <c:set var="prevWins" value="" />

                        <table>
                            <tds:tableRows items="${leagueStandings}" var="standings" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="8">${fsteam.FSLeague.leagueName} Standings</td>
                                    </tr>
                                    <tr>
                                        <td colspan="8">through Week #${fantasyDisplayWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>Rank</td>
                                        <td>Team</td>
                                        <td>Rec</td>
                                        <td>Pts F</td>
                                        <td>Pts A</td>
                                        <td>Hi</td>
                                        <td>Strk</td>
                                        <td>Last 5</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <%-- RANK COLUMN --%>
                                        <c:choose>
                                            <c:when test="${prevWins != standings.wins}">
                                                <td><c:out value="${currentRow1}" /></td>
                                                <c:set var="prevWins" value="${standings.wins}" />
                                            </c:when>
                                            <c:otherwise>
                                                <td></td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td><c:out value="${standings.FSTeam.teamName}" /></td>
                                        <td><c:out value="${standings.wins}" />-<c:out value="${standings.losses}" /></td>
                                        <td><fmt:formatNumber value="${standings.totalFantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                        <td><fmt:formatNumber value="${standings.totalFantasyPtsAgainst}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                        <td><c:out value="${standings.totalHiScores}" /></td>
                                        <td><c:if test="${standings.currentStreak > 0}"><c:out value="+" /></c:if><c:out value="${standings.currentStreak}" /></td>
                                        <td><c:out value="${standings.lastFive}" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="8">There are no standings for this week.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>
                    </div> <!-- inner League Standings -->
                </div> <!-- League Standings -->

                <div id="transactionOrder">
                    <div id="innerTransactionOrder">
                        <table>
                            <tds:tableRows items="${transactionOrder}" var="team" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="2">Trans. Order</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">for Week #${fantasyCurrentWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>Pos</td>
                                        <td>Team</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td><c:out value="${currentRow1}" /></td>
                                        <td><c:out value="${team.teamName}" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="2">Not set yet.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>
                    </div> <!-- inner Transaction Order -->
                </div> <!-- Transaction Order -->

                <hr />
                <div id="leagueResults">
                    <div id="innerLeagueResults">
                        <table>
                            <tds:tableRows items="${previousResults}" var="matchup" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="3">Results</td>
                                    </tr>
                                    <tr>
                                        <td colspan="3">for Week #${fantasyDisplayWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>TEAM 1</td>
                                        <td>vs</td>
                                        <td>TEAM 2</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td><a href="gameMatchup.htm?game=${matchup.gameNo}&wk=${fantasyDisplayWeek.FSSeasonWeekNo}"><c:out value="${matchup.FSTeam1.teamName}" /></a> : <fmt:formatNumber value="${matchup.team1Pts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                        <td>vs</td>
                                        <td><a href="gameMatchup.htm?game=${matchup.gameNo}&wk=${fantasyDisplayWeek.FSSeasonWeekNo}"><c:out value="${matchup.FSTeam2.teamName}" /></a> : <fmt:formatNumber value="${matchup.team2Pts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr class="rowData2">
                                        <td colspan="3">No results to display.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>
                    </div> <!-- inner League Results -->
                </div> <!-- League Results -->

                <div id="leagueSchedule">
                    <div id="innerLeagueSchedule">
                        <table>
                            <tds:tableRows items="${currentSchedule}" var="matchup" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="3">Next Week's Schedule</td>
                                    </tr>
                                    <tr>
                                        <td colspan="3">for Week #${fantasyCurrentWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>TEAM 1</td>
                                        <td>vs</td>
                                        <td>TEAM 2</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td><a href="gameMatchup.htm?game=${matchup.gameNo}&wk=${fantasyCurrentWeek.FSSeasonWeekNo}"><c:out value="${matchup.FSTeam1.teamName}" /></a></td>
                                        <td>vs</td>
                                        <td><a href="gameMatchup.htm?game=${matchup.gameNo}&wk=${fantasyCurrentWeek.FSSeasonWeekNo}"><c:out value="${matchup.FSTeam2.teamName}" /></a></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr class="rowData2">
                                        <td colspan="3">No schedule to display.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>
                    </div> <!-- inner League Schedule -->
                </div> <!-- League Schedule -->

                <div id="leagueTransactions">
                    <div id="innerLeagueTransactions">
                        <!-- Current Transactions -->
                        <table>
                            <tds:tableRows items="${currentTransactions}" var="transaction" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="10">Transactions</td>
                                    </tr>
                                    <tr>
                                        <td colspan="10">for Week #${fantasyCurrentWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>DATE</td>
                                        <td>TEAM</td>
                                        <td>TYPE</td>
                                        <td>PLAYER</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                        <td>TYPE</td>
                                        <td>PLAYER</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td><fmt:formatDate value="${transaction.transactionDate.time}" pattern="E MM/dd HH:mm"/></td>
                                        <td><c:out value="${transaction.FSTeam.teamName}" /></td>
                                        <td><c:out value="${transaction.dropType}" /></td>
                                        <td><tds:player player="${transaction.dropPlayer}" displayStatsLink="true" /></td>
                                        <td><c:out value="${transaction.dropPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.dropPlayer.team.abbreviation}" /></td>
                                        <td><c:out value="${transaction.PUType}" /></td>
                                        <td><tds:player player="${transaction.PUPlayer}" displayStatsLink="true" /></td>
                                        <td><c:out value="${transaction.PUPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.PUPlayer.team.abbreviation}" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="10">There are no transactions for this week.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>

                        <!-- Previous Transactions -->
                        <table>
                            <tds:tableRows items="${previousTransactions}" var="transaction" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr>
                                        <td colspan="10">for Week #${fantasyDisplayWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>DATE</td>
                                        <td>TEAM</td>
                                        <td>TYPE</td>
                                        <td>PLAYER</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                        <td>TYPE</td>
                                        <td>PLAYER</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td><fmt:formatDate value="${transaction.transactionDate.time}" pattern="E MM/dd HH:mm"/></td>
                                        <td><c:out value="${transaction.FSTeam.teamName}" /></td>
                                        <td><c:out value="${transaction.dropType}" /></td>
                                        <td><tds:player player="${transaction.dropPlayer}" displayStatsLink="true" /></td>
                                        <td><c:out value="${transaction.dropPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.dropPlayer.team.abbreviation}" /></td>
                                        <td><c:out value="${transaction.PUType}" /></td>
                                        <td><tds:player player="${transaction.PUPlayer}" displayStatsLink="true" /></td>
                                        <td><c:out value="${transaction.PUPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.PUPlayer.team.abbreviation}" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="10">There are no transactions for this week.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>
                    </div> <!-- inner League Transactions -->
                </div> <!-- league Transactions -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
