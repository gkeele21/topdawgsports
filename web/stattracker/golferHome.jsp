<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - Golfer Home</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="/topdawgsports/css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="/topdawgsports/css/topDawgCommon.css" media="screen" />
    
    <style type="text/css">
        #profilePicture {
            float:left;
            width: 20%;
        }
        
        #profilePicture img {
            width: 150px;
            margin: 15px 5px;
        }
        
        #profileInfo {
            float:left;
            width:80%;
        }
        
        #innerProfileInfo {
            margin: 5px 5px;
        }
        
        #profileInfo table {
            line-height: 50px; 
            text-align: left;
        }

        #innerContent {
            margin: 5px 5px;
        }
        
        .eventName, .winner {
            text-align: left;
        }
        
        .profileHeading {
            font-weight: bold;
            color: #731702;
        }
        
        .relativeToPar {
            color: white;
        }
        
        #events {
            clear: both;
        }
        
        #events table, #golferRounds table {
            text-align: left;
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
                
                <div id="golferProfile">
                    
                    <div id="profilePicture">
                        <img src="/topdawgsports/images/Golfers/${Golfer.FSUserID}.png" />
                    </div>
                    
                    <div id="profileInfo">
                        <table>
                            <tr>
                                <td class="profileHeading">Name:</td>
                                <td>${Golfer.FSUser.firstName} ${Golfer.FSUser.lastName}</td>
                                <td class="profileHeading">Hometown:</td>
                                <td>${Golfer.FSUser.city}, ${Golfer.FSUser.state}</td>
                            </tr>
                            <tr>
                                <td class="profileHeading">Rank:</td>
                                <td>${Golfer.currentRank}</td>
                                <td class="profileHeading">Turned Pro:</td>
                                <td>${Golfer.dateJoined}</td>
                            </tr>
                            <tr>
                                <td class="profileHeading">Handicap:</td>
                                <td>${Golfer.currentHandicap}</td>
                                <td class="profileHeading">Career Highlight:</td>
                                <td>${Golfer.notes}</td>
                            </tr>
                        </table>
                    </div>
                    
                </div>
                
                <div id="events">
                    <table>
                        <tds:tableRows items="${Events}" var="event" tableNumber="1" >
                            
                            <jsp:attribute name="rowTitle">
                                <tr class="rowTitle">
                                    <td colspan="5">Tournament Events</td>
                                </tr>
                            </jsp:attribute>
                            
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td>Date</td>
                                    <td>Name</td>
                                    <td>Holes</td>
                                    <td>Winner</td>
                                </tr>
                            </jsp:attribute>

                            <jsp:attribute name="rowData">
                                <tr class="rowData">
                                    <td>${event.startDate}</td>
                                    <td class="eventName"><a href="eventInfo.htm?geid=${event.golfEventID}">${event.eventName}</a></td>
                                    <td>${event.numHoles}</td>
                                    <td class="winner">${event.winner.FSUser.firstName} ${event.winner.FSUser.lastName}</td>      
                                </tr>
                            </jsp:attribute>
                        </tds:tableRows>
                    </table>
                </div>
      
                <div id="golferRounds">
                    <table>
                        <tds:tableRows items="${Rounds}" var="round" tableNumber="2" >
                            
                            <jsp:attribute name="rowTitle">
                                <tr class="rowTitle">
                                    <td colspan="5">Latest Rounds</td>
                                </tr>
                            </jsp:attribute>
                            
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td>Date</td>
                                    <td>Course</td>
                                    <td>Tee</td>                        
                                    <td>Holes</td>
                                    <td>Score</td>
                                </tr>
                            </jsp:attribute>

                            <jsp:attribute name="rowData">
                                <tr class="rowData2">
                                    <td>${round.roundDate}</td>
                                    <td>${round.course.golfCourse.golfCourseName}</td>
                                    <td>${round.tee.color}</td>
                                    <td>${round.numHoles}</td>
                                    <td>
                                        <c:choose>                                          
                                            <c:when test="${round.score > 0}">
                                                <%--<a href="enterHoleScores.htm?golferRoundID=${round.golferRoundID}">${round.score}</a>--%>
                                                ${round.score}
                                                <c:if test="${round.relativeToPar > 0}">
                                                    <label class="relativeToPar">(+${round.relativeToPar})</label>
                                                </c:if>
                                                <c:if test="${round.relativeToPar < 0}">
                                                    <label class="relativeToPar">(-${round.relativeToPar})</label>
                                                </c:if>

                                            </c:when>
                                            <c:otherwise>
                                                <%--<a href="enterHoleScores.htm?golferRoundID=${round.golferRoundID}">Incomplete</a>--%>
                                            </c:otherwise>
                                        </c:choose>                                                                                
                                    </td>   
                                </tr>
                            </jsp:attribute>
                        </tds:tableRows>
                    </table>
                </div>

            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>