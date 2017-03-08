<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - College Love Em & Leave Em Standings</title>
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
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
        table { background-color: #103B40; color: #F2BC57; }
        thead { background-color: white; color: #731702; cursor: pointer; font-size: 1em; font-weight: bold; text-transform: uppercase; }
        th:hover { color: #BF8339; cursor: pointer; }
        tr.odd { background-color: #1C5953; color: #F2BC57; }        
        
        #weekNumLinks { font-size: 1.1em; margin: 25px 25px; text-align: center; }
        #weekNumLinks a { color: #1C5953; font-size: 1.1em; padding-left: 20px; }
        #weekNumLinks a:hover { color: #BF8339; }
        #weekNumLinks a.currWeek { color: #BF8339; font-size: 1.6em; text-decoration: none; }

    </style>
  </head>

<body>
    
    <div id="container">
        <div id="header"><jsp:include page="../inc_header.jsp" /></div>
        <div id="mainMenu"><jsp:include page="../menu/mainMenu.jsp" /></div>
        <div id="leftMenu"><div id="innerLeftMenu"><jsp:include page="../menu/inc_leftMenu.jsp" /></div></div>

        <div id="content">
            <div id="innerContent">

               <div id="standings">
                   <div id="innerStandings">
                       
                       <%-- Week Number Links --%>
                       <div id="weekNumLinks">                           
                           <label>Week #</label>
                           <c:forEach items="${standingsWeeks}" var="week">
                               <a <c:if test="${week.FSSeasonWeekID == displayWeek.FSSeasonWeekID}">class="currWeek"</c:if>
                                   href="standings.htm?wid=${week.FSSeasonWeekID}">${week.FSSeasonWeekNo}
                               </a>
                           </c:forEach>                    
                       </div>

                       <%-- Standings --%>
                       <table>

                           <thead>
                               <tr>
                                   <th>Rank</th>
                                   <th>Player</th>                                   
                                   <th>Total Pts</th>
                                   <th>Week Pts</th>
                                   <th>Overall</th>
                                   <th>Week</th>
                               </tr>
                           </thead>
                           
                           <c:forEach items="${leagueStandings}" var="standings">
                               <tr>
                                   <td>${standings.rank}</td>
                                   <td><a href="weeklySchedule.htm?dtid=${standings.FSTeamID}&wid=${displayWeek.FSSeasonWeekID}" title="${standings.FSTeam.FSUser.firstName} ${standings.FSTeam.FSUser.lastName}">${standings.FSTeam.teamName}</a></td>                                    
                                   <td><fmt:formatNumber value="${standings.totalGamePoints}" /></td>
                                   <td><fmt:formatNumber value="${standings.gamePoints}" /></td>
                                   <td>${standings.totalGamesCorrect}-${standings.totalGamesWrong}</td>
                                   <td>${standings.gamesCorrect}-${standings.gamesWrong}</td>
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