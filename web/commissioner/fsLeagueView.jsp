<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/styles.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../css/frontpage.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../css/registration.css" media="screen" />
        <title>TopDawgSports - FSLeague View</title>
    </head>
    <body>
        <div id="container">
        
            <jsp:include page="inc_header.jsp" />

            <jsp:include page="inc_mainmenu.jsp" />

            <%--<jsp:include page="../inc_submenu.jsp" />--%>

            <c:if test="${validUser != null}" >
                <jsp:include page="inc_errorMessage.jsp" />
            </c:if>

            <div class="colmask outside">

                <div class="main">

                    <h3>
                        <a href="fsSeasonView.htm">Back to Season View</a>
                    </h3>
                    <br />
                    <jsp:include page="inc_gameMenu.jsp" />

                    <br />
                    <div align="center" class="rowTitle"><strong><h2>FSLEAGUE : ${commFSLeague.leagueName}</h2></strong></div>
                    
                    <!-- FSLeague Info -->
                    <div align="center">
                        <table width="80%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tr class="rowData2">
                                <td colspan="4" align="right"><a href="fsLeagueEdit.jsp?fsLeagueID=${commFSLeague.FSLeagueID}" ><img src="../images/edit-icon.png" height="14"/></a></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">FSLeagueID : </td><td align="center"><c:out value="${commFSLeague.FSLeagueID}" /></td>
                                <td align="right">FSSeasonID : </td><td align="center"><c:out value="${commFSLeague.FSSeasonID}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">LeagueName : </td><td align="center"><c:out value="${commFSLeague.leagueName}" /></td>
                                <td align="right">LeaguePassword : </td><td align="center"><c:out value="${commFSLeague.leaguePassword}" /></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">IsFull : </td><td align="center"><c:out value="${commFSLeague.isFull}" /></td>
                                <td align="right">IsPublic : </td><td align="center"><c:out value="${commFSLeague.isPublic}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">NumTeams : </td><td align="center"><c:out value="${commFSLeague.numTeams}" /></td>
                                <td align="right">Description : </td><td align="center"><c:out value="${commFSLeague.description}" /></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">IsGeneral : </td><td align="center"><c:out value="${commFSLeague.isGeneral}" /></td>
                                <td align="right">StartFSSeasonWeekID : </td><td align="center"><c:out value="${commFSLeague.startFSSeasonWeekID}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">VendorID : </td><td align="center"><c:out value="${commFSLeague.vendorID}" /></td>
                                <td align="right">DraftType : </td><td align="center"><c:out value="${commFSLeague.draftType}" /></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">DraftDate : </td><td align="center"><c:out value="${commFSLeague.draftDate}" /></td>
                                <td align="right">HasPaid : </td><td align="center"><c:out value="${commFSLeague.hasPaid}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">IsDraftComplete : </td><td align="center"><c:out value="${commFSLeague.isDraftComplete}" /></td>
                                <td align="right">CommissionerUserID : </td><td align="center"><c:out value="${commFSLeague.commissionerUserID}" /></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">IsCustomLeague : </td><td align="center"><c:out value="${commFSLeague.isCustomLeague}" /></td>
                                <td align="right">ScheduleName : </td><td align="center"><c:out value="${commFSLeague.scheduleName}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">IsDefaultLeaugue : </td><td align="center"><c:out value="${commFSLeague.isDefaultLeague}" /></td>
                                <td align="right">SignupType : </td><td align="center"><c:out value="${commFSLeague.signupType}" /></td>
                            </tr>
                        </table>
                    </div>

                    <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tds:tableRows items="${fsTeams}" var="fsTeam" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">

                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr align="center" class="rowTitle">
                                    <td colspan="8">
                                        <div><strong><h2>Teams</h2></strong></div><br />
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8">
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td width="8%" align="center"><strong>FSTeamID</strong></td>
                                    <td width="8%" align="center"><strong>Team Name</strong></td>
                                    <td width="12%" align="center"><strong>FSUser (ID)</strong></td>
                                    <td width="12%" align="center"><strong>Email</strong></td>
                                    <td width="14%" align="center"><strong>Date Created</strong></td>
                                    <td width="8%" align="center"><strong>Is Active</strong></td>
                                    <td width="6%" align="center"><strong>Team No</strong></td>
                                    <td width="8%" align="center"><strong>Sched Team No</strong></td>
                                    <td width="8%" align="center"><strong>Last Accessed</strong></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <td align="center"><c:out value="${fsTeam.FSTeamID}" /></td>
                                    <td align="center"><c:out value="${fsTeam.teamName}" /></td>
                                    <td align="center">
                                        <a href="fsTeamView.htm?fsTeamID=${fsTeam.FSTeamID}">
                                        <c:out value="${fsTeam.FSUser.firstName} ${fsTeam.FSUser.lastName} (${fsTeam.FSUserID})" />
                                        </a>
                                    </td>
                                    <td align="center"><c:out value="${fsTeam.FSUser.email}" /></td>
                                    <td align="center"><c:out value="${fsTeam.dateCreated}" /></td>
                                    <td align="center"><c:out value="${fsTeam.isActive}" /></td>
                                    <td align="center"><c:out value="${fsTeam.teamNo}" /></td>
                                    <td align="center"><c:out value="${fsTeam.scheduleTeamNo}" /></td>
                                    <td align="center"><c:out value="${fsTeam.lastAccessed}" /></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no FSTeams to display.</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowNavigation">
                                <c:set var="colspan" value="2" />
                                <tr><td height="10" colspan="8"><img src="../images/spacer.gif" /></td></tr>
                                <tr>
                                    <td colspan="8">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                                <tr class="rowData2">
                                                    <td width="65" align="center"><ct:navFirst link="fan_viewFSSeasons.htm?fsgameid=${comm_fsgameid}">&#160;FIRST</ct:navFirst></td>
                                                    <td width="65" align="center"><ct:navPrev link="fan_viewFSSeasons.htm?fsgameid=${comm_fsgameid}"><< PREV</ct:navPrev></td>
                                                    <td width="260" align="center">&#160;${fromRows1} to ${toRows1} of ${totalRows1}</td>
                                                    <td width="65" align="center"><ct:navNext link="fan_viewFSSeasons.htm?fsgameid=${comm_fsgameid}">NEXT >></ct:navNext></td>
                                                    <td width="65" align="center"><ct:navLast link="fan_viewFSSeasons.htm?fsgameid=${comm_fsgameid}">LAST</ct:navLast></td>
                                                </tr>
                                        </table>
                                    </td>
                                </tr>
                            </jsp:attribute>
                        </tds:tableRows>
                    </table>


                </div> <!-- main -->
            </div> <!-- colmask outside -->

            <jsp:include page="inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
