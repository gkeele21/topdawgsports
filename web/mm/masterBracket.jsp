<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>Master Bracket</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    <script type="text/javascript">

        $(document).ready(function(){

            // Ensure the container is big enough to see the whole bracket
            $("#container").css("min-height",$('#finalTop').val()+"px");

        });
    </script>

    <style type="text/css" >
        #content { position: relative; }
        #innerTournament { margin: 0px 0px 0px 10px; }
        .bottomTeam, .topTeam, .ncaaTitle, .roundTitle, .regionName, .tourneyBracket, .bracketType, .winner, .seeds, .score, .finalLine, .bracketNumText, .champsLabel, .champsText { position: absolute; text-align: center; }
        .bottomTeam, .topTeam, .score { display: block; }
        .bottomTeam, .topTeam { color: #103B40; text-decoration: none; }
        .ncaaTitle, .regionName { font-size: 2.0em; text-transform: uppercase; }
        .champsLabel { font-size: 1.3em; text-transform: uppercase; }
        .champsText { color: #103B40; font-size: 1.3em; }
        .finalLine { border-bottom: solid black 3px; }
        .roundTitle { color: #731702; }
        .score { color: #BF8339; }
        .seeds { color: black; font-size: 1.2em; text-align: right; }
        .tourneyBracket { border: solid black 3px; border-left: none; }
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
                        <c:set var="initialBracketYCoord" value="50"/>
                        <c:set var="regionSpacing" value="50"/>

                        <!-- INITIALIZATION -->
                        <c:set var="currentLeft" value="-${horizontalLength * .6}"/>
                        <c:set var="currentRegion" value="0"/>
                        <c:set var="power" value="1"/>

                        <!-- LOOP THROUGH ALL GAMES -->
                        <c:forEach items="${games}" var="game">

                            <!-- WHEN CHANGING ROUNDS -->
                            <c:if test="${currentRoundID != game.round.roundID}">

                                <c:set var="currentRoundID" value="${game.round.roundID}"/>
                                <c:set var="roundNumber" value="${game.round.roundNumber}" />
                                <c:set var="currentTop" value="${(initialBracketYCoord + (power * firstRoundHeight) - (firstRoundHeight / 2))}"/>
                                <c:set var="currentLeft" value="${(currentLeft + horizontalLength)}"/>

                                <!-- FIGURE OUT POWERS OF 2 -->
                                <c:set var="begRoundNumber" value="1"/>
                                <%
                                    double roundNumber = Double.parseDouble(pageContext.getAttribute("roundNumber").toString());
                                    int cumulativeRoundsPower = (int)Math.pow(2, (roundNumber - 1));

                                    int begRoundNumber = Integer.parseInt(pageContext.getAttribute("begRoundNumber").toString());
                                    int power = (int)Math.pow(2, (roundNumber + 1 - begRoundNumber));

                                    pageContext.setAttribute("cumulativeRoundsPower", cumulativeRoundsPower);
                                    pageContext.setAttribute("power", power);
                                %>

                                <!-- Round Name-->
                                <label class="roundTitle" style="top:10px; left:${currentLeft + 20}px;">
                                    ${game.round.roundName}
                                </label>

                            </c:if>

                            <!-- WHEN CHANGING REGIONS -->
                            <c:if test="${currentRegion != game.region.regionID}">
                                <c:if test="${roundNumber == begRoundNumber}">                                    
                                    <label class="regionName" style="top:${currentTop - 25}px;">${game.region.regionName}</label>
                                </c:if>
                                <c:set var="currentTop" value="${currentTop + regionSpacing}"/>
                                <c:set var="currentRegion" value="${game.region.regionID}"/>
                            </c:if>

                            <!-- BRACKET TAG -->
                            <div id="${game.gameID}" class="tourneyBracket" title="Game Location: ${game.location}" style="top: ${currentTop}px; left: ${currentLeft}px; height: ${firstRoundHeight * power}px; width: ${horizontalLength}px;">

                                <!-- TOP TEAM -->
                                <a class="topTeam" title="${game.topTeamSeed.team.fullName} ${game.topTeamSeed.team.mascot}" style=" top: -27px; width: ${horizontalLength}px;">
                                    ${game.topTeamSeed.team.displayName}
                                </a>

                                <!-- SCORE -->
                                <a class="score" style="top: ${((firstRoundHeight * power) / 2) - 25}px; width: ${horizontalLength}px;">
                                    <c:if test="${game.status ==  'FINAL'}">
                                        <fmt:formatNumber value="${game.team1Pts}" maxFractionDigits="0" /> - <fmt:formatNumber value="${game.team2Pts}" maxFractionDigits="0" />
                                    </c:if>
                                </a>

                                <!-- BOTTOM TEAM -->
                                <a class="bottomTeam" title="${game.bottomTeamSeed.team.fullName} ${game.bottomTeamSeed.team.mascot}" style="top: ${(firstRoundHeight * power) - 25}px; width: ${horizontalLength}px;">
                                    ${game.bottomTeamSeed.team.displayName}
                                </a>

                                <!-- SEEDS -->
                                <c:if test="${roundNumber == begRoundNumber}">
                                    <label class="seeds" style="top: -27px; left: -25px; width:20px;">${game.topTeamSeed.seedNumber}</label>
                                    <label class="seeds" style="top: ${(firstRoundHeight * power) - 25}px; left: -25px; width:20px;">${game.bottomTeamSeed.seedNumber}</label>
                                </c:if>

                            </div>
                                
                            <%-- FINAL GAME --%>
                            <c:if test="${game.nextGameID == null}">
                                
                                <!-- CHAMPS LINE-->
                                <label class="finalLine" style="top: ${(initialBracketYCoord + (power * firstRoundHeight) - (firstRoundHeight / 2))}px; left: ${(currentLeft + horizontalLength)}px; width: ${horizontalLength}px;" ></label>

                                <!-- #1 CHAMPS LABEL-->
                                <label class="champsLabel" style="top: ${(initialBracketYCoord + (power * firstRoundHeight) - (firstRoundHeight / 2)) + 15}px; left: ${(currentLeft + horizontalLength) + 5}px; width: ${horizontalLength}px;">#1 Champs</label>
                                
                                <!-- WINNER-->
                                <c:if test="${game.winnerID > 0}">
                                    <a class="champsText" style="top: ${(initialBracketYCoord + (power * firstRoundHeight) - (firstRoundHeight / 2)) - 30}px; left: ${(currentLeft + horizontalLength)}px; width: ${horizontalLength}px;">
                                        ${game.winner.team.displayName}
                                    </a>
                                </c:if>                                
                                
                            </c:if>

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