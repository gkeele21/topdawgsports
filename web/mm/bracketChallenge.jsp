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
    <script type="text/javascript">
    </script>

    <style type="text/css" >
        #content { position: relative; }
        #container { min-height: 3600px;}
        #innerTournament { margin: 0px 0px 0px 10px; }
        .champsLabel { font-size: 1.3em; text-transform: uppercase; }
        .correctPick { color: green; display: block; }
        .finalLine { border-bottom: solid black 3px; }
        .highlightedTeam { background-color: #F2BC57; }
        .incorrectPick { color: red; display: block; text-decoration: line-through; }
        .ncaaTitle, .regionName { font-size: 2.0em; text-transform: uppercase; }        
        .ncaaTeam { color: black; display: block; } 
        .roundPts { color: #731702; font-size: 1.4em; text-align: center; }
        .roundTitle { color: #103B40; text-align: center; }
        .score { color: #BF8339; display: block; }
        .seeds { color: darkred; font-size: 1.2em; text-align: right; }
        .teamNameTitle { font-size: 1.7em; }
        .totalGamePts { border: 3px solid black; color: #731702; font-size: 1.7em; text-align: center; }
        .tourneyBracket { border: solid black 3px; border-left: none; }
        .winner { font-size: 1.4em; }
        
        .ncaaTitle, .teamNameTitle, .roundTitle, .roundPts, .totalGamePts, .regionName, .winner, 
        .ncaaTeam, .correctPick, .incorrectPick, .tourneyBracket, .bracketType, .winner, .seeds, 
        .score, .finalLine, .bracketNumText, .champsLabel, #allTeams, #showTeam {
            position: absolute; text-align: center; }
    </style>
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
        </div> <!-- left menu -->

        <div id="content">
            <div id="innerContent">

                <div id="Tournament">
                    <div id="innerTournament">

                        <!-- CONSTANTS -->
                        <c:set var="horizontalLength" value="113"/>
                        <c:set var="firstRoundHeight" value="25"/>
                        <c:set var="bracketSpacing" value="25"/>
                        <c:set var="initialBracketYCoord" value="80"/>
                        <c:set var="regionSpacing" value="50"/>

                        <!-- INITIALIZATION -->
                        <c:set var="currentLeft" value="-65"/>
                        <c:set var="currentRegionID" value="0"/>
                        <c:set var="power" value="1"/>

                        <!-- LOOP THROUGH ALL GAMES -->
                        <c:forEach items="${picks}" var="pick">

                            <!-- WHEN CHANGING WEEKS -->
                            <c:if test="${currentRoundID != pick.game.round.roundID}">
                                <c:set var="currentRoundID" value="${pick.game.round.roundID}"/>
                                <c:set var="roundNumber" value="${pick.game.round.roundNumber}" />
                                <c:set var="currentTop" value="${(initialBracketYCoord + (power * firstRoundHeight) - (firstRoundHeight / 2))}"/>
                                <c:set var="currentLeft" value="${(currentLeft + horizontalLength)}"/>

                                <!-- FIGURE OUT POWER OF 2 -->
                                <%  pageContext.setAttribute("power", (int)Math.pow(2, (Double.parseDouble(pageContext.getAttribute("roundNumber").toString())))); 
                                    pageContext.setAttribute("TOTAL_PTS_KEY", Integer.parseInt("-1"));
                                %>                                

                                <!-- ROUND NAME -->
                                <label class="roundTitle" style="top:10px; left:${currentLeft}px; width:${horizontalLength}px;">${pick.game.round.roundName}</label>
                                
                                <!-- ROUND PTS (STANDINGS) -->
                                <label class="roundPts" style="top:30px; left:${currentLeft}px; width:${horizontalLength}px;">
                                    <fmt:formatNumber value="${roundStandings[pick.game.round.seasonWeekID]}" maxFractionDigits="0" />
                                </label>
                            </c:if>

                            <!-- WHEN CHANGING REGIONS -->
                            <c:if test="${currentRegionID != pick.game.region.regionID}">
                                <c:if test="${roundNumber == 1}">
                                    <label class="regionName" style="top:${currentTop - 25}px;">${pick.game.region.regionName}</label>
                                </c:if>
                                <c:set var="currentTop" value="${currentTop + regionSpacing}"/>
                                <c:set var="currentRegionID" value="${pick.game.region.regionID}"/>                                
                            </c:if>

                            <!-- BRACKET TAG -->
                            <div id="${pick.game.gameID}" class="tourneyBracket" title="Game Location: ${pick.game.location}" style="top: ${currentTop}px; left: ${currentLeft}px; height: ${firstRoundHeight * power}px; width: ${horizontalLength}px;">

                                <!-- TOP TEAM -->
                                <label style=" top: -27px; width: ${horizontalLength}px;"
                                   <c:choose>
                                       <c:when test="${roundNumber == 1}">
                                           class="ncaaTeam">${pick.game.topTeamSeed.team.displayName}
                                       </c:when>
                                       <c:otherwise>
                                           <c:choose>
                                               <c:when test="${pick.prevTopGamePick.game.winnerID == null}">
                                                   <c:choose>
                                                       <c:when test="${!empty defeatedTeams[pick.prevTopGamePick.teamSeedPicked.teamID]}">
                                                           class="incorrectPick">
                                                       </c:when>
                                                       <c:otherwise>
                                                           class="ncaaTeam">
                                                       </c:otherwise>
                                                   </c:choose>                                                   
                                               </c:when>
                                               <c:otherwise>
                                                   <c:choose>
                                                       <c:when test="${pick.prevTopGamePick.game.winnerID.equals(pick.prevTopGamePick.teamSeedPickedID)}">
                                                           class="correctPick">
                                                       </c:when>
                                                       <c:otherwise>
                                                           class="incorrectPick">
                                                           <label class="ncaaTeam" style="top: -20px; width: ${horizontalLength}px;">${pick.game.topTeamSeed.team.displayName}</label>
                                                       </c:otherwise>
                                                   </c:choose>
                                                           
                                               </c:otherwise>
                                            </c:choose>                                            
                                            ${pick.prevTopGamePick.teamSeedPicked.team.displayName}
                                        </c:otherwise>
                                    </c:choose>
                                </label>

                                <!-- SCORE -->
                                <c:if test="${pick.game.status != 'UPCOMING'}">
                                    <label class="score" style="top: ${((firstRoundHeight * power) / 2) - 25}px; width: ${horizontalLength}px;">                                    
                                        <fmt:formatNumber value="${pick.game.team1Pts}" maxFractionDigits="0" /> - <fmt:formatNumber value="${pick.game.team2Pts}" maxFractionDigits="0" />                                    
                                    </label>
                                </c:if>

                                <!-- BOTTOM TEAM -->
                                <label style=" top: ${(firstRoundHeight * power) - 25}px; width: ${horizontalLength}px;"
                                    <c:choose>
                                       <c:when test="${roundNumber == 1}">
                                           class="ncaaTeam">${pick.game.bottomTeamSeed.team.displayName}
                                       </c:when>
                                       <c:otherwise>
                                           <c:choose>
                                               <c:when test="${pick.prevBottomGamePick.game.winnerID == null}">
                                                   <c:choose>
                                                       <c:when test="${!empty defeatedTeams[pick.prevBottomGamePick.teamSeedPicked.teamID]}">
                                                           class="incorrectPick">
                                                       </c:when>
                                                       <c:otherwise>
                                                           class="ncaaTeam">
                                                       </c:otherwise>
                                                   </c:choose>                                                   
                                               </c:when>
                                               <c:otherwise>
                                                   <c:choose>
                                                       <c:when test="${pick.prevBottomGamePick.game.winnerID.equals(pick.prevBottomGamePick.teamSeedPickedID)}">
                                                           class="correctPick">
                                                       </c:when>
                                                       <c:otherwise>
                                                           class="incorrectPick">
                                                           <label class="ncaaTeam" style="top: -20px; width: ${horizontalLength}px;">${pick.game.bottomTeamSeed.team.displayName}</label>
                                                       </c:otherwise>
                                                   </c:choose>
                                                           
                                               </c:otherwise>
                                            </c:choose>                                            
                                            ${pick.prevBottomGamePick.teamSeedPicked.team.displayName}
                                        </c:otherwise>
                                    </c:choose>    
                                </label>

                                <!-- SEEDS -->
                                <c:if test="${roundNumber == 1}">
                                    <label class="seeds" style="top: -27px; left: -25px; width:20px;">${pick.game.topTeamSeed.seedNumber}</label>
                                    <label class="seeds" style="top: ${(firstRoundHeight * power) - 25}px; left: -25px; width:20px;">${pick.game.bottomTeamSeed.seedNumber}</label>
                                </c:if>

                            </div>
                                    
                            <%-- ONE TIME IN LOOP --%>
                            <c:if test="${pick.game.nextGameID == null}">
                                
                                <!-- CHAMPS LINE -->
                                <label class="finalLine" style="top: ${(initialBracketYCoord + (power * firstRoundHeight) - (firstRoundHeight / 2))}px; left: ${(currentLeft + horizontalLength)}px; width: ${horizontalLength}px;" ></label>

                                <!-- #1 CHAMPS LABEL -->
                                <label class="champsLabel" style="top: ${(initialBracketYCoord + (power * firstRoundHeight) - (firstRoundHeight / 2)) + 15}px; left: ${(currentLeft + horizontalLength) + 5}px; width: ${horizontalLength}px;">#1 Champs</label>

                                <!-- WINNER -->
                                <label style="top: ${(initialBracketYCoord + (power * firstRoundHeight) - (firstRoundHeight / 2)) - 30}px; left: ${(currentLeft + horizontalLength)}px; width: ${horizontalLength}px;" 
                                <c:choose>
                                    <c:when test="${pick.game.winnerID.equals(pick.teamSeedPickedID)}">
                                        class="correctPick">
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${pick.game.winnerID != null}">
                                                class="incorrectPick">
                                                <label class="ncaaTeam" style="top: -20px; width: ${horizontalLength}px;">${pick.game.winner.team.displayName}</label>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${!empty defeatedTeams[pick.teamSeedPicked.teamID]}">
                                                        class="incorrectPick">
                                                    </c:when>
                                                </c:choose>                                                
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                                ${pick.teamSeedPicked.team.displayName}
                                </label>

                                <!-- TEAM NAME TITLE -->
                                <label class="teamNameTitle" style="top:${(initialBracketYCoord + (firstRoundHeight) - (firstRoundHeight / 2)) - 25}px; left:${(horizontalLength * 2) + 65}px;">
                                    ${displayTeam.teamName}
                                </label>
                                
                                <!-- TOTAL GAME POINTS -->
                                <label class="totalGamePts" style="top:25px; left:${currentLeft + horizontalLength + 35}px; width:50px;">
                                    <fmt:formatNumber value="${roundStandings[TOTAL_PTS_KEY]}" maxFractionDigits="0" />
                                </label>
                                
                                <!-- ALL TEAMS DROPDOWN -->
                                <form action="bracketChallenge.htm">
                                    <select id="allTeams" name="dtid" style="top: ${(initialBracketYCoord + (firstRoundHeight) - (firstRoundHeight / 2)) - 13}px; left: ${currentLeft}px;  width: ${horizontalLength}px;" >
                                        <c:forEach items="${allLeagueTeams}" var="team">
                                            <option value="${team.FSTeamID}"
                                                <c:if test="${team.FSTeamID == displayTeam.FSTeamID}">
                                                    selected="selected"
                                                </c:if>
                                            >${team.teamName}</option>                           
                                        </c:forEach>                               
                                    </select>                                    
                                    
                                    <input id="showTeam" type="submit" style="top: ${(initialBracketYCoord + (firstRoundHeight) - (firstRoundHeight / 2)) - 16}px; left: ${currentLeft + horizontalLength + 10}px;" value="Show" />
                                </form>
                                
                            </c:if>

                            <!-- Increment the top value -->
                            <c:set var="currentTop" value="${currentTop + (power * bracketSpacing) + (power * firstRoundHeight)}"/>

                        </c:forEach>
                            
                        <!-- This determines the final y coordinate so we can use that value to set the min height on the container div in order to view the entire bracket -->
                        <form action="">
                            <input type="hidden" id="finalTop" name="finalTop" value="${currentTop}" />
                        </form>

                    </div> <!-- inner Tournament -->
                </div> <!-- Tournament -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

</body>
</html>