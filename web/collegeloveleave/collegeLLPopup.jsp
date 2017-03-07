<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <title>TopDawgSports - College Love Em & Leave Em Other User Picks</title>
        <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
        <style>
body {
    background-color: white;
}
        </style>
    </head>

<body>
    <%-- User Picks --%>
    <div id="loveLeavePop">

        <div id="userPicks">
            <div id="innerUserPicks">
                 <table>
                     <tds:tableRows items="${otherUserPicks}" var="pick" tableNumber="1" >
                         <jsp:attribute name="rowTitle">
                            <tr class="rowTitle">
                                <td colspan="5"><h2>${otherUserPicks[0].FSTeam.teamName}</h2></td>
                            </tr>
                        </jsp:attribute>
                         <jsp:attribute name="rowHeader">
                             <tr class="rowHeader">
                                 <td><c:out value="Week" /></td>
                                 <td><c:out value="Pick" /></td>
                                 <td><c:out value="Result" /></td>
                                 <td><c:out value="Opponent" /></td>
                                 <td><c:out value="Score" /></td>
                             </tr>
                         </jsp:attribute>

                         <jsp:attribute name="rowData">
                             <tr>
                                 <%-- Week # --%>
                                 <td>${pick.FSSeasonWeek.FSSeasonWeekNo}</td>

                                 <%-- Picked Team's Helmet --%>
                                 <td>
                                     <c:choose>
                                         <c:when test="${pick.teamPickedID > 0}">
                                             <img src="/topdawgsports/images/NCAAImages/Color/${pick.teamPickedID}.gif" alt="" />
                                         </c:when>
                                         <c:otherwise>
                                             -
                                         </c:otherwise>
                                     </c:choose>
                                 </td>

                                 <%-- Result --%>
                                 <td>
                                     <c:choose>
                                        <c:when test="${pick.isPickCorrect == true}">
                                            <img class="checkmark" src="/topdawgsports/images/greenCheckmark.jpg" alt="checkmark" />
                                        </c:when>
                                        <c:otherwise>
                                            <img class="checkmark" src="/topdawgsports/images/redX.jpg" alt="checkmark" />
                                        </c:otherwise>
                                    </c:choose>
                                 </td>

                                 <%-- Opponent & Score --%>
                                 <c:choose>
                                     <c:when test="${pick.teamPickedID > 0}">
                                         <c:choose>
                                             <c:when test="${pick.teamPickedID == pick.game.homeID}">
                                                 <td>${pick.game.visitor.fullName} ${pick.game.visitor.mascot}</td>
                                                 <td>${pick.game.homePts} - ${pick.game.visitorPts}</td>
                                             </c:when>
                                             <c:otherwise>                                                 
                                                 <td>@ ${pick.game.home.fullName} ${pick.game.home.mascot}</td>
                                                 <td>${pick.game.visitorPts} - ${pick.game.homePts}</td>
                                             </c:otherwise>
                                         </c:choose>
                                     </c:when>
                                     <c:otherwise>
                                         <td>-</td>
                                         <td>-</td>
                                     </c:otherwise>
                                 </c:choose>
                             </tr>
                         </jsp:attribute>
                     </tds:tableRows>
                </table>
                
            </div> <!-- inner user picks -->
        </div> <!-- user picks -->
    </div> <!-- loveLeavePop -->

</body>
</html>
