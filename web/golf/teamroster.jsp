<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<html>
<head>
    <title>TopDawgSports : Golf</title>
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
                <tr>
                    <td colspan="15">Tournament <c:out value="${tournamentWeek.PGATournament.tournamentName}" /></td>
                </tr>
            </table>
            <table>
                <c:set var="sort" value="Position_PositionName" />
                <tds:tableRows items="${teamRoster}" var="roster" highlightRowAttribute="class" highlightRowValue="rowData2">
                    <jsp:attribute name="rowHeader">
                        <tr class="rowHeader">
                            <td>Player</td>                            
                            <td>Country</td>
                            <td>Rank</td>
                            <td>Score</td>
                            <td>$ Earned</td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowData" >
                        <tr ${highlightRow1} class="rowData">
                            <td><tds:player player="${roster.player}" displayStatsLink="true" /></td>                                
                            <td><c:out value="${roster.player.country.country}"/></td>
                            <td><c:out value="${roster.PGATournamentWeekPlayer.tournamentRank}" /></td>
                            <td><c:out value="${roster.PGATournamentWeekPlayer.relativeToPar}" /></td>
                            <td><fmt:formatNumber type="currency" value="${roster.PGATournamentWeekPlayer.moneyEarned}" maxFractionDigits="0"/></td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowEmpty">
                        <tr>
                            <td colspan="15">There are no players on the roster</td>
                        </tr>
                    </jsp:attribute>
                </tds:tableRows>
            </table>
        </div>
    </div>
</body>
</html>
