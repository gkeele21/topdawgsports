<%@ page errorPage="error.jsp" %>
<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <title>TopDawgSports - College Team Schedule</title>
        <link rel="stylesheet" type="text/css" href="css/topDawgMain.css" media="screen" />       
        <script type="text/javascript" src="js/script.js" ></script>
        <style type="text/css">
            body { background-color: white; margin: 10px; }
            img { height: 30px; }
            table { border: solid black thick; }
            td { border: solid black thin; }
            .opponent { text-align: left; }
            #innerTeamSchedule a { color: #1C5953; text-decoration: none; }
            #innerTeamSchedule a:hover { color: #BF8339; }
            #innerTeamSchedule .ranking { color: blue; font-weight: bold; }
        </style>
    </head>
    
<body>

        <div id="teamSchedule">
            <div id="innerTeamSchedule">

                <h2>${team.fullName} ${team.mascot}</h2>

                <form action="collegeTeamSchedule.htm" method="post">

                    Year:
                    <select name="year" >

                        <c:forEach items="${allYears}" var="year">
                            <option value="${year}"
                                <c:if test="${year == schedYear}">
                                    selected="selected"
                                </c:if>
                            >${year}
                            </option>
                        </c:forEach>

                    </select>

                    <input type="hidden" name="tid" value="${team.teamID}" />
                    <input type="submit" value="Show" />

                </form>

                <table>

                    <!-- Table Headers -->
                    <tr>
                        <td>Week</td>
                        <td>Date</td>
                        <td colspan="3">Opponent</td>
                        <td>Result</td>
                        <td>Score</td>
                    </tr>

                    <c:forEach items="${games}" var="game">

                        <!-- Initialize Variables -->
                        <c:set var="oppId" value="${game.visitorID}" />
                        <c:set var="oppName" value="${game.visitor.fullName} ${game.visitor.mascot}" />
                        <c:set var="oppPts" value="${game.visitorPts}" />
                        <c:set var="teamPts" value="${game.homePts}" />
                        <c:set var="result" value="L" />
                        <c:set var="versus" value="" />

                        <!-- Override defaults (doing it this way so there aren't so many choose when otherwise statements -->
                        <c:if test="${team.teamID == game.visitorID}">
                            <c:set var="oppId" value="${game.homeID}" />
                            <c:set var="oppName" value="@ ${game.home.fullName} ${game.home.mascot}" />
                            <c:set var="oppPts" value="${game.homePts}" />
                            <c:set var="teamPts" value="${game.visitorPts}" />
                            <c:set var="versus" value="@" />
                        </c:if>

                        <c:if test="${team.teamID == game.winnerID}">
                            <c:set var="result" value="W" />
                        </c:if>

                        <tr>
                            <td>${game.seasonWeek.weekNo}</td>
                            <td><fmt:formatDate value="${game.gameDate.time}" pattern="MMM. d"/></td>

                            <c:choose>
                                <c:when test="${game.visitorID == 0}">
                                    <td colspan="4">BYE</td>
                                </c:when>
                                <c:otherwise>
                                    <td class="ranking"></td>
                                    <td><img src="/topdawgsports/images/NCAAImages/Color/${oppId}.gif" alt="" /></td>
                                    <td class="opponent">
                                        <a href="javascript:viewTeamSchedule('/topdawgsports/collegeTeamSchedule.htm?tid=${oppId}&year=${schedYear}')">${oppName}</a>
                                    </td>
                                    <c:choose>
                                        <c:when test="${game.winnerID > 0}">
                                            <td>${result}</td>
                                            <td>${teamPts} - ${oppPts}</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>

                        </tr>

                    </c:forEach>

                </table>

            </div> <!-- innerTeamSchedule -->
        </div> <!-- teamSchedule -->
    
</body>
</html>