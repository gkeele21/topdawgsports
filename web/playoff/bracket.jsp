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
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    <script type="text/javascript">

        $(document).ready(function(){

            // Ensure the container is big enough to see the whole bracket
            $("#container").css("min-height",$('#finalTop').val()+"px");

            // Jump to the game id of the fsteam
            $('html,body').animate({scrollTop: ($("#"+${currentGameID}).offset().top -400)},'fast');
        });
    </script>

    <style type="text/css" >
        #content { position: relative; }
        #innerTournament { margin: 0px 0px 0px 10px; }

        .bottomTeam, .topTeam, .tourneyBracket, .bracketType, .winner, .score, .finalLine, .bracketNumText, .champsLabel, .champsText, 
            .headerInfo, .headerTitle, .navArrows, .navText { position: absolute; text-align: center; }
        .bottomTeam, .topTeam, .score { display: block; }
        .bottomTeam, .topTeam { color: #103B40; text-decoration: none; }
        .champsLabel { font-size: 1.3em; text-transform: uppercase; }
        .champsText { font-size: 1.3em; color: #103B40; }
        .finalLine { border-bottom: solid black 3px; }
        .headerTitle, .navText { color: #731702; }        
        .highlightedTeam { background-color: #F2BC57; }
        .navArrows { height: 50px; }
        .score { color: #BF8339; text-decoration: underline; }
        .tourneyBracket { border: solid black 3px; border-left: none; }

    </style>
  </head>

<body>
    <div id="container">
        <div id="header"><jsp:include page="../inc_header.jsp" /></div>
        <div id="mainMenu"><jsp:include page="../menu/mainMenu.jsp" /></div>
        <div id="leftMenu"><div id="innerLeftMenu"><jsp:include page="../menu/inc_leftMenu.jsp" /></div></div>

        <div id="content">
            <div id="innerContent">

                <div id="tournament">
                    <div id="innerTournament">
                        
                        <c:choose>
                            <c:when test="${games==null}">
                                <p>
                                    The bracket will be created once the sign-up process deadline passes (Saturday Nov 8th at 11:59 p.m.). <br />
                                    Please fill out your roster by picking all of your players on the roster page even before the bracket is created.
                                </p>  
                                
                            </c:when>
                            <c:otherwise>

                                <!-- CONSTANTS -->
                                <c:set var="horizontalLength" value="130"/>
                                <c:set var="firstRoundHeight" value="25"/>
                                <c:set var="bracketSpacing" value="25"/>
                                <c:set var="topMargin" value="125"/>
                                <c:set var="headerTop" value="15"/>

                                <!-- INITIALIZATION -->
                                <c:set var="gameCount" value="1" />
                                <c:set var="currentLeft" value="-${horizontalLength * .6}"/>
                                <c:set var="power" value="1"/>

                                <!-- LOOP THROUGH ALL GAMES -->
                                <c:forEach items="${games}" var="game">

                                    <!-- WHEN CHANGING WEEKS -->
                                    <c:if test="${currentFSSeasonWeekID != game.round.FSSeasonWeekID}">

                                        <c:set var="currentFSSeasonWeekID" value="${game.round.FSSeasonWeekID}"/>
                                        <c:set var="roundNumber" value="${game.round.FSSeasonWeek.FSSeasonWeekNo}" />
                                        <c:set var="currentTop" value="${(topMargin + (power * firstRoundHeight) - (firstRoundHeight / 2))}"/>                                
                                        <c:set var="currentLeft" value="${(currentLeft + horizontalLength)}"/>

                                        <!-- FIGURE OUT POWERS OF 2 -->
                                        <c:set var="begRoundNumber" value="${begRoundNumber}"/>
                                        <%
                                            double roundNumber = Double.parseDouble(pageContext.getAttribute("roundNumber").toString());
                                            int cumulativeRoundsPower = (int)Math.pow(2, (roundNumber - 1));

                                            int begRoundNumber = Integer.parseInt(pageContext.getAttribute("begRoundNumber").toString());
                                            int power = (int)Math.pow(2, (roundNumber + 1 - begRoundNumber));

                                            pageContext.setAttribute("cumulativeRoundsPower", cumulativeRoundsPower);
                                            pageContext.setAttribute("power", power);
                                        %>                                

                                        <!-- HEADER INFO -->
                                        <a class="headerTitle" href="bracket.htm?rd=${game.roundID}" style="top:${headerTop}px; left:${currentLeft + 20}px;">
                                            Round #${roundNumber}
                                        </a>

                                        <label class="headerInfo" style="top:${headerTop + 25}px; left:${currentLeft + 45}px;">
                                            <fmt:formatNumber value="${game.round.playoffTournament.numTeams / cumulativeRoundsPower}" maxFractionDigits="0" />
                                        </label>

                                    </c:if>

                                    <!-- BRACKET TAG -->
                                    <div id="${game.gameID}" class="tourneyBracket" style="top: ${currentTop}px; left: ${currentLeft}px; height: ${firstRoundHeight * power}px; width: ${horizontalLength}px;">

                                        <!-- TOP TEAM -->
                                        <a title="${game.topTeam.FSUser.firstName} ${game.topTeam.FSUser.lastName}" style=" top: -27px; width: ${horizontalLength}px;" class="topTeam
                                        <c:if test="${game.FSTeam1ID == fsteam.FSTeamID}">
                                            highlightedTeam
                                        </c:if>
                                        ">
                                            ${game.topTeam.teamName}
                                        </a>

                                        <!-- SCORE -->
                                        <a class="score" href="gameMatchup.htm?gid=${game.gameID}" style="top: ${((firstRoundHeight * power) / 2) - 25}px; width: ${horizontalLength}px;">
                                            <c:choose>
                                                <c:when test="${game.status == 'FINAL'}">
                                                    ${game.team1Pts} - ${game.team2Pts}
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${game.status == 'ONGOING'}">
                                                        Game Matchup
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </a>

                                        <!-- BOTTOM TEAM -->
                                        <a title="${game.bottomTeam.FSUser.firstName} ${game.bottomTeam.FSUser.lastName}" style="top: ${(firstRoundHeight * power) - 25}px; width: ${horizontalLength}px;" class="bottomTeam
                                        <c:if test="${game.FSTeam2ID == fsteam.FSTeamID}">
                                            highlightedTeam
                                        </c:if>
                                        ">
                                            ${game.bottomTeam.teamName}
                                        </a>

                                    </div>

                                    <!-- ONE TIME ONLY --!>
                                    <c:if test="${gameCount == 1}">                                
                                        <c:if test="${begRoundNumber > 1}">
                                            <!-- LEFT ARROW -->
                                            <a href="bracket.htm?prd=${begRoundNumber - 1}">
                                                <img class="navArrows" src="/topdawgsports/images/leftArrow.png" style="top: ${headerTop}px; left:5px;" alt="" />
                                            </a>

                                            <!-- PREVIOUS ROUND TEXT -->
                                            <label class="navText" style="top: ${(headerTop + 50)}px; left:5px;">Round #${(roundNumber - 1)}</label>
                                        </c:if>

                                    </c:if>

                                    <c:set var="currentTop" value="${currentTop + (power * bracketSpacing) + (power * firstRoundHeight)}"/>
                                    <c:set var="gameCount" value="${gameCount + 1}" />

                                </c:forEach>            
                                            
                                <!-- OUTSIDE THE LOOP --!>

                                <!-- This determines the final y coordinate so we can use that value to set the min height on the container div in order to view the entire bracket -->
                                <form action="">
                                    <input type="hidden" id="finalTop" name="finalTop" value="${currentTop}" />
                                </form>

                                <c:choose>
                                    <c:when test="${roundNumber < games[0].round.playoffTournament.numRounds}">
                                        <!-- RIGHT ARROW -->
                                        <a href="bracket.htm?nrd=${roundNumber + 1}">
                                            <img class="navArrows" src="/topdawgsports/images/rightArrow.png" style="top: ${headerTop}px; left:${currentLeft + horizontalLength}px;" alt="" />
                                        </a>
                                        <!-- PREVIOUS ROUND TEXT -->
                                        <label class="navText" style="top: ${(headerTop + 50)}px; left:${currentLeft + horizontalLength}px;">Round #${(roundNumber + 1)}</label>
                                    </c:when>
                                    <c:otherwise>

                                        <c:set var="currentTop" value="${(topMargin + (power * firstRoundHeight) - (firstRoundHeight / 2))}"/>
                                        <c:set var="currentLeft" value="${(currentLeft + horizontalLength)}"/>

                                        <!-- CHAMPS LINE-->
                                        <label class="finalLine" style="top: ${currentTop}px; left: ${currentLeft}px; width: ${horizontalLength}px;" ></label>

                                        <!-- #1 CHAMPS LABEL-->
                                        <label class="champsLabel" style="top: ${currentTop + 15}px; left: ${currentLeft + 5}px; width: ${horizontalLength}px;">#1 Champs</label>

                                        <!-- WINNER-->
                                        <c:if test="${games[0].round.playoffTournament.winnerID != null}">
                                            <a style="top: ${currentTop - 30}px; left: ${currentLeft}px; width: ${horizontalLength}px;" class="champsText
                                            <c:if test="${games[0].round.playoffTournament.winnerID == fsteam.FSTeamID}">
                                                highlightedTeam
                                            </c:if>
                                            ">
                                                ${games[0].round.playoffTournament.winner.teamName}
                                            </a>
                                        </c:if>

                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </div> <!-- inner Tournament -->
                </div> <!-- tournament -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

</body>
</html>