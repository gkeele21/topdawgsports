<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - FF Playoff Rules</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
  </head>

  <body>
    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div>

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

                <div id="rules">

                    <h1>Playoff Rules</h1>

                    <h2>Game Play</h2>
                        <h3>Overview :</h3>
                            <label>In this game, an owner selects Pro players on a weekly basis to compete against one opponent.  Each Pro player is given a value of how much they cost to pick.  An owner is given $1,000,000 each week to spend on their entire roster.  The team with the most points moves on to the next round.  The loser is out.</label><br />
                        <h3>Leagues :</h3>
                            <label>An owner can join or create their own leagues.  A General league will be created for a chance to become the ultimate Top Dawg.</label><br />
                        <h3>Cost :</h3>
                            <label>FREE</label><br />
                            <!--<label>$2 for 1 team, $5 for 3 teams (General League only)</label><br />-->

                    <h2>Season</h2>
                        <h3>Duration :</h3>
                            <label>The playoff will begin on the 10th week of the NFL season.  At 11:59 p.m on Saturday November 8th, all teams will be placed in the bracket.</label><br />

                    <h2>Roster</h2>
                        <h3>Overview :</h3>
                            <label>There is no drafting of players at the beginning of the season.  Each owner picks players on a weekly basis to comprise their roster.  Each Pro player is given a dollar amount and that is the value of what they cost to pick.</label><br />
                        <h3>Lineup :</h3>
                            <label>An owner's roster consists of 1 Quarterback, 2 Running Backs, 2 Wide Receivers, 1 Tight End, and 1 Kicker.</label><br />
                        <h3>Deadline :</h3>
                            <label>Players can be selected up until the first Sunday game starts on a given week.</label><br />
                        <h3>Player Values :</h3>
                            <label>Each week of the season, a Pro player’s value might change on a weekly basis based on how well they are performing.</label><br />
                        <h3>Regulations :</h3>
                            <ul>
                                <li>An owner must log in to the site and fill out their roster every week.</li>
                                <li>An owner’s roster will not carry over from week to week. </li>
                                <li>A weekly roster may not exceed the $1,000,000 cap.</li>
                                <li>An incomplete roster will result in 0 total fantasy points for that week and result in a loss.</li>
                            </ul>
                        <h3>Notes :</h3>
                            <label>It is more than likely that multiple owners will have the same Pro player on their roster for a given week.</label><br />

                    <h2>Scoring</h2>
                        <h3>Passing Yards :</h3>
                            <label>Total passing yards divided by 25.</label><br />
                        <h3>Rushing Yards :</h3>
                            <label>Total rushing yards divided by 10.</label><br />
                        <h3>Receiving Yards :</h3>
                            <label>Total receiving yards divided by 10.</label><br />
                        <h3>Touchdowns :</h3>
                            <label>6 points for every TD (passing, rushing, receiving, special teams, fumble rec, etc.).</label><br />
                        <h3>Field Goals :</h3>
                            <label>Length of field goal divided by 10 (a minimum of 3 points per field goal will be given).</label><br />
                        <h3>Extra Points :</h3>
                            <label>1 point for every one kicking; 2 points for every one passing, rushing, or receiving.</label><br />
                        <h3>Negative Points :</h3>
                            <label>Negative points are split into 2 different categories, Interceptions and Fumbles Lost.  In each category you -1 point for the first occurrence, -2 points for the second occurrence and so forth.  (For example, if a player throws 3 interceptions and loses 1 fumble, he would minus a total of 7 points.  6 for the interceptions -1 + -2 + -3 and -1 for the fumble lost.</label><br />
                        <h3>Regulations :</h3>
                            <ul>
                                <li>All fantasy points are not rounded to the nearest whole number.</li>
                                <li>Fantasy points are calculated to two decimal places.</li>
                                <li>If a player has negative yardage or negative fantasy points, this will result in a 0.</li>
                            </ul>
                        <h3>Game Matchup :</h3>
                            <label>An owner’s total fantasy points for a given week is the accumulation of all player’s fantasy points from their completed roster.  After totaling all fantasy points, the owner with the most points advances to the next round of the playoffs.</label><br />

                    <h2>Payoffs :</h2>
                        <h3>Grand Prize :</h3>
                            <label>The champion will receive a $50 gift card to PF Changs.</label><br />

                    <h2>Help</h2>
                        <label>If you come across any problems or errors, please help out by sending us an email. We really appreciate any extra help we can get in adding to the quality of our product. Also, any feedback you can give us or ways to improve the site would sure be welcomed.</label><br />
                    
                </div> <!-- rules -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>