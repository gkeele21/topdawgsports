<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html>
    <head>
        <title>March Madness Field</title>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>        
        <script>

            $(document).ready(function () {
                
                var teamSeedId = 0;                
                var teamId = 0;
                var teamName = "";                

                $("button").click(function() {
                    saveTeamSeed($(this));
                });

                $("img").click(function() {
                    teamSeedId = $(this).attr('id').toString().split('_')[1];
                    $("#allTeams").val("");
                    $("#dialogSection").dialog("open");
                });

                $("#dialogSection").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 350,
                    modal: true,
                    buttons: {
                        "Done": function() {                  
                            $(this).dialog("close");
                            $("#record_"+teamSeedId).focus();
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });

                var allTeamsParsed = $.parseJSON('${allTeamsJSON}');
                var allTeamsArray = new Array();
                
                for (var team in allTeamsParsed) {                    
                    allTeamsArray.push({"label" : allTeamsParsed[team]._FullName, "value" : allTeamsParsed[team]._TeamID});
                }
                
                $("#allTeams").autocomplete( {source: allTeamsArray,
                    select: function(event, ui) {
                        event.preventDefault();        
                        teamName = ui.item.label;
                        teamId = ui.item.value;
                        $("#allTeams").val(teamName);
                        $("#img_"+teamSeedId).attr("src","/topdawgsports/images/CollegeTeams/"+teamId+".gif");
                        $("#teamId_"+teamSeedId).text(teamId);
                        $("#teamName_"+teamSeedId).text(teamName);
                    }
                });

                function saveTeamSeed(me) {
                    teamSeedId = me.attr('id').toString().split('_')[1];
                    $.ajax({
                        url:"ajaxCall.ajax",
                        dataType: "xml",
                        type:"POST",
                        data:"method=UpdateTeamSeed&ts="+teamSeedId+"&tid="+$("#teamId_"+teamSeedId).text()+"&rec="+$("#record_"+teamSeedId).val(),
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
                }
            });

        </script>
        <style>
            img { border: 4px solid black; height: 35px; width: 40px; }
            img:hover { cursor: pointer; }
            table { width:25%; }
            .pageTitle { font-size: 1.3em; font-weight: bold; }
            .regionHeading { color: darkred; font-size: 1.3em; }
            .seedNumber { color:green; font-size: 1.4em;}
            .subTitle { font-size: 1.2em; }
        </style>
    </head>

    <body>
        <%-- MODAL DIALOG --%>
        <div id="dialogSection">        
            <label>Team: </label>
            <input id="allTeams" />
        </div>

        <div>
            <label class="pageTitle">March Madness Field Setup - </label><label class="subHeading">(${tournament.tournamentID}) ${tournament.tournamentName}</label>
            <table>
                <c:set var="lastRegionId" value="0" />
                <c:forEach items="${teamSeeds}" var="teamSeed">
                    <c:if test="${teamSeed.regionID != lastRegionId}">
                        <tr><td colspan="7">&nbsp;</td></tr>
                        <tr class="regionHeading"><td colspan="7">${teamSeed.region.regionName} Region</td></tr>
                    </c:if>
                    <tr>
                        <td>(${teamSeed.teamSeedID})</td>
                        <td class="seedNumber">#${teamSeed.seedNumber}</td>
                        <td><img id="img_${teamSeed.teamSeedID}" src="/topdawgsports/images/CollegeTeams/${teamSeed.teamID}.gif" alt="Team" /></td>
                        <td id="teamId_${teamSeed.teamSeedID}">${teamSeed.teamID}</td>
                        <td id="teamName_${teamSeed.teamSeedID}">${teamSeed.team.fullName}</td>
                        <td><input id="record_${teamSeed.teamSeedID}" type="text" size="4" value="${teamSeed.seasonRecord}" /></td>
                        <td><button id="button_${teamSeed.teamSeedID}">Save</button></td>
                    </tr>
                    <c:set var="lastRegionId" value="${teamSeed.regionID}" />
                </c:forEach>
            </table>
        </div>
    </body>
</html>