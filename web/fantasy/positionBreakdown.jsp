<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>Position Breakdown</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <style type="text/css">
        td { border: 1px solid black; }
        #container { margin-left: auto; margin-right: auto; min-height: 1100px; }
        #innerContent { margin: 15px; }
        #leftPH { float: left; }
        #leftPH h2 { color: #103B40; display:inline; }
        #leftPH label { color: #BF8339; font-size: 1.4em; margin-left: 10px; }
        #rightPH { float: right; }
        .rowHeader:first-child { background-color: #103B40; color: white; font-size: 1em; font-weight: bold; }
        .rowHeader:last-child { font-size: 1em; font-weight: bold; }
        .rowTitle { font-size: 1.5em; }
        .green { color: green; }
        .red { color: red; }
        .total { font-weight: bold; }
        
    </style>
  </head>
  <body>

    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div> <!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div> <!-- left Menu -->

        <div id="content">
            <div id="innerContent">
                
                <div id="positionBreakdown">
                    <div id="innerPositionBreakdown">
                        
                        <%-- Page Header --%>
                        <div id="pageHeader">

                            <div id="leftPH">
                                <h2>${displayTeam.teamName}</h2>
                            </div>

                            <div id="rightPH">
                                <form action="positionBreakdown.htm">
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
                                </form>
                            </div>

                        </div>
                        <table>
                            <tds:tableRows items="${posStats}" var="stat" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <center class="rowTitle">
                                        Position Breakdown
                                    </center>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>Week</td>
                                        <td>Opponent</td>
                                        <td>QB</td>
                                        <td>RB's</td>
                                        <c:if test="${isWRTECombo == false}"><td>WR's</td></c:if>
                                        <c:if test="${isWRTECombo == false}"><td>TE</td></c:if>
                                        <c:if test="${isWRTECombo == true}"><td>WR's</td></c:if>
                                        <td>K</td>
                                        <c:if test="${isDynasty == true}">
                                            <td>DL</td>
                                            <td>LB</td>
                                            <td>DB</td>
                                        </c:if>
                                        <td>Total Diff</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">                                    
                                    <tr ${highlightRow1} class="rowData">
                                        <td>${stat.weekNo}</td>
                                        <td>${stat.opponent}</td>
                                        <td <c:if test="${stat.QBPts > 0}"> class='green'</c:if><c:if test="${stat.QBPts < 0}"> class='red'</c:if>>
                                            ${stat.QBPts}<tds:addRowTotal name="qbtot" value="${stat.QBPts}" />
                                        </td>
                                        <td <c:if test="${stat.RBPts > 0}"> class='green'</c:if><c:if test="${stat.RBPts < 0}"> class='red'</c:if>>
                                            ${stat.RBPts}<tds:addRowTotal name="rbtot" value="${stat.RBPts}" />
                                        </td>
                                        <c:if test="${isWRTECombo == false}">
                                            <td <c:if test="${stat.WRPts > 0}"> class='green'</c:if><c:if test="${stat.WRPts < 0}"> class='red'</c:if>>
                                                ${stat.WRPts}<tds:addRowTotal name="wrtot" value="${stat.WRPts}" />
                                            </td>
                                        </c:if>
                                        <c:if test="${isWRTECombo == false}">
                                            <td <c:if test="${stat.TEPts > 0}"> class='green'</c:if><c:if test="${stat.TEPts < 0}"> class='red'</c:if>>
                                                ${stat.TEPts}<tds:addRowTotal name="tetot" value="${stat.TEPts}" />
                                            </td>
                                        </c:if>
                                        <c:if test="${isWRTECombo == true}">
                                            <td <c:if test="${stat.WRTEPts > 0}"> class='green'</c:if><c:if test="${stat.WRTEPts < 0}"> class='red'</c:if>>
                                                ${stat.WRTEPts}<tds:addRowTotal name="wrtetot" value="${stat.WRTEPts}" />
                                            </td>
                                        </c:if>
                                        <td <c:if test="${stat.PKPts > 0}"> class='green'</c:if><c:if test="${stat.PKPts < 0}"> class='red'</c:if>>
                                            ${stat.PKPts}<tds:addRowTotal name="pktot" value="${stat.PKPts}" />
                                        </td>
                                        <c:if test="${isDynasty == true}">
                                            <td <c:if test="${stat.DLPts > 0}"> class='green'</c:if><c:if test="${stat.DLPts < 0}"> class='red'</c:if>>
                                                ${stat.DLPts}<tds:addRowTotal name="dltot" value="${stat.DLPts}" />
                                            </td>
                                            <td <c:if test="${stat.LBPts > 0}"> class='green'</c:if><c:if test="${stat.LBPts < 0}"> class='red'</c:if>>
                                                ${stat.LBPts}<tds:addRowTotal name="lbtot" value="${stat.LBPts}" />
                                            </td>
                                            <td <c:if test="${stat.DBPts > 0}"> class='green'</c:if><c:if test="${stat.DBPts < 0}"> class='red'</c:if>>
                                                ${stat.DBPts}<tds:addRowTotal name="dbtot" value="${stat.DBPts}" />
                                            </td>
                                        </c:if>
                                        <td title='${stat.myPts} - ${stat.oppPts}' class='total
                                            <c:if test="${stat.totalDiff > 0}"> green</c:if><c:if test="${stat.totalDiff < 0}"> red</c:if>
                                        '>
                                            ${stat.totalDiff}
                                            <tds:addRowTotal name="myptstot" value="${stat.myPts}" />
                                            <tds:addRowTotal name="oppptstot" value="${stat.oppPts}" />
                                            <tds:addRowTotal name="difftot" value="${stat.totalDiff}" />
                                        </td>
                                    </tr>
                                </jsp:attribute>
                                
                                <jsp:attribute name="rowTotal">                        
                                    <tr class="rowHeader">
                                        <td colspan="2">Total</td>
                                        <td <c:if test="${qbtot.total > 0}"> class='green'</c:if><c:if test="${qbtot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${qbtot.total}"/></td>
                                        <td <c:if test="${rbtot.total > 0}"> class='green'</c:if><c:if test="${rbtot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${rbtot.total}"/></td>
                                        <c:if test="${isWRTECombo == false}">                                            
                                            <td <c:if test="${wrtot.total > 0}"> class='green'</c:if><c:if test="${wrtot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${wrtot.total}"/></td>
                                        </c:if>
                                        <c:if test="${isWRTECombo == false}">
                                            <td <c:if test="${tetot.total > 0}"> class='green'</c:if><c:if test="${tetot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${tetot.total}"/></td>
                                        </c:if>
                                        <c:if test="${isWRTECombo == true}">
                                            <td <c:if test="${wrtetot.total > 0}"> class='green'</c:if><c:if test="${wrtetot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${wrtetot.total}"/></td>
                                        </c:if>
                                        <td <c:if test="${pktot.total > 0}"> class='green'</c:if><c:if test="${pktot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${pktot.total}"/></td>
                                        <!-- DEFENSE -->
                                        <c:if test="${isDynasty == true}">
                                            <td <c:if test="${dltot.total > 0}"> class='green'</c:if><c:if test="${dltot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${dltot.total}"/></td>
                                            <td <c:if test="${lbtot.total > 0}"> class='green'</c:if><c:if test="${lbtot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${lbtot.total}"/></td>
                                            <td <c:if test="${dbtot.total > 0}"> class='green'</c:if><c:if test="${dbtot.total < 0}"> class='red'</c:if>><fmt:formatNumber minFractionDigits="2" value="${dbtot.total}"/></td>
                                        </c:if>
                                        <!-- TOTALS -->
                                        <td title='<fmt:formatNumber minFractionDigits="2" value="${myptstot.total}"/> - <fmt:formatNumber minFractionDigits="2" value="${oppptstot.total}"/>'
                                            <c:if test="${difftot.total > 0}"> class='green'</c:if><c:if test="${difftot.total < 0}"> class='red'</c:if>
                                        >
                                            <fmt:formatNumber minFractionDigits="2" value="${difftot.total}"/>
                                        </td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                            
                        </table>

                    </div> <!-- inner position Breakdown -->
                </div> <!-- position Breakdown -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
