<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Tenman Rules</title>
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

                    <h1>Tenman League Rules</h1>

                    <h2>Fantasy League</h2>
                        <h3>Name :</h3>
                            <label>Tenman</label><br />
                        <h3>Type :</h3>
                            <label>Standard Draft</label><br />
                        <h3>Owners :</h3>
                            <label>10</label><br />
                        <h3>Fee :</h3>
                            <label>$25.00 ($20 Payoffs - $5 Stats Service)</label><br />
                        <h3>Commissioner :</h3>
                            <label>Bert Keele</label><br />
                        <h3>Bookkeeper :</h3>
                            <label>Grant Keele</label><br />    
                        <h3>Rule Changes :</h3>
                            <ul>
                                <li>The deadline for submitting new rule changes is set at the annual owner’s meeting.</li>
                                <li>There must be a majority vote among the owners for any rules to be added or changed for the upcoming season.</li>
                                <li>A tie means no change.</li>
                                <li>A neutral vote by an owner does not count as a vote at all.</li>
                                <li>A rule can be passed during the season only with a 100% owner approval.</li>
                            </ul>
                    <h2>Commissioner Responsibilities</h2>
                        <h3>Off-Season Preparation :</h3>
                            <ul>
                                <li>Hold an owner’s meeting.</li>
                                <li>Replace owners when necessary.</li>
                                <li>Coordinate draft day.</li>
                                <li>Collect and email all rule proposals.</li>
                                <li>Gather and send out final results on all rule change proposals.</li>
                                <li>Draw cards to establish draft order.</li>
                                <li>Disperse the draft order.</li>
                                <li>Prepare hard copies of draft sheets.</li>
                            </ul>
                        <h3>In-Season :</h3>
                            <ul>
                                <li>Solve all matters fairly.</li>
                                <li>Serve as league treasurer.</li>
                            </ul>
                    <h2>Owner's Meeting</h2>
                        <h3>Agenda :</h3>
                            <label>An annual owner’s meeting is to be held each year.  The commissioner should schedule a time to meet and discuss upcoming rule change proposals, refine our games involving new ideas, and schedule the time of when all rule change proposals are due.</label><br/>
                    <h2>Roster Requirements</h2>
                        <h3>Minimum Requirements :</h3>
                            <label>An owner must always have 1 QB, 2 RB’s, 3 WR’s, and 1 K throughout the duration of the entire season.</label><br />
                        <h3>Backup Positions :</h3>
                            <label>There are no requirements for the backup positions.  After fulfilling the minimum requirements, an owner can comprise the remaining roster spots from players at any position.</label><br />
                    <h2>The Draft</h2>
                        <h3>Rounds :</h3>
                            <label>16</label><br/>
                        <h3>Draft Order :</h3>
                            <ul>
                                <li>Cards are drawn for all odd number rounds.  Round #3 is the exception.  When drawing for Round 1, the owners are drawing for spots not necessarily the draft pick.</li>
                                <li>The owner that draws the first spot in Round 1 gets to select which pick he would like to have in either the 1st Round or 3rd Round. The owner with the second spot then chooses from the remaining possibilities. This continues until all 10 owners have chosen their first pick that they would like whether it be in Round 1 or Round 3. The order then reverses so the owner that had the 10th spot now gets to pick again. An owner must choose the opposite round that he didn’t choose before and will select any pick that is still available in that round. This process continues down to the owner that originally had the first spot. All picks in the first and third rounds should then be fulfilled.</li>
                                <li>Each even number round in the draft serpentines with the round that precedes it.</li>
                            </ul>
                        <h3>Position Requirements :</h3>
                                <label>There are no position requirements.  An entire roster can be comprised of players at any position.</label><br />
                        <h3>Final Roster Size :</h3>
                                <label>16</label><br />
                        <h3>Time Limit</h3>
                                <label>2 minutes per pick</label><br />
                    <h2>Roster Changes</h2>
                        <h3>Transactions and Trades :</h3>
                            <ul>
                                <li>A transaction can be made on any week throughout the duration of the season.</li>
                                <li>There is not a limit of how many transactions an owner can make each week.</li>
                                <li>To add a player, one must be dropped first.</li>
                                <li>When a player is dropped, he cannot be picked up by any owner until the following week.</li>
                                <li>An owner may not perform any transaction involving a player that has already started playing their game on a given week.</li>
                                <li>Trades between owners are not allowed after Week #14.</li>
                                <li>There is a limit of 3 trades per year between two owners.</li>
                            </ul>
                        <h3>Transaction Deadline :</h3>
                            <label>Each week, all transactions should be submitted into the system from Sunday up to 6:00 AM MST Thursday morning.  The system will then carry out all submitted transactions based on the transaction order (see Transaction Order). Immediately following the processed transactions, all other submitted transactions for the week will be processed on a first-come first-served basis.</label><br />
                        <h3>Transaction Order :</h3>
                            <ul>
                                <li>After the draft and before the first Pro game, the transaction order will be on a first-come first-served basis.</li>
                                <li>After Week 1, transactions are always processed in an orderly fashion.  The initial order is based on Week 1 results.  The first determining factor is the fewest total fantasy points, followed by worst record.</li>
                                <li>Anytime an owner makes a transaction on a given week by the transaction deadline, that owner moves to the bottom of the order, while every other owner shifts up one. If more than one transaction per owner occurs by the deadline, the second round of transactions will be carried out in the exact same manner, and so on.  Any transactions made after the transaction deadline is allowed without moving an owner to the bottom of the order.</li>
                            </ul>
                    <h2>Injured Reserve</h2>
                        <h3>Regulations :</h3>
                            <label>There is no injured reserve list.</label><br />
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
                            <label>The fantasy season begins with NFL’s week 1 and ends after week 16.</label><br />

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

                    <h2>Owner Information</h2>
                        <table>
                            <tr>
                                <th>Owner</th>
                                <th>Home Phone</th>
                                <th>Cell Phone</th>
                                <th>Email</th>
                            </tr>
                            <tr>
                                <td>Bert Keele</td>
                                <td>801-226-2949</td>
                                <td>801-830-2422</td>
                                <td>bertkeele@gmail.com</td>
                            </tr>
                            <tr>
                                <td>Brian Keele</td>
                                <td>801-561-4636</td>
                                <td>801-673-4139</td>
                                <td>briankeele@yahoo.com</td>
                            </tr>
                            <tr>
                                <td>Dave Keele</td>
                                <td>208-385-7151</td>
                                <td>208-841-9756</td>
                                <td>dkeele@micron.com</td>
                            </tr>
                            <tr>
                                <td>Grant Keele</td>
                                <td>801-765-1677</td>
                                <td>801-830-2350</td>
                                <td>grantkeele@gmail.com</td>
                            </tr>
                            <tr>
                                <td>Jeremy Sorhus</td>
                                <td>&nbsp;</td>
                                <td>406-781-7509</td>
                                <td>drsorhus@msn.com</td>
                            </tr>
                            <tr>
                                <td>Joe Willie</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>John Keele</td>
                                <td>801-224-3185</td>
                                <td>801-376-3447</td>
                                <td>johnfkeele@gmail.com</td>
                            </tr>
                            <tr>
                                <td>Mike Keele</td>
                                <td>&nbsp;</td>
                                <td>801-722-4457</td>
                                <td>mkeele34@gmail.com</td>
                            </tr>
                            <tr>
                                <td>Ryan Ragozzine</td>
                                <td>619-773-6076</td>
                                <td>619-871-7497</td>
                                <td>mrragzzz@yahoo.com</td>
                            </tr>
                            <tr>
                                <td>Scott Hansen</td>
                                <td>&nbsp;</td>
                                <td>801-836-6769</td>
                                <td>yoscottiedog@gmail.com </td>
                            </tr>
                        </table>


                </div> <!-- rules -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>