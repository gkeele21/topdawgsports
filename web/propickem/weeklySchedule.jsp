<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - Pro Pickem</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.min.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.9/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../js/script.js" ></script>
    <script src="../js/jquery.ui.touch-punch.min.js"></script>
    
    <style type="text/css">
 
        h2 { text-align: center; }
        select { background-color: #F2BC57; }
        table { border-collapse: separate; }
        
        #container { overflow: hidden; }
        
        #schedule { clear: both; width: 100%; }
        #innerSchedule a { color: #1C5953; text-decoration: none; }
        #innerSchedule a:hover { color: #BF8339; }
        #innerSchedule th { color: #103B40; text-align: left; text-transform:  uppercase; }
        #innerSchedule th:first-child { text-align: right; }
        
        #leftPH { float: left; }
        #leftPH h2 { color: #103B40; display:inline; }
        #leftPH label { color: #BF8339; font-size: 1.4em; margin-left: 10px; }
        #rightPH { float: right; }
        
        #weekNumLinks { clear: both; }
        #weekNumLinks { font-size: 1.1em; margin: 25px 25px; text-align: center; }
        #weekNumLinks a { color: #1C5953; font-size: 1.1em; padding-left: 20px; }
        #weekNumLinks a:hover { color: #BF8339; }
        #weekNumLinks a.currWeek { color: #BF8339; font-size: 1.6em; text-decoration: none; }
        
        #confidencePtsToAssign { clear: both; text-align: center; display: inline;}
        #confidencePtsToAssign td { border: 2px solid #103B40; margin-left: 3px; text-align: center; width: 35px; }           
        #confidencePtsToAssign label { color: #731702; font-size: 2.3em; }
   
        #byeTeams { clear: both; }
        #byeTeams img { height : 60px; width : 75px; }
        #byeTeams hr { border: medium solid black; margin: 20px 10px; }
        #byeTeams table{ width: auto; }
        #byeTeams th { font-size: 1.8em; }
        
        img[id*=helmet] { height : 60px; width : 75px; }        

        .assignedPts, .correctPick, .missedPick { border: 3px solid #103B40; font-size: 2.3em; text-align: center; width: 35px; }        
        .assignedPts { color: #731702; }
        .blankPtsDesc { font-size: .6em; }
        .checkmark { height: 30px; width: 30px; }
        .correctPick { background-color: #1C5953; color: white; }
        .gameDate { font-weight: bold; text-align: left; }
        .gameNote { color: #731702; }
        .hName { text-align: left; }
        .loserPts, .winnerPts { font-size: 1.5em; padding-left: 5px; padding-right: 5px; text-align: center; }
        .loserPts { color: darkred; }
        .missedPick { background-color: #731702; color: white; }
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
                
                <input type="hidden" name="fsTeamId" value="${fsteam.FSTeamID}"></input>
                <input type="hidden" name="displayTeamId" value="${displayTeam.FSTeamID}"></input>
                <input type="hidden" name="displayWeekId" value="${displayWeek.FSSeasonWeekID}"></input>

                <%-- Variables --%>
                <c:set var="prevGameDate" value="" />
                
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
                
                <%-- Confidence Pts to Assign --%>    
                <c:if test="${showConfidencePts}" >
                    <div id="confidencePtsToAssign">
                        <table title="Assign each matchup a specific number of confidence points. You will earn that many game points if you correctly choose the winning team.">
                            <thead>Confidence Points</thead>
                            <tr>
                                <c:forEach var="ptsNum" begin="1" end="${picks.size()}" > 
                                    <td>
                                        <label id="availPts_${ptsNum}">
                                            <c:choose>
                                                <c:when test="${empty assignedPts[ptsNum]}">
                                                    ${ptsNum}
                                                </c:when>
                                                <c:otherwise>
                                                    &nbsp;
                                                </c:otherwise>
                                            </c:choose>                       
                                        </label>
                                    </td>
                                </c:forEach>
                            </tr>
                        </table>
                    </div>
                </c:if>
                
                <%-- Weekly Schedule --%>
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
                                <th>Home Team</th>                                
                            </thead>
                            
                            <c:forEach items="${picks}" var="pick">

                                <%-- Game Date --%>
                                <c:if test="${pick.game.gameDate != prevGameDate}">
                                    <tr>
                                        <td class="gameDate" colspan="8"><fmt:formatDate value="${pick.game.gameDate.time}" pattern="EEEE, MMM. d - h:mm a"/></td>
                                        <c:set var="prevGameDate" value="${pick.game.gameDate}" />
                                    </tr>
                                </c:if>

                                <tr id="gameMatchup_${pick.game.gameID}" gid="${pick.game.gameID}" gs="${pick.game.gameHasStarted}">
                                    
                                    <%-- Visitor Name and Record --%>
                                    <td class="vName">
                                        <a href="javascript:viewTeamSchedule('/topdawgsports/proTeamSchedule.htm?tid=${pick.game.visitorID}')">
                                            ${pick.game.visitor.fullName} ${pick.game.visitor.mascot}
                                            <c:choose>
                                                <c:when test="${!empty weekStandings[pick.game.visitorID]}">
                                                    (${weekStandings[pick.game.visitorID].wins}-${weekStandings[pick.game.visitorID].losses})
                                                </c:when>
                                                <c:otherwise>
                                                    (0-0)
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                    </td>

                                    <%-- Visitor Helmet --%>
                                    <td>
                                        <img id="helmet_${pick.game.visitorID}" 
                                             <c:if test="${pick.teamPickedID == pick.game.visitorID}"> class="pickedTeam" </c:if>
                                             src="/topdawgsports/images/NFLImages/Color/${pick.game.visitorID}.gif" alt=""
                                             tp="${pick.game.visitorID}" opp="${pick.game.homeID}"                                              
                                        />
                                    </td>
                                        
                                    <%-- Visitor Score --%>
                                    <td
                                        <c:choose>
                                            <c:when test="${pick.game.visitorID == pick.game.winnerID}">
                                                class="winnerPts"
                                            </c:when>
                                            <c:when test="${pick.game.homeID == pick.game.winnerID}">
                                                class="loserPts"
                                            </c:when>
                                        </c:choose>
                                    >
                                        <c:if test="${pick.game.gameHasStarted}">
                                            ${pick.game.visitorPts}
                                        </c:if>
                                    </td>

                                    <%-- Result / Confidence Pts --%>
                                    <td id="assignedPts_${pick.game.gameID}" gid="${pick.game.gameID}" 
                                        <c:choose>
                                            <c:when test="${pick.game.gameHasStarted}">
                                                <c:choose>
                                                    <c:when test="${pick.confidencePts > 0}">
                                                        <c:choose>
                                                            <c:when test="${pick.game.winnerID > 0}">
                                                                <c:choose>
                                                                    <c:when test="${pick.teamPickedID == pick.game.winnerID}">
                                                                        class="correctPick">${pick.confidencePts}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        class="missedPick">${pick.confidencePts}
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:otherwise>
                                                                class="assignedPts">${pick.confidencePts}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${pick.game.winnerID != null}">
                                                                <c:choose>
                                                                    <c:when test="${pick.teamPickedID == pick.game.winnerID}">
                                                                        ><img class="checkmark" src="/topdawgsports/images/greenCheckmark.jpg" alt="checkmark" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ><img class="checkmark" src="/topdawgsports/images/redX.jpg" alt="checkmark" />
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:otherwise>
                                                                ><label>AT</label>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>                                                
                                                <c:choose>
                                                    <c:when test="${showConfidencePts}">
                                                        class="assignedPts
                                                        <c:choose>
                                                            <c:when test="${pick.confidencePts > 0}">
                                                                "><label>${pick.confidencePts}</label>
                                                            </c:when>
                                                            <c:otherwise>
                                                                blankPtsDesc"><label>Drag confidence pts here</label>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ><label>AT</label>
                                                    </c:otherwise>
                                                </c:choose>                                                        
                                            </c:otherwise>
                                        </c:choose>                                        
                                    </td>

                                    <%-- Home Score --%>   
                                    <td
                                        <c:choose>
                                            <c:when test="${pick.game.homeID == pick.game.winnerID}">
                                                class="winnerPts"
                                            </c:when>
                                            <c:when test="${pick.game.visitorID == pick.game.winnerID}">
                                                class="loserPts"
                                            </c:when>
                                        </c:choose>
                                    >
                                        <c:if test="${pick.game.gameHasStarted}">
                                            ${pick.game.homePts}
                                        </c:if>
                                    </td>

                                    <%-- Home Helmet --%>
                                    <td>
                                        <img id="helmet_${pick.game.homeID}" 
                                             <c:if test="${pick.teamPickedID == pick.game.homeID}"> class="pickedTeam" </c:if>
                                             src="/topdawgsports/images/NFLImages/Color/${pick.game.homeID}.gif" alt=""
                                             tp="${pick.game.homeID}" opp="${pick.game.visitorID}"
                                        />
                                    </td>
                                    <%-- Home Name and Record --%>
                                    <td class="hName">
                                        <a href="javascript:viewTeamSchedule('/topdawgsports/proTeamSchedule.htm?tid=${pick.game.homeID}')">
                                            ${pick.game.home.fullName} ${pick.game.home.mascot}
                                            <c:choose>
                                                <c:when test="${!empty weekStandings[pick.game.homeID]}">
                                                    (${weekStandings[pick.game.homeID].wins}-${weekStandings[pick.game.homeID].losses})
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
                                    <td><img src="/topdawgsports/images/NFLImages/Color/${team.homeID}.gif" alt="" /></td>
                                </c:forEach>
                            </tr>
                        </table>
                    </div>
                </c:if>

            </div> <%-- inner content --%>
        </div> <%-- content --%>
    </div> <%-- container --%>
    
    <script>
        $(document).ready(function () {
            
            var fsTeamId = $("input[name^=fsTeamId]").val();
            var displayTeamId = $("input[name^=displayTeamId]").val();
            var fsSeasonWeekId = $("input[name^=displayWeekId]").val();
            
            // Only allow the saving of points and picks for of the team that is logged in.
            if (fsTeamId != displayTeamId) {
                $('tr[id*=gameMatchup][gs=false] img').removeClass("pickedTeam");
                return;
            }            
            
            $('tr[id*=gameMatchup][gs=false] img').css("cursor","pointer");
                    
            $('tr[id*=gameMatchup][gs=false] img[id*=helmet]').click(function() {
                
                try {                
                    var me = $(this);
                    var gameId = me.parent().parent().attr('gid');                    
                    var confPts = parseInt($('#assignedPts_'+gameId).html());
                    if (isNaN(confPts)) { confPts = 0; }

                    $.ajax({
                        url:"ajaxCall.ajax",
                        dataType: "xml",
                        type:"POST",
                        data:"method=SavePickemPick&fst="+fsTeamId+"&gid="+gameId+"&tp="+me.attr('tp')+"&wk="+fsSeasonWeekId+"&fsg=4",
                        success: function(data){
                            if ($(data).text() == 'Success') {
                                me.addClass("pickedTeam");
                                $('#helmet_'+me.attr('opp')).removeClass('pickedTeam');
                            } else {
                                alert("Data Error, please try again.");
                            }
                        },
                        error: function(){
                            alert("Function error");
                        }
                    })

                }
                catch (err) {
                    alert("Sorry an unknown problem happened.  Here are the details: "+err);
                }
            })

            $("div[id^=confidencePtsToAssign] label").draggable( {
                revert: true
            });

            $("td[id^=assignedPts] label").draggable( {
                revert: true
            });

            $("td[id^=assignedPts]").droppable( {
                drop : handleDrop
            });

            function handleDrop( event, ui ) {

                try {
                    var me = $(this)
                    var ptsToAssign = parseInt(ui.draggable.text());
                    var srcGameId = parseInt(ui.draggable.parent().attr('gid'));
                    var prevAssignedPts = parseInt(me.text());

                    if (ptsToAssign == prevAssignedPts) { return; } // Won't override pts assignment with a blank
                    if (isNaN(ptsToAssign)) { return; } // Couldn't retrieve the pts to assign
                    if (isNaN(srcGameId)) { srcGameId = 0;} 
                    if (isNaN(prevAssignedPts)) { prevAssignedPts = 0;}
                    
                    $.ajax({
                        url:"ajaxCall.ajax",
                        dataType: "xml",
                        type:"POST",
                        data:"method=SavePickemConfidencePts&wk="+fsSeasonWeekId+"&fst="+fsTeamId+"&gid="+me.attr('gid')+"&pts="+ptsToAssign+"&sgid="+srcGameId+"&fsg=4",
                        success: function(data){
                            if ($(data).text() == 'Success') {
                                $("td[id^='assignedPts_"+me.attr('gid')+"'] label").html(ptsToAssign);
                                me.removeClass("blankPtsDesc");

                                /* This is the way to determine if it's coming from the available section or the already assigned section */
                                if (srcGameId > 0) {
                                    $("td[id^='assignedPts_"+srcGameId+"'] label").html('Drag confidence pts here');
                                    $("td[id^='assignedPts_"+srcGameId+"']").addClass('blankPtsDesc');
                                    if (prevAssignedPts > 0) {                                    
                                        $("#availPts_"+prevAssignedPts).html(prevAssignedPts); 
                                    }
                                }
                                else {
                                    $("#availPts_"+ptsToAssign).html('&nbsp;');
                                    if (prevAssignedPts > 0) {                                    
                                        $("#availPts_"+prevAssignedPts).html(prevAssignedPts);
                                    }
                                }
                            } else {
                                alert("Data Error, please try again.");
                            }
                        },
                        error: function(){
                            alert("Function error");
                        }
                    })
                }
                catch (err) {
                    alert("Sorry an unknown problem happened.  Here are the details: "+err);
                }
            }
        });
        
    </script>
    
</body>
</html>