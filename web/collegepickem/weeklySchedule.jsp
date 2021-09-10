<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - College Pickem Weekly Schedule</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
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
        #confidencePtsToAssign td { border: 2px solid #103B40; margin-left: 3px; text-align: center; width: 31px; }
        #confidencePtsToAssign label { color: #731702; cursor:pointer; font-size: 2em; }

        #byeTeams { clear: both; }
        #byeTeams img { height : 60px; width : 75px; }
        #byeTeams hr { border: medium solid black; margin: 20px 10px; }
        #byeTeams table{ width: auto; }
        #byeTeams th { font-size: 1.8em; }
        #byeTeams td { color: blue; }

        img[id*=helmet] { height : 60px; width : 75px; }

        .assignedPts, .correctPick, .missedPick { border: 2px solid #103B40; font-size: 2.3em; text-align: center; width: 31px; }
        .assignedPts label { color: #731702; cursor: pointer; }
        .blankPtsDesc { font-size: .6em; }
        .checkmark { height: 30px; width: 30px; }
        .correctPick { background-color: #1C5953; color: white; }
        .hName { text-align: left; }
        .hRank, .vRank { color: blue; font-size: 1.2em; font-weight: bold; padding: 0px 5px; }
        .gameDate { font-weight: bold; text-align: left; }
        .gameNote { color: #731702; }
        .loserPts, .winnerPts { font-size: 1.5em; padding-left: 5px; padding-right: 5px; text-align: center; }
        .loserPts { color: darkred; }
        .missedPick { background-color: #731702; color: white; }
        .pickedTeam { border: thick solid #731702; }
        .pointsAssigned { background: #F2BC57
                             ; }
        .vName { text-align: right; }
        .winnerPts { color: darkgreen; }

        #dialog label { border: 2px solid #103B40; color: #731702; display: inline-block; font-size: 2em; text-align: center; width: 44px; }
    </style>

    <script type="text/javascript">

        var fsTeamId, displayTeamId, fsSeasonWeekId, selectedGameId, prevAssignedPts;

        $(document).ready(function () {

            fsTeamId = $("#fsTeamId").val();
            displayTeamId = $("#displayTeamId").val();
            fsSeasonWeekId = $("#displayWeekId").val();

            // Only allow the saving of points and picks for of the team that is logged in.
            if (fsTeamId != displayTeamId) {
                $('tr[id*=gameMatchup][gs=false] img').removeClass("pickedTeam");
                return;
            }

            $('tr[id*=gameMatchup][gs=false] img').css("cursor","pointer");
            $('tr[id*=gameMatchup][gs=false] img[id*=helmet]').click(function() {
                handleHelmetClick($(this))
            });
            $("div[id^=confidencePtsToAssign] label").draggable( { revert: true });
            $("td[id^=assignedPts] label").draggable( { revert: true });
            $("td[id^=assignedPts]").droppable( { drop : handleDrop });

            $("tr[gs=false] td[id^=assignedPts]").click(function() {
                handleAssignPtsClick($(this));
            });

            $("#dialog").dialog( {
                autoOpen: false,
                title: "Assign Confidence Pts"
            });
            $("#dialog label").click(function() {
                handleDialogClick($(this));
            });

            function handleDrop(event, ui) {

                try {
                    var ptsToAssign = parseInt(ui.draggable.text());
                    var srcGameId = parseInt(ui.draggable.parent().attr('gid'));
                    prevAssignedPts = parseInt($(this).text());

                    if (ptsToAssign == prevAssignedPts) { return; } // Won't override pts assignment with a blank
                    if (isNaN(ptsToAssign)) { return; } // Couldn't retrieve the pts to assign
                    if (isNaN(srcGameId)) { srcGameId = 0;}
                    if (isNaN(prevAssignedPts)) { prevAssignedPts = 0;}
                    selectedGameId = $(this).attr('gid');

                    saveConfidencePoints(ptsToAssign, srcGameId);
                }
                catch (err) {
                    alert("Sorry an unknown problem happened.  Here are the details: "+err);
                }
            }
        });

        function handleAssignPtsClick(ui) {
            $("#dialog").dialog('open');
            selectedGameId = parseInt(ui.attr('gid'));
            prevAssignedPts = parseInt(ui.text());
        }

        function handleDialogClick(ui) {
            var ptsToAssign = parseInt(ui.text());
            if (isNaN(ptsToAssign)) { return; }

            var srcGameId = 0;
            var existingNumber = $("td[id^='assignedPts_'] label").filter(function() {
                return $(this).text() == ptsToAssign;
            });
            if (existingNumber.length > 0 ) {
                srcGameId = existingNumber.parent().attr('gid');
            }

            saveConfidencePoints(ptsToAssign, srcGameId);
            $("#dialog").dialog('close');
        }

        function handleHelmetClick(ui) {

            var gameId = ui.parent().parent().attr('gid');
            var teamPickedId = ui.attr('tp');
            var opponentId = ui.attr('opp');

            $.ajax({
                url:"ajaxCall.ajax",
                dataType: "xml",
                type:"POST",
                data:"method=SavePickemPick&fsg=7&fst="+fsTeamId+"&wk="+fsSeasonWeekId+"&gid="+gameId+"&tp="+teamPickedId,
                success: function(data){
                    // if ($(data).text() == 'Success') {
                        $('#helmet_'+teamPickedId).addClass("pickedTeam");
                        $('#helmet_'+opponentId).removeClass('pickedTeam');
                    // } else {
                    //     alert("Data Error, please try again.");
                    // }
                },
                error: function(){
                    alert("Function error");
                }
            });
        }

        function saveConfidencePoints(ptsToAssign, srcGameId) {

            $.ajax({
                url:"ajaxCall.ajax",
                dataType: "xml",
                type:"POST",
                data:"method=SavePickemConfidencePts&fsg=7&wk="+fsSeasonWeekId+"&fst="+fsTeamId+"&gid="+selectedGameId+"&pts="+ptsToAssign+"&sgid="+srcGameId,
                success: function(data){
                    // if ($(data).text() == 'Success') {

                        // Update the new spot
                        $("td[id^='assignedPts_"+selectedGameId+"'] label").html(ptsToAssign);
                        $("td[id^='assignedPts_"+selectedGameId+"']").removeClass("blankPtsDesc");
                        $("#dialogPts_"+ptsToAssign).addClass('pointsAssigned');

                        /* Clear out the spot we dragged from. Note: Using srcGameId to determine if it's coming from the available section or the already assigned section */
                        if (srcGameId > 0) {
                            $("td[id^='assignedPts_"+srcGameId+"'] label").html('Click or drag a number here');
                            $("td[id^='assignedPts_"+srcGameId+"']").addClass('blankPtsDesc');
                        }
                        else {
                            $("#availPts_"+ptsToAssign).html('&nbsp;');
                        }

                        // if a number was already in the new spot, need to make it visible again in the available section
                        if (prevAssignedPts > 0) {
                            $("#availPts_"+prevAssignedPts).html(prevAssignedPts);
                            $("#dialogPts_"+prevAssignedPts).removeClass('pointsAssigned');
                        }

                    // } else {
                    //     alert("Data Error, please try again.");
                    // }
                },
                error: function(){
                    alert("Function error");
                }
            });

        }
    </script>
  </head>

<body>
    <div id="container">
        <div id="header"><jsp:include page="../inc_header.jsp" /></div>
        <div id="mainMenu"><jsp:include page="../menu/mainMenu.jsp" /></div>
        <div id="leftMenu"><div id="innerLeftMenu"><jsp:include page="../menu/inc_leftMenu.jsp" /></div></div>

        <div id="content">
            <div id="innerContent">

                <input id="fsTeamId" type="hidden" value="${fsteam.FSTeamID}" />
                <input id="displayTeamId" type="hidden" value="${displayTeam.FSTeamID}" />
                <input id="displayWeekId" type="hidden" value="${displayWeek.FSSeasonWeekID}" />

                <%-- Initialize variables --%>
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
                                <th></th>
                                <th></th>
                                <th>Home Team</th>
                            </thead>

                            <c:forEach items="${picks}" var="pick">

                                <%-- Game Date --%>
                                <c:if test="${pick.game.gameDate != prevGameDate || pick.game.gameInfo != null}">
                                    <tr>
                                        <td class="gameDate" colspan="8">
                                            <fmt:parseDate  value="${pick.game.gameDate}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="gameDate" />
                                            <fmt:formatDate value="${gameDate}" pattern="EEEE, MMM. d - h:mm a" timeZone="America/Denver" />
                                            <c:if test="${pick.game.gameInfo != null}">
                                                <label class="gameNote">&nbsp;${pick.game.gameInfo}</label>
                                            </c:if>
                                        </td>
                                        <c:set var="prevGameDate" value="${pick.game.gameDate}" />
                                    </tr>
                                </c:if>

                                <tr id="gameMatchup_${pick.game.gameID}" gid="${pick.game.gameID}" gs="${pick.game.gameHasStarted}">

                                    <%-- Visitor Name and Record --%>
                                    <td class="vName">
                                        <a href="javascript:viewTeamSchedule('/topdawgsports/collegeTeamSchedule.htm?tid=${pick.game.visitorID}')">
                                            ${pick.game.visitor.fullName}
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

                                    <%-- Visitor Ranking--%>
                                    <td class="vRank">
                                        <c:if test="${!empty apRankings[pick.game.visitorID]}">
                                            #${apRankings[pick.game.visitorID].overallRanking}
                                        </c:if>
                                    </td>

                                    <%-- Visitor Helmet --%>
                                    <td>
                                        <img id="helmet_${pick.game.visitorID}"
                                             <c:if test="${pick.teamPickedID == pick.game.visitorID}"> class="pickedTeam" </c:if>
                                             src="/topdawgsports/images/Helmets/Color/${pick.game.visitorID}.gif" alt=""
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
                                                                "><label>AT</label>
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
                                                                blankPtsDesc"><label>Click or drag a number here</label>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        "><label>AT</label>
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
                                             src="/topdawgsports/images/Helmets/Color/${pick.game.homeID}.gif" alt=""
                                             tp="${pick.game.homeID}" opp="${pick.game.visitorID}"
                                        />
                                    </td>

                                    <%-- Home Ranking--%>
                                    <td class="hRank">
                                        <c:if test="${!empty apRankings[pick.game.homeID]}">
                                            #${apRankings[pick.game.homeID].overallRanking}
                                        </c:if>
                                    </td>

                                    <%-- Home Name and Record --%>
                                    <td class="hName">
                                        <a href="javascript:viewTeamSchedule('/topdawgsports/collegeTeamSchedule.htm?tid=${pick.game.homeID}')">
                                            ${pick.game.home.fullName}
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
                                    <td><img src="/topdawgsports/images/Helmets/Color/${team.homeID}.gif" alt="" /></td>
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

                <%-- Dialog Window if using confidence pts --%>
                <c:if test="${showConfidencePts}" >
                    <div id="dialog" title="Basic dialog">
                        <c:forEach var="ptsNum" begin="1" end="${picks.size()}" >
                            <label id="dialogPts_${ptsNum}"
                                <c:if test="${!empty assignedPts[ptsNum]}">
                                    class="pointsAssigned"
                                </c:if>
                            >${ptsNum}</label>
                        </c:forEach>
                    </div>
                </c:if>

            </div> <%-- inner content --%>
        </div> <%-- content --%>
    </div> <%-- container --%>

</body>
</html>
