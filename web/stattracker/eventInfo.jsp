<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - Event Info</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="/topdawgsports/css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="/topdawgsports/css/topDawgCommon.css" media="screen" />
    
    <style type="text/css">
        
        table {
            text-align: left;
            border: 3px solid black;
        }
        
        td {
            padding-left: 15px;
            padding-top: 15px;
        }
        
        #innerContent {
            margin: 15px 15px;
        }
        
        #eventInfo {
            width: 100%;
        }
        
        #innerEventInfo, .innerTournamentRounds {
            margin: 10px;
        }
                  
        .heading {
            font-weight: bold;
            width: 20%;
            text-align: right;
        }
        
        .resultsLinks {
            padding-left: 30px;
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
                
                <div id="eventInfo">
                    <div id="innerEventInfo">
                        <label class="rowTitle">Event Info</label>
                        <table>
                            <tr>
                                <td class="heading">Name:</td>
                                <td>${golfEvent.eventName}</td>
                            </tr>
                            <tr>
                                <td class="heading">Date(s):</td>
                                <td>${eventDates}</td>
                            </tr>
                            <tr>
                                <td class="heading">GolfCourse(s):</td>
                                <td>${eventGolfCourses}</td>
                            </tr>
                             <tr>
                                <td class="heading">Holes:</td>
                                <td>${golfEvent.numHoles}</td>
                            </tr>
                            <tr>
                                <td class="heading">Notes:</td>
                                <td>${golfEvent.notes}</td>
                            </tr>
                            <tr>
                                <td class="heading">Winner:</td>
                                <td>${eventRounds[0].golfEvent.winner.FSUser.firstName} ${eventRounds[0].golfEvent.winner.FSUser.lastName}</td>
                            </tr>
                        </table>
                    </div>
                </div>
                            
                <div id="tournamentRounds">
                    <c:forEach items="${eventRounds}" var="round" >
                        <div class="innerTournamentRounds">
                            <label class="rowTitle">${round.roundName}</label>
                            <table>
                                <tr>
                                    <td class="heading">Date:</td>
                                    <td>${round.roundDate}</td>
                                </tr>
                                <tr>
                                    <td class="heading">GolfCourse:</td>
                                    <td>
                                        ${round.course.golfCourse.golfCourseName}
                                        <c:if test="${fn:length(round.course.courseName) > 0}">
                                            (${round.course.courseName})
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="heading">Holes:</td>
                                    <td>${round.numHoles}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Tee:</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:length(round.tee.teeName) == 0}">
                                                ${round.tee.color}
                                            </c:when>
                                            <c:otherwise>
                                                ${round.tee.teeName} (${round.tee.color})
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="heading">Groups:</td>
                                    <c:if test="${!empty golfGroups[round.golfEventRoundID]}">
                                        <td>                                        
                                            <c:forEach items="${golfGroups[round.golfEventRoundID]}" var="group">
                                                ${group}
                                                <br />
                                            </c:forEach>
                                        </td>                                        
                                    </c:if>                                 
                                </tr>                                
                                <tr>
                                    <td class="heading">Teams:</td>
                                    <c:if test="${!empty golfTeams[round.golfEventRoundID]}">
                                        <td>                                        
                                            <c:forEach items="${golfTeams[round.golfEventRoundID]}" var="team">
                                                ${team}
                                                <br />
                                            </c:forEach>                                        
                                        </td>                                        
                                    </c:if>
                                </tr>
                                <tr> 
                                    <td class="heading">Show Scores By:</td>
                                    <td>
                                        <a href="eventResults.htm?gerid=${round.golfEventRoundID}">Field</a>
                                        <c:if test="${!empty golfGroups[round.golfEventRoundID]}">
                                            <a class="resultsLinks" href="eventResults.htm?gerid=${round.golfEventRoundID}&dop=<%=tds.stattracker.bo.GolfEventRound.DisplayOption.GROUPS %>">Groups</a>
                                        </c:if>
                                        <c:if test="${!empty golfTeams[round.golfEventRoundID]}">
                                            <a class="resultsLinks"href="eventResults.htm?gerid=${round.golfEventRoundID}&dop=<%=tds.stattracker.bo.GolfEventRound.DisplayOption.TEAMS %>">Teams</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                        </div>

                    </c:forEach>
                </div>           
    
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>