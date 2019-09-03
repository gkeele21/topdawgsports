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
                            <td colspan="2">Receiving</td>
                            <td>Ret</td>
                            <td></td>
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
                            <td>Yds</td>
                            <td>TD</td>
                            <td>TD</td>
                            <td>2 pt</td>
                            <td>Lost</td>
                            <td>FP</td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowData" >
                        <c:if test="${roster.player.position.positionName != 'PK'}">
                            <tr ${highlightRow1} class="rowData">
                                <c:set var="game" value="${tds:getGame(rosterWeek.seasonWeekID,roster.player.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,roster.player.teamID)}" />
                                <td><c:out value="${roster.player.position.positionName}" /></td>
                                <td><c:out value="${roster.player.team.abbreviation}"/></td>
                                <td><tds:player player="${roster.player}" displayStatsLink="true" /></td>                                
                                <td><c:out value="${opp}"/></td>
                                <td><fmt:formatNumber value="${roster.footballStats.passYards}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.passTD}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.passInt}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.rushYards}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.rushTD}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.recYards}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.recTD}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.xtraTD}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.passTwoPt}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.fumblesLost}" maxFractionDigits="0" /></td>
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
                            <td>XPM</td>
                            <td>XPA</td>
                            <td>0-29</td>
                            <td>30-39</td>
                            <td>40-49</td>
                            <td>50+</td>
                            <td>FGA</td>
                            <td>Ftsy Pts</td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowData" >
                        <c:if test="${roster.player.position.positionName == 'PK'}">
                            <tr ${highlightRow1} class="rowData">
                                <c:set var="game" value="${tds:getGame(rosterWeek.seasonWeekID,roster.player.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,roster.player.teamID)}" />
								
                                <td><c:out value="${roster.player.position.positionName}" /></td>
                                <td><c:out value="${roster.player.team.abbreviation}"/></td>
                                <td><tds:player player="${roster.player}" displayStatsLink="true" /></td>                                
                                <td><c:out value="${opp}"/></td>
                                <td><fmt:formatNumber value="${roster.footballStats.XPM}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.XPA}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.FG29Minus}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.FG30to39}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.FG40to49}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.FG50Plus}" maxFractionDigits="0" /></td>
                                <td><fmt:formatNumber value="${roster.footballStats.FGA}" maxFractionDigits="0" /></td>
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
