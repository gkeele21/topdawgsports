<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>Stat Leaders</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script src="../js/jquery-1.6.2.min.js" type="text/javascript"></script>
    
    <script type="text/javascript">
        $(document).ready(function() {
            $("table").tablesorter({widgets: ['zebra']});
        });
    </script>
    <script type="text/javascript" src="../js/script.js" ></script>
    <style type="text/css">        
        table a:hover { color: white; cursor: pointer; }
        thead { background-color: white; color: #731702; cursor: pointer; font-size: 1em; font-weight: bold; text-transform: uppercase; }
        thead tr:first-child { color: #103B40; cursor: default; }
        th:hover { color: #BF8339; cursor: pointer; }
        tr.even { background-color: #103B40; color: #F2BC57; }
        table { background-color: #1C5953; color: #F2BC57; }
        
    </style>
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

                <div id="playerstats">
                    <div id="innerPlayerStats">

                        <div id="position">
                            <label>Choose a Position :</label>
                            <a href="statLeaders.htm?pos=QB&wk=${reqWeek}">QB</a>
                            <a href="statLeaders.htm?pos=RB&wk=${reqWeek}">RB</a>
                            <a href="statLeaders.htm?pos=WR&wk=${reqWeek}">WR</a>
                            <a href="statLeaders.htm?pos=TE&wk=${reqWeek}">TE</a>
                            <a href="statLeaders.htm?pos=PK&wk=${reqWeek}">PK</a>
                            <c:if test="${showDefense == true}">
                                <a href="statLeaders.htm?pos=DL&wk=${reqWeek}">DL</a>
                                <a href="statLeaders.htm?pos=LB&wk=${reqWeek}">LB</a>
                                <a href="statLeaders.htm?pos=DB&wk=${reqWeek}">DB</a>
                            </c:if>
                        </div>
                        
                        <%-- Week Number Links --%>
                        <div id="weekNumLinks">
                            <label>Week #</label>
                            <a <c:if test="${reqWeek == 0}">class="currWeek"</c:if> href="statLeaders.htm?pos=${posname}">TOTAL</a>
                            <c:forEach items="${displayWeeks}" var="week">
                                <a <c:if test="${week.FSSeasonWeekID == reqWeek}">class="currWeek"</c:if>
                                    href="statLeaders.htm?pos=${posname}&wk=${week.FSSeasonWeekID}">${week.FSSeasonWeekNo}</a>
                            </c:forEach>
                        </div>

                        <table>
                            <thead>
                            <tds:tableRows items="${players}" var="playerstats" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2" displayRows="50" startingRowNum="${startingRowNum}">
                                <jsp:attribute name="rowInfo">
                                    
                                </jsp:attribute>
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="14">${posname} Leaders</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr>
                                        <th>#</th>
                                        <c:if test="${showOwner == true}"><th>Owner</th></c:if>
                                        <th>Team</th>
                                        <th>Player</th>
                                        <c:if test="${posname == 'QB'}">
                                            <th>Comp</th>
                                            <th>Att</th>
                                            <th>Yards</th>
                                            <th>TD's</th>
                                            <th>Int's</th> 
                                        </c:if>
                                        <c:if test="${posname == 'QB' || posname == 'RB' || posname == 'WR'}">
                                            <th>Rush</th>
                                            <th>Yards</th>
                                            <th>TD's</th>
                                        </c:if>
                                        <c:if test="${posname == 'RB' ||posname == 'WR' || posname == 'TE'}">
                                            <th>Rec</th>
                                            <th>Yards</th>
                                            <th>TD's</th>
                                        </c:if>
                                        <!-- KICKING COLUMN HEADERS -->
                                        <c:if test="${posname == 'PK'}">
                                            <th>XP</th>
                                            <th>XPA</th>
                                            <th>FG</th>
                                            <th>FGA</th>
                                            <c:if test="${reqWeek > 0}"><th>Distances</th></c:if>
                                        </c:if>
                                        <!-- DEFENSE MAIN HEADER -->
                                        <c:if test="${posname == 'DL' || posname == 'LB' || posname == 'DB'}">
                                            <th>Tkl</th>
                                            <th>Ast</th>
                                            <th>Sacks</th>
                                            <th>Int's</th>
                                            <th>FF</th>
                                            <th>FR</th>
                                            <th>TD's</th>
                                        </c:if>
                                        <th>2 Pt</th>
                                        <th>Points</th>
                                        <th>Avg</th>
                                    </tr>
                                </thead>
                                </jsp:attribute>
                                
                                <jsp:attribute name="rowData">
                                    
                                    <tr>
                                        <td>${currentRowOverall1}</td>
                                        <c:if test="${showOwner == true}">                                            
                                            <td>
                                                <c:if test="${playerstats.FSTeam != null && playerstats.FSTeam.FSTeamID > 0}">
                                                    <c:out value="${playerstats.FSTeam.teamName}" />
                                                </c:if>
                                            </td>
                                        </c:if>
                                        <td><c:out value="${playerstats.player.team.abbreviation}" /></td>
                                        <td align="left"><tds:player player="${playerstats.player}" displayStatsLink="true" displayInjury="false" /></td>                                        
                                        <!-- PASSING / RUSHING / RECEIVING Stats -->
                                        <c:if test="${posname == 'QB'}">
                                            <td>${playerstats.player.totalFootballStats.passComp}</td>
                                            <td>${playerstats.player.totalFootballStats.passAtt}</td>
                                            <td>${playerstats.player.totalFootballStats.passYards}</td>
                                            <td>${playerstats.player.totalFootballStats.passTD}</td>
                                            <td>${playerstats.player.totalFootballStats.passInt}<tds:addRowTotal name="interceptions" value="${playerstats.player.totalFootballStats.passInt}" /></td>
                                        </c:if>
                                        <c:if test="${posname == 'QB' || posname == 'RB' || posname == 'WR'}">    
                                            <td>${playerstats.player.totalFootballStats.rushAtt}</td>
                                            <td>${playerstats.player.totalFootballStats.rushYards}</td>
                                            <td>${playerstats.player.totalFootballStats.rushTD}</td>                                    
                                        </c:if>
                                        <c:if test="${posname == 'RB' ||posname == 'WR' || posname == 'TE'}">    
                                            <td>${playerstats.player.totalFootballStats.recCatches}</td>
                                            <td>${playerstats.player.totalFootballStats.recYards}</td>
                                            <td>${playerstats.player.totalFootballStats.recTD}</td>
                                        </c:if>
                                        <!-- KICKING Stats -->
                                        <c:if test="${playerstats.player.position.positionName == 'PK'}">
                                            <td>${playerstats.player.totalFootballStats.XPM}</td>
                                            <td>${playerstats.player.totalFootballStats.XPA}</td>
                                            <td>${playerstats.player.totalFootballStats.FGM}</td>
                                            <td>${playerstats.player.totalFootballStats.FGA}</td>
                                            <c:if test="${reqWeek > 0}"><td>${playerstats.player.totalFootballStats.TDDistances}</td></c:if>
                                        </c:if>  
                                        <!-- DEFENSE Stats -->
                                        <c:if test="${playerstats.player.position.positionName == 'DL' || playerstats.player.position.positionName == 'LB' || playerstats.player.position.positionName == 'DB'}">
                                            <td>${playerstats.player.totalFootballStats.IDPTackles}</td>
                                            <td>${playerstats.player.totalFootballStats.IDPAssists}</td>
                                            <td>${playerstats.player.totalFootballStats.IDPSacks}</td>
                                            <td>${playerstats.player.totalFootballStats.IDPInterceptions}</td>
                                            <td>${playerstats.player.totalFootballStats.IDPFumblesForced}</td>
                                            <td>${playerstats.player.totalFootballStats.IDPFumbleRecoveries}</td>
                                            <td>${playerstats.player.totalFootballStats.IDPIntReturnsForTD}</td>
                                        </c:if>
                                        <td>${playerstats.player.totalFootballStats.passTwoPt + stats.rushTwoPt + stats.recTwoPt}</td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.fantasyPts}" minFractionDigits="2" /></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" /></td>
                                    </tr>                                    
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="14">There are no stats available for this week.</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowNavigation">
                                    <tfoot>
                                        <tr>
                                            <td colspan="14"><hr /></td>
                                        </tr>
                                        <tr class="rowData2">
                                            <td colspan="2"><tds:navFirst link="statLeaders.htm?pos=${posname}&wk=${reqWeek}">FIRST</tds:navFirst></td>
                                            <td><tds:navPrev link="statLeaders.htm?pos=${posname}&wk=${reqWeek}"><< PREV</tds:navPrev></td>
                                            <td colspan="3">${fromRows1} to ${toRows1} of ${totalRows1}</td>
                                            <td colspan="2"><tds:navNext link="statLeaders.htm?pos=${posname}&wk=${reqWeek}">NEXT >></tds:navNext></td>
                                            <td><tds:navLast link="statLeaders.htm?pos=${posname}&wk=${reqWeek}">LAST</tds:navLast></td>
                                        </tr>
                                    </tfoot>
                               </jsp:attribute>
                            </tds:tableRows>
                        </table>
                    
                    </div> <!-- inner Player Stats -->
                </div> <!-- Player Stats -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>
