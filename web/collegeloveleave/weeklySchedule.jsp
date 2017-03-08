<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - College Love Leave </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script type="text/javascript" src="../js/script.js" ></script>
    <style type="text/css">
        h2 { text-align: center; }
        select { background-color: #F2BC57; }
        
        #container { overflow: hidden; }
        
        #leftPH { float: left; }
        #leftPH h2 { color: #103B40; display:inline; }
        #leftPH label { color: #BF8339; font-size: 1.4em; margin-left: 10px; }
        #rightPH { float: right; }
        
        #weekNumLinks { clear: both; }
        #weekNumLinks { font-size: 1.1em; margin: 25px 25px; text-align: center; }
        #weekNumLinks a { color: #1C5953; font-size: 1.1em; padding-left: 20px; }
        #weekNumLinks a:hover { color: #BF8339; }
        #weekNumLinks a.currWeek { color: #BF8339; font-size: 1.6em; text-decoration: none; }
        
        #byeTeams { clear: both; }
        #byeTeams img { height : 60px; width : 75px; }
        #byeTeams hr { border: medium solid black; margin: 20px 10px; }
        #byeTeams table{ width: auto; }
        #byeTeams th { font-size: 1.8em; }
        #byeTeams td { color: blue; }
        
        #schedule { clear:both; width: 100%; }
        #innerSchedule a { color: #1C5953; text-decoration: none; }        
        #innerSchedule a:hover { color: #BF8339; }
        #innerSchedule th { color: #103B40; text-align: left; text-transform:  uppercase; }
        #innerSchedule th:first-child { text-align: right; }
        #weeklyPicks { clear: both; width: 100%; }        
        #innerWeeklyPicks { margin: 5px; }        
        #innerWeeklyPicks td, #innerWeeklyPicks img { height : 35px; width : 45px; }
        #innerWeeklyPicks .checkmark { height: 25px; width: 25px; } 
        #innerWeeklyPicks a { color: #1C5953; font-size: 1.3em; text-decoration: underline; }
        #innerWeeklyPicks a:hover { color: #BF8339; }        
        #innerWeeklyPicks a.highlightedWeek { color: #BF8339; font-size: 1.7em; text-decoration: none; }        
        #innerWeeklyPicks table { border: solid black thick; }        
        #innerWeeklyPicks td { border-bottom: 1px solid black; border-right: 1px solid black; }
        
        img[id*=helmet] { height : 60px; width : 75px; }
        
        .checkmark { height: 30px; width: 30px; }
        .gameDate { font-weight: bold; text-align: left; }
        .gameNote { color: #731702; }
        .hName { text-align: left; }
        .hRank, .vRank { color: blue; font-size: 1.2em; font-weight: bold; }
        .loserPts, .winnerPts { font-size: 1.5em; padding-left: 5px; padding-right: 5px; text-align: center; }
        .loserPts { color: darkred; }
        .pickedTeam { border: thick solid #731702; }
        .vName { text-align: right; }
        .winnerPts { color: darkgreen; }
    </style>
  </head>

<body>
    <div id="container">
        
        <div id="header"><jsp:include page="../inc_header.jsp" /></div>
        <div id="mainMenu"><jsp:include page="../menu/mainMenu.jsp" /></div>
        <div id="leftMenu"><div id="innerLeftMenu"><jsp:include page="../menu/inc_leftMenu.jsp" /></div></div>

        <div id="content">
            <div id="innerContent">
                
                <%-- Page Header --%>
                <div id="pageHeader">
                
                    <div id="leftPH">
                        <h2>${displayTeam.teamName}</h2>
                        <c:if test="${displayWeek.status == 'COMPLETED'}" >
                            <label>(<fmt:formatNumber value="${fsStandings.gamePoints}" maxFractionDigits="0" /> pts)</label>
                        </c:if>
                        
                    </div>
                
                    <div id="rightPH">
                        <form action="weeklySchedule.htm">
                            <select id="allTeams" name="dtid" >
                                <c:forEach items="${allLeagueTeams}" var="team">
                                    <option value="${team.FSTeamID}"
                                        <c:if test="${team.FSTeamID == displayTeam.FSTeamID}">
                                            selected="selected"
                                        </c:if>
                                    >${team.teamName}</option>
                                </c:forEach>                               
                            </select>
                            <input id="showTeam" type="submit" value="Show" />
                            <input type="hidden" name="wk" value="${displayWeek.FSSeasonWeekID}"></input>
                        </form>                        
                    </div>

                </div>

                <%-- Week Number Links --%>
                <div id="weekNumLinks">
                    <label>Week #</label>
                    <c:forEach items="${allWeeks}" var="week">
                        <a <c:if test="${week.FSSeasonWeekID == displayWeek.FSSeasonWeekID}">class="currWeek"</c:if>
                            href="weeklySchedule.htm?wk=${week.FSSeasonWeekID}&dtid=${displayTeam.FSTeamID}">${week.FSSeasonWeekNo}
                        </a>
                    </c:forEach>                    
                </div>
     
                <div id="weeklyPicks">
                    <div id="innerWeeklyPicks">
                        
                        <%-- Initialize variables --%>
                        <c:set var="prevGameDate" value="" />
                        <c:set var="prevFSSeasonWeekID" value="" />
                        
                        <table>
                            <tr>
                                <c:forEach items="${picks}" var="pick">
                                    <c:if test="${pick.FSSeasonWeek.FSSeasonWeekID != prevFSSeasonWeekID}">                                    
                                        <td>${pick.FSSeasonWeek.FSSeasonWeekNo}</td>
                                    </c:if>
                                    <c:set var="prevFSSeasonWeekID" value="${pick.FSSeasonWeek.FSSeasonWeekID}" />
                                </c:forEach>
                            </tr>
                            
                            <tr>
                                <c:forEach items="${picks}" var="pick">
                                    <c:if test="${pick.FSSeasonWeek.FSSeasonWeekID != prevFSSeasonWeekID}">                                    
                                        <td>
                                    </c:if>
                                    <c:if test="${pick.teamPickedID > 0 && (fsteam.FSTeamID == displayTeam.FSTeamID || pick.game.gameHasStarted)}">
                                        <img src="/topdawgsports/images/NCAAImages/Color/${pick.teamPickedID}.gif" alt="" />
                                    </c:if>
                                    <c:if test="${pick.game.winnerID > 0}">
                                        <br />
                                        <c:choose>
                                            <c:when test="${pick.isPickCorrect == true}">
                                                <img class="checkmark" src="/topdawgsports/images/greenCheckmark.jpg" alt="checkmark" />
                                            </c:when>
                                            <c:otherwise>
                                                <img class="checkmark" src="/topdawgsports/images/redX.jpg" alt="checkmark" />
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <c:set var="prevFSSeasonWeekID" value="${pick.FSSeasonWeek.FSSeasonWeekID}" />
                                </c:forEach>
                            </tr>
                        </table>                        
                    </div> <%-- inner weekly picks --%>
                </div> <%-- weekly picks --%>

                <%-- Game Matchup --%>
                <div id="schedule">
                    <div id="innerSchedule">
                        
                        <label class="gameNote">*All games are displayed in Mountain Standard Time</label>
                        <h2>Week #${displayWeek.FSSeasonWeekNo}</h2>
                        
                        <table>
                            
                            <thead>                            
                                <th>Visitor Team</th>
                                <th></th>
                                <th></th>
                                <th></th>
                                <th></th>
                                <th></th>
                                <th></th>
                                <th></th>
                                <th>Home Team</th>                                
                            </thead>
                            
                            <c:forEach items="${games}" var="game">
                                
                                <%-- Game Date --%>
                                <c:if test="${(game.gameDate != prevGameDate) || game.gameInfo != null}">
                                    <tr>
                                        <td class="gameDate" colspan="8"><fmt:formatDate value="${game.gameDate.time}" pattern="EEEE, MMM. d - h:mm a"/></td>
                                        <c:set var="prevGameDate" value="${game.gameDate}" />
                                    </tr>
                                </c:if>

                                <tr id="gameMatchup_${game.gameID}">
                                    
                                    <%-- Visitor Name and Record --%>
                                    <td class="vName">
                                        <a href="javascript:viewTeamSchedule('/topdawgsports/collegeTeamSchedule.htm?tid=${game.visitorID}')">
                                            ${game.visitor.fullName}
                                            <c:choose>
                                                <c:when test="${!empty weekStandings[game.visitorID]}">
                                                    (${weekStandings[game.visitorID].wins}-${weekStandings[game.visitorID].losses})
                                                </c:when>
                                                <c:otherwise>
                                                    (0-0)
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                    </td>
                                         
                                    <%-- Visitor Ranking--%>
                                    <td class="vRank">
                                        <c:if test="${!empty apRankings[game.visitorID]}">
                                            #${apRankings[game.visitorID].overallRanking}
                                        </c:if>
                                    </td>

                                    <%-- Visitor Helmet --%>
                                    <td>
                                        <%-- See if the helmet should be in color or grayscale --%>
                                        <c:choose>
                                            <c:when test="${empty expiredTeams[game.visitorID]}">
                                                <c:set var="visitorTeamPath" value = "/topdawgsports/images/NCAAImages/Color/${game.visitorID}" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="visitorTeamPath" value = "/topdawgsports/images/NCAAImages/Grayscale/${game.visitorID}" />
                                            </c:otherwise>
                                        </c:choose>
                                        
                                        <c:choose>
                                            <c:when test="${!game.gameHasStarted && empty expiredTeams[game.visitorID] && fsteam.FSTeamID == displayTeam.FSTeamID}">
                                                <a href="weeklySchedule.do?gid=${game.gameID}&tp=${game.visitorID}&fst=${displayTeam.FSTeamID}&wk=${displayWeek.FSSeasonWeekID}">
                                                    <img id="helmet_${game.visitorID}" <c:if test="${teamPicked1ID == game.visitorID || teamPicked2ID == game.visitorID}"> class="pickedTeam"</c:if> src="${visitorTeamPath}.gif" alt="" />
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <img id="helmet_${game.visitorID}" <c:if test="${(teamPicked1ID == game.visitorID || teamPicked2ID == game.visitorID) && game.gameHasStarted}"> class="pickedTeam"</c:if> src="${visitorTeamPath}.gif" alt="" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                        
                                    <%-- Visitor Score --%>
                                    <td
                                        <c:choose>
                                            <c:when test="${game.visitorID == game.winnerID}">
                                                class="winnerPts"
                                            </c:when>
                                            <c:when test="${game.homeID == game.winnerID}">
                                                class="loserPts"
                                            </c:when>
                                        </c:choose>
                                    >
                                        <c:if test="${game.gameHasStarted}">
                                            ${game.visitorPts}
                                        </c:if>
                                    </td>
                                                                                
                                    <%-- Result --%>
                                    <td class="versusLabel">
                                        <c:choose>
                                                <c:when test="${game.winnerID > 0}">
                                                    <c:choose>
                                                        <c:when test="${(teamPicked1ID == game.visitorID || teamPicked2ID == game.visitorID) || (teamPicked1ID == game.homeID || teamPicked2ID == game.homeID)}">
                                                            <c:choose>
                                                                <c:when test="${teamPicked1ID == game.winnerID || teamPicked2ID == game.winnerID}">
                                                                    <img class="checkmark" src="/topdawgsports/images/greenCheckmark.jpg" alt="checkmark" />
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img class="checkmark" src="/topdawgsports/images/redX.jpg" alt="checkmark" />
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            -
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    AT
                                                </c:otherwise>
                                        </c:choose>
                                    </td>
                                    
                                    <%-- Home Score --%>   
                                    <td
                                        <c:choose>
                                            <c:when test="${game.homeID == game.winnerID}">
                                                class="winnerPts"
                                            </c:when>
                                            <c:when test="${game.visitorID == game.winnerID}">
                                                class="loserPts"
                                            </c:when>
                                        </c:choose>
                                    >
                                        <c:if test="${game.gameHasStarted}">
                                            ${game.homePts}
                                        </c:if>
                                    </td>

                                    <%-- Home Helmet --%>
                                    <td>
                                        <%-- See if the helmet should be in color or grayscale --%>
                                        <c:choose>
                                            <c:when test="${empty expiredTeams[game.homeID]}">
                                                <c:set var="homeTeamPath" value = "/topdawgsports/images/NCAAImages/Color/${game.homeID}" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="homeTeamPath" value = "/topdawgsports/images/NCAAImages/Grayscale/${game.homeID}" />
                                            </c:otherwise>
                                        </c:choose>
                                        
                                        <c:choose>
                                            <c:when test="${!game.gameHasStarted && empty expiredTeams[game.homeID] && fsteam.FSTeamID == displayTeam.FSTeamID}">
                                                <a href="weeklySchedule.do?gid=${game.gameID}&tp=${game.homeID}&fst=${displayTeam.FSTeamID}&wk=${displayWeek.FSSeasonWeekID}">
                                                    <img id="helmet_${game.homeID}" <c:if test="${teamPicked1ID == game.homeID || teamPicked2ID == game.homeID}"> class="pickedTeam"</c:if> src="${homeTeamPath}.gif" alt="" />
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <img id="helmet_${game.homeID}" <c:if test="${(teamPicked1ID == game.homeID || teamPicked2ID == game.homeID)  && game.gameHasStarted}"> class="pickedTeam"</c:if> src="${homeTeamPath}.gif" alt="" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <%-- Home Ranking--%>
                                    <td class="hRank">
                                        <c:if test="${!empty apRankings[game.homeID]}">
                                            #${apRankings[game.homeID].overallRanking}
                                        </c:if>
                                    </td>

                                    <%-- Home Name and Record --%>
                                    <td class="hName">
                                        <a href="javascript:viewTeamSchedule('/topdawgsports/collegeTeamSchedule.htm?tid=${game.homeID}')">
                                            ${game.home.fullName}
                                            <c:choose>
                                                <c:when test="${!empty weekStandings[game.homeID]}">
                                                    (${weekStandings[game.homeID].wins}-${weekStandings[game.homeID].losses})
                                                </c:when>
                                                <c:otherwise>
                                                    (0-0)
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div> <%-- inner schedule --%>
                </div> <%-- schedule --%>

                <%-- BYE TEAMS --%>
                <c:if test="${!empty byeTeams}">
                    <div id="byeTeams">
                        <hr />
                        <h3>Bye Teams:</h3>
                        <table>                           
                            <tr>
                                <c:forEach items="${byeTeams}" var="team">
                                    <td><img src="/topdawgsports/images/NCAAImages/Color/${team.homeID}.gif" alt="" /></td>
                                </c:forEach>
                            </tr>
                            <tr>
                                <c:forEach items="${byeTeams}" var="team">
                                    <td>
                                        <c:if test="${!empty apRankings[team.homeID]}">
                                            #${apRankings[team.homeID].overallRanking}
                                        </c:if>
                                    </td>
                                </c:forEach>
                            </tr>
                        </table>
                    </div>
                </c:if>
            </div> <%-- inner content--%>
        </div> <%-- content --%>
    </div> <%-- container --%>

</body>
</html>