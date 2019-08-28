<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Salary Cap Eliminator Rules</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <style>
        #rules table { border: 1px solid black; border-collapse: separate; border-spacing: 3px; margin-left: 100px; text-align: center; width: auto; }
        .example { color: darkred; font-size: .8em; }
    </style>
  </head>

  <body>
    <div id="container">
        <div id="header"><jsp:include page="../inc_header.jsp" /></div>
        <div id="mainMenu"><jsp:include page="../menu/mainMenu.jsp" /></div>
        <div id="leftMenu"><div id="innerLeftMenu"><jsp:include page="../menu/inc_leftMenu.jsp" /></div></div>

        <div id="content">
            <div id="innerContent">

                <div id="rules">

                    <h1>Salary Cap Eliminator Rules</h1>

                    <h3>Game Play</h3>                    
                        <h4>Overview</h4>
                            <p>In this game, an owner selects NFL players on a weekly basis to compete against everyone in the league.  Each NFL player is given a value of how much they cost to pick.  An owner is given $1,000,000 each week to spend on their entire roster.  Points are awarded to each owner based on the order of how they finished compared to every other owner within the league.  Once an owner has picked a player, they may not pick that player again for the rest of the season.</p>
                        <h4>Leagues</h4>
                            <p>An owner can join or create their own leagues.  A General league will be created for all owners wishing to compete against everyone in the public.</p>
                        <h4>Cost</h4>
                            <p>$5 per team (General league only).  Free for all custom leagues.</p>

                    <h3>Roster</h3>
                        <h4>Overview</h4>
                            <p>There is no drafting of players at the beginning of the season.  Each owner picks players on a weekly basis to comprise their roster.  Each NFL player is given a dollar amount and that is the value of what they cost to pick.</p>
                        <h4>Lineup</h4>
                            <p>An owner's roster consists of 1 Quarterback, 2 Running Backs, 2 Wide Receivers, 1 Tight End, and 1 Kicker.</p>
                        <h4>Deadline</h4>
                            <p>Players can be selected up until their game starts on a given week when a roster slot is available.</p>
                        <h4>Player Values</h4>
                            <p>After the 3rd week of the season, each NFL player’s value might change on a weekly basis based on how well they are performing.  For the first 3 weeks the values are assigned based on yearly projections.</p>
                        <h4>Regulations</h4>
                            <ul>
                                <li>An owner must log in to the site and fill out their roster every week.</li>
                                <li>An owner’s roster will not carry over from week to week. </li>
                                <li>A weekly roster may not exceed the $1,000,000 cap.</li>
                                <li>An incomplete roster will result in 0 total fantasy points for that week.</li>
                            </ul>
                        <h4>Notes</h4>
                            <p>It is more than likely that multiple owners will have the same NFL player on their roster for a given week.</p>

                    <h3>Player Scoring</h3>
                        <h4>Passing Yards</h4>
                            <p>Total passing yards divided by 25.</p>
                        <h4>Rushing Yards</h4>
                            <p>Total rushing yards divided by 10.</p>
                        <h4>Receiving Yards</h4>
                            <p>Total receiving yards divided by 10.</p>
                        <h4>Touchdowns</h4>
                            <p>6 points for every TD (passing, rushing, receiving, special teams, etc.).</p>
                        <h4>Field Goals</h4>
                            <p>Length of field goal divided by 10 (minimum of 3 points per field goal).</p>
                        <h4>Extra Points</h4>
                            <p>1 point for kicking; 2 points for passing, rushing, or receiving.</p>
                        <h4>Negative Points</h4>
                            <p>Negative points are split into 2 different categories, Interceptions and Fumbles Lost.  In each category you -1 point for the first occurrence, -2 points for the second occurrence and so forth.  (For example, if a player throws 3 interceptions and loses 1 fumble, he would minus a total of 7 points.  6 for the interceptions -1 + -2 + -3 and -1 for the fumble lost.</p>
                        <h4>Regulations</h4>
                            <ul>
                                <li>All fantasy points are not rounded to the nearest whole number.</li>
                                <li>Fantasy points are calculated to two decimal places.</li>
                                <li>If a player has negative yardage or negative fantasy points, this will result in a 0.</li>
                            </ul>

                    <h3>Owner Scoring</h3>
                        <h4>Overview</h4>
                            <p>An owner’s total fantasy points for a given week is the accumulation of all player’s fantasy points from their completed roster.  After totaling all fantasy points, each team in the league is given a certain amount of game points.  The game points are determined by the order in which all teams in the league finished on the week.  The owner with the most fantasy points is awarded the number of points equal to the number of teams in the league.  (For example, if a league has 28 teams in it, then the team with the most Fantasy Points each week will receive 28 points. The team with the 2nd highest Fantasy Points total will get 27 points, and so on down to 1 point for the team that had the fewest Fantasy Points for that week)</p>
                        <h4>Wins and Losses</h4>
                        <p>There are no wins and losses since there is no head to head matchups.</p>

                    <h3>Season</h3>
                        <h4>Duration</h4>
                            <p>The fantasy season begins with NFL’s week 1 and ends after week 17.</p>
                        <h4>Playoffs</h4>
                            <p>There are no playoffs for this game.</p>

                    <h3>Payoffs</h3>
                        <h4>Overview</h4>
                            <p>Game points accumulated for the entire season is what determines the winners, not the total fantasy points.</p>
                        <h4>Payouts</h4>
                            <p>Payouts are for the General league only <label class="example">($ amount figures in red are example scenarios based on specified teams)</label></p>
                            <table border="1">
                                <tr>
                                    <th>Place</th>
                                    <th>1-9 Teams</th>
                                    <th>10-19 Teams</th>
                                    <th>20-29 Teams</th>
                                    <th>30+ Teams</th>                                    
                                </tr>
                                <tr>
                                    <td>1st</td>
                                    <td>65% <label class="example">($16.25)</label></td>
                                    <td>55% <label class="example">($41.25)</label></td>
                                    <td>50% <label class="example">($62.5)</label></td>
                                    <td>45% <label class="example">($78.75)</label></td>
                                </tr>
                                <tr>
                                    <td>2nd</td>
                                    <td>35% <label class="example">($8.75)</label></td>
                                    <td>27% <label class="example">($20.25)</label></td>
                                    <td>25% <label class="example">($31.25)</td>
                                    <td>25% <label class="example">($43.75)</td>
                                </tr>
                                <tr>
                                    <td>3rd</td>
                                    <td>-</td>
                                    <td>18% <label class="example">($13.5)</label></td>
                                    <td>16% <label class="example">($20)</label></td>
                                    <td>16% <label class="example">($28)</label></td>
                                </tr>
                                <tr>
                                    <td>4th</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>9% <label class="example">($11.25)</label></td>
                                    <td>9% <label class="example">($15.75)</label></td>
                                </tr>
                                <tr>
                                    <td>5th</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>5% <label class="example">($8.75)</label></td>
                                </tr>
                                <tr style="font-weight: bold; color:darkred; ">
                                    <td></td>
                                    <td>(5 teams - $25)</td>
                                    <td>(15 teams - $75)</td>
                                    <td>(25 teams - $125)</td>
                                    <td>(35 teams - $175)</td>
                                </tr>
                            </table>
                        <h4>Ties</h4>
                            <ul>
                                <li>A tie in Game Points will be broken by looking at the total number of fantasy points accumulated for the entire season.</li>
                                <li>If a tie still exists, the winnings will be split evenly amongst tied teams.</li>
                            </ul>

                    <h3>Help</h3>
                        <p>If you come across any problems or errors, please help out by sending us an email. We really appreciate any extra help we can get in adding to the quality of our product. Also, any feedback you can give us or ways to improve the site would sure be welcomed.</p>

                </div> <!-- rules -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>