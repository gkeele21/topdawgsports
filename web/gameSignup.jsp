<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="tds.constants.FSGame"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - Game Signup</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/topDawgCommon.css" media="screen" />

    <style>

#innerUserProfile table {
    text-align: left;
    margin: 50px 0px 0px 0px;
}

#innerUserProfile th {
    padding-bottom: 20px;
}

#gameSignup {
    width: 100%;
}

#gameSignup .rounded-corners{
    background-color: #FFFFB0;
}

#gameInfo {
    float: left;
    width: 55%;
    min-height: 1100px;
}

#userProfile {
    float: left;
    width: 45%;
    min-height: 1100px;
}

/* INNER DIVS */

#innerGameInfo {
    border-right: medium solid black;
    margin: 30px 0px 0px 0px;
}

#innerGameInfo h1 {
    text-align: center;
    margin: 0px 0px 0px 30px;
}

#innerGameInfo img {
    margin: 100px 0px 0px 100px;
}

#innerGameInfo p {
    font-size: 1.2em;
    margin: 50px 0px 0px 30px;
}

#innerGameInfo ul {
    margin: 75px 0px 0px 50px;
}

#innerGameInfo a{
    padding-left: 250px;
}

#innerGameInfo a:hover{
    color: #F2BC57;
}

#innerUserProfile {
    margin: 30px 0px 0px 10px;
}

.buttons {
    text-align: center;
    padding-top: 25px;
}

.signupNow  {
    margin: 200px 0px 0px 150px;
}


    </style>
  </head>

<body>
    <div id="container">

        <div id="header">
            <jsp:include page="inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="menu/mainMenu.jsp" />
        </div>

        <div id="gameSignup">
            <div id="innerGameSignup">

                 <div id="stepInfo">
                    <label class="bold">Step : </label> <label>1 of 3</label><br />
                    <c:if test="${validUser != null}" >
                        <label class="bold">Username : </label><c:out value="${validUser.username}" /><br />
                    </c:if>
                </div>

                <div id="gameInfo">
                    <div id="innerGameInfo">
                        <c:choose>
                            <c:when test="${gameSignupFSSeason != null}">
                                <c:choose>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 2}">
                                        <img src="images/football_money11.jpg"/>
                                        <h1>Salary Cap</h1>
                                        <ul>
                                            <li>$5 entry fee (General League only)</li>
                                            <li>Create your own league</li>
                                            <li>Pick a new team each week</li>
                                            <li>You're given $1,000,000 weekly to spend on your roster</li>                                            
                                            <li>No head to head play</li>
                                            <li>Compete against everyone in the league</li>
                                            <li>Top 5 finishers win (General League only)</li>
                                            <a href="sal/rules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 3}">
                                        <img src="images/football_money11.jpg"/>
                                        <h1>Player Love Em & Leave Em</h1>
                                        <ul>
                                            <li>$3 entry fee (General League only)</li>
                                            <li>Create your own league</li>
                                            <li>Pick a new team each week</li>
                                            <li>You can only pick each player once per season</li>
                                            <li>No head to head play</li>
                                            <li>Compete against everyone in the league</li>
                                            <li>Total Fantasy Points for the whole season will determine the winners</li>
                                            <li>Top 5 finishers win (General League only)</li>
                                            <a href="proloveleaveplayer/rules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 4}">
                                        <img src="images/football_money11.jpg"/>
                                        <h1>Pro Pickem</h1>
                                        <ul>
                                            <li>$3 entry fee (General League only)</li>
                                            <li>Create your own league</li>
                                            <li>Predict the winner of each Pro game</li>
                                            <li>Assign confidence point values to each prediction</li>
                                            <li>Most total points at season's end wins</li>
                                            <li>Winner takes all (General League only)</li>
                                            <a href="propickem/rules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 5}">
                                        <img src="images/football_money11.jpg"/>
                                        <h1>Pro Love Em & Leave Em</h1>
                                        <ul>
                                            <li>$3 entry fee (General League only)</li>
                                            <li>Create your own league</li>
                                            <li>Pick one weekly winner</li>
                                            <li>You can only pick a team once all year</li>
                                            <li>Score 1 point for each correct pick</li>
                                            <li>Most total correct at season's end wins</li>
                                            <li>Winner takes all (General League only)</li>
                                            <a href="proloveleave/rules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 6}">
                                        <img src="images/football_money11.jpg"/>
                                        <h1>College Love Em & Leave Em</h1>
                                        <ul>
                                            <li>$3 entry fee (General League only)</li>
                                            <li>Create your own league</li>
                                            <li>Pick two weekly winners</li>
                                            <li>You can only pick a team once all year</li>
                                            <li>Score 1 point for each correct pick</li>
                                            <li>Most total correct at season's end wins</li>
                                            <li>Winner takes all (General League only)</li>
                                            <a href="collegeloveleave/rules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 7}">
                                        <img src="images/football_money11.jpg"/>
                                        <h1>College Pickem</h1>
                                        <ul>
                                            <li>$3 entry fee (General League only)</li>
                                            <li>Create your own league</li>
                                            <li>Predict the winner of each matchup involving a Top 25 team</li>
                                            <li>Assign confidence point values to each prediction</li>
                                            <li>Most total correct at season's end wins</li>
                                            <li>Winner takes all (General League only)</li>
                                            <a href="collegepickem/rules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 8}">
                                        <img src="images/march_madness.jpg"/>
                                        <h1>Bracket Challenge</h1>
                                        <ul>
                                            <li>$5 entry fee (General League only)</li>
                                            <li>Create as many entries as you want</li>
                                            <li>Entries must be completed by the start of the tournament</li>
                                            <li>Create your own league</li>
                                            <li>Predict the winner of each game for the entire college tournament</li>
                                            <li>Earn points for each correct pick</li>
                                            <li>Points increase with each round of the tournament</li>
                                            <li>Most total accumulated points at tournament's end wins</li>
                                            <li>Winner takes all (General League only)</li>
                                            <a href="mm/bracketChallengeRules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 9}">
                                        <img src="images/march_madness.jpg"/>
                                        <h1>Seed Challenge</h1>
                                        <ul>
                                            <li>$5 entry fee (General League only)</li>
                                            <li>Create as many entries as you want</li>
                                            <li>Entries must be completed by the start of the tournament</li>
                                            <li>Create your own league</li>
                                            <li>Choose 8 different teams that belong to different seed groups</li>
                                            <li>Win by selecting the college tournament winner</li>
                                            <li>If multiple people have the tournament winner, comparisons are made with their teams that went the next furthest in the tournament</li>
                                            <li>Winner takes all (General League only)</li>
                                            <a href="mm/seedChallengeRules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 10}">
                                        <img src="images/tourney1.bmp"/>
                                        <h1>Fantasy Football Playoff</h1>                                        
                                        <ul>
                                            <li>Compete for FREE to win a $50 PF Changs gift card</li>
                                            <li>You're given $1,000,000 weekly to spend on your roster</li>
                                            <li>Beat your single opponent each week to stay alive</li>
                                            <li>Half the tournament field is eliminated each week</li>
                                            <li>Win every game to become the last man standing and the ultimate Top Dawg</li>
                                            <a href="playoff/rules.htm">Click HERE to learn more</a>
                                        </ul>
                                    </c:when>
                                    <c:when test="${gameSignupFSSeason.FSGameID == 12}">
                                        <img src="images/mastersLogo.jpg"/>
                                        <h1>Fantasy Golf</h1>                                        
                                        <ul>
                                            <li>$10 entry fee (General League only)</li>
                                            <li>Create your own league</li>
                                            <li>Pick new golfers each week</li>
                                            <li>You're given $1,000,000 weekly to spend on your roster</li>                                            
                                            <li>Season lasts for 12 consecutive weeks</li>
                                            <li>Compete against everyone in the league</li>
                                            <li>Top 2 finishers win (General League only)</li>
                                        </ul>
                                    </c:when>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <h1>Please select a valid game to sign up for.</h1>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div id="userProfile">
                    <div id="innerUserProfile">
                        
                        <jsp:include page="inc_errorMessage.jsp" />                       
                        
                        <c:choose>
                            <c:when test="${validUser == null}" >
                                <form action="/topdawgsports/login.do?nextURL=createTeam.htm&origURL=gameSignup.htm" method="post">
                                    <table>
                                        <th colspan="2">Login to create a team</th>
                                        <tr>
                                            <td>Username:</td>
                                            <td><input class="rounded-corners" type="text" name="txtUserName" /></td>
                                        </tr>
                                        <tr>
                                            <td>Password:</td>
                                            <td><input class="rounded-corners" type="password" name="txtPassword" /></td>
                                        </tr>
                                        <tr>
                                            <td class="buttons" colspan="2"><input type="image" src="images/login.png"/></td>
                                        </tr>
                                    </table>
                                </form>
                                <form action="/topdawgsports/register.do?origURLgameSignup.htm&nextURL=createTeam.htm" method="post">
                                    <table>
                                        <th colspan="2">Create a profile if you don't have a login</th>
                                        <tr>
                                            <td>* First Name:</td>
                                            <td><input class="rounded-corners" type="text" name="frmFirstName" /></td>
                                        </tr>
                                        <tr>
                                            <td>* Last Name:</td>
                                            <td><input class="rounded-corners" type="text" name="frmLastName" /></td>
                                        </tr>
                                        <tr>
                                            <td>* Username:</td>
                                            <td><input class="rounded-corners" type="text" name="frmUsername" /></td>
                                        </tr>
                                        <tr>
                                            <td>* Password:</td>
                                            <td><input class="rounded-corners" type="password" name="frmPassword" /></td>
                                        </tr>
                                        <tr>
                                            <td>* Confirm Password:</td>
                                            <td><input class="rounded-corners" type="password" name="frmPassword2" /></td>
                                        </tr>
                                        <tr>
                                            <td>* Email:</td>
                                            <td><input class="rounded-corners" type="text" name="frmEmail" /></td>
                                        </tr>
                                        <tr>
                                            <td>Zip Code</td>
                                            <td><input class="rounded-corners" type="text" name="frmZip" /></td>
                                        </tr>
                                        <tr>
                                            <td class="buttons" colspan="2"><input type="image" src="images/createProfile.png"/></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><br />* denotes a required field</td>
                                        </tr>

                                    </table>
                                </form>
                            </c:when>
                            <c:otherwise>
                                    <tr><a href="createTeam.htm"><img class="signupNow" src="images/signUp.png"/></a></tr>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
            </div>
        </div><!-- siteImage -->
    </div> <!-- container -->

</body>
</html>
