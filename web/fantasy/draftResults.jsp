<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>Draft Results</title>
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
        </div> <!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div> <!-- left Menu -->

        <div id="content">
            <div id="innerContent">
                
                <div id="leagueTransactions">
                    <div id="innerLeagueTransactions">
                        <!-- Current Draft -->
                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tds:tableRows items="${leagueDraft}" var="draft" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">

                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr align="center" class="rowTitle">
                                    <td colspan="8">
                                        <div><strong><h2>Draft Results</h2></strong></div><br />
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8">
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td><strong>Round</strong></td>
                                    <td><strong>Pick #</strong></td>
                                    <td><strong>Owner</strong></td>
                                    <td><strong>Pos</strong></td>
                                    <td><strong>Team</strong></td>
                                    <td><strong>Player</strong></td>
                                    
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <c:choose>
                                        <c:when test="${draft.round == 99}">
                                            <td align="center" colspan="2">Retained</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>${draft.round}
                                            <td>${draft.place}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>${draft.FSTeam.teamName}</td>
                                    <td>${draft.player.position.positionName}</td>
                                    <td>${draft.player.team.abbreviation}</td>
                                    <td>${draft.player.fullName}</td>
                                    
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no draft records to display.</td>
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

                    </div> <!-- inner League Transactions -->
                </div> <!-- league Transactions -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
