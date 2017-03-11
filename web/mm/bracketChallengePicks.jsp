<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Bracket Challenge Picks Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

    <style>                               
        h3 { text-align: center; }    
        .regionName { font-size: 1.7em; font-weight: bold; position: absolute; }
        label[class*="Team"] { cursor: pointer; }
        
        /* TOURNEY ROUNDS */                
        div[id^='tourneyRound'] { display: inline; float: left; min-height: 3500px; position: relative; width: 120px; }
        div[id^='tourneyRound'] div { width: 100%; }
        div[id^='tourneyRound'] div[id^='bracket'] { border: solid black 3px; border-left: none; position:absolute; }
        div[id^='tourneyRound_1'] { width: 140px; } 
        div[id^='tourneyRound_6'] { width: 100px; }        
        
        /* BRACKET HEIGHTS */        
        div[id^='tourneyRound_1'] div[id^='bracket'] { height: 50px; }        
        div[id^='tourneyRound_2'] div[id^='bracket'] { height: 100px; }        
        div[id^='tourneyRound_3'] div[id^='bracket'] { height: 200px; }        
        div[id^='tourneyRound_4'] div[id^='bracket'] { height: 400px; }        
        div[id^='tourneyRound_5'] div[id^='bracket'] { height: 800px; }        
        div[id^='tourneyRound_6'] div[id^='bracket'] { height: 1600px; }
        
        /* SEED NUMBERS */        
        .bottomSeedNumber { top: 22px;}        
        .topSeedNumber { top: -30px; }        
        label[class*="SeedNumber"] { color: darkred; font-size: 1.3em; position: absolute; }
        
        /* TEAM NAMES */        
        .topTeam { top: -27px; }        
        label[class*="Team"] { left: 10px; position: absolute; }
        div[id^='tourneyRound_1'] label[class*="Team"] { left: 30px; }        
        div[id^='tourneyRound_1'] .bottomTeam { top: 25px; }        
        div[id^='tourneyRound_2'] .bottomTeam { top: 75px; }        
        div[id^='tourneyRound_3'] .bottomTeam { top: 175px; }        
        div[id^='tourneyRound_4'] .bottomTeam { top: 375px; }        
        div[id^='tourneyRound_5'] .bottomTeam { top: 775px; }        
        div[id^='tourneyRound_6'] .bottomTeam { top: 1575px; }
        
        /* CHAMPIONSHIP WINNER */
        div[id^='tourneyRound_7'] label { position: absolute; }        
        .winner { font-size: 1.4em; left: 10px; top: 1760px; }        
        .finalLine { border-bottom: solid black 3px; top: 1790px; width: 100%; }        
        .champsLabel { font-size: 1.2em; left: 10px; top: 1800px; }
  
    </style>
  </head>

  <body>
    <div id="container">
        <div id="header"><jsp:include page="../inc_header.jsp" /></div>
        <div id="mainMenu"><jsp:include page="../menu/mainMenu.jsp" /></div>
        <div id="leftMenu"><div id="innerLeftMenu"><jsp:include page="../menu/inc_leftMenu.jsp" /></div></div>

        <div id="content">
            <div id="innerContent">
                
                <!-- CONSTANTS -->
                <c:set var="firstRoundHeight" value="25"/>
                <c:set var="initialTop" value="80"/>
                <c:set var="regionSpacing" value="50"/>
                
                <!-- INITIALIZATION -->
                <c:set var="currentRegionID" value="0"/>
                <c:set var="power" value="1"/>
        
                <c:forEach items="${picks}" var="pick">
                        
                    <!-- LOOP INITIALIZATION -->                        
                    <c:set var="roundNumber" value="${pick.game.round.roundNumber}"/>
                    <c:choose>
                        <c:when test="${roundNumber == 1}">
                            <c:set var="tp1ID" value="${pick.game.teamSeed1ID}"/>                                   
                            <c:set var="tp1Name" value="${pick.game.topTeamSeed.team.displayName}"/>
                            <c:set var="tp2ID" value="${pick.game.teamSeed2ID}"/>                                   
                            <c:set var="tp2Name" value="${pick.game.bottomTeamSeed.team.displayName}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="tp1ID" value="${pick.prevTopGamePick.teamSeedPickedID}"/>                                   
                            <c:set var="tp1Name" value="${pick.prevTopGamePick.teamSeedPicked.team.displayName}"/>
                            <c:set var="tp2ID" value="${pick.prevBottomGamePick.teamSeedPickedID}"/>                                   
                            <c:set var="tp2Name" value="${pick.prevBottomGamePick.teamSeedPicked.team.displayName}"/>
                        </c:otherwise>
                    </c:choose>

                    <!-- WHEN CHANGING ROUNDS -->
                    <c:if test="${currentRoundID != pick.game.round.roundID}">
                        <c:set var="currentRoundID" value="${pick.game.round.roundID}"/>
                        <c:set var="currentTop" value="${(initialTop + (power * firstRoundHeight) - (firstRoundHeight / 2))}"/>
                        
                        <c:if test="${roundNumber > 1}">
                            </div>
                        </c:if>

                        <div id="tourneyRound_${roundNumber}" tr="${roundNumber}" wk="${pick.game.round.seasonWeekID}" fst="${fsteam.FSTeamID}">

                        <h3>Round ${roundNumber}</h3>
                        
                        <!-- FIGURE OUT POWER OF 2 -->
                        <% pageContext.setAttribute("power", (int)Math.pow(2, (Double.parseDouble(pageContext.getAttribute("roundNumber").toString())))); %>

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
                    <div id="bracket_${pick.game.gameID}" style="top: ${currentTop}px;" title="Game Location: ${pick.game.location}" ng="${pick.game.nextGameID}" np="${pick.game.nextPosition}">

                        <!-- SEEDS -->
                        <c:if test="${roundNumber == 1}">
                            <label class="topSeedNumber">${pick.game.topTeamSeed.seedNumber}</label>
                            <label class="bottomSeedNumber">${pick.game.bottomTeamSeed.seedNumber}</label>
                        </c:if>

                        <label class="topTeam" tp="${tp1ID}">${tp1Name}</label>
                        <label class="bottomTeam" tp="${tp2ID}">${tp2Name}</label>

                    </div>

                    <c:set var="currentTop" value="${currentTop + (power * firstRoundHeight) + (power * firstRoundHeight)}"/>
                    
                    <!-- FINAL GAME -->
                    <c:if test="${pick.game.nextGameID == null}">

                        </div>

                        <div id="tourneyRound_7" tr="7">
                            <h3>Winner</h3>
                            
                            <!-- CHAMPS LINE-->
                            <label class="winner">${pick.teamSeedPicked.team.displayName}</label>
                            <label class="finalLine"></label>
                            <label class="champsLabel">#1 Champs</label>                           
                        </div>
                    </c:if>
                
                </c:forEach>
                
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->
    
    <script>

        $(document).ready(function(){
           
            $("label[class=topTeam]").draggable({
                revert: true
            });
            
            $("label[class=bottomTeam]").draggable({
                revert: true
            });
            
            $("div[id^=tourneyRound]").droppable({
                drop : handleDrop
            });
            
            function handleDrop( event, ui ) {
                var fsTeamId = ui.draggable.parent().parent().attr('fst');                
                var teamSeedPickedId = ui.draggable.attr('tp');
                var begRd = parseInt(ui.draggable.parent().parent().attr('tr'));
                var endRd = parseInt($(this).attr('tr'))
                var gameId = 0;
                var seasonWeekId = 0;
                var nextPosClass= '';
                 
                while (begRd < endRd) {
                    if (gameId == 0) {
                        gameId = ui.draggable.parent().attr('id').toString().split('_')[1];
                        seasonWeekId = ui.draggable.parent().parent().attr('wk');                        
                    }                    
                                        
                    $.ajax({
                        url:"ajaxCall.ajax",
                        dataType: "xml",
                        type:"POST",
                        data:"method=SubmitBracketChallengePick&wk="+seasonWeekId+"&fst="+fsTeamId+"&gid="+gameId+"&tp="+teamSeedPickedId,
                        error: function(){
                            alert("Unknown error, please try again.");
                        }

                    })
                    
                    // Determine which labels to refresh
                    switch (parseInt($("#bracket_"+gameId).attr('np'))) {
                        case 1: nextPosClass = 'topTeam'; break;
                        case 2: nextPosClass = 'bottomTeam'; break;
                        default: if (endRd == 7) {
                                nextPosClass = ''
                                $("label[class*='winner']").html(ui.draggable.text());
                        }
                    }
                                
                    gameId = $("#bracket_"+gameId).attr('ng');
                    seasonWeekId = $("#bracket_"+gameId).parent().attr('wk');
                    
                    $("div[id^='bracket_"+gameId+"'] label[class*='"+nextPosClass+"']").html(ui.draggable.text());
                    $("div[id^='bracket_"+gameId+"'] label[class*='"+nextPosClass+"']").attr("tp", teamSeedPickedId);

                    begRd += 1;
                }               
                return;
            }
            
        });
    </script>

  </body>
</html>