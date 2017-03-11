<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>Seed Challenge</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <style type="text/css" >
                
        #availableTeams { float: left; width: 100%; }
        #horizontalLine { border-top: 3px solid black; clear: both; margin-top: 20px; width: 100%; }
        #innerAvailableTeams { margin: 10px 5px 0px 5px; }        
        #innerAvailableTeams img { height: 90px; width: 120px; }        
        #innerAvailableTeams .ncaaTeam { display: block; position: relative; text-align: center; }
        #innerSeedChallengeRoster { margin: 0px 2px 0px 2px; }
        #innerSeedChallengeRoster form { display: inline; float:right; margin-top: 5px; }
        #innerSeedChallengeRoster img { border: 4px solid black; display: block; height: 75px; width: 85px; }
        #regions { float: left; margin: 40px 0px 0px 5px; }        
        #regions label { display: block; font-size: 1.7em; line-height: 140px; text-align: center; }
        #seedChallengeRoster { background-color: #F2BC57; float: left; width: 100%; }
        #seedGroupLinks a { color: #1C5953; font-size: 1.1em; padding-left: 35px; }        
        #seedGroupLinks a.highlightedGroup { font-size: 1.6em; text-decoration: underline; }
        #teamStatus { font-size: 1.5em; margin-left: 10px; text-transform: uppercase; }         
        .groupedTeams { float: left; margin: 10px 0px 0px 45px; text-align: center; }        
        .ncaaTeam { display: block; }
        .pageHeading { font-size: 1.5em; text-transform: uppercase; }
        .rosterPicks { float: left; margin-left: 7px; text-align: center; }
        .seedColumnTitle { display: block; font-size: 1.5em; font-weight: bold; }        
        .teamIn { color: green; }
        .teamName { font-size: 1.3em; }
        .teamOut { color: red; }              
        .teamOutStrike { text-decoration: line-through; }
        .teamsLeft {font-size: 1.5em; font-weight: bold; margin-left: 20px; margin-right: 5px; }        
        .timesPicked { color: #731702; font-size: 1.7em; left: -30px; position: absolute; top: -55px; }
        .tourneyWins { display: block; }
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
                       
                <div id="seedChallengeRoster">
                    <div id="innerSeedChallengeRoster">
                        
                        <label class="pageHeading">Team :</label>
                        <label class="teamName">${displayTeam.teamName}</label> 
                        
                        <c:if test="${leagueTournament.status != 'UPCOMING'}">
                            
                            <!-- STATUS -->
                            <label id="teamStatus"
                                <c:choose>
                                    <c:when test="${displayTeam.isAlive == true}">
                                        class="teamIn">In
                                    </c:when>
                                    <c:otherwise>
                                        class="teamOut">Out
                                    </c:otherwise>
                                </c:choose>
                            </label>
                            
                            <!-- TEAMS REMAINING -->
                            <label class="teamsLeft">${teamsRemaining}</label>
                            <label>team(s) remaining</label>
                            
                            <!-- ALL TEAMS DROPDOWN -->
                            <form action="seedChallenge.htm">
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
                            </form>
                            
                        </c:if>
                        
                        <br/>
                        
                        <!-- ROSTER SLOTS -->
                        <c:forEach items="${picks}" var="pick">
                            <div id="rosterSlot_${pick.seedChallengeGroup.seedChallengeGroupID}" 
                                 class="rosterPicks" tournamentId="${leagueTournament.tournamentID}" 
                                 fsTeamId="${fsteam.FSTeamID}" 
                                 seedGroupId="${pick.seedChallengeGroup.seedChallengeGroupID}"
                                 startingSeed="${pick.seedChallengeGroup.startingSeedNumber}">
                                
                                <!-- Seed Title -->
                                <label class="seedColumnTitle">
                                    #${pick.seedChallengeGroup.startingSeedNumber} 
                                    <c:if test="${pick.seedChallengeGroup.endingSeedNumber > pick.seedChallengeGroup.startingSeedNumber}">
                                        - ${pick.seedChallengeGroup.endingSeedNumber}
                                    </c:if>
                                </label>   
                                
                                <!-- Image -->
                                <img id="seedPickImage_${pick.seedChallengeGroup.seedChallengeGroupID}" src="/topdawgsports/images/CollegeTeams/${pick.teamSeedPicked.team.teamID}.gif" alt="Drag and drop team here" />
                                
                                <!-- Team Name -->
                                <label id="ncaaTeamDisplay_${pick.seedChallengeGroup.seedChallengeGroupID}" 
                                    <c:choose>
                                        <c:when test="${pick.teamSeedPicked.status == 'IN'}">
                                            class="ncaaTeam">
                                        </c:when>
                                        <c:otherwise>
                                            class="teamOutStrike">
                                        </c:otherwise>
                                    </c:choose>
                                    ${pick.teamSeedPicked.team.displayName}
                                </label>
                                
                                <!-- Tourney Wins -->
                                <c:if test="${leagueTournament.status != 'UPCOMING'}">
                                    <label title="Tournament Wins" class="tourneyWins 
                                        <c:choose>
                                            <c:when test="${pick.teamSeedPicked.status == 'IN'}">
                                                teamIn">
                                            </c:when>
                                            <c:otherwise>
                                                teamOut">
                                            </c:otherwise>
                                        </c:choose>
                                        ${pick.teamSeedPicked.tournamentWins}
                                    </label>
                                </c:if>                                      
                                
                            </div>
                        </c:forEach> 
                    </div>
                </div>

                <div id="horizontalLine"></div>

                <%-- AVAILABLE TEAMS --%>
                <c:set var="prevSeedNum" value ="0" />
                <div id="availableTeams">
                    <div id="innerAvailableTeams">
                        
                        <%-- SEED GROUP LINKS --%>
                        <div id="seedGroupLinks">
                            <label class="pageHeading">Available Teams :</label>
                            <c:forEach items="${seedGroups}" var="group">
                                <a href="seedChallenge.htm?sg=${group.seedChallengeGroupID}"
                                    <c:if test="${group.seedChallengeGroupID == reqSeedGroupId}">
                                        class="highlightedGroup"
                                    </c:if>

                                    >${group.startingSeedNumber} 

                                    <c:if test="${group.endingSeedNumber > group.startingSeedNumber}">
                                        - ${group.endingSeedNumber}
                                    </c:if>
                                </a>
                            </c:forEach>
                        </div>

                        <!-- REGION NAME -->
                        <div id="regions">
                            <label>${availableTeams[0].region.regionName}</label>
                            <label>${availableTeams[1].region.regionName}</label>
                            <label>${availableTeams[2].region.regionName}</label>
                            <label>${availableTeams[3].region.regionName}</label>
                        </div>

                        <!-- AVAILABLE TEAM LOGOS -->
                        <c:forEach items="${availableTeams}" var="team">
                                <c:if test="${team.seedNumber != prevSeedNum}">          
                                    <c:if test="${prevSeedNum != 0}">
                                        </div>
                                    </c:if>
                                    <div class="groupedTeams">
                                    <label class="seedColumnTitle">#${team.seedNumber}</label><br />
                                </c:if>
                                <c:if test="${leagueTournament.status == 'UPCOMING'}">

                                    <div id="availableTeam_${team.teamSeedID}" teamSeedId="${team.teamSeedID}" teamId="${team.teamID}"
                                         seedNumber="${team.seedNumber}" teamName="${team.team.displayName}">
                                </c:if>
                                    <img class="availTeamImg" src="/topdawgsports/images/CollegeTeams/${team.team.teamID}.gif"/>
                                    <label class="ncaaTeam">${team.team.displayName}
                                        <c:if test="${leagueTournament.status != 'UPCOMING'}">
                                            <label class="timesPicked">(${team.timesPicked})</label>
                                        </c:if>
                                    </label> 
                                <c:if test="${leagueTournament.status == 'UPCOMING'}">
                                    </div>
                                </c:if>
                                <br />
                                <c:set var="prevSeedNum" value ="${team.seedNumber}" />                                        
                        </c:forEach>
                                </div>
                        </div>
                    </div>
                </div>
                        
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

    <script>
        $(document).ready(function() {
 
            $("div[id*=availableTeam]").draggable( {
                cursor: 'move',
                revert: true
            });

            $('div[id*=rosterSlot]').droppable( {
                drop: handleDrop
            } );

        
            function handleDrop( event, ui ) {
                var fsTeamId = $(this).attr('fsTeamId');
                var tournamentId = $(this).attr('tournamentId');
                var seedGroupId = parseInt($(this).attr('seedGroupId'));
                var startingSeed = parseInt($(this).attr('startingSeed'));
                var teamSeedId = ui.draggable.attr('teamSeedId');
                var teamId = ui.draggable.attr('teamId');
                var teamName = ui.draggable.attr('teamName');
                var seedNumber = parseInt(ui.draggable.attr('seedNumber'));

                if (seedNumber < startingSeed) {
                    alert("Error! You can't place a " + seedNumber + " seed into that group.");
                    return;
                }

                $.ajax({
                    url:"ajaxCall.ajax",
                    dataType: "xml",
                    type:"POST",
                    data:"method=SubmitSeedChallengePick&tsid="+teamSeedId+"&sgid="+seedGroupId+"&fst="+fsTeamId+"&tid="+tournamentId,
                    success: function(data){
                        $("#seedPickImage_" + seedGroupId).attr("src","/topdawgsports/images/CollegeTeams/"+teamId+".gif");
                        $("#ncaaTeamDisplay_" + seedGroupId).html(teamName);
                    },
                    error: function(){
                        alert("Unknown error, please try again.");
                    }
                })
            }
        });        
    </script>
</body>
</html>