<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<html>
<head>
    <title>TopDawgSports Salary : Fantasy Football</title>
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script type="text/javascript" src="../js/script.js" ></script>
</head>
<body>
    <div id="playerStats">
        <div id="innerPlayerStats">
   
            <table>
                <tr class="rowTitle">
                    <td colspan="15">${rosterteam.teamName}</td>
                </tr>
                <tr>
                    <td colspan="15">Week # <c:out value="${rosterWeek.FSSeasonWeekNo}" /></td>
                </tr>
            </table>
            <!-- Stats QB to TE -->
            <table>
                <c:set var="sort" value="Position_PositionName" />
                <tds:tableRows items="${teamRoster}" var="roster" highlightRowAttribute="class" highlightRowValue="rowData2">
                    <jsp:attribute name="rowHeader">
                        <tr class="rowHeader">
                            <td colspan="4"></td>
                            <td colspan="3">Passing</td>
                            <td colspan="2">Rushing</td>                            
                            <td colspan="3">Receiving</td>
                            <td></td>
                            <td>Ret</td>
                            <td>Fum</td>
                            <td>Total</td>
                        </tr>
                        <tr class="rowHeader">
                            <td>Pos</td>
                            <td>Team</td>
                            <td>Player</td>                            
                            <td>Opp</td>
                            <td>Yds</td>
                            <td>TD</td>
                            <td>Int</td>
                            <td>Yds</td>
                            <td>TD</td>                            
                            <td>Rec</td>
                            <td>Yds</td>
                            <td>TD</td>                            
                            <td>2 pt</td> 
                            <td>TD</td>
                            <td>Lost</td>
                            <td>Points</td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowData" >
                        <c:if test="${roster.player.position.positionName != 'PK'}">
                            <tr ${highlightRow1} class="rowData">
                                <c:set var="game" value="${tds:getGame(rosterWeek.seasonWeekID,roster.player.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,roster.player.teamID)}" />
                                <td>${roster.player.position.positionName}</td>
                                <td>${roster.player.team.abbreviation}</td>
                                <td><tds:player player="${roster.player}" displayStatsLink="true" /></td>                                
                                <td>${opp}</td>
                                <td>${roster.footballStats.passYards}</td>
                                <td>${roster.footballStats.passTD}</td>
                                <td>${roster.footballStats.passInt}</td>
                                <td>${roster.footballStats.rushYards}</td>
                                <td>${roster.footballStats.rushTD}</td>                                
                                <td>${roster.footballStats.recCatches}</td>
                                <td>${roster.footballStats.recYards}</td>
                                <td>${roster.footballStats.recTD}</td>                                
                                <td>${roster.footballStats.passTwoPt}</td>
                                <td>${roster.footballStats.xtraTD}</td>
                                <td>${roster.footballStats.fumblesLost}</td>
                                <td>
                                    <c:set var="fpts" value="${roster.footballStats.salFantasyPts}" />
                                    <fmt:formatNumber value="${fpts}" minFractionDigits="2" />
                                    <tds:addRowTotal name="fantasypts" value="${fpts}" />
                                </td>
                            </tr>
                        </c:if>
                    </jsp:attribute>
                    <jsp:attribute name="rowTotal">
                        <c:set var="fptstotal" value="${fptstotal + fantasypts.total}" />
                        <c:set var="fptscount" value="${fptscount + fantasypts.count}" />
                    </jsp:attribute>
                    <jsp:attribute name="rowEmpty">
                        <tr>
                            <td colspan="15">There are no QB, RB, WR, or TE on the roster</td>
                        </tr>
                    </jsp:attribute>
                </tds:tableRows>
            </table>
            <!-- Stats PK -->
            <table>
                <c:set var="sort" value="Position_PositionName" />
                <tds:tableRows items="${teamRoster}" var="roster" highlightRowAttribute="class" highlightRowValue="rowData2">
                    <jsp:attribute name="rowHeader">
                        <tr class="rowHeader">
                            <td>Pos</td>
                            <td>Team</td>
                            <td>Player</td>                            
                            <td>Opp</td>
                            <td>XP</td>
                            <td>XPA</td>
                            <td>FG</td>
                            <td>FGA</td>
                            <td>Distances</td>
                            <td>Points</td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowData" >
                        <c:if test="${roster.player.position.positionName == 'PK'}">
                            <tr ${highlightRow1} class="rowData">
                                <c:set var="game" value="${tds:getGame(rosterWeek.seasonWeekID,roster.player.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,roster.player.teamID)}" />
								
                                <td>${roster.player.position.positionName}</td>
                                <td>${roster.player.team.abbreviation}</td>
                                <td><tds:player player="${roster.player}" displayStatsLink="true" /></td>                                
                                <td>${opp}</td>
                                <td>${roster.footballStats.XPM}</td>
                                <td>${roster.footballStats.XPA}</td>
                                <td>${roster.footballStats.FGM}</td>
                                <td>${roster.footballStats.FGA}</td>
                                <td>${roster.footballStats.TDDistances}</td>
                                <td>
                                    <c:set var="fpts" value="${roster.footballStats.salFantasyPts}" />
                                    <fmt:formatNumber value="${fpts}" minFractionDigits="2" />
                                    <tds:addRowTotal name="fantasypts" value="${fpts}" />
                                </td>
                            </tr>
                        </c:if>
                    </jsp:attribute>
                    <jsp:attribute name="rowTotal">
                        <c:set var="fptstotal" value="${fptstotal + fantasypts.total}" />
                        <c:set var="fptscount" value="${fptscount + fantasypts.count}" />
                    </jsp:attribute>
                    <jsp:attribute name="rowEmpty">
                        <tr>
                            <td colspan="15">There are no PK on the roster</td>
                        </tr>
                    </jsp:attribute>
                </tds:tableRows>
            </table>
        </div>
    </div>
</body>
</html>
