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
    <style type="text/css">
        th { background-color: white; color: #731702; font-size: 1em; font-weight: bold; text-transform: uppercase; }
        tr { color: #F2BC57; }
        tr:nth-child(odd) { background-color: #103B40; }
        tr:nth-child(even) { background-color: #1C5953; }
        #gameHeader { font-size: 1.5em; text-align: center; }
        #innerContent { margin: 10px; }
        .teamPts { color: #731702; margin: 15px;  }
        .teamTitle { color: #1C5953; font-size: 1.5em; text-transform: uppercase; }
    </style>
  </head>

<body>
    <div id="container">
        <div id="header"><jsp:include page="../inc_header.jsp" /></div>
        <div id="mainMenu"><jsp:include page="../menu/mainMenu.jsp" /></div>
        <div id="leftMenu"><div id="innerLeftMenu"><jsp:include page="../menu/inc_leftMenu.jsp" /></div></div>

        <div id="content">
            <div id="innerContent">

                <%-- Initialization --%>
                <c:set var="prevTeamId" value="0" />
                <c:set var="clearFirst" value="False" />


                <%-- GAME INFORMATION --%>
                <div id="gameHeader">
                    <h3>Round ${game.round.roundNumber} - ${game.status}</h3>
                    <div>
                        <label>${game.topTeam.teamName}</label>
                        <label class="teamPts">${game.team1Pts}</label>
                        <label>-</label>
                        <label class="teamPts">${game.team2Pts}</label>
                        <label>${game.bottomTeam.teamName}</label>
                    </div>
                </div>

                <%-- TOP TEAM --%>
                <label class="teamTitle">${game.topTeam.teamName}</label>
                <table>
                    <tr>
                        <th>Pos</th>
                        <th>Team</th>
                        <th>Player</th>
                        <th>Pass</th>
                        <th>Rush</th>
                        <th>Rec</th>
                        <th>TDs</th>
                        <th>2 Pt</th>
                        <th>Ftsy Pts</th>
                    </tr>
                    <c:forEach items="${topTeamRoster}" var="topTeam">
                        <tr>
                            <td>${topTeam.player.position.positionName}</td>
                            <td>${topTeam.player.team.abbreviation}</td>
                            <td>${topTeam.player.firstName} ${topTeam.player.lastName}</td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${topTeam.footballStats.passYards}"/></td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${topTeam.footballStats.rushYards}"/></td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${topTeam.footballStats.recYards}"/></td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${topTeam.footballStats.passTD + topTeam.footballStats.rushTD + topTeam.footballStats.recTD}"/></td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${topTeam.footballStats.passTwoPt + topTeam.footballStats.rushTwoPt + topTeam.footballStats.recTwoPt}"/></td>
                            <td><fmt:formatNumber minFractionDigits="2" value="${topTeam.footballStats.salFantasyPts}"/></td>
                            
                            <c:set var="passYds" value="${passYds + topTeam.footballStats.passYards}" />
                            <c:set var="rushYds" value="${rushYds + topTeam.footballStats.rushYards}" />
                            <c:set var="recYds" value="${recYds + topTeam.footballStats.recYards}" />
                            <c:set var="tds" value="${tds + topTeam.footballStats.passTD + topTeam.footballStats.rushTD + topTeam.footballStats.recTD}" />
                            <c:set var="twoPts" value="${twoPts + topTeam.footballStats.passTwoPt + topTeam.footballStats.rushTwoPt + topTeam.footballStats.recTwoPt}" />
                            <c:set var="pts" value="${pts + topTeam.footballStats.salFantasyPts}" />
                        </tr>
                    </c:forEach>
                    <tr> 
                        <th>&nbsp;</th>
                        <th>&nbsp;</th>
                        <th>TOTALS :</th>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${passYds}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${rushYds}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${recYds}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${tds}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${twoPts}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="2" value="${pts}"/></td>
                    </tr>
                    
                </table>
                    
                <%-- Reset variables --%>
                <c:set var="passYds" value="0" />
                <c:set var="rushYds" value="0" />
                <c:set var="recYds" value="0" />
                <c:set var="tds" value="0" />
                <c:set var="twoPts" value="0" />
                <c:set var="pts" value="0" />
                    
                <%-- BOTTOM TEAM --%>
                <label class="teamTitle">${game.bottomTeam.teamName}</label>
                <table>
                    <tr>
                        <th>Pos</th>
                        <th>Team</th>
                        <th>Player</th>
                        <th>Pass</th>
                        <th>Rush</th>
                        <th>Rec</th>
                        <th>TDs</th>
                        <th>2 Pt</th>
                        <th>Ftsy Pts</th>
                    </tr>
                    <c:forEach items="${bottomTeamRoster}" var="bottomTeam">
                        <tr>
                            <td>${bottomTeam.player.position.positionName}</td>
                            <td>${bottomTeam.player.team.abbreviation}</td>
                            <td>${bottomTeam.player.firstName} ${bottomTeam.player.lastName}</td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${bottomTeam.footballStats.passYards}"/></td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${bottomTeam.footballStats.rushYards}"/></td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${bottomTeam.footballStats.recYards}"/></td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${bottomTeam.footballStats.passTD + bottomTeam.footballStats.rushTD + bottomTeam.footballStats.recTD}"/></td>
                            <td><fmt:formatNumber maxFractionDigits="0" value="${bottomTeam.footballStats.passTwoPt + bottomTeam.footballStats.rushTwoPt + bottomTeam.footballStats.recTwoPt}"/></td>
                            <td><fmt:formatNumber minFractionDigits="2" value="${bottomTeam.footballStats.salFantasyPts}"/></td>
                            
                            <c:set var="passYds" value="${passYds + bottomTeam.footballStats.passYards}" />
                            <c:set var="rushYds" value="${rushYds + bottomTeam.footballStats.rushYards}" />
                            <c:set var="recYds" value="${recYds + bottomTeam.footballStats.recYards}" />
                            <c:set var="tds" value="${tds + bottomTeam.footballStats.passTD + bottomTeam.footballStats.rushTD + bottomTeam.footballStats.recTD}" />
                            <c:set var="twoPts" value="${twoPts + bottomTeam.footballStats.passTwoPt + bottomTeam.footballStats.rushTwoPt + bottomTeam.footballStats.recTwoPt}" />
                            <c:set var="pts" value="${pts + bottomTeam.footballStats.salFantasyPts}" />
                        </tr>
                    </c:forEach>
                    <tr> 
                        <th>&nbsp;</th>
                        <th>&nbsp;</th>
                        <th>TOTALS :</th>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${passYds}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${rushYds}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${recYds}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${tds}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="0" value="${twoPts}"/></td>
                        <th><fmt:formatNumber maxFractionDigits="2" value="${pts}"/></td>
                    </tr>
                    
                </table>
                        
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

</body>
</html>