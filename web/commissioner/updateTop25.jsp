<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>

<html>
    <head>
        <title>TopDawgSports - Update Top 25</title>
        <script src="../js/jquery-3.1.1.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="http://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
        <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">	
        <style>            
            i { cursor: pointer; }

            #lastWeeksRankings .header{ background-color: navy; color: white; }
            #lastWeeksRankings h2 { display: inline; }		
            #lastWeeksRankings h3 { display: block; height: 0px; }
            #lastWeeksRankings img { height: 75px; width: 85px; }

            #rankedTeams, #rankedTeamsModal { border: thin solid black; float: left; }
            #rankedTeams div, #rankedTeamsModal div { float: left; }
            #rankedTeams h3, #rankedTeamsModal h3 { display: block; text-align: center; }
            #rankedTeams label, #rankedTeamsModal label { display: block; text-align: center; }

            #dialogSection img { height: 60px; width: 70px; }
            #dialogSection h3 { font-size: .7em; }
            #dialogSection label { font-size: .7em; }

            #newRankingsSection { clear: both; }
            #newRankingsSection div { float: left; text-align: center; }
            #newRankingsSection h1 { display: block; text-align: center; }
            #newRankingsSection img { border: 4px solid black; display: block; height: 75px; margin-left: 7px; width: 85px; }		
            #newRankingsSection label { display: inline-flex; }

            #droppedOut { color: darkred; }
            #droppedOut i { border: 4px solid darkred; display: block; font-size: 4.2em; height: 75px; margin-left: 7px; width: 85px; }
         
        </style>
        
        <script>
            
            $(document).ready(function () {
                
                
                $("#lastWeeksRankings i").click(function() {
                    $(this).toggleClass("fa-plus-circle fa-minus-circle");
                    $("#rankedTeams").toggle();
                });
			
                $("#droppedOut i").click(function() {
                    priorNewRankingDivId = -1;
                    $("#dialogSection").dialog("open");
                });
			
			
                $("#rankedTeams div").draggable( {
                    cursor: 'move',
                    revert: true
                });

                $('#newRankingsSection div').droppable( {
                    drop: handleDrop
                } );
			
                $("#newRankingsSection img").click(function() {
                    priorNewRankingDivId = $(this).parent().attr("id");
                    $("#dialogSection").dialog("open");
                    $("#findTeam").val("");
                    $("#findTeam").focus();
                });
			
                $("#dialogSection img").click(function() {
                    $("#dialogSection").dialog("close");
                    var divId = $(this).parent().attr("id");
                    var extractedTeamId = parseInt(divId.substring(divId.indexOf("_") + 1));
                    var teamName = $("#" + divId + " label").text();
                    if (priorNewRankingDivId == -1) {
                        $("#" + divId).hide();
                        $("#lw_" + extractedTeamId).hide();
                    }
                    if (extractedTeamId > 0) {
                        $("#" + priorNewRankingDivId + " img").attr("src","helmets/" + extractedTeamId + ".gif");
                        $("#" + priorNewRankingDivId + " label").text(teamName);
                        $("#" + divId).hide();
                        $("#lw_" + extractedTeamId).hide();
                    }				
                });			
			
                $("#dialogSection").dialog({
                    autoOpen: false,
                    height: 500,
                    width: 500,
                    modal: true,
                    buttons: {
                        "Done": function() {                  
                            $(this).dialog("close");
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });

                var allTeamsArray = new Array();



                allTeamsArray.push({"label" : "Alabama", "value" : 132});
                allTeamsArray.push({"label" : "LSU", "value" : 135});
                allTeamsArray.push({"label" : "Ohio State", "value" : 74});
                allTeamsArray.push({"label" : "Michigan", "value" : 71});
                allTeamsArray.push({"label" : "Louisville", "value" : 61});

                $("#findTeam").autocomplete( {source: allTeamsArray,
                    select: function(event, ui) {
                        event.preventDefault();        
                        teamName = ui.item.label;
                        teamId = ui.item.value;

                        $("#" + priorNewRankingDivId + " img").attr("src","helmets/" + teamId + ".gif")
                        $("#" + priorNewRankingDivId + " label").text(teamName);
                        $("#dialogSection").dialog("close");
                        $("#lw_" + teamId).hide();
                        $("#lwm_" + teamId).hide();
                    }
                });
               
            });
            
            function finalizeWeek(me, finishOffWeek) {
            }
            
        </script>
    </head>

    <body>       
        <div id="lastWeeksRankings">
            <div class="header">
                <i class="fa fa-minus-circle"></i>
                <h2>Last Week's Top 25</h2>
            </div>
            <div id="rankedTeams">
                <c:forEach items="${priorRankings}" var="rank">
                    <div id="lw_${rank.teamID}}"><h3>#${rank.overallRanking}</h3><img src="/topdawgsports/images/Helmets/Color/${rank.teamID}.gif" /><label>${rank.team.fullName}</label></div>
                </c:forEach>
            </div>
        </div>
        
        <div id="newRankingsSection">
            <h3>New Top 25 Ranking</h3>
            <c:forEach items="${currentRankings}" var="team">
                <div id="nr${team.overallRanking}}"><h1>#${team.overallRanking}</h1><img /><label></label></div>
            </c:forEach>
            <div id="droppedOut"><h1>Out!</h1><i class="fa fa-trash-o"></i></div>
        </div>
    </body>
</html>