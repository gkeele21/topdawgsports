<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - March Madness Seed Challenge All Picks</title>
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <script src="/topdawgsports/js/jquery-1.6.2.min.js" type="text/javascript"></script>
    <script src="/topdawgsports/js/jquery.tablesorter.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#tblPicksSummary").tablesorter({widgets: ['zebra'], sortList:[[1,0], [2,0], [3,0], [4,0], [5,0], [6,0], [7,0], [8,0]]});
        });
    </script>
    <style type="text/css">

        #innerPicksSummary { margin: 0px 10px 0px 10px; }
        #innerPicksSummary a { color: #BF8339; }
        #innerPicksSummary a:hover { color: white; cursor: pointer; }        
        #tblPicksSummary { font-size: .9em; }
        #tblPicksSummary th { color: #731702; font-size: 1.2em; font-weight: bold; padding: 10px 0px; text-transform: uppercase; }
        #tblPicksSummary th:hover { color: #BF8339; cursor: pointer; }
        #tblPicksSummary tr.odd { background-color: #1C5953; color: #F2BC57; }
        #tblPicksSummary tr.even { background-color: #103B40; color: #F2BC57; }        
        .out { color: #731702; }

    </style>
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
                
                <div id="picksSummary">
                    <div id="innerPicksSummary">
                
                        <!-- Initialization -->
                        <c:set var="prevFSTeam" value="0" />

                        <table id="tblPicksSummary">
                            
                            <thead>
                                <tr>
                                    <th>Team Name</th>
                                    <th>#1</th>
                                    <th>#2</th>
                                    <th>#3</th>
                                    <th>#4-5</th>
                                    <th>#6-7</th>
                                    <th>#8-10</th>
                                    <th>#11-12</th>
                                    <th>#13-16</th>
                                </tr>
                            </thead>                            

                            <tbody>
                                <c:forEach items="${picks}" var="pick">
                                    <c:choose>
                                        <c:when test="${prevFSTeam != pick.FSTeam.FSTeamID}">
                                            </tr>
                                            <tr>
                                                <td><a title="${pick.FSTeam.FSUser.firstName} ${pick.FSTeam.FSUser.lastName}" 
                                                       href="seedChallenge.htm?dtid=${pick.FSTeam.FSTeamID}">${pick.FSTeam.teamName}</a></td>
                                                <td
                                                    <c:if test="${pick.teamSeedPicked.status == 'OUT'}">
                                                        class="out"
                                                    </c:if>
                                                    >${pick.teamSeedPicked.team.displayName}
                                                </td>
                                                <c:set var="prevFSTeam" value="${pick.FSTeam.FSTeamID}" />
                                        </c:when>
                                        <c:otherwise>
                                                <td
                                                    <c:if test="${pick.teamSeedPicked.status == 'OUT'}">
                                                        class="out"
                                                    </c:if>
                                                    >${pick.teamSeedPicked.team.displayName}
                                                </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                            </tr>
                            </tbody>                            
                        </table>
                    </div> <!-- picks -->
                </div> <!-- innner picks -->
            </div> <!-- innner content -->
        </div> <!-- content -->
    </div> <!-- container -->
    
</body>
</html>