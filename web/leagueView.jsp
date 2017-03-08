<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="ct.control.registerAction"%>
<%@ taglib prefix="fas" uri="fas.taglib" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<un:useConstants var="fields" className="ct.control.registerAction" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="css/styles.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/registration.css" media="screen" />
<title>Coltarama - League View</title>

</head>

<body>
    <div id="container">
        
        <jsp:include page="inc_header.jsp" />

        <jsp:include page="inc_mainmenu.jsp" />
        
        <div class="colmask outside">

            <div class="main">
            
                <div class="leftcolumn">
                    
                    <div id="content">
						
                        <br />
                        <h1>League View</h1>
                        <br />
                        
                        * League Standings<br />
                        <!-- Standings -->
                        <table width="100%" border="0" cellpadding="0" cellspacing="1">
                            <fas:tableRows items="${leagueStandings}" var="team">
                                <jsp:attribute name="rowTitle">
                                    <tr align="center">
                                        <td colspan="6" class="bgMaroon">
                                            <div class="GTW10"><strong>CURRENT STANDINGS<br />
                                            (After Week #<a name="stWeekNo" />)</strong></div>
                                        </td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr bgcolor="#545454">
                                        <td width="10%" class="GTW10" align="center"><strong>RANK</strong></td>
                                        <td width="30%" class="GTW10" align="center"><strong>TEAM</strong></td>
                                        <td width="15%" class="GTW10" align="center"><strong>W - L</strong></td>
                                        <td width="15%" class="GTW10" align="center"><strong>PTS FOR</strong></td>
                                        <td width="15%" class="GTW10" align="center"><strong>PTS AGAINST</strong></td>
                                        <td width="10%" class="GTW10" align="center"><strong>HI</strong></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr bgcolor="#FFFFFF">
                                        <td align="center" class="GT10" bgcolor="#DDDDDD" ><strong><a name="rank">rank</a></strong></td>
                                        <td align="center" class="GT10"><a name="team">team</a></td>
                                        <td align="center" class="GT10"><a name="win">win</a>-<a name="loss">loss</a></td>
                                        <td align="center" class="GT10"><strong><a name="fPts">fPts</a></strong></td>
                                        <td align="center" class="GT10"><strong><a name="fPtsAgainst">fPtsAg</a></strong></td>
                                        <td align="center" class="GT10"><strong><a name="hi">hi</a></strong></td>
                                    </tr>
                                </jsp:attribute>
                            </fas:tableRows>
                        </table>
                        <!-- End Standings -->
                        <br />

                        * Last Week's Results<br />
                        * This Week's Schedule<br />
                        * Recent Transactions<br />
                        * Current Transaction Order<br />
                        * Top Performers<br />
                        
                    </div> <!-- content -->

                </div> <!-- left column -->
                <div class="rightcolumn">
                    
                    <jsp:include page="inc_rightsidebar.jsp" />
                    
                </div> <!-- right column -->
            </div> <!-- main -->
        </div> <!-- colmask outside -->

        <jsp:include page="inc_footer.jsp" />
        
    </div> <!-- container -->
</body>
</html>
