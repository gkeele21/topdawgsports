<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="tds.control.registerAction"%>
<%@page import="bglib.util.AuDate"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="../css/styles.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../css/registration.css" media="screen" />
<title>TopDawgSports - Salary Cap Players</title>
<script type="text/javascript" src="../js/script.js" ></script>

</head>

<body>
    <div id="container">
        
        <jsp:include page="../inc_header.jsp" />

        <jsp:include page="../inc_mainmenu.jsp" />
        
        <jsp:include page="../inc_submenu.jsp" />
        
        <jsp:include page="../inc_errorMessage.jsp" />
        
        <div class="colmask outside">

            <div class="main">
            
                <div class="leftcolumn">
                    
                    <div id="content">

                        <table>
                            <tr><td height="10" bgcolor="#FFFFFF" colspan="6"><img src="/topdawg/images/spacer.gif" /></td></tr>
                            <tr>
                                <td bgcolor="#FFFFFF"><img src="/topdawg/images/spacer.gif" /></td>
                                <td bgcolor="#FFFFFF" valign="top" class="title_black_15">Players Picked</td>
                                <td bgcolor="#FFFFFF" colspan="3" align="right" class="text">&nbsp;&nbsp;</td>
                                <td bgcolor="#FFFFFF"><img src="/topdawg/images/spacer.gif" /></td>
                            </tr>
                            <tr><td height="10" bgcolor="#FFFFFF" colspan="6"><img src="/topdawg/images/spacer.gif" /></td></tr>
                            <tr>
                                <td bgcolor="#FFFFFF"><img src="/topdawg/images/spacer.gif" /></td>
                                <td bgcolor="#FFFFFF" align="center" valign="top" class="title_black_15" colspan="4">
                                    <c:if test="${dispWeek > 1}" >
                                        <a href="playersSelected.htm?wk=1"><<</a>&#160;&#160;
                                    </c:if>
                                    <c:if test="${dispWeek > 1}" >
                                        <a href="playersSelected.htm?wk=${dispWeek-1}"><</a>
                                    </c:if>
                                    &#160;&#160;Week #${dispWeek}&#160;&#160;
                                    <c:if test="${dispWeek < fsseasonweek.FSSeasonWeekNo - 1}" >
                                        <a href="playersSelected.htm?wk=${dispWeek+1}">></a>
                                    </c:if>
                                    <c:if test="${dispWeek < fsseasonweek.FSSeasonWeekNo - 1}" >
                                        &#160;&#160;<a href="playersSelected.htm?wk=${fsseasonweek.FSSeasonWeekNo - 1}">>></a>
                                    </c:if>
                                </td>
                                <td bgcolor="#FFFFFF"><img src="/topdawg/images/spacer.gif" /></td>
                            </tr>
                            <tr>
                                <td colspan="6" valign="top" id="whitefill">

                                            <!-- Standings -->
                                    <table width="770" align="center" cellpadding="0" cellspacing="0" border="0" class="ctTable">
                                        <tds:tableRows items="${playersSelected}" var="playerValue" displayRows="200" highlightRowAttribute="class" highlightRowValue="rowData2" tableNumber="1">
                                            <jsp:attribute name="rowHeader" >
                                                <tr class="rowHeader">
                                                    <td width="1%" align="center"><img src="/topdawg/images/spacer.gif" /></td>
                                                    <td width="5%" valign="bottom">
                                                        <a href="playersSelected.htm?wk=${dispWeek}&sort=1${(listSort!="1") ? "_d" : "_a"}" class="title_black">#</a><img src="${(listSort=="1") ? "/topdawg/images/arrow_up.gif" : ((listSort=="1_d") ? "/topdawg/images/arrow_down.gif" : "/topdawg/images/spacer.gif")}" alt="" />
                                                    </td>
                                                    <td width="20%" valign="bottom">
                                                        <a href="playersSelected.htm?wk=${dispWeek}&sort=2${(listSort!="2") ? "_d" : "_a"}" class="title_black">Player</a><img src="${(listSort=="2") ? "/topdawg/images/arrow_up.gif" : ((listSort=="2_d") ? "/topdawg/images/arrow_down.gif" : "/topdawg/images/spacer.gif")}" alt="" />
                                                    </td>
                                                    <td width="6%" valign="bottom" align="center">
                                                        <a href="playersSelected.htm?wk=${dispWeek}&sort=3${(listSort!="3") ? "_d" : "_a"}" class="title_black">Pos</a><img src="${(listSort=="3") ? "/topdawg/images/arrow_up.gif" : ((listSort=="3_d") ? "/topdawg/images/arrow_down.gif" : "/topdawg/images/spacer.gif")}" alt="" />
                                                    </td>
                                                    <td width="6%" valign="bottom" align="center">
                                                        <a href="playersSelected.htm?wk=${dispWeek}&sort=4${(listSort!="4") ? "_d" : "_a"}" class="title_black">Team</a><img src="${(listSort=="4") ? "/topdawg/images/arrow_up.gif" : ((listSort=="4_d") ? "/topdawg/images/arrow_down.gif" : "/topdawg/images/spacer.gif")}" alt="" />
                                                    </td>
                                                    <td width="6%" valign="bottom" align="center">
                                                        <a href="playersSelected.htm?wk=${dispWeek}&sort=5${(listSort!="5") ? "_d" : "_a"}" class="title_black">Fantasy<br />Pts</a><img src="${(listSort=="5") ? "/topdawg/images/arrow_up.gif" : ((listSort=="5_d") ? "/topdawg/images/arrow_down.gif" : "/topdawg/images/spacer.gif")}" alt="" />
                                                    </td>
                                                    <td width="6%" valign="bottom" align="center">
                                                        <a href="playersSelected.htm?wk=${dispWeek}&sort=6${(listSort!="6") ? "_d" : "_a"}" class="title_black">Salary</a><img src="${(listSort=="6") ? "/topdawg/images/arrow_up.gif" : ((listSort=="6_d") ? "/topdawg/images/arrow_down.gif" : "/topdawg/images/spacer.gif")}" alt="" />
                                                    </td>
                                                    <td width="1%" align="center"><img src="/topdawg/images/spacer.gif" /></td>
                                                    <td width="49%" valign="bottom" align="center"><span class="title_black">Teams</span></td>
                                                </tr>
                                            </jsp:attribute>
                                            <jsp:attribute name="rowData" >
                                                <tr ${highlightRow1} class="rowData">
                                                    <td align="center" ${highlightRow1}><img src="/topdawg/images/spacer.gif" /></td>
                                                    <td ${highlightRow1}><c:out value="${playerValue.count}" /></td>
                                                    <td ${highlightRow1}><tds:player player="${playerValue.FSPlayerValue.player}" displayStatsLink="true" /></td>
                                                    <td align="center" ${highlightRow1}><c:out value="${playerValue.FSPlayerValue.player.position.positionName}" /> </td>
                                                    <td align="center" ${highlightRow1}><c:out value="${playerValue.FSPlayerValue.player.team.teamName}" /></td>
                                                    <td align="center" ${highlightRow1}><fmt:formatNumber type="number" value="${playerValue.FSPlayerValue.footballStats.fantasyPts}" minFractionDigits="1" maxFractionDigits="1" /> </td>
                                                    <td align="center" ${highlightRow1}><fmt:formatNumber type="currency" value="${playerValue.FSPlayerValue.value}" maxFractionDigits="0" /> </td>
                                                    <td ${highlightRow1}>&#160;</td>
                                                    <td ${highlightRow1}>
                                                        <%--<c:if test="${dispWeek < currentweek}" >--%>
                                                            <c:set var="teams" value="${tds:getTeamsWithPlayer(playerValue.FSPlayerValue.player.playerID,playerValue.FSPlayerValue.FSSeasonWeekID,leagueTeams)}" />
                                                            <tds:tableRows2 items="${teams}" var="team" tableNumber="2">
                                                                <jsp:attribute name="rowData">
                                                                    <c:if test="${currentRow2 > 1}"><c:out value="," /></c:if>
                                                                    <c:out value="${team.teamName}" />
                                                                </jsp:attribute>
                                                            </tds:tableRows2>
                                                        <%--</c:if>--%>
                                                    </td>
                                                </tr>
                                            </jsp:attribute>
                                            <jsp:attribute name="rowEmpty">
                                                    <tr><td height="5" colspan="5"><img src="/topdawg/images/spacer.gif" /></td></tr>
                                                <tr>
                                                    <td colspan="5">&nbsp;&nbsp;&nbsp;There are no players selected.</td>
                                                </tr>
                                                <tr><td height="10" colspan="5"><img src="/topdawg/images/spacer.gif" /></td></tr>
                                            </jsp:attribute>
                                      </tds:tableRows>
                                    </table>
                                              </td>
                                            </tr>
                            <tr><td height="20" bgcolor="#FFFFFF" colspan="6"><img src="/topdawg/images/spacer.gif" /></td></tr>

                        </table>
                    </div> <!-- content -->

                </div> <!-- left column -->
                <div class="rightcolumn">
                    
                    <jsp:include page="../inc_rightsidebar.jsp" />
                    
                </div> <!-- right column -->
            </div> <!-- main -->
        </div> <!-- colmask outside -->

        <jsp:include page="../inc_footer.jsp" />
        
    </div> <!-- container -->
</body>
</html>
