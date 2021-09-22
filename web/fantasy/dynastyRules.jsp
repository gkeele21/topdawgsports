<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Dynasty Rules</title>
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

                    <h1>Dynasty League Rules</h1>

                    <h2>Fantasy League</h2>
                        <h3>Name :</h3>
                            <label>Dynasty</label><br />
                        <h3>Type :</h3>
                            <label>Franchise</label><br />
                        <h3>Owners :</h3>
                            <label>12</label><br />
                        <h3>Fee :</h3>
                            <label>$20.00</label><br />
                        <h3>Commissioner :</h3>
                            <label>Bert Keele</label><br />

                    <h2>Rule Changes :</h2>
                            <ul>
                                <li>Any rule change can be submitted and passed during the season with 100% owner approval.</li>
                                <li>Any rule change proposals for the upcoming season need to be submitted to the commissioner by a specific date set by the league commissioner.</li>
                                <li>In order for a rule change to pass, there must be a majority vote among all owners. (A tie means no change)</li>
                                <li>A neutral vote by an owner does not count as a vote at all.</li>
                            </ul>

                    <h2>Roster and Lineup</h2>
                        <h3>Roster Size :</h3><label> 25 players</label><br />
                        <h3>Position Requirements :</h3>
                            <label>An owner must have 1 QB, 2 RB’s, 3 WR’s, 1 TE, 1 K, 1 DL, 1 LB and 1 DB on their roster at all times. The rest of the roster can be composed of players at any of those positions. There is no maximum limit of how many players an owner can have at a specific position.</label><br />
                        <h3>Trades :</h3>
                            <label>Trading players between owners can be done at any time. Trades can even involve future draft picks.</label><br/>
                        <h3>Starters :</h3>
                            <label>An owner does not need to specify starters at any point during the season.  Every player on an owner’s roster is eligible to score points towards their weekly total.  Our system will automatically assign your best player(s) at each position as starters after the NFL games have been played for the week.</label><br />
                        <h3>Scoring :</h3>
                            <label>An owner’s total fantasy points are accumulated by the system and only get rewarded points from the highest scoring players at the following positions: 1 QB, 2 RB’s, 3 WR’s, 1 TE, 1 K, 1 DL, 1 LB and 1 DB.</label><br />
                        <h3>Bye Week :</h3>
                            <label>If a player has a bye on a given week, his score will result in 0 points.</label><br />
                        <h3>Injured Reserve :</h3>
                            <label>There is no IR for this league. If a player gets injured an owner must decide if they want to hold a roster spot for him or not.</label>

                    <h2>The Draft</h2>
                        <h3>Roster Announcement :</h3>
                            <label>Each owner will declare the players on their roster that they want to keep for the upcoming fantasy season.  All players are eligible to be kept.</label><br/>
                        <h3>Rounds :</h3>
                            <label>There will be as many rounds in the draft as needed. It is determined by how many players an owner decided to cut before the draft. If an owner cuts 2 players from his team, he will draft in Round 1 and Round 2. If an owner chooses to keep all of his players before the draft he will not be part of that year’s draft.</label><br/>
                        <h3>Draft Order :</h3>
                            <label>None of the rounds in this draft will serpentine.  The order is the same for each round and is determined by the results from the previous year.  All categories are based on teams that finished at the bottom in the following order: money won, total fantasy points, final record in standings.  If there is still a tie, the year previous to that will be looked at using the same criteria.</label><br/>
                        <h3>Time Limit</h3>
                            <label>2 minutes per pick</label><br />

                    <h2>Roster Changes</h2>
                        <h3>Transactions and Trades :</h3>
                            <ul>
                                <li>A transaction can be made as soon as the current year’s draft is complete up until the conclusion of the fantasy playoffs. No changes are permitted during the off-season.</li>
                                <li>There is not a limit of how many transactions an owner can make each week.</li>
                                <li>To add a player, one must be dropped first.</li>
                                <li>When a player is dropped, he cannot be picked up by any owner until the following week.</li>
                                <li>An owner may not perform any transaction involving a player that has already started playing their game on a given week.</li>
                            </ul>
                        <h3>Transaction Deadline :</h3>
                            <label>Each week, all transactions should be submitted into the system after the prior week is completed up to 10:00 AM Mountain Standard Time Thursday morning.  The system will then carry out all submitted transactions based on the transaction order (see Transaction Order).  Immediately following the processed transactions, any other transaction that a team submits for the week will be processed on a first-come first-served basis.</label><br />
                        <h3>Transaction Order :</h3>
                            <ul>
                                <li>After the draft and before the first NFL game, the transaction order will be on a first-come first-served basis.</li>
                                <li>After Week 1, transactions are always processed in an orderly fashion.  The initial order is based on the previous year’s results and follows the same criteria as the original draft order.</li>
                                <li>Anytime an owner submits a transaction by the transaction deadline (and that transaction is successfully processed), that owner moves to the bottom of the order, while every other owner shifts up one. If more than one transaction per owner occurs by the deadline, the second round of transactions will be carried out in the exact same manner, and so on.  Any transactions made after the transaction deadline is allowed without moving an owner to the bottom of the order.</li>
                            </ul>

                    <h2>Scoring</h2>
                        <h3>Passing Yards :</h3>
                            <label>Total passing yards divided by 25.</label><br />
                        <h3>Rushing Yards :</h3>
                            <label>Total rushing yards divided by 10.</label><br />
                        <h3>Receiving Yards :</h3>
                            <label>Total receiving yards divided by 10.</label><br />
                        <h3>Touchdowns :</h3>
                            <label>6 points for every TD (passing, rushing, receiving, special teams, fumble rec, int's.).</label><br />
                        <h3>Field Goals :</h3>
                            <label>Length of field goal divided by 10 (a minimum of 3 points per field goal will be given).</label><br />
                        <h3>Extra Points :</h3>
                            <label>1 point for every one kicking; 2 points for every one passing, rushing, or receiving.</label><br />
                        <h3>Solo Tackles :</h3>
                            <label>1 point for every solo tackle for an individual defensive player.</label><br />
                        <h3>Ast. Tackles :</h3>
                            <label>0.5 point for every assisted tackle for an individual defensive player.</label><br />
                        <h3>Sacks :</h3>
                            <label>3 points for an individual defensive player.</label><br />
                        <h3>Int's :</h3>
                            <label>4 points for an individual defensive player. A thrown INT on offense does not minus points.</label><br />
                        <h3>Forced Fumble :</h3>
                            <label>1 point for an individual defensive player that forced a fumble.</label><br />
                        <h3>Fumble Recovery :</h3>
                            <label>3 points for an individual defensive player that recovered a fumble.</label><br />
                        <h3>Negative Points :</h3>
                            <label>There are no negative points. If a player ends a game with negative yardage in any category (i.e. -11 yards rushing), he is given 0 points for that category.</label><br />
                        <h3>Wins and Losses :</h3>
                            <label>In a weekly head-to-head matchup, the owner with the most fantasy points wins.</label><br />

                    <h2>Schedule</h2>
                        <h3>Head-to-Head :</h3>
                            <label>The fantasy schedule created each year is determined by the owner’s team number.  The team numbers are auto-assigned, based on results from the previous year.  All categories are based on teams that finished at the top in the following order: money won, total fantasy points, final record in standings.  If there is still a tie, the year previous will be looked at using the same criteria.</label><br />

                    <h2>Playoffs</h2>
                        <h3>Duration :</h3>
                            <label>The playoffs will start beginning Week #15. Three total rounds of playoffs will be played. The championship game will be on Week #17. That will conclude the season.</label><br />
                        <h3>Format :</h3>
                            <label>The Top 8 teams with the best record in the league advance to the playoffs. Teams are ordered 1-8 and seeded into a playoff bracket based on the standings. If there is a tie in the standings between 2 teams, then a head to head matchup comparison will be the first determining factor. If there is still a tie or a tie occurs with 3 teams or more, then total fantasy points is the final determining factor.</label>

                    <h2>Payouts</h2>
                        <ul>
                            <li>Champion - $80</li>
                            <li>Championship game playoff loser - $60</li>
                            <li>Semi-final game playoff losers - $40 x 2</li>
                            <li>Highest season fantasy point getter between the 1st Round losers - $20</li>
                        </ul>

                </div> <!-- rules -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>
