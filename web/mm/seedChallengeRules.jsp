<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Bracket Challenge Rules</title>
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

                    <h1>Seed Challenge Rules</h1>

                    <h2>Game Play</h2>
                        <h3>Overview :</h3>
                            <label>In this game, a person selects 8 different NCAA teams belonging to different seed groups. The seed groups correspond to the NCAA team's actual seed number in the tournament and distinguishes which NCAA teams are available to be picked for the certain roster slot. The winner of this game must have the NCAA Tournament winner as 1 of their roster selections.</label><br />
                        <h3>Leagues :</h3>
                            <label>An owner can join or create their own leagues.  A General league will be created for a chance to become the Top Dawg of the seed challenge.</label><br />
                        <h3>Cost :</h3>
                            <label>$5 per entry</label><br />

                    <h2>Tournament</h2>
                        <h3>Duration :</h3>
                            <label>This game begins with Round 2 of the NCAA Tournament and lasts until the end of the NCAA Tournament.</label><br />
                        <h3>Play-in Games :</h3>
                            <label>The play-in games that are now part of Round 1 of the NCAA Tournament will not be part of this game.  Shortly after each of those 4 play-in games have finished, the appropriate team will be placed into the bracket.</label><br />
                        <h3>Note :</h3>
                            <label>Since the play-in games are not counted, there is an advantage in waiting for those games to finish before completely filling out the bracket.</label><br />
                            
                    <h2>Roster</h2>
                        <h3>Size :</h3>
                            <label>8 NCAA teams comprise a person's roster.</label><br />
                        <h3>Seed Groups :</h3>
                            <label>There are 8 different seed groups that correspond to an NCAA tournament team's seed number.  Each seed group as a range of seed numbers that distinguish which NCAA teams are available to be picked in that roster spot.</label><br />
                        <h3>Regulations :</h3>
                            <ul>
                                <li>All rosters must be completely filled out by the start of the tournament.</li>
                                <li>Failure to pick an NCAA team in any of the roster slots will count as 0 for that seed group.</li>
                                <li>It is allowed to pick a lower seeded team than what the seed group range shows but you may not pick a higher seeded team. </li>
                            </ul>
                        <h3>Scenario :</h3>
                            <label>You may pick a #2 seeded team in your #1 seed group, but you can't pick a #1 seeded team in your #2 seed group.</label><br />
                            
                    <h2>Entries</h2>
                        <h3>Deadline :</h3>
                            <label>All rosters must be selected and entered into the system before the tip-off of the first Thursday morning Round 2 game.</label><br />
                        <h3>Limit :</h3>
                            <label>There is no limit on the number of entries a person can have.</label><br />
                    
                    <h2>Scoring</h2>
                        <h3>Round 2 :</h3>
                            <label>1 point awarded for each team on your roster that wins their game in this round.</label><br />
                        <h3>Round 3 :</h3>
                            <label>10 points awarded for each team on your roster that wins their game in this round.</label><br />
                        <h3>Sweet 16 :</h3>
                            <label>100 points awarded for each team on your roster that wins their game in this round.</label><br />                            
                        <h3>Elite 8 :</h3>
                            <label>1,000 points awarded for each team on your roster that wins their game in this round.</label><br />                            
                        <h3>Final 4 :</h3>
                            <label>10,000 points awarded for each team on your roster that wins their game in this round.</label><br />                            
                        <h3>Championship :</h3>
                            <label>100,000 points awarded for picking the tournament winner.</label><br />
                        <h3>Explanation :</h3>
                            <label>The winner is the person who accumulated the most points.  Essentially it works out whoever had the team go the furthest in the tournament wins.  If multiple people had the same team then whichever person had the team on their roster that went the next furthest in the tournament will win.</label><br />
                    <h2>Payoffs :</h2>
                        <h3>Top 2 :</h3>
                            <label>1st place wins 70% and 2nd place wins 30% of the total amount of all entry fees.</label><br />
                        <h3>Tie :</h3>
                            <label>If a tie exists, the prize will be split equally among all tied winners.</label><br />

                    <h2>Help</h2>
                        <label>If you come across any problems or errors, please help out by sending us an email. We really appreciate any extra help we can get in adding to the quality of our product. Also, any feedback you can give us or ways to improve the site would sure be welcomed.</label><br />
                    
                </div> <!-- rules -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>