<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Keeper Rules</title>
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

                    <h1>Keeper League Rules</h1>

                    <h2>Fantasy League</h2>
                        <h3>Name :</h3>
                            <label>Keeper</label><br />
                        <h3>Type :</h3>
                            <label>Franchise</label><br />
                        <h3>Owners :</h3>
                            <label>8</label><br />
                        <h3>Fee :</h3>
                            <label>$25.00</label><br />
                        <h3>Commissioner :</h3>
                            <label>Bert Keele</label><br />
                        <h3>Rule Changes :</h3>
                            <ul>
                                <li>Any rule change can be submitted and passed during the season with 100% owner approval.</li>
                                <li>Any rule change proposals for the upcoming season need to be submitted to the commissioner by a specific date set by the league commissioner.</li>
                                <li>In order for a rule change to pass, there must be a majority vote among all owners. (A tie means no change)</li>
                                <li>A neutral vote by an owner does not count as a vote at all.</li>
                            </ul>                    
                    <h2>Roster Requirements</h2>
                        <h3>Minimum Requirements :</h3>
                            <label>An owner must always have 1 QB, 2 RB’s, 3 WR’s, and 1 K, this includes the duration of the entire season as well as after the Pre-draft Roster cuts.</label><br />
                        <h3>Backup Positions :</h3>
                            <label>There are no requirements for the backup positions.  After fulfilling the minimum requirements, an owner can comprise the remaining roster spots from players at any position.</label><br />
                    <h2>Pre-Draft Roster</h2>
                        <h3>Cut Deadline :</h3>
                            <label>The Monday before draft day.</label><br/>
                        <h3>Roster Size :</h3>
                            <label>13</label><br/>
                        <h3>Regulations :</h3>
                            <label>Each owner will cut their roster down to the stated roster size.  Some owners may end up cutting a few more players than other owners; this all depends on how many players an owner had on the Injured Reserve list by the end of the prior season (see Injured Reserve).</label><br/>
                        <h3>Trades :</h3>
                            <label>After the pre-draft roster is set, you may trade players or draft picks with other owners up until the draft.</label><br/>
                    <h2>The Draft</h2>
                        <h3>Roster Announcement :</h3>
                            <label>Each owner will declare the players from their Pre-draft Roster (see Pre-Draft Roster) that they want to keep for the upcoming fantasy season.  All players can be kept.</label><br/>
                        <h3>Rounds :</h3>
                            <label>3 Rounds + any additional rounds (see Additional Rounds).</label><br/>
                        <h3>Additional Rounds :</h3>
                            <label>An owner will gain additional rounds in the draft to complete their roster for as many roster spots that they have unfilled. These additional rounds will be given at the end of the regular draft rounds and the same draft order will be maintained.</label><br/>
                        <h3>Draft Order :</h3>
                            <label>None of the rounds in this draft will serpentine.  The order is the same for each round and is determined by the results from the previous year.  All categories are based on teams that finished at the bottom in the following order: money won, total fantasy points.  If there is still a tie, the year previous to that will be looked at using the same criteria.</label><br/>                           
                        <h3>Regulations :</h3>
                            <label>You may not draft a player that is already on the NFL’s Injured Reserve list.</label><br />
                        <h3>Final Roster Size :</h3>
                            <label>16</label><br />
                        <h3>Time Limit</h3>
                            <label>2 minutes per pick</label><br />
                    <h2>Roster Changes</h2>
                        <h3>Transactions and Trades :</h3>
                            <ul>
                                <li>A transaction can be made on any week throughout the duration of the season.</li>
                                <li>Trades for future draft picks are allowed throughout the entire season once teams have established their Pre-Draft Roster (see Pre-Draft Roster).</li>
                                <li>Transactions are not allowed during the off-season.</li>
                                <li>There is not a limit of how many transactions an owner can make each week.</li>
                                <li>To add a player, one must be dropped first.</li>
                                <li>When a player is dropped, he cannot be picked up by any owner until the following week.</li>
                                <li>An owner may not perform any transaction involving a player that has already started playing their game on a given week.</li>
                                <li>An owner may not pickup a player that is already on the NFL’s IR list.</li>
                                <li>There is a limit of 3 trades per year between two owners.</li>
                            </ul>
                        <h3>Transaction Deadline :</h3>
                            <label>Each week, all transactions should be submitted into the system from Sunday up to 10:00 AM MST Thursday morning.  The system will then carry out all submitted transactions based on the transaction order (see Transaction Order). Immediately following the processed transactions, all other submitted transactions for the week will be processed on a first-come first-served basis.</label><br />
                        <h3>Transaction Order :</h3>
                            <ul>
                                <li>After the draft and before the first NFL game, the transaction order will be on a first-come first-served basis.</li>
                                <li>After Week 1, transactions are always processed in an orderly fashion.  The initial order is based on the previous year’s results and follows the same criteria as the original draft order.</li>
                                <li>Anytime an owner makes a transaction on a given week by the transaction deadline, that owner moves to the bottom of the order, while every other owner shifts up one. If more than one transaction per owner occurs by the deadline, the second round of transactions will be carried out in the exact same manner, and so on.  Transactions made after the transaction deadline is allowed without moving an owner to the bottom of the order until an owner has made 3 transactions.  Once 3 transactions occur then they will be placed at the bottom of the order.</li>
                            </ul>
                    <h2>Injured Reserve</h2>
                        <h3>Regulations :</h3>
                            <ul>
                                <li>A player that is injured can be placed on IR but must stay on IR for at least 8 weeks.</li>
                                <li>As soon as a player plays in a game after the 8 weeks are up, the owner must make a decision the following week. The owner can take the player off of IR and drop somebody else OR he must drop that player completely off his team.</li>
                                <li>There is no limit of how many players an owner can place on IR.</li>
                                <li>An owner may not pick up a player that is already on IR.</li>
                                <li>When a player gets dropped off of IR, he will not be available to be picked up until the following week</li>
                            </ul>
                    <h2>Weekly Lineup</h2>
                        <h3>Lineup Size :</h3>
                            <label>An owner’s weekly lineup during the season must always consist of 16 players.</label><br />
                        <h3>Starters :</h3>
                            <label>An owner does not need to specify starters at any point during the season.  Every player on an owner’s roster is eligible to score points towards their weekly total.</label><br />
                        <h3>Scoring :</h3>
                            <label>An owner’s total fantasy points are accumulated by the system and only get rewarded points from the highest scoring players at the following player positions: 1 QB, 2 RB’s, 3 WR’s, 1 K.</label><br />
                        <h3>Bye Week :</h3>
                            <label>If a player has a bye on a given week, his score will result in 0 points.</label><br />
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
                            <label>There are no negative points. If a player ends a game with negative yardage in any category (i.e. -11 yards rushing), he is given 0 points for that category.</label><br />
                        <h3>Wins and Losses :</h3>
                            <label>In a weekly head-to-head matchup, the owner with the most fantasy points wins.</label><br />

                    <h2>Schedule</h2>
                        <h3>Head-to-Head :</h3>
                            <label>The fantasy schedule created each year is determined by the owner’s team number.  The team numbers are auto-assigned, based on results from the previous year.  All categories are based on teams that finished at the top in the following order: money won, total fantasy points, final record in standings.  If there is still a tie, the year previous will be looked it using the same criteria.</label><br />
                        <h3>Season Duration :</h3>
                            <label>The fantasy season begins with NFL’s week 1 and ends after week 17.</label><br />

                    <h2>Payoffs</h2>
                        <h3>Weekly Winner :</h3>
                            <label>No money is awarded for a weekly winner.</label><br />
                        <h3>Season Winners :</h3>
                            <label>Winners are determined by both Final Standings and Total Fantasy Points.</label><br />
                        <h3>Final Standings :</h3>
                            <ul>
                                <li>1st Place &nbsp;&nbsp;&nbsp;&nbsp; 25% of remaining pot - ($50.00)</li>
                                <li>2nd Place &nbsp;&nbsp; 17% of remaining pot - ($34.00)</li>
                                <li>3rd Place &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 8% of remaining pot - ($16.00)</li>
                            </ul>
                        <h3>Total Points :</h3>
                            <ul>
                                <li>1st Place &nbsp;&nbsp;&nbsp;&nbsp; 25% of remaining pot - ($50.00)</li>
                                <li>2nd Place &nbsp;&nbsp; 17% of remaining pot - ($34.00)</li>
                                <li>3rd Place &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 8% of remaining pot - ($16.00)</li>
                            </ul>
                        <h3>Ties :</h3>
                            <ul>
                                <li>A tie in the Final Standings will result in the tied teams splitting the money evenly.</li>
                                <li>A tie in Total Points will result in the tied teams splitting the money evenly.</li>
                            </ul>
                   
                        </table>


                </div> <!-- rules -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>