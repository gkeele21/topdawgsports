<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
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
<title>TopDawgSports - Keele Fantasy index</title>

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
                            <h1>Keele Fantasy Football</h1><br />
                            <img class="contentleft" src="/topdawgsports/images/young.jpg" width="300"/>
                            <div id="contenttxt">This is our standard fantasy football game. It's a draft-mode game where all
                                    teams compete Head to Head in a league format.  We do not yet have the ability
                                to draft online, but drafts should be conducted separately.  We have a standard
                            scoring style, however each league may use its own custom scoring style.  We currently
                                offer Keeper leagues and Re-draft leagues with 8, 10, or 12 teams per league.
                            </div> <!-- contenttxt -->
                        </div> <!-- contentleft -->

                        <div class="existingmember">
                            <div id="contenttxt">
                                <c:choose>
                                    <c:when test="${validUser != null}">
                                        <c:choose>
                                            <c:when test="${tds:getTeamInSeason(validUser,14) == 1}">
                                                <img src="/coltarama/images/icon_check.gif" />You are already signed
                                                up for this game.
                                            </c:when>
                                            <c:otherwise>
                                                <%--<h3>SIGN UP NOW</h3>
                                                <a href="../registerGames.htm">Click here</a> to signup for this game.--%>
                                            </c:otherwise>
                                            </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <%--<h3>SIGN UP NOW</h3>
                                        <a href="../register.htm">Click here</a> to signup for this game.--%>
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
