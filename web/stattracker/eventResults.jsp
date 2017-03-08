<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - Event Results</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="/topdawgsports/css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="/topdawgsports/css/topDawgCommon.css" media="screen" />
    
    <style type="text/css">
     
        form {
            display: inline;
        }
        
        table {
            line-height: 30px;
        }
        
        td {
            border: 1px solid black;
        }
        
        #tournamentResults {
            margin: 10px 10px;            
        }
        
        .eventName {
            padding-left: 25px;
            font-size: 1.3em;
        }
        
        .golfer {
            text-align: right;
            width:20%;
            padding-right: 10px;
        }
        
        .grandTotal {
            font-weight: bold;
            color: #103B40;
            background-color: #F2BC57;
        }
        
        .holeNumber {
            background-color: yellow;
        }
        
        .holePar {
            background-color: pink;
        }
        
        .holeYardage {
            background-color: lightgreen;
        }
        
        .nineTotal {
            font-weight: bold;
            color: #731702;
        }
        
        .strokes{
            text-align: center;
        }

        .teamOrGroupHeading {
            font-weight: bold;
            font-size: 1.3em;
            text-align: left;
            border: none;
        }
        
        .heading {
            display: block;
            font-size: 1.5em;
            text-align: center;
            color: #731702;
        }
        
        .heading a {
            text-decoration: none;
            padding-left: 20px;
            color: #731702;
        }
        
        .currentRound {
            font-size: 1.5em;
        }

    </style>
  </head>

  <body>
    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div>

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">
                
                <div id="tournamentResults">
                    
                    <%--<a href="enterHoleScores.htm?golferRoundID=${golfEventRound.golferRoundID}">Edit Scores</a>--%>
                    <label>Event Name:</label>
                    <label class="eventName">${golfEvent.eventName}</label><br /><br />                    
                    <label>Show by:</label>
                    <form action="eventResults.htm">
                        <select name="dop">
                            <c:forEach items="${availableOptions}" var="option">
                                <option value="${option}"
                                    <c:if test="${option == selectedOption}">
                                        selected="selected"
                                    </c:if>
                                >${option}
                                </option> 
                            </c:forEach>
                        </select>                             

                        <input type="submit" value="Show" />
                    </form>
                    
                    <br /><br />
                    
                    <%-- Table Heading --%>
                    <div class="heading">
                        <c:if test="${currentRoundNumber > 1}">
                            <a href="eventResults.htm?ge=${golfEvent.golfEventID}&rn=${currentRoundNumber-1}&dop=${selectedOption}">
                                <img src="../images/leftArrow.png" height="30"/>
                            </a>
                        </c:if>
                        Round ${currentRoundNumber}
                        <c:if test="${currentRoundNumber < golfEvent.numRounds}">
                            <a href="eventResults.htm?ge=${golfEvent.golfEventID}&rn=${currentRoundNumber+1}&dop=${selectedOption}">
                                <img src="../images/rightArrow.png" height="30"/>
                            </a>
                        </c:if>
                    </div>               
                    
                    <%-- Initialize Variables --%>
                    <c:set var="nineTotal" value="0" />
                    <c:set var="grandTotal" value="0" />

                    <%-- Display the Results --%>
                    <table id="results">
                               
                        <%-- Hole # --%>
                        <tr>
                            <td>Hole</td>
                            <c:forEach items="${holeInfo}" var="hole">
                                <td class="holeNumber">${hole.hole.holeNumber}</td>
                                <c:if test="${hole.hole.holeNumber == 9}">
                                    <td>Out</td>
                                </c:if>
                                <c:if test="${hole.hole.holeNumber == 18}">
                                    <td>In</td>
                                    <td>Total</td>
                                </c:if>
                            </c:forEach>
                        </tr> 
                        
                        <%-- Par --%>
                        <tr>
                            <td>Par</td>
                            <c:forEach items="${holeInfo}" var="hole">                                   
                                <td class="holePar">${hole.par}</td>
                                <c:set var="nineTotal" value="${nineTotal + hole.par}" />
                                <c:set var="grandTotal" value="${grandTotal + hole.par}" />
                                
                                <!-- Total After 9 -->
                                <c:if test="${hole.hole.holeNumber == 9}">                                    
                                    <td class="nineTotal">${nineTotal}</td>
                                    <c:set var="nineTotal" value="0" />
                                </c:if>
                                    
                                <!-- Total After 18 -->    
                                <c:if test="${hole.hole.holeNumber == 18}">
                                    <td class="nineTotal">${nineTotal}</td>
                                    <td class="grandTotal">${grandTotal}</td>
                                    <c:set var="nineTotal" value="0" />
                                    <c:set var="grandTotal" value="0" />
                                </c:if>
                            </c:forEach>
                        </tr>
                        
                        <%-- Yardage --%>
                        <tr>
                            <td>Yards</td>
                            <c:forEach items="${holeInfo}" var="hole">                                   
                                <td class="holeYardage">${hole.yardage}</td>
                                <c:set var="nineTotal" value="${nineTotal + hole.yardage}" />
                                <c:set var="grandTotal" value="${grandTotal + hole.yardage}" />
                                
                                <%-- Total After 9 --%>
                                <c:if test="${hole.hole.holeNumber == 9}">                                    
                                    <td class="nineTotal">${nineTotal}</td>
                                    <c:set var="nineTotal" value="0" />
                                </c:if>
                                    
                                <%-- Total After 18 --%>     
                                <c:if test="${hole.hole.holeNumber == 18}">
                                    <td class="nineTotal">${nineTotal}</td>
                                    <td class="grandTotal">${grandTotal}</td>
                                    <c:set var="nineTotal" value="0" />
                                    <c:set var="grandTotal" value="0" />
                                </c:if>
                            </c:forEach>
                        </tr>
                        
                        <%-- Initialize Variables --%>
                        <c:set var="prevTeamOrGroupId" value="0" />
                        <c:set var="teamOrGroupID" value="0" />

                        <%-- Display All Golfer's Scores --%>
                        <c:forEach items="${eventRoundGolfers}" var="golfer">                           
                                                            
                            <%-- Event Field specific --%>
                            <c:if test='<%= tds.stattracker.bo.GolfEventRound.DisplayOption.valueOf(request.getAttribute("selectedOption").toString()) == tds.stattracker.bo.GolfEventRound.DisplayOption.FIELD %>'>
                                <c:set var="teamOrGroupID" value="-1" />
                                <c:set var="teamOrGroupName" value="Field" />
                            </c:if>

                            <%-- Event Group specific --%>
                            <c:if test='<%= tds.stattracker.bo.GolfEventRound.DisplayOption.valueOf(request.getAttribute("selectedOption").toString()) == tds.stattracker.bo.GolfEventRound.DisplayOption.GROUPS %>'>
                                <c:set var="teamOrGroupID" value="${golfer.golfEventGroupID}" />
                                <c:set var="teamOrGroupName" value="${golfer.golfEventGroup.groupName}" />
                            </c:if>

                            <%-- Event Team specific --%>
                            <c:if test='<%= tds.stattracker.bo.GolfEventRound.DisplayOption.valueOf(request.getAttribute("selectedOption").toString()) == tds.stattracker.bo.GolfEventRound.DisplayOption.TEAMS %>'>
                                <c:set var="teamOrGroupID" value="${golfer.golfEventTeamID}" />
                                <c:set var="teamOrGroupName" value="${golfer.golfEventTeam.teamName}" />
                            </c:if>

                            <%-- Only display the field, group, or team if we haven't already --%>
                            <c:if test="${teamOrGroupID != prevTeamOrGroupId}">
                                <tr>
                                    <td class="teamOrGroupHeading">${teamOrGroupName}</td>                                    
                                </tr>
                            </c:if>

                            <c:set var="prevTeamOrGroupId" value="${teamOrGroupID}" />                                
                            
                            <%-- Reset variables --%>
                            <c:set var="nineTotal" value="0" />
                            <c:set var="grandTotal" value="0" />
                            
                            <tr>
                                <%-- Golfer Name --%>
                                <td>${golfer.golferRound.golfer.FSUser.firstName}</td>                                
                                
                                <%-- Display each hole score --%>
                                <c:forEach items="${golferRoundScores[golfer.golferRoundID]}" var="score">
                                    
                                    <td>${score.strokes}</td>
                                    <c:set var="nineTotal" value="${nineTotal + score.strokes}" />
                                    <c:set var="grandTotal" value="${grandTotal + score.strokes}" />
                                    
                                    <%-- Total After 9 --%>
                                    <c:if test="${score.hole.holeNumber == 9}">                                    
                                        <td class="nineTotal">${nineTotal}</td>
                                        <c:set var="nineTotal" value="0" />
                                    </c:if>
                                        
                                    <%-- Total After 18 --%>   
                                    <c:if test="${score.hole.holeNumber == 18}">
                                        <td class="nineTotal">${nineTotal}</td>
                                        <td class="grandTotal">${grandTotal}</td>
                                    </c:if>
                                                                       
                                </c:forEach>                                
                            </tr>
                        </c:forEach>
              
                    </table>                        
                </div>
    
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>