<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script type="text/javascript" src="../js/script.js" ></script>
  </head>
  <body>

    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div><!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

                <jsp:include page="../inc_errorMessage.jsp" />
                
                <div id="salaryRoster">
                    <div id="innerSalaryRoster">

                        <table>
                            <tr class="rowTitle">
                                <td colspan="8">${fsteam.FSLeague.leagueName} Roster</td>
                            </tr>
                            <tr>
                                <td colspan="8">for Week #${salCurrentWeek.FSSeasonWeekNo}</td>
                            </tr>
                            <tr class="rowHeader">
                                <td>Pos</td>
                                <td>Team</td>
                                <td colspan="2">Player</td>
                                <td>Salary</td>
                                <td>Avg Pts</td>
                                <td>Opp</td>
                                <td>Game Time</td>                                
                            </tr>

                            <%-- QUARTERBACK --%>
                            <tr class="rowData2">
                                <c:set var="game" value="${tds:getGame(salCurrentWeek.seasonWeekID,QB1.player.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,QB1.player.team.teamID)}" />
                                
                                <td><a href="yourPlayers.htm?pos=1">QB</a></td>
                                <c:if test="${QB1 != null}">
                                    <td>${QB1.player.team.abbreviation}</td>
                                    <td>
                                        <c:if test="${game.bye==true || game.gameHasStarted==false}">
                                            <a href="yourPlayers.htm?dip=${QB1.playerID}&pos=${createpostpos}">
                                                <img class="plusMinus" src="/topdawgsports/images/minus.png" />
                                            </a>
                                        </c:if>
                                    </td>
                                    <td class="playerName"><tds:player player="${QB1.player}" displayStatsLink="true" displayInjury="false" /></td>
                                    <td><fmt:formatNumber type="currency" value="${QB1.value}" maxFractionDigits="0" /></td>
                                    <td><%--<fmt:formatNumber value="${QB1.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" />--%></td>
                                    <td>${opp}</td>
                                    <td>
                                        <c:if test="${QB1!=null && opp!='BYE'}">
                                            <fmt:formatDate value="${game.gameDate.time}" pattern="E h:mm a"/>
                                        </c:if>
                                    </td>                                    
                                </c:if>
                                <c:if test="${QB1 == null}">
                                    <td colspan="7"></td>
                                </c:if>
                            </tr>

                            <%-- RUNNING BACK 1 --%>
                            <tr class="rowData2">
                                <c:set var="game" value="${tds:getGame(salCurrentWeek.seasonWeekID,RB1.player.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,RB1.player.team.teamID)}" />

                                <td><a href="yourPlayers.htm?pos=2">RB</a></td>
                                <c:if test="${RB1 != null}">
                                    <td>${RB1.player.team.abbreviation}</td>
                                    <td>
                                        <c:if test="${game.bye==true || game.gameHasStarted==false}">
                                            <a href="yourPlayers.htm?dip=${RB1.playerID}&pos=${createpostpos}">
                                                <img class="plusMinus" src="/topdawgsports/images/minus.png" />
                                            </a>
                                        </c:if>
                                    </td>
                                    <td class="playerName"><tds:player player="${RB1.player}" displayStatsLink="true" displayInjury="false" /></td>
                                    <td><fmt:formatNumber type="currency" value="${RB1.value}" maxFractionDigits="0" /></td>
                                    <td><%--<fmt:formatNumber value="${RB1.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" />--%></td>
                                    <td>${opp}</td>
                                    <td>
                                        <c:if test="${RB1!=null && opp!='BYE'}">
                                            <fmt:formatDate value="${game.gameDate.time}" pattern="E h:mm a"/>
                                        </c:if>
                                    </td>
                                </c:if>
                                <c:if test="${RB1 == null}">
                                    <td colspan="7"></td>
                                </c:if>
                            </tr>

                            <%-- RUNNING BACK 2 --%>
                            <tr class="rowData2">
                                <c:set var="game" value="${tds:getGame(salCurrentWeek.seasonWeekID,RB2.player.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,RB2.player.team.teamID)}" />

                                <td><a href="yourPlayers.htm?pos=2">RB</a></td>
                                <c:if test="${RB2 != null}">
                                    <td>${RB2.player.team.abbreviation}</td>
                                    <td>
                                        <c:if test="${game.bye==true || game.gameHasStarted==false}">
                                            <a href="yourPlayers.htm?dip=${RB2.playerID}&pos=${createpostpos}">
                                                <img class="plusMinus" src="/topdawgsports/images/minus.png" />
                                            </a>
                                        </c:if>
                                    </td>
                                    <td class="playerName"><tds:player player="${RB2.player}" displayStatsLink="true" displayInjury="false" /></td>
                                    <td><fmt:formatNumber type="currency" value="${RB2.value}" maxFractionDigits="0" /></td>
                                    <td><%--<fmt:formatNumber value="${RB2.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" />--%></td>
                                    <td>${opp}</td>
                                    <td>
                                        <c:if test="${RB2!=null && opp!='BYE'}">
                                            <fmt:formatDate value="${game.gameDate.time}" pattern="E h:mm a"/>
                                        </c:if>
                                    </td>
                                </c:if>
                                <c:if test="${RB2 == null}">
                                    <td colspan="7"></td>
                                </c:if>
                            </tr>

                            <%-- WIDE RECEIVER 1 --%>
                            <tr class="rowData2">
                                <c:set var="game" value="${tds:getGame(salCurrentWeek.seasonWeekID,WR1.player.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,WR1.player.team.teamID)}" />

                                <td><a href="yourPlayers.htm?pos=3">WR</a></td>
                                <c:if test="${WR1 != null}">
                                    <td>${WR1.player.team.abbreviation}</td>
                                    <td>
                                        <c:if test="${game.bye==true || game.gameHasStarted==false}">
                                            <a href="yourPlayers.htm?dip=${WR1.playerID}&pos=${createpostpos}">
                                                <img class="plusMinus" src="/topdawgsports/images/minus.png" />
                                            </a>
                                        </c:if>
                                    </td>
                                    <td class="playerName"><tds:player player="${WR1.player}" displayStatsLink="true" displayInjury="false" /></td>
                                    <td><fmt:formatNumber type="currency" value="${WR1.value}" maxFractionDigits="0" /></td>
                                    <td><%--<fmt:formatNumber value="${WR1.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" />--%></td>
                                    <td>${opp}</td>
                                    <td>
                                        <c:if test="${WR1!=null && opp!='BYE'}">
                                            <fmt:formatDate value="${game.gameDate.time}" pattern="E h:mm a"/>
                                        </c:if>
                                    </td>
                                </c:if>
                                <c:if test="${WR1 == null}">
                                    <td colspan="7"></td>
                                </c:if>
                            </tr>

                            <%-- WIDE RECEIVER 2 --%>
                            <tr class="rowData2">
                                <c:set var="game" value="${tds:getGame(salCurrentWeek.seasonWeekID,WR2.player.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,WR2.player.team.teamID)}" />

                                <td><a href="yourPlayers.htm?pos=3">WR</a></td>
                                <c:if test="${WR2 != null}">
                                    <td>${WR2.player.team.abbreviation}</td>
                                    <td>
                                        <c:if test="${game.bye==true || game.gameHasStarted==false}">
                                            <a href="yourPlayers.htm?dip=${WR2.playerID}&pos=${createpostpos}">
                                                <img class="plusMinus" src="/topdawgsports/images/minus.png" />
                                            </a>
                                        </c:if>
                                    </td>
                                    <td class="playerName"><tds:player player="${WR2.player}" displayStatsLink="true" displayInjury="false" /></td>
                                    <td><fmt:formatNumber type="currency" value="${WR2.value}" maxFractionDigits="0" /></td>
                                    <td><%--<fmt:formatNumber value="${WR2.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" />--%></td>
                                    <td>${opp}</td>
                                    <td>
                                        <c:if test="${WR2!=null && opp!='BYE'}">
                                            <fmt:formatDate value="${game.gameDate.time}" pattern="E h:mm a"/>
                                        </c:if>
                                    </td>
                                </c:if>
                                <c:if test="${WR2 == null}">
                                    <td colspan="7"></td>
                                </c:if>
                            </tr>

                            <%-- TIGHT END --%>
                            <tr class="rowData2">
                                <c:set var="game" value="${tds:getGame(salCurrentWeek.seasonWeekID,TE1.player.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,TE1.player.team.teamID)}" />

                                <td><a href="yourPlayers.htm?pos=4">TE</a></td>
                                <c:if test="${TE1 != null}">
                                    <td>${TE1.player.team.abbreviation}</td>
                                    <td>
                                        <c:if test="${game.bye==true || game.gameHasStarted==false}">
                                            <a href="yourPlayers.htm?dip=${TE1.playerID}&pos=${createpostpos}">
                                                <img class="plusMinus" src="/topdawgsports/images/minus.png" />
                                            </a>
                                        </c:if>
                                    </td>
                                    <td class="playerName"><tds:player player="${TE1.player}" displayStatsLink="true" displayInjury="false" /></td>
                                    <td><fmt:formatNumber type="currency" value="${TE1.value}" maxFractionDigits="0" /></td>
                                    <td><%--<fmt:formatNumber value="${TE1.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" />--%></td>
                                    <td>${opp}</td>
                                    <td>
                                        <c:if test="${TE1!=null && opp!='BYE'}">
                                            <fmt:formatDate value="${game.gameDate.time}" pattern="E h:mm a"/>
                                        </c:if>
                                    </td>
                                </c:if>
                                <c:if test="${TE1 == null}">
                                    <td colspan="7"></td>
                                </c:if>
                            </tr>
                            
                            <%-- KICKER --%>
                            <tr class="rowData2">
                                <c:set var="game" value="${tds:getGame(salCurrentWeek.seasonWeekID,PK1.player.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,PK1.player.team.teamID)}" />

                                <td><a href="yourPlayers.htm?pos=5">PK</a></td>
                                <c:if test="${PK1 != null}">
                                    <td>${PK1.player.team.abbreviation}</td>
                                    <td>
                                        <c:if test="${game.bye==true || game.gameHasStarted==false}">
                                            <a href="yourPlayers.htm?dip=${PK1.playerID}&pos=${createpostpos}">
                                                <img class="plusMinus" src="/topdawgsports/images/minus.png" />
                                            </a>
                                        </c:if>
                                    </td>
                                    <td class="playerName"><tds:player player="${PK1.player}" displayStatsLink="true" displayInjury="false" /></td>
                                    <td><fmt:formatNumber type="currency" value="${PK1.value}" maxFractionDigits="0" /></td>
                                    <td><%--<fmt:formatNumber value="${PK1.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" />--%></td>
                                    <td>${opp}</td>
                                    <td>
                                        <c:if test="${PK1!=null && opp!='BYE'}">
                                            <fmt:formatDate value="${game.gameDate.time}" pattern="E h:mm a"/>
                                        </c:if>
                                    </td>
                                </c:if>
                                <c:if test="${PK1 == null}">
                                    <td colspan="7"></td>
                                </c:if>
                            </tr>

                            <%-- TOTALS --%>
                            <tr>
                                <td colspan="3"></td>
                                <td>Totals:</td>
                                <td><fmt:formatNumber type="currency" value="${QB1.value + RB1.value + RB2.value + WR1.value + WR2.value + TE1.value + PK1.value }" maxFractionDigits="0" /></td>
                                <td><%--<fmt:formatNumber value="${QB1.player.totalFootballStats.avgFantasyPts + RB1.player.totalFootballStats.avgFantasyPts + RB2.player.totalFootballStats.avgFantasyPts + WR1.player.totalFootballStats.avgFantasyPts + WR2.player.totalFootballStats.avgFantasyPts + TE1.player.totalFootballStats.avgFantasyPts + PK1.player.totalFootballStats.avgFantasyPts}" minFractionDigits="2" maxFractionDigits="2" />--%></td>
                            </tr>
                            <tr class="salaryRemaining">
                                <td colspan="3"></td>
                                <td>Salary Remaining:</td>
                                <td><fmt:formatNumber type="currency" value="${salaryCap - (QB1.value + RB1.value + RB2.value + WR1.value + WR2.value + TE1.value + PK1.value )}" maxFractionDigits="0" /></td>
                            </tr>
                        </table>
                                
                        <p> NOTE : Your picks are automatically saved after each selection.  Your roster is required to have a player selected in each positional slot.
                            Failure to meet the requirements of a full roster or failure to stay under the salary cap allotted will result in 0 Fantasy Pts.
                        </p>

                        <div id="position">
                            <label>Choose a Position :</label>
                            <a href="yourPlayers.htm?pos=1">QB</a>
                            <a href="yourPlayers.htm?pos=2">RB</a>
                            <a href="yourPlayers.htm?pos=3">WR</a>
                            <a href="yourPlayers.htm?pos=4">TE</a>
                            <a href="yourPlayers.htm?pos=5">PK</a>
                        </div>
                                    
                        <!-- NFL Players -->
                        <table>                            
                            <tds:tableRows displayRows="25" items="${playerSalaries}" var="playervalue" startingRowNum="${startingRowNum}" tableNumber="1">
                                <jsp:attribute name="rowInfo">
                                    
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>Pos</td>
                                        <td>
                                            <a href="yourPlayers.htm?sort=2${(listSort=="2") ? "_d" : ""}">Team</a>
                                            <img src="${(listSort=="2") ? "/topdawgsports/images/arrow_up.gif" : ((listSort=="2_d") ? "/topdawgsports/images/arrow_down.gif" : "/topdawgsports/images/spacer.gif")}" alt="" />
                                        </td>                                        
                                        <td colspan="2">
                                            <a href="yourPlayers.htm?sort=1${(listSort=="1") ? "_d" : ""}">Player</a>
                                            <img src="${(listSort=="1") ? "/topdawgsports/images/arrow_up.gif" : ((listSort=="1_d") ? "/topdawgsports/images/arrow_down.gif" : "/topdawgsports/images/spacer.gif")}" alt="" />
                                        </td>
                                        <td>
                                            <a href="yourPlayers.htm?sort=4${(listSort=="4_d") ? "" : "_d"}">Salary</a>
                                            <img src="${(listSort=="4") ? "/topdawgsports/images/arrow_up.gif" : ((listSort=="4_d") ? "/topdawgsports/images/arrow_down.gif" : "/topdawgsports/images/spacer.gif")}" alt="" />
                                        </td>
                                        <td>Avg Pts</td>
                                        <td>Opp</td>
                                        <td>Game Time</td>                                                                            
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData" >
                                    <tr class="rowData">
                                        <c:set var="opp" value="${tds:getOpponentString(playervalue.game,playervalue.player.team.teamID)}" />
                                        <c:set var="game" value="${playervalue.game}" />

                                        <td>${playervalue.player.position.positionName}</td>
                                        <td>${playervalue.player.team.abbreviation}</td>
                                        <%-- PLUS SIGN --%>
                                        <td>
                                            <c:if test="${game.bye!=true && game.gameHasStarted==false}">
                                                <a href="yourPlayers.htm?pid=${playervalue.player.playerID}&pos=${createpostpos}">
                                                    <img class="plusMinus" src="/topdawgsports/images/plus.png" />
                                                </a>
                                           </c:if>
                                        </td>
                                        <td class="playerName">                                            
                                            <tds:player player="${playervalue.player}" displayStatsLink="true" displayInjury="false" />
                                        </td>
                                        <td><fmt:formatNumber type="currency" value="${playervalue.value}" maxFractionDigits="0" /></td>
                                        <td><fmt:formatNumber type="number" value="${playervalue.totalFootballStats.avgSalFantasyPts}" maxFractionDigits="1" minFractionDigits="1" /></td>
                                        <td>${opp}</td>
                                        <td>
                                            <c:if test="${opp != 'BYE'}">
                                                <fmt:formatDate value="${game.gameDate.time}" pattern="E h:mm a"/>
                                            </c:if>
                                        </td>
                                        <%--<td><fmt:formatNumber type="number" value="${playervalue.totalFootballStats.salFantasyPts}" maxFractionDigits="1" minFractionDigits="1" /></td>--%>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr class="rowData2">
                                        <td colspan="8">There are no players to display.</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowNavigation">
                                    <tr>
                                        <td colspan="7"><hr /></td>
                                    </tr>
                                    <tr class="rowData">
                                        <td><tds:navFirst link="yourPlayers.htm?sort=${salSort}">FIRST</tds:navFirst></td>
                                        <td colspan="2"><tds:navPrev link="yourPlayers.htm?sort=${salSort}"><< PREV</tds:navPrev></td>
                                        <td colspan="3">${fromRows1} to ${toRows1} of ${totalRows1}</td>
                                        <td><tds:navNext link="yourPlayers.htm?sort=${salSort}">NEXT >></tds:navNext></td>
                                        <td><tds:navLast link="yourPlayers.htm?sort=${salSort}">LAST</tds:navLast></td>
                                    </tr>
                               </jsp:attribute>
                            </tds:tableRows>
                          </table>
                    </div> <!-- inner salary roster -->
                </div> <!-- salary roster -->
            </div> <!-- inner content -->
        </div> <!-- content -->

    </div> <!-- container-->

  </body>
</html>
