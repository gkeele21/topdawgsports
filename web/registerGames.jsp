<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="tds.main.control.registerAction"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<un:useConstants var="fields" className="tds.control.registerAction" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="css/styles.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/registration.css" media="screen" />
<title>TopDawgSports - register for Games</title>

</head>

<body>
    <div id="container">
        
        <jsp:include page="inc_header.jsp" />

        <jsp:include page="inc_mainmenu.jsp" />
        
        <jsp:include page="inc_errorMessage.jsp" />
        
        <div class="colmask outside">

            <div class="main">
            
                <div class="leftcolumn">
                    
                    <div id="content">
						
                        <br />
                        <h1>TopDawgSports Game Registration</h1>
                        <br />
                        <div class="regform">
                        	<div class="newmember">
                                <div class="regGamesTitle">Register for as many games as you want.  After you've signed up, you will receive an email from our Commissioner,
                                 Bert Keele, so you can arrange to give him the money.</div>
                                 <br />
                                <form action="registerGames.do" method="post">
                                    <table border="0" cellspacing="5" cellpadding="0" width="100%">
                                        <tr>
                                            <td width="5%"></td>
                                            <td width="7%"></td>
                                            <td width="35%"></td>
                                            <td width="53%"></td>
                                        </tr>
                                        <tr>
                                            <td align="left" valign="top">
                                                <c:choose>
                                                    <c:when test="${tds:getTeamInSeason(validUser,15) == 1}">
                                                        <img src="/topdawgsports/images/icon_check.gif" /><br />
                                                        <span class="regGamesDesc">Already playing</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="salarycap" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td valign="top" class="blue">$5 - </td>
                                            <td valign="top" class="blue">Salary Cap</td>
                                            <td valign="top" class="regGamesDesc">A salary cap fantasy football game
                                            where you pick your roster every week from a $1,000,000 salary.  You pick players from several
                                            positions to get the most fantasy points possible.</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><img src="/topdawgsports/images/spacer.gif" height="10" /></td>
                                        </tr>
                                        <tr>
                                            <td align="left" valign="top">
                                                <c:choose>
                                                    <c:when test="${tds:getTeamInSeason(validUser,17) == 1}">
                                                        <img src="/topdawgsports/images/icon_check.gif" /><br />
                                                        <span class="regGamesDesc">Already playing</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="nflloveem" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td valign="top" class="blue">$2 - </td>
                                            <td valign="top" class="blue">Pro Love Em and Leave Em</td>
                                            <td valign="top" class="regGamesDesc">Each week, pick the winner of the
                                            Pro games.  Once you've picked a team, you cannot pick that team again the rest of the
                                            season.  Whoever misses the least amount of games, wins.</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><img src="/topdawgsports/images/spacer.gif" height="10" /></td>
                                        </tr>
                                        <tr>
                                            <td align="left" valign="top">
                                                <c:choose>
                                                    <c:when test="${tds:getTeamInSeason(validUser,18) == 1}">
                                                        <img src="/topdawgsports/images/icon_check.gif" /><br />
                                                        <span class="regGamesDesc">Already playing</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="collegeloveem" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td valign="top" class="blue">$2 - </td>
                                            <td valign="top" class="blue">College Love Em and Leave Em</td>
                                            <td valign="top" class="regGamesDesc">Each week, pick 2 winners of some
                                            of the college football games (games with the AP Top 25 teams).  Once you've picked a team, you cannot pick that team again the rest of the
                                            season.  Whoever misses the least amount of games, wins.</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><img src="/topdawgsports/images/spacer.gif" height="10" /></td>
                                        </tr>
                                        <tr>
                                            <td align="left" valign="top">
                                                <c:choose>
                                                    <c:when test="${tds:getTeamInSeason(validUser,16) == 1}">
                                                        <img src="/topdawgsports/images/icon_check.gif" /><br />
                                                        <span class="regGamesDesc">Already playing</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="nflpickem" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td valign="top" class="blue">$2 - </td>
                                            <td valign="top" class="blue">Pro Pickem</td>
                                            <td valign="top" class="regGamesDesc">Each week, pick the winner of every
                                            Pro game.  Track your results and compare your percentage with everyone else's.</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><img src="/topdawgsports/images/spacer.gif" height="10" /></td>
                                        </tr>
                                        <tr>
                                            <td align="left" valign="top">
                                                <c:choose>
                                                    <c:when test="${tds:getTeamInSeason(validUser,19) == 1}">
                                                        <img src="/topdawgsports/images/icon_check.gif" /><br />
                                                        <span class="regGamesDesc">Already playing</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="ncaapickem" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td valign="top" class="blue">$2 - </td>
                                            <td valign="top" class="blue">College Pickem</td>
                                            <td valign="top" class="regGamesDesc">Each week, pick the winner of every
                                            College game (that includes a top 25 team).  Track your results and compare your percentage with everyone else's.</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" align="center"><input type="submit" name="frmSubmit" value="Submit"/></td>
                                        </tr>
                                    </table>
                                </form>
                            </div> <!-- newmember -->
                            
                            <div class="existingmember">
                            	<div class="emailCasey">You will pay the entry fees to Bert Keele after signing up.<br /><br />
                                You can reach Bert by : <br />
                                <br />
                                &#160;&#160;&#160;&#160; Email : bertkeele@gmail.com<br />
                                &#160;&#160;&#160;&#160; Phone : 801-830-2422</div>
                            </div> <!-- existingmember -->
                            
                        </div> <!-- regform -->
                        
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
