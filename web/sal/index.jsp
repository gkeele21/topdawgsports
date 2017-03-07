<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<%--
The taglib directive below impsorts the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="../css/styles.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../css/frontpage.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../css/registration.css" media="screen" />
<title>TopDawgSports - Salary Cap index</title>

</head>

<body>
    <div id="container">
        
        <jsp:include page="../inc_header.jsp" />

        <jsp:include page="../inc_mainmenu.jsp" />
        
        <jsp:include page="../inc_submenu.jsp" />
        
        <c:if test="${validUser != null}" >
            <jsp:include page="../inc_errorMessage.jsp" />
        </c:if>
        
        <div class="colmask outside">

            <div class="main">
            
                <div class="leftcolumn">
                    
                    <div id="content">
                        <br />

                        <div class="newmember">
                            <h1>Salary Cap</h1><br />
                            <img class="contentleft" src="/topdawgsports/images/tomlinson.jpg" width="300"/>
                            <div id="contenttxt">Leagues are created with a max of 49 teams.  There's no draft.  Every week, each team
                            is given $1,000,000 to spend to grab 1 QB, 2 RB, 2 WR, 1 TE, 1 PK, and 3 IDP (Individual Defensive
                            Players).<br /><br />
                            Your Fantasy Points are added up at the end of the week and compared to all the other
                            teams' fantasy points in your league.  Assuming there are 30 teams in the league, the team with the most
                            fantasy points for that week would get 30 points.  The 2nd highest team would get 29 points, and so on
                            down to the lowest scoring team of the week receiving 1 point.  For ties, all tied teams will receive the same
                            # of points.
                            </div> <!-- contenttxt -->
                        </div> <!-- contentleft -->

                        <div class="existingmember">
                            <div id="contenttxt">
                                <c:choose>
                                    <c:when test="${validUser != null}">
                                        <c:choose>
                                            <c:when test="${tds:getTeamInSeason(validUser,9) == 1}">
                                                <img src="/topdawgsports/images/icon_check.gif" />You are already signed
                                                up for this game.
                                            </c:when>
                                            <c:otherwise>
                                                <h3>SIGN UP NOW</h3>
                                                <a href="../registerGames.htm">Click here</a> to signup for this game.
                                            </c:otherwise>
                                            </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <h3>SIGN UP NOW</h3>
                                        <a href="../register.htm">Click here</a> to signup for this game.
                                    </c:otherwise>
                                </c:choose>
                            </div> <!-- contenttxt -->
                        </div> <!-- contentright -->

                        <div class="contenttop">

                            <div id="contenttxt">

                            </div> <!-- contenttxt -->
                        </div> <!-- contenttop -->

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
