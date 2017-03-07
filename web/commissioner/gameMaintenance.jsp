<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <title>TopDawgSports - Game Maintenance</title>
        <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
        <script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
        <style>
            button { background-color: #103B40; border-color: #731702; color: white; margin-left: 10px;}
            form[name*=finish] { display:inline; margin-left: 20px; }
            img { height: 40px; width: 50px; }
            input { text-align: center; }
            input[name*=Wins], input[name*=Losses] { background-color: lightgreen; width: 20px; }
            input[name*=Score] { background-color: yellow; width: 20px; }
            input[name*=Ranking] { color: red; width: 20px; }
            
            #weekNumLinks { font-size: 1.1em; }
            #weekNumLinks a { color: #1C5953; font-size: 1.1em; padding-left: 20px; }
            #weekNumLinks a:hover { color: #BF8339; }
            #weekNumLinks a.currWeek { color: #BF8339; font-size: 1.6em; text-decoration: none; }
            
            #byeTeams { clear: both; }
            #byeTeams img { height : 60px; width : 75px; }
            #byeTeams hr { border: medium solid black; margin: 20px 10px; }
            #byeTeams table{ width: auto; }
            #byeTeams th { font-size: 1.8em; }
            
            #weekHeading { font-size: 1.8em; font-weight: bold; margin-top: 20px; }
            .bottomTeam { color: #731702; font-weight: bold; }
            .final { background-color: #F2BC57; text-align: center; }
            .gameDate { font-weight: bold; text-align: left; }
            .gameNote { color: #731702; }
            .topTeam { color: #1C5953; font-weight: bold; }
         
        </style>
        
        <script>
            
            $(document).ready(function () {

                /* PRO AND COLLEGE UPDATE SCORE */
                $("input[type=button]").click(function() {
                    saveGameMatchup($(this));                    
                })
                                
                /* MARCH MADNESS UPDATE SCORE */
                $("button[id*=updateScore]").click(function() {
                    updateMarchMadnessScore($(this));
                });
                
                /* FINISH OFF THE WEEK */
                $("button[id=finishWeek]").click(function() {
                    finalizeWeek($(this), 1);
                });
                
                /* RECALCULATE STANDINGS */
                $("button[id=recalculateStandings]").click(function() {
                    finalizeWeek($(this), 0);
                });
            });
            
            function finalizeWeek(me, finishOffWeek) {
                me.hide();

                $.ajax({
                    url:"ajaxCall.ajax",
                    dataType: "xml",
                    type:"POST",
                    data:"method=FinalizeWeek&sw="+me.attr('sw')+"&fw="+finishOffWeek+"&spid="+me.attr('spid'),
                    async:false,
                    success: function(data){
                        if ($(data).text() == 'Success') {
                        } else {
                            alert("Data Error, please try again. - "+$(data).text());
                        }
                    },
                    error: function(){
                        alert("Function error, please try again.");
                    }
                })

                me.show();
                
                location.reload(true);
            }
            
            function saveGameMatchup(me) {
                me.hide();

                var gid = me.attr('gid');
                var vid = me.attr('vid');
                var hid = me.attr('hid');
                var wk = me.attr('wk');

                var gd = $("[name=gameDate_"+gid+"]").val();
                var vr = $("[name=visitorRanking_"+gid+"]").val();
                var vs = $("[name=visitorScore_"+gid+"]").val();
                var vw = $("[name=visitorWins_"+gid+"]").val();
                var vl = $("[name=visitorLosses_"+gid+"]").val();
                var hr = $("[name=homeRanking_"+gid+"]").val();
                var hs = $("[name=homeScore_"+gid+"]").val();
                var hw = $("[name=homeWins_"+gid+"]").val();
                var hl = $("[name=homeLosses_"+gid+"]").val();

                // Save Game Score / Matchup
                $.ajax({
                    url:"ajaxCall.ajax",
                    dataType: "xml",
                    type:"POST",
                    data:"method=SaveGameMatchup&gid="+gid+"&gd="+gd+"&wk="+wk+"&vid="+vid+"&vr="+vr+"&vs="+vs+"&vw="+vw+"&vl="+vl+"&hid="+hid+"&hs="+hs+"&hw="+hw+"&hl="+hl+"&hr="+hr,
                    success: function(data){
                        if ($(data).text() == 'Success') {
                        } else {
                            alert("Data Error, please try again.");
                        }
                    },
                    error: function(){
                        alert("Function error, please try again.");
                    }
                })

                me.show();
            }
            
            function updateMarchMadnessScore(me) {
                me.hide();

                var t1pts = $("#txtTeam1Pts_"+me.attr('gid')).val();
                var t2pts = $("#txtTeam2Pts_"+me.attr('gid')).val();
               
                $.ajax({
                    url:"ajaxCall.ajax",
                    dataType: "xml",
                    type:"POST",
                    data:"method=UpdateMarchMadnessGameResult&tid="+me.attr('tid')+"&gid="+me.attr('gid')+"&sw="+me.attr('sw')+"&t1pts="+t1pts+"&t2pts="+t2pts,
                    async:false,
                    success: function(data){
                        if ($(data).text() == 'Success') {
                        } else {
                            alert("Data Error, please try again. - "+$(data).text());
                        }
                    },
                    error: function(){
                        alert("Function error, please try again.");
                    }
                })

                $("#txtTeam1Pts_"+me.attr('gid')).css("background-color","#F2BC57");
                $("#txtTeam2Pts_"+me.attr('gid')).css("background-color","#F2BC57");

                me.show();
            }
            
        </script>
    </head>

    <body>
        <div id="sports">
            <form action="gameMaintenance.htm">
                <input type="radio" name="spid" value="1" <c:if test="${sportId == 1}"> checked</c:if>>Pro Football</input>
                <input type="radio" name="spid" value="2" <c:if test="${sportId == 2}"> checked</c:if>>College Football</input>
                <input type="radio" name="spid" value="3" <c:if test="${sportId == 3}"> checked</c:if>>College Basketball</input>
                <input type="submit" value="GO" />
            </form>
        </div>

        <div id="weekNumLinks">
            <c:forEach items="${allWeeks}" var="week">
                <a <c:if test="${week.seasonWeekID == displayWeek.seasonWeekID}"> class="currWeek"</c:if>
                    href="gameMaintenance.htm?swid=${week.seasonWeekID}&spid=${sportId}&yr=${yr}">${week.weekNo}
                </a>
            </c:forEach>
  
        </div>

        <br />
        <label id="weekHeading">Week #${displayWeek.weekNo} - ${displayWeek.status}</label>
        
        <button id="recalculateStandings" sw="${displayWeek.seasonWeekID}" spid="${sportId}">Recalculate Standings</button>
        <c:if test="${displayWeek.status == 'CURRENT'}">
            <button id="finishWeek" sw="${displayWeek.seasonWeekID}" spid="${sportId}">Finish off the week</button>
        </c:if>
        <br />
        
        <%-- PRO/COLLEGE FOOTBALL --%>
        <c:if test="${sportId == 1 || sportId == 2}">
            <%-- Initialize Varibales --%>
            <c:choose>
                <c:when test="${sportId == 1}">
                    <c:set var="path" value="/topdawgsports/images/NFLImages/Color/" />
                </c:when>
                <c:otherwise>
                    <c:set var="path" value="/topdawgsports/images/NCAAImages/Color/" />
                </c:otherwise>
            </c:choose>

            <div id="gameMatchup">
                <p class="gameNote">* All these game matchups are displayed in US/Eastern timezone because that's how it needs to be entered into the database.</p>
                <table>
                    <c:forEach items="${footballGames}" var="game">

                        <tr>
                            <%-- Game Date --%>
                            <td><input type="text" name="gameDate_${game.gameID}" value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" timeZone="US/Eastern" value="${game.gameDate.time}"/>" /></td>

                            <%-- Visitor Name --%>
                            <td class="visitorTeamName" title="${game.visitorID}">${game.visitor.fullName} ${game.visitor.mascot}</td>

                            <%-- Visitor Record --%>
                            <td>
                                <c:choose>
                                    <c:when test="${displayWeek.weekType == 'INITIAL'}">
                                        <c:set var="wins" value="0" />
                                        <c:set var="losses" value="0" />
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="wins" value="${footballStandings[game.visitorID].wins}" />
                                        <c:set var="losses" value="${footballStandings[game.visitorID].losses}" />
                                    </c:otherwise>
                                </c:choose>

                                <input type="text" name="visitorWins_${game.gameID}" value="${wins}" />
                                -
                                <input type="text" name="visitorLosses_${game.gameID}" value="${losses}" />                            
                            </td>

                            <%-- Visitor Ranking--%>
                            <c:if test="${sportId == 2}"><td><input type="text" name="visitorRanking_${game.gameID}" value="<c:if test="${!empty apRankings[game.visitorID]}">${apRankings[game.visitorID].overallRanking}</c:if>" /></td></c:if>

                            <%-- Visitor Helmet --%>
                            <td><img src="${path}${game.visitorID}.gif" alt="" title="${game.visitorID}" /></td>

                            <%-- Visitor Pts--%>
                            <td><input type="text" name="visitorScore_${game.gameID}" value="${game.visitorPts}" /></td>

                            <td>AT</td>

                            <%-- Home Pts--%>
                            <td><input type="text" name="homeScore_${game.gameID}" value="${game.homePts}" /></td>

                            <%-- Home Helmet --%>
                            <td><img src="${path}${game.homeID}.gif" alt="" title="${game.homeID}" /></td>

                            <%-- Home Ranking--%>
                            <c:if test="${sportId == 2}"><td><input type="text" name="homeRanking_${game.gameID}" value="<c:if test="${!empty apRankings[game.homeID]}">${apRankings[game.homeID].overallRanking}</c:if>" /></td></c:if>

                            <%-- Home Record --%>
                            <td>
                                <c:choose>
                                    <c:when test="${displayWeek.weekType == 'INITIAL'}">
                                        <c:set var="wins" value="0" />
                                        <c:set var="losses" value="0" />
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="wins" value="${footballStandings[game.homeID].wins}" />
                                        <c:set var="losses" value="${footballStandings[game.homeID].losses}" />
                                    </c:otherwise>
                                </c:choose>

                                <input type="text" name="homeWins_${game.gameID}" value="${wins}" />
                                -
                                <input type="text" name="homeLosses_${game.gameID}" value="${losses}" />                            
                            </td>

                            <%-- Home Name --%>
                            <td class="homeTeamName" title="${game.homeID}">${game.home.fullName} ${game.home.mascot}</td>

                            <td><input type="button" value="SAVE" gid="${game.gameID}" vid="${game.visitorID}" hid="${game.homeID}" wk="${displayWeek.seasonWeekID}" /></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <%-- BYE TEAMS --%>
            <c:if test="${!empty byeTeams}">
                <div id="byeTeams">
                    <hr />
                    <h3>Bye Teams:</h3>
                    <table>                           
                        <tr>
                            <c:forEach items="${byeTeams}" var="team">
                                <td><img src="${path}${team.homeID}.gif" alt="" /></td>
                            </c:forEach>
                        </tr>
                        <c:if test="${sportId == 2}">
                            <tr>
                                <c:forEach items="${byeTeams}" var="team">
                                    <c:if test="${!empty apRankings[team.homeID]}">
                                        #${apRankings[team.homeID].overallRanking}
                                    </c:if>                            
                                </c:forEach>
                            </tr>
                        </c:if>
                    </table>
                </div>
            </c:if>
        </c:if>
        
        <%-- MARCH MADNESS --%>
        <c:if test="${sportId == 3}">
            <table>
                <c:forEach items="${marchMadnessGames}" var="game">
                    <tr>
                        <td>(${game.gameID})</td>
                        <td class="topTeam">(${game.topTeamSeed.seedNumber}) ${game.topTeamSeed.team.displayName}</td>
                        <td>vs.</td>
                        <td class="bottomTeam">(${game.bottomTeamSeed.seedNumber}) ${game.bottomTeamSeed.team.displayName}</td>
                        <td>
                            <input id="txtTeam1Pts_${game.gameID}" type="text" size="2"
                                <c:if test="${game.team1Pts > 0}">
                                    value="<fmt:formatNumber value="${game.team1Pts}" maxFractionDigits="0" />" class="final" 
                                </c:if>
                            />
                        </td>
                        <td>
                            <input id="txtTeam2Pts_${game.gameID}" type="text" size="2"
                                <c:if test="${game.team2Pts > 0}">
                                    value="<fmt:formatNumber value="${game.team2Pts}" maxFractionDigits="0" />" class="final" 
                                </c:if>
                            />
                        </td>
                        <td><button id="updateScore_${game.gameID}" tid="${game.round.tournamentID}" gid="${game.gameID}" sw="${displayWeek.seasonWeekID}">Update Score</button></td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

    </body>
</html>