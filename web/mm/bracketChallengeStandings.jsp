<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - March Madness Bracket Challenge Standings</title>
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script src="../js/jquery-1.6.2.min.js" type="text/javascript"></script>
    <script src="../js/jquery.tablesorter.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $("table").tablesorter({widgets: ['zebra']});
        });
    </script>
    <style type="text/css">

        #innerStandings { line-height: 30px; margin: 0px 10px 0px 10px; }
        #innerStandings a { color: #BF8339; }
        #innerStandings a:hover { color: white; cursor: pointer; }
        #weekStatus { color:#BF8339; margin-left: 25px; }
        table { background-color: #103B40; color: #F2BC57; }
        thead { background-color: white; color: #731702; cursor: pointer; font-size: 1em; font-weight: bold; text-transform: uppercase; }
        thead tr:first-child { color: #103B40; cursor: default; }
        th:hover { color: #BF8339; cursor: pointer; }
        tr.odd { background-color: #1C5953; color: #F2BC57; }        
        
        #weekNumLinks { font-size: 1.1em; margin: 25px 25px; text-align: center; }
        #weekNumLinks a { color: #1C5953; font-size: 1.1em; padding-left: 20px; }
        #weekNumLinks a:hover { color: #BF8339; }
        #weekNumLinks a.highlightedWeek { color: #BF8339; font-size: 1.6em; text-decoration: none; }
        
        .tourneyWinner { color: white; }
        
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

               <div id="standings">
                   <div id="innerStandings">
                       
                       <%-- Week Number Links --%>
                       <div id="weekNumLinks">                           
                           <label>Round #</label>
                           <c:forEach items="${standingsWeeks}" var="week">
                               <a <c:if test="${week.FSSeasonWeekID == displayWeek.FSSeasonWeekID}">class="highlightedWeek"</c:if>
                                   href="bracketChallengeStandings.htm?fsSeasonWeekID=${week.FSSeasonWeekID}">${week.FSSeasonWeekNo}
                               </a>
                           </c:forEach>
                           <label id="weekStatus">(${displayWeek.status})</label>
                       </div>
                       
                       <%-- Standings --%>
                       <table>

                           <thead>
                               <tr>
                                   <th colspan="2"></th>
                                   <th colspan="3">Points</th>
                                   <th colspan="4">Final Four</th>
                                   <th></th>
                               </tr>
                               <tr>
                                   <th>Rank</th>
                                   <th>Player</th>
                                   <th>Total</th>
                                   <th>Round</th>                                   
                                   <th>Max</th>                                   
                                   <th>${regions[0].regionName}</th>
                                   <th>${regions[1].regionName}</th>
                                   <th>${regions[2].regionName}</th>
                                   <th>${regions[3].regionName}</th>                                   
                               </tr>
                           </thead>
                           
                           <c:forEach items="${standings}" var="s">
                               <tr>
                                   <td>${s.rank}</td>
                                   <td><a title="${s.FSTeam.FSUser.firstName} ${s.FSTeam.FSUser.lastName}"
                                            <c:if test="${leagueTournament.status != 'UPCOMING'}">
                                                href="bracketChallenge.htm?dtid=${s.FSTeam.FSTeamID}" 
                                            </c:if>
                                        >${s.FSTeam.teamName}
                                        </a>
                                   </td>
                                   <td>${s.totalPoints}</td>
                                   <td>${s.roundPoints}</td>
                                   <td>${s.maxPossible}</td>
                                   <c:choose>
                                       <c:when test="${leagueTournament.status == 'UPCOMING'}">
                                           <td>&nbsp;</td>
                                           <td>&nbsp;</td>
                                           <td>&nbsp;</td>
                                           <td>&nbsp;</td>
                                       </c:when>
                                       <c:otherwise>
                                           <td <c:if test="${s.region1Winner == s.champWinner}"> class="tourneyWinner" </c:if> >${s.region1Winner}</td>
                                           <td <c:if test="${s.region2Winner == s.champWinner}"> class="tourneyWinner" </c:if> >${s.region2Winner}</td>
                                           <td <c:if test="${s.region3Winner == s.champWinner}"> class="tourneyWinner" </c:if> >${s.region3Winner}</td>
                                           <td <c:if test="${s.region4Winner == s.champWinner}"> class="tourneyWinner" </c:if> >${s.region4Winner}</td>
                                       </c:otherwise>
                                   </c:choose>                                   
                               </tr>                                                              
                           </c:forEach>

                       </table>

                   </div> <!-- inner standings -->
               </div> <!-- standings -->
            </div> <!-- innner content -->
        </div> <!-- content -->
    </div> <!-- container -->
    
</body>
</html>