<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - Front Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/topDawgCommon.css" media="screen" />

    <style>

#siteImage {
    float: left;
    border-right: 3px solid black;
    width: 590px;
}

#innerSiteImage {
    margin: 20px 0px 20px 7px;
}

#promoGame {
    float: left;
    width: 397px;
}

#innerPromoGame {
    text-align: center;
}

#innerPromoGame a {
    display: inline;
    color: #103B40;
    text-decoration: underline;
    vertical-align: top;
}

#innerPromoGame a:hover {
    color: #1C5953;
    cursor: pointer;
}

#innerPromoGame img {
    height: 90px;
}

#innerPromoGame p {
    padding: 20px 20px 20px 20px;
    text-align: left;
}

#innerPromoGame ul {
    padding-left: 30px;
    text-align: left;
}

.gameSeparation {
    float:left;
    border-bottom: medium solid black;
    width: 100%;
    margin-top: 20px;
}

#gameBuckets img{
    height: 60px;
}

#gameBuckets li{
    text-align: left;
}

#gameBuckets ul{
    padding-left: 25px;
}

#gameBuckets a {
    display: inline;
    color: #103B40;
    text-decoration: underline;
    vertical-align: top;
}

#gameBuckets a:hover {
    color: #1C5953;
    cursor: pointer;
}

#bucketGame1, #bucketGame2, #bucketGame3, #bucketGame4 {
    float: left;
    width: 33%;
}

#innerBucketGame1, #innerBucketGame2, #innerBucketGame3, #innerBucketGame4  {
    border-left: medium solid black;
    text-align: center;
}

#innerBucketGame1 {
    border-left: none;
}

    </style>
  </head>

<body>
    <div id="container">

        <div id="header">
            <jsp:include page="inc_header.jsp" />
        </div>

            <jsp:include page="inc_errorMessage.jsp" />

        <div id="mainMenu">
            <jsp:include page="menu/mainMenu.jsp" />
        </div>

        <div id="siteImage">
            <div id="innerSiteImage">
                <img src="images/byustadium.jpg" />
            </div>
        </div>

        <div id="promoGame">
            <div id="innerPromoGame">
                <h2>Fantasy Golf</h2>
                <img src="images/mastersLogo.jpg"/>
                <a href="gameSignup.htm?fsGameId=12">Sign Up</a>
                <ul>
                    <li>Pick up to 6 golfers each week</li>
                    <li>Given $1,000,000 to buy your roster</li>
                    <li>New game every week - no more season running totals</li>
                    <li>Compete against everyone in the league every week</li>
                    <li>Game points awarded based on total money earnings from picked players</li>
                    <li>Create private leagues or compete for prizes in the General league</li>
                </ul>
            </div>
        </div>

        <div class="gameSeparation"></div>

        <div id="gameBuckets">
            <div id="bucketGame1">
                <div id="innerBucketGame1">
                    <h3>Salary Cap</h3>
                    <img src="images/football_money11.jpg"/>
                    <a href="gameSignup.htm?fsGameId=2">Sign Up</a>
                    <ul>
                        <li>Pick a new team each week</li>
                        <li>Given $1,000,000 to buy your roster</li>
                        <li>Player values determined by performance</li>
                        <li>No head to head competition</li>
                        <li>Compete against everyone in the league</li>
                        <li>Game points awarded based on weekly finish amongst all league members</li>
                        <li>Create private leagues or compete for prizes in the General league</li>
                    </ul>

                </div>
            </div> <!-- bucketGame1 -->

            <div id="bucketGame2">
                <div id="innerBucketGame2">
                    <h3>Pro Pickem</h3>
                    <img src="images/football_money11.jpg"/>
                    <a href="gameSignup.htm?fsGameId=4">Sign Up</a>
                    <ul>
                        <li>Predict the winner for each Pro game</li>
                        <li>Assign point values to each prediction</li>
                    </ul>

                    <div class="gameSeparation"></div>

                    <h3>College Pickem</h3>
                    <img src="images/football_money11.jpg"/>
                    <a href="gameSignup.htm?fsGameId=7">Sign Up</a>
                    <ul>
                        <li>Predict the winner of all Top 25 games</li>
                        <li>Assign point values to each prediction</li>
                    </ul>
                </div>
            </div> <!-- bucketGame2 -->

            <div id="bucketGame3">
                <div id="innerBucketGame3">
                    <h3>Pro Love Em & Leave Em</h3>
                    <img src="images/football_money11.jpg"/>
                    <a href="gameSignup.htm?fsGameId=5">Sign Up</a>
                    <ul>
                        <li>Pick one weekly winner</li>
                        <li>Can only pick a team once</li>
                    </ul>

                    <div class="gameSeparation"></div>

                    <h3>College Love Em & Leave Em</h3>
                    <img src="images/football_money11.jpg"/>
                    <a href="gameSignup.htm?fsGameId=6">Sign Up</a>
                    <ul>
                        <li>Pick two weekly winners</li>
                        <li>Can only pick a team once</li>
                    </ul>
                </div>
            </div> <!-- bucketGame3 -->
        </div>
    </div> <!-- container -->
    <p>v1.0.4</p>
</body>
</html>

<%--

        <div id="promoGame">
            <div id="innerPromoGame">
                <h2>March Madness</h2>
                <img src="images/march_madness.jpg" alt=""/>
                <h3>Bracket Challenge</h3>
                <a href="gameSignup.htm?fsGameId=8">Sign Up!</a>
                <ul>
                    <li>Fill out the entire tournament bracket</li>
                    <li>Earn points for every correct pick</li>
                    <li>Points increase with each round of the tournament</li>
                </ul>

                <h3>Seed Challenge</h3>
                <a href="gameSignup.htm?fsGameId=9">Sign Up!</a>
                <ul>
                    <li>Choose 8 different tournament teams</li>
                    <li>Each team is represented in a range of seed groups</li>
                    <li>Win by selecting the College tournament winner</li>
                </ul>
            </div>

        </div>

        <div id="promoGame">
            <div id="innerPromoGame">
                <h2>FF Playoff</h2>
                <img src="images/tourney3.bmp"/>
                <a href="gameSignup.htm?fsGameId=10">Sign Up</a>
                <p> The format of this game is one gigantic playoff bracket.  You pick a brand new team each week and compete
                    against one other individual.  The winner moves on and the loser is out of the competition.</p>
                <ul>
                    <li><b>Signup deadline is Sat. Nov. 8th at 11:59 p.m.</b></li>
                    <li>Compete for FREE to win a $50 PF Changs gift card </li>
                    <li>Even newbies to fantasy football can do well</li>
                    <li>Create multiple teams to increase your odds</li>
                </ul>

            </div>
        </div>


        <div id="promoGame">
            <div id="innerPromoGame">
                <h2>March Madness</h2>
                <img src="images/march_madness.jpg" alt=""/>
                <h3>Bracket Challenge</h3>
                <a href="gameSignup.htm?fsGameId=8">Sign Up!</a>
                <ul>
                    <li>Fill out the entire tournament bracket</li>
                    <li>Earn points for every correct pick</li>
                    <li>Points increase with each round of the tournament</li>
                </ul>

                <h3>Seed Challenge</h3>
                <a href="gameSignup.htm?fsGameId=9">Sign Up!</a>
                <ul>
                    <li>Choose 8 different tournament teams</li>
                    <li>Each team is represented in a range of seed groups</li>
                    <li>Win by selecting the College tournament winner</li>
                </ul>
            </div>
        </div>





                <div id="innerBucketGame1">
                    <h3>Salary Cap</h3>
                    <img src="images/football_money11.jpg"/>
                    <a href="gameSignup.htm?fsGameId=2">Sign Up</a>
                    <ul>
                        <li>Pick a new team weekly given $1,000,000</li>
                        <li>Compete against everyone in the league</li>
                    </ul>

                    <div class="gameSeparation"></div>

                    <h3>FF Playoff</h3>
                    <img src="images/tourney3.bmp"/>
                    <a href="gameSignup.htm?fsGameId=10">Sign Up</a>
                    <ul>
                        <li>Pick a new team each week</li>
                        <li>One big playoff where half of the teams are eliminated weekly</li>
                    </ul>
                </div>

        <div id="promoGame">
            <div id="innerPromoGame">
                <h2>Player Love Em & Leave Em</h2>
                <img src="images/football_money11.jpg"/>
                <a href="gameSignup.htm?fsGameId=3">Sign Up</a>
                <ul>
                    <li>Pick a new team each week</li>
                    <li>Can only pick each player once the whole season</li>
                    <li>No head to head competition</li>
                    <li>Compete against everyone in the league</li>
                    <li>Total Fantasy Points for the whole season will determine the winners</li>
                    <li>Create private leagues or compete for prizes in the General league</li>
                </ul>
            </div>
        </div>

        <div id="promoGame">
            <div id="innerPromoGame">
                <h2>Fantasy Golf</h2>
                <img src="images/mastersLogo.jpg"/>
                <a href="gameSignup.htm?fsGameId=12">Sign Up</a>
                <ul>
                    <li>Pick up to 6 golfers each week</li>
                    <li>Given $1,000,000 to buy your roster</li>
                    <li>New game every week - no more season running totals</li>
                    <li>Compete against everyone in the league every week</li>
                    <li>Game points awarded based on total money earnings from picked players</li>
                    <li>Create private leagues or compete for prizes in the General league</li>
                </ul>
            </div>
        </div>

--%>
