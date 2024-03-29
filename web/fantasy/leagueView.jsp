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
    <style>
        table { text-align: center;}
        #content { position: relative; }
        #oldTimerStandings { float:left; margin-left: 50px;}
        #peeWeeStandings { float:left; margin-left: 150px;}
        #playoffBracket { clear: both;}
        #playoffBracket h2 { padding-top: 15px; text-align: center}
        .bracket, .team, .score, .seed, .finalBracket, .champion, .leagueName { position: absolute; }
        .bracket { border: solid black 3px; border-left: none; }
        .finalBracket { border-top: solid black 3px; }
        .leagueName { font-size: 1.1em; text-transform: uppercase; }
    </style>
  </head>
  <body>

      <div id="container" <c:if test="${fsteam.FSLeague.FSLeagueID == 159}"> style='min-height:1500px;' </c:if>>
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
                                        <td colspan="8">
                                            <c:if test="${fsteam.FSLeague.FSLeagueID != 159}">through Week #${fantasyDisplayWeek.FSSeasonWeekNo}</c:if>
                                        </td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>Rank</td>
                                        <td>Owner</td>
                                        <td>W-L</td>
                                        <td>Pts F</td>
                                        <td>Pts A</td>
                                        <td>Hi</td>
                                        <td>Strk</td>
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
                                        <td title="${standings.FSTeam.FSTeamID}"><c:out value="${standings.FSTeam.teamName}" /></td>
                                        <td><c:out value="${standings.wins}" />-<c:out value="${standings.losses}" /><c:if test="${standings.ties > 0}">-<c:out value="${standings.ties}" /></c:if></td>
                                        <td><fmt:formatNumber value="${standings.totalFantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                        <td><fmt:formatNumber value="${standings.totalFantasyPtsAgainst}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                        <td><c:out value="${standings.totalHiScores}" /></td>
                                        <td><c:if test="${standings.currentStreak > 0}"><c:out value="+" /></c:if><c:out value="${standings.currentStreak}" /></td>
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
                                        <td>OWNER</td>
                                        <td>TYPE</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                        <td>PLAYER</td>
                                        <td>TYPE</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                        <td>PLAYER</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td>
                                            <fmt:parseDate  value="${transaction.transactionDate}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="transactionDate" />
                                            <fmt:formatDate value="${transactionDate}" pattern="E MM/dd HH:mm" timeZone="America/Denver" />
                                        </td>
                                        <td><c:out value="${transaction.FSTeam.teamName}" /></td>
                                        <td><c:out value="${transaction.dropType}" /></td>
                                        <td><c:out value="${transaction.dropPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.dropPlayer.team.abbreviation}" /></td>
                                        <td><tds:player player="${transaction.dropPlayer}" displayStatsLink="true" /></td>
                                        <td><c:out value="${transaction.PUType}" /></td>
                                        <td><c:out value="${transaction.PUPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.PUPlayer.team.abbreviation}" /></td>
                                        <td><tds:player player="${transaction.PUPlayer}" displayStatsLink="true" /></td>
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
                                        <td>OWNER</td>
                                        <td>TYPE</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                        <td>PLAYER</td>
                                        <td>TYPE</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                        <td>PLAYER</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td>
                                            <fmt:parseDate  value="${transaction.transactionDate}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="transactionDate" />
                                            <fmt:formatDate value="${transactionDate}" pattern="E MM/dd HH:mm" timeZone="America/Denver" />
                                        </td>
                                        <td><c:out value="${transaction.FSTeam.teamName}" /></td>
                                        <td><c:out value="${transaction.dropType}" /></td>
                                        <td><c:out value="${transaction.dropPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.dropPlayer.team.abbreviation}" /></td>
                                        <td><tds:player player="${transaction.dropPlayer}" displayStatsLink="true" /></td>
                                        <td><c:out value="${transaction.PUType}" /></td>
                                        <td><c:out value="${transaction.PUPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.PUPlayer.team.abbreviation}" /></td>
                                        <td><tds:player player="${transaction.PUPlayer}" displayStatsLink="true" /></td>
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

                <c:if test="${fsteam.FSLeague.FSLeagueID == 159}">
                    <div id="playoffBracket">
                        <h2>2021 Playoffs</h2>

                        <c:set var="y" value="1000"/>

                        <!-- ROUND 1 -->
                        <div class="bracket" style="top: ${y}px; left: 100px; height: 50px; width: 100px;">
                            <label class="seed" style="top: -27px; left: -25px; width:20px;">1</label>
                            <label style=" top: -27px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=159&wk=15&game=1">Jordan</a></label>
                            <label class="score" style="top: 0.0px; width: 100px;"></label>
                            <label class="seed" style="top: 25px; left: -25px; width:20px;">8</label>
                            <label style=" top: 25px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=159&wk=15&game=1">Nathan</a></label>
                        </div>

                        <div class="bracket" style="top: ${y+100}px; left: 100px; height: 50px; width: 100px;">
                            <label class="seed" style="top: -27px; left: -25px; width:20px;">4</label>
                            <label style=" top: -27px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=159&wk=15&game=2">John</a></label>
                            <label class="score" style="top: 0.0px; width: 100px;"></label>
                            <label class="seed" style="top: 25px; left: -25px; width:20px;">5</label>
                            <label style=" top: 25px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=159&wk=15&game=2">Case</a></label>
                        </div>

                        <div class="bracket" style="top: ${y+200}px; left: 100px; height: 50px; width: 100px;">
                            <label class="seed" style="top: -27px; left: -25px; width:20px;">3</label>
                            <label style=" top: -27px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=159&wk=15&game=3">Mike</a></label>
                            <label class="score" style="top: 0.0px; width: 100px;"></label>
                            <label class="seed" style="top: 25px; left: -25px; width:20px;">6</label>
                            <label style=" top: 25px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=159&wk=15&game=3">Bert</a></label>
                        </div>

                        <div class="bracket" style="top: ${y+300}px; left: 100px; height: 50px; width: 100px;">
                            <label class="seed" style="top: -27px; left: -25px; width:20px;">2</label>
                            <label style=" top: -27px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=159&wk=15&game=4">Cooper</a></label>
                            <label class="score" style="top: 0.0px; width: 100px;"></label>
                            <label class="seed" style="top: 25px; left: -25px; width:20px;">7</label>
                            <label style=" top: 25px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=159&wk=15&game=4">Jackson</a></label>
                        </div>

                        <!-- SEMI FINAL -->
                        <div class="bracket" style="top: ${y+25}px; left: 200px; height: 100px; width: 100px;">
                            <label style=" top: -27px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=159&wk=16&game=1">Jordan</a></label>
                            <label class="score" style="top: 0.0px; width: 100px;"></label>
                            <label style=" top: 75px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=159&wk=16&game=1">Casey</a></label>
                        </div>

                        <div class="bracket" style="top: ${y+225}px; left: 200px; height: 100px; width: 100px;">
                            <label style=" top: -27px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=159&wk=16&game=2">Mike</a></label>
                            <label class="score" style="top: 0.0px; width: 100px;"></label>
                            <label style=" top: 75px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=159&wk=16&game=2">Cooper</a></label>
                        </div>

                        <!-- CHAMPIONSHIP -->
                        <div class="bracket" style="top: ${y+75}px; left: 300px; height: 200px; width: 100px;">
                            <label style=" top: -27px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=159&wk=17&game=1">Jordan</a></label>
                            <label class="score" style="top: 0.0px; width: 100px;"></label>
                            <label style=" top: 175px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=159&wk=17&game=1">Cooper</a></label>
                        </div>

                        <!-- CHAMPION -->
                        <div class="finalBracket" style="top: ${y+175}px; left: 400px; height: 250px; width: 100px;">
                            <label style=" top: -27px; width: 100px;" class="team">&#160;&#160;&#160;Jordan</label>
                        </div>

                    </div>
                </c:if>

            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
