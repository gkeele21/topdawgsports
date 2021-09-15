package tds.data;

import static tds.data.CTColumnLists._Cols;

public enum CTDataSetDef {
    // Rules:
    // - the number of underscores in the idbreakcol must match the greatest number of underscores in the collist.
    // - the idbreakcol can't end in an underscore; if the last break col occurs at a higher level, then _repeat it_ to the end.

    COUNTRY_BY_COUNTRYID(
            "select " + _Cols.getColumnList("Country", "", "") +
            " from " + "Country where CountryID = ?"
    ),
    DELETE_FSFOOTBALLTRANSACTIONREQUEST(
            " delete from FSFootballTransactionRequest" +
            " where RequestID = ?"
    ),
    DELETE_FSROSTER_BY_FSTEAMID_FSSEASONWEEKID(
            "delete from " + "FSRoster" +
            " where FSTeamID = ? and FSSeasonWeekID = ?"
    ),
    FANTASYPTS_BY_FSSEASONWEEKID_FSTEAMID(
            "select SUM(s.FantasyPts) as totalfantasypts " +
            " from FSTeam t " +
            " inner join FSRoster r on t.FSTeamID=r.FSTeamID and r.FSSeasonWeekID = ? " +
            " inner join Player p on p.PlayerID=r.PlayerID " +
            " inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID " +
            " inner join SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID " +
            " inner join FootballStats s on s.PlayerID = p.PlayerID and s.SeasonWeekID = sw.SeasonWeekID " +
            " where t.FSTeamID = ? " +
            " group by t.FSTeamID"
    ),
    FOOTBALLSTATS_BY_PLAYERID_SEASONID(
            " select " + _Cols.getColumnList("FootballStats", "st.", "FootballStats$") +
            "," + _Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$") +
            "," + _Cols.getColumnList("Season", "ss.", "Season$") +
            "," + _Cols.getColumnList("Team", "t.", "Team$") +
            " from FootballStats st " +
            " left join SeasonWeek sw on sw.SeasonWeekID = st.SeasonWeekID " +
            " left join Season ss on ss.SeasonID = sw.SeasonID " +
            " left join Team t on t.StatsTeamID = st.TeamID " +
            " where st.StatsPlayerID = ? and st.SeasonID = ?" +
            " order by st.SeasonWeekID"
    ),
//    FREEAGENTS_BY_FSSEASONWEEKID_FSLEAGUEID_POSITIONNAME(
//            "select " + _Cols.getColumnList("Player", "p.", "Player$") +
//            "," + _Cols.getColumnList("Team", "t.", "Team$") +
//            "," + _Cols.getColumnList("Position", "ps.", "Position$") +
//            "," + _Cols.getColumnList("FSSeasonWeek", "sw.", "FSSeasonWeek$") +
//            " from " + "Player p " +
//            " inner join " + "Position ps on ps.PositionID = p.PositionID " +
//            " inner join " + "Team t on t.TeamID = p.TeamID " +
//            " left join " + "FSRoster r on r.PlayerID = p.PlayerID " +
//            "  and r.FSSeasonWeekID = ? and r.FSTeamID in (?)" +
//            " left join " + "FSTeam tm on tm.FSTeamID = r.FSTeamID and tm.FSLeagueID = ? " +
//            " left join " + "FSSeasonWeek sw on sw.FSSeasonWeekID = r.FSSeasonWeekID " +
//            " where r.PlayerID is null and ps.PositionName = ? " +
//            " order by ps.PositionID, p.LastName "
//    ),
    FSFOOTBALLMATCHUP_BY_FSLEAGUEID_FSSEASONWEEKID(
            "select " + _Cols.getColumnList("FSFootballMatchup", "m.", "") +
            "," + _Cols.getColumnList("FSTeam", "t1.", "FSTeam1$") +
            "," + _Cols.getColumnList("FSTeam", "t2.", "FSTeam2$") +
            "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$") +
            " from  " + "FSFootballMatchup m " +
            " inner join " + "FSTeam t1 on t1.FSTeamID = m.Team1ID " +
            " inner join " + "FSTeam t2 on t2.FSTeamID = m.Team2ID " +
            " inner join " + "FSLeague l on l.FSLeagueID = t1.FSLeagueID " +
            " inner join " + "FSSeasonWeek w on w.FSSeasonWeekID = m.FSSeasonWeekID " +
            " where m.FSLeagueID = ? and m.FSSeasonWeekID = ?"
    ),
    FSFOOTBALLMATCHUP(
            "select  " + _Cols.getColumnList("FSFootballMatchup", "m.", "") +
            "," + _Cols.getColumnList("FSTeam", "t1.", "FSTeam1$") +
            "," + _Cols.getColumnList("FSTeam", "t2.", "FSTeam2$") +
            "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$") +
            " from  " + "FSFootballMatchup m " +
            " inner join " + "FSTeam t1 on t1.FSTeamID = m.Team1ID " +
            " inner join " + "FSTeam t2 on t2.FSTeamID = m.Team2ID " +
            " inner join " + "FSLeague l on l.FSLeagueID = t1.FSLeagueID " +
            " inner join " + "FSSeasonWeek w on w.FSSeasonWeekID = m.FSSeasonWeekID " +
            " where m.FSLeagueID = ? and m.FSSeasonWeekID = ? and m.GameNo = ?"
    ),
    FSFOOTBALLMATCHUP_BY_FSMATCHUPID(
            "select " + _Cols.getColumnList("FSFootballMatchup", "m.", "") +
            "," + _Cols.getColumnList("FSTeam", "t1.", "FSTeam1$") +
            "," + _Cols.getColumnList("FSTeam", "t2.", "FSTeam2$") +
            "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$") +
            " from  " + "FSFootballMatchup m " +
            " inner join " + "FSTeam t1 on t1.FSTeamID = m.Team1ID " +
            " inner join " + "FSTeam t2 on t2.FSTeamID = m.Team2ID " +
            " inner join " + "FSLeague l on l.FSLeagueID = t1.FSLeagueID " +
            " inner join " + "FSSeasonWeek w on w.FSSeasonWeekID = m.FSSeasonWeekID " +
            " where m.ID = ?"
    ),
    FSFOOTBALLMAXREQUESTS_BY_FSTEAMID_FSSEASONWEEKID (
            "select " + _Cols.getColumnList("FSFootballMaxRequests", "r.", "") +
            " from " + "FSFootballMaxRequests r " +
            " where r.FSTeamID = ? and r.FSSeasonWeekID = ? "
    ),
    FSFOOTBALLROSTERPOSITIONS_BY_FSSEASONID (
            "select " + _Cols.getColumnList("FSFootballRosterPositions", "rp.", "") +
            " from " + "FSFootballRosterPositions rp " +
            " where rp.FSSeasonID = ? "
    ),
    FSFOOTBALLROSTERPOSITIONS_BY_FSSEASONID_POSITIONID (
            "select " + _Cols.getColumnList("FSFootballRosterPositions", "rp.", "") +
            " from " + "FSFootballRosterPositions rp " +
            " where rp.FSSeasonID = ? and rp.PositionID = ? "
    ),
    FSFOOTBALLSEASONDETAIL_BY_FSSEASONID (
            "select " + _Cols.getColumnList("FSFootballSeasonDetail", "sd.", "") +
            " from " + "FSFootballSeasonDetail sd " +
            " where sd.FSSeasonID = ? "
    ),
    FSFOOTBALLSTANDINGS_BY_FSLEAGUEID_FSSEASONWEEKID (
            "select " + _Cols.getColumnList("FSTeam", "t.", "FSTeam#") + "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague#") +
            "," + _Cols.getColumnList("FSFootballStandings", "s.", "FSFootballStandings#") + "," + _Cols.getColumnList("FSUser", "u.", "FSUser#") +
            "," + _Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek#") +
            " from FSTeam t " +
            " inner join FSLeague l on l.FSLeagueID = t.FSLeagueID " +
            " inner join FSFootballStandings s on s.FSTeamID = t.FSTeamID " +
            " inner join FSUser u on u.FSUserID = t.FSUserID" +
            " inner join FSSeasonWeek w on w.FSSeasonWeekID = s.FSSeasonWeekID" +
            " where l.FSLeagueID = ? and s.FSSeasonWeekID = ? " +
            " order by ? "
    ),
    FSFOOTBALLTRANSACTION_BY_FSLEAGUEID_FSSEASONWEEKID(
            "select " + _Cols.getColumnList("FSFootballTransaction", "t.", "") +
            "," + _Cols.getColumnList("FSTeam", "tm.", "FSTeam$") +
            "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$") +
            "," + _Cols.getColumnList("Player", "dp.", "DropPlayer$") +
            "," + _Cols.getColumnList("Player", "pp.", "PUPlayer$") +
            "," + _Cols.getColumnList("Position", "dps.", "DropPlayerPosition$") +
            "," + _Cols.getColumnList("Position", "pps.", "PUPlayerPosition$") +
            "," + _Cols.getColumnList("Team", "dt.", "DropPlayerTeam$") +
            "," + _Cols.getColumnList("Team", "pt.", "PUPlayerTeam$") +
            " from  " + "FSFootballTransaction t " +
            " inner join " + "FSTeam tm on tm.FSTeamID = t.FSTeamID " +
            " inner join " + "FSLeague l on l.FSLeagueID = tm.FSLeagueID " +
            " inner join " + "FSSeasonWeek w on w.FSSeasonWeekID = t.FSSeasonWeekID " +
            " inner join " + "Player dp on dp.PlayerID = t.DropPlayerID " +
            " inner join " + "Player pp on pp.PlayerID = t.PUPlayerID " +
            " inner join " + "Position dps on dps.PositionID = dp.PositionID " +
            " inner join " + "Position pps on pps.PositionID = pp.PositionID " +
            " inner join " + "Team dt on dt.TeamID = dp.TeamID " +
            " inner join " + "Team pt on pt.TeamID = pp.TeamID " +
            " where t.FSLeagueID = ? and t.FSSeasonWeekID = ?"
    ),
    FSFOOTBALLTRANSACTION_BY_FSTRANSACTIONID(
            "select " + _Cols.getColumnList("FSFootballTransaction", "t.", "") +
            "," + _Cols.getColumnList("FSTeam", "tm.", "FSTeam$") +
            "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$") +
            "," + _Cols.getColumnList("Player", "dp.", "DropPlayer$") +
            "," + _Cols.getColumnList("Player", "pp.", "PUPlayer$") +
            "," + _Cols.getColumnList("Position", "dps.", "DropPlayerPosition$") +
            "," + _Cols.getColumnList("Position", "pps.", "PUPlayerPosition$") +
            "," + _Cols.getColumnList("Team", "dt.", "DropPlayerTeam$") +
            "," + _Cols.getColumnList("Team", "pt.", "PUPlayerTeam$") +
            " from  " + "FSFootballTransaction t " +
            " inner join " + "FSTeam tm on t.FSTeamID = tm.FSTeamID " +
            " inner join " + "FSLeague l on l.FSLeagueID = tm.FSLeagueID " +
            " inner join " + "FSSeasonWeek w on w.FSSeasonWeekID = t.FSSeasonWeekID " +
            " inner join " + "Player dp on dp.PlayerID = t.DropPlayerID " +
            " inner join " + "Player pp on pp.PlayerID = t.PUPlayerID " +
            " inner join " + ".Position dps on dps.PositionID = dp.PositionID " +
            " inner join " + "Position pps on pps.PositionID = pp.PositionID " +
            " inner join " + "Team dt on dt.TeamID = dp.TeamID " +
            " inner join " + "Team pt on pt.TeamID = pp.TeamID " +
            " where t.FSTransactionID = ?"
    ),
    FSFOOTBALLTRANSACTIONORDER_BY_FSLEAGUEID_FSSEASONWEEKID(
            "select " + _Cols.getColumnList("FSTeam", "tm.", "FSTeam$") +
            "," + _Cols.getColumnList("FSFootballTransactionOrder", "t.", "") +
            " from FSFootballTransactionOrder t " +
            " inner join FSTeam tm on t.FSTeamID = tm.FSTeamID " +
            " where t.FSLeagueID = ? and t.FSSeasonWeekID = ? " +
            " order by t.OrderNumber"
    ),
    FSFOOTBALLTRANSACTIONREQUEST_BY_REQUESTID(
            "select " + _Cols.getColumnList("FSTeam", "tm.", "FSTeam$") +
            "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$") +
            "," + _Cols.getColumnList("FSFootballTransactionRequest", "r.", "FSFootballTransactionRequest$") +
            " from FSFootballTransactionRequest r " +
            " inner join FSTeam tm on r.FSTeamID = tm.FSTeamID " +
            " inner join FSLeague l on l.FSLeagueID = tm.FSLeagueID " +
            " inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID " +
            " where r.RequestID = ?"
    ),
    FSFOOTBALLTRANSACTIONREQUEST_BY_FSTEAMID_FSSEASONWEEKID_RANK(
            "select " + _Cols.getColumnList("FSTeam", "tm.", "FSTeam$") +
            "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$") +
            "," + _Cols.getColumnList("FSFootballTransactionRequest", "r.", "FSFootballTransactionRequest$") +
            " from FSFootballTransactionRequest r " +
            " inner join FSTeam tm on r.FSTeamID = tm.FSTeamID " +
            " inner join FSLeague l on l.FSLeagueID = tm.FSLeagueID " +
            " inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID " +
            " where r.FSTeamID = ? and r.FSSeasonWeekID = ? and r.Rank = ?"
    ),
//    FSFOOTBALLTRANSACTIONREQUESTS_BY_FSTEAMID_FSSEASONWEEKID(
//            "select " + _Cols.getColumnList("FSTeam", "tm.", "FSTeam$") +
//            "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
//            "," + _Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$") +
//            "," + _Cols.getColumnList("FSFootballTransactionRequest", "r.", "FSFootballTransactionRequest$") +
//            " from FSFootballTransactionRequest r " +
//            " inner join FSTeam tm on r.FSTeamID = tm.FSTeamID " +
//            " inner join FSLeague l on l.FSLeagueID = tm.FSLeagueID " +
//            " inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID " +
//            " where r.FSTeamID = ? and r.FSSeasonWeekID = ? " +
//            " order by r.Rank"
//    ),
    FSGAME_BY_FSGAMEID(
            "select " + _Cols.getColumnList("FSGame", "g.", "") +
            " from FSGame g where g.FSGameID = ?"
    ),
    FSLEAGUE_BY_FSLEAGUEID(
            "select " + _Cols.getColumnList("FSLeague", "l.", "") +
            "," + _Cols.getColumnList("FSSeason", "s.", "FSSeason$") + "," + _Cols.getColumnList("FSGame", "g.", "FSGame$") +
            "," + _Cols.getColumnList("Sport", "sp.", "Sport$") + "," + _Cols.getColumnList("Season", "se.", "Season$") +
            " from " + "FSLeague l " +
            " inner join " + "FSSeason s on s.FSSeasonID = l.FSSeasonID " +
            " inner join " + "FSGame g on g.FSGameID = s.FSGameID " +
            " inner join " + "Sport sp on sp.SportID = g.SportID " +
            " inner join " + "Season se on se.SeasonID = s.SeasonID " +
            " where l.FSLeagueID = ?"
    ),
    FSLEAGUES_BY_FSSEASONID(
            "select " + _Cols.getColumnList("FSLeague", "l.", "") +
            " from " + "FSLeague l where l.FSSeasonID = ?"
    ),
    FSPLAYERVALUE_BY_FSPLAYERID_FSSEASONWEEKID(
            "select " + _Cols.getColumnList("FSPlayerValue", "pv.", "PlayerValue$") +
            "," + _Cols.getColumnList("Team", "t.", "Team$") +
            "," + _Cols.getColumnList("Position", "ps.", "Position$") +
            "," + _Cols.getColumnList("Country", "c.", "") +
            "," + _Cols.getColumnList("FSSeasonWeek","fsw.", "FSSeasonWeek$") +
            //"," + _Cols.getColumnList("FootballStats","st.", "FootballStats$") +
            //"," + _Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$") +
            " from " + "FSPlayerValue pv " +
            " inner join " + "Player p on p.PlayerID = pv.PlayerID " +
            " inner join " + "Team t on t.TeamID = p.TeamID " +
            " inner join " + "Position ps on ps.PositionID = p.PositionID " +
            " left join " + "Country c on c.CountryID = p.CountryID " +
            " inner join " + "FSSeasonWeek fsw on fsw.FSSeasonWeekID = pv.FSSeasonWeekID " +
            //" left join " + "FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = fsw.SeasonWeekID " +
            //" left join " + "FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 " +
            " where pv.PlayerID = ? and pv.FSSeasonWeekID = ?"
    ),
    FSROSTER_BY_ID(
            "select " + _Cols.getColumnList("FSRoster", "r.", "") +
            "," + _Cols.getColumnList("Player", "p.", "Player$") +
            "," + _Cols.getColumnList("Team", "t.", "Team$") +
            "," + _Cols.getColumnList("Position", "ps.", "Position$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "sw.", "FSSeasonWeek$") +
            " from " + "FSRoster r " +
            " inner join " + "Player p on p.PlayerID = r.PlayerID " +
            " inner join " + "Position ps on ps.PositionID = p.PositionID " +
            " inner join " + "Team t on t.TeamID = p.TeamID " +
            " inner join " + "FSSeasonWeek sw on sw.FSSeasonWeekID = r.FSSeasonWeekID " +
            " where r.ID = ? " +
            " order by r.ActiveState, r.StarterState desc, p.PositionID"
    ),
    FSROSTER_BY_FSTEAMID_FSSEASONWEEKID(
            "select " + _Cols.getColumnList("FSRoster", "r.", "") +
            "," + _Cols.getColumnList("Player", "p.", "Player$") +
            "," + _Cols.getColumnList("Team", "t.", "Team$") +
            "," + _Cols.getColumnList("Position", "ps.", "Position$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$") +
            "," + _Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$") +
            "," + _Cols.getColumnList("FootballStats","st.","FootballStats$") +
            "," + _Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$") +
            " from " + "FSRoster r " +
            " inner join " + "Player p on p.PlayerID = r.PlayerID " +
            " inner join " + "Position ps on ps.PositionID = p.PositionID " +
            " inner join " + "Team t on t.TeamID = p.TeamID " +
            " inner join " + "FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID " +
            " inner join " + "SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID " +
            " left join " + "FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = sw.SeasonWeekID " +
            " left join " + "FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 and tst.SeasonID = ? " +
            " where r.FSTeamID = ? and r.FSSeasonWeekID = ? " +
            " order by r.ActiveState, r.StarterState desc, p.PositionID"
    ),
    FSROSTER_BY_FSTEAMID_FSSEASONWEEKID_ACTIVESTATE(
            "select " + _Cols.getColumnList("FSRoster", "r.", "") +
            "," + _Cols.getColumnList("Player", "p.", "Player$") +
            "," + _Cols.getColumnList("Team", "t.", "Team$") +
            "," + _Cols.getColumnList("Position", "ps.", "Position$") +
            "," + _Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$") +
            "," + _Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$") +
            "," + _Cols.getColumnList("FootballStats","st.","FootballStats$") +
            "," + _Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$") +
            " from " + "FSRoster r " +
            " inner join " + "Player p on p.PlayerID = r.PlayerID " +
            " inner join " + "Position ps on ps.PositionID = p.PositionID " +
            " inner join " + "Team t on t.TeamID = p.TeamID " +
            " inner join " + "FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID " +
            " inner join " + "SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID " +
            " left join " + "FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = sw.SeasonWeekID " +
            " left join " + "FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 and tst.SeasonID = ? " +
            " where r.FSTeamID = ? and r.FSSeasonWeekID = ? and r.ActiveState = ?" +
            " order by r.ActiveState, r.StarterState desc, p.PositionID"
    ),
    FSSEASON_BY_FSSEASONID(
            "select " + _Cols.getColumnList("FSSeason", "s.", "") +
            " from " + "FSSeason s where s.FSSeasonID = ?"
    ),
    FSSEASONS_ALL(
            "select " + _Cols.getColumnList("FSSeason", "s.", "") +
            " from " + "FSSeason s"
    ),
    FSSEASONS_BY_FSGAMEID(
            "select " + _Cols.getColumnList("FSSeason", "s.", "") +
            " from " + "FSSeason s where s.FSGameID = ?"
    ),
    FSSEASONWEEK_BY_FSSEASONWEEKID(
            "select " + _Cols.getColumnList("FSSeasonWeek", "w.", "") +
            "," + _Cols.getColumnList("FSSeason", "s.", "FSSeason$") +
            "," + _Cols.getColumnList("FSGame", "g.", "FSGame$") +
            "," + _Cols.getColumnList("Sport", "sp.", "Sport$") +
            "," + _Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$") +
            " from " + "FSSeasonWeek w " +
            " inner join " + "FSSeason s on s.FSSeasonID = w.FSSeasonID " +
            " inner join " + "FSGame g on g.FSGameID = s.FSGameID " +
            " inner join " + "Sport sp on sp.SportID = g.SportID " +
            " inner join " + "SeasonWeek sw on sw.SeasonWeekID = w.SeasonWeekID " +
            " where w.FSSeasonWeekID = ?"
    ),
    FSSEASONWEEK_BY_FSLEAGUEID_WEEKNO(
            "select " + _Cols.getColumnList("FSSeasonWeek", "w.", "") +
            "," + _Cols.getColumnList("FSSeason", "s.", "FSSeason$") +
            "," + _Cols.getColumnList("FSGame", "g.", "FSGame$") +
            "," + _Cols.getColumnList("Sport", "sp.", "Sport$") +
            "," + _Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$") +
            " from FSSeasonWeek w " +
            " inner join FSSeason s on s.FSSeasonID = w.FSSeasonID " +
            " inner join FSGame g on g.FSGameID = s.FSGameID " +
            " inner join Sport sp on sp.SportID = g.SportID " +
            " inner join SeasonWeek sw on sw.SeasonWeekID = w.SeasonWeekID " +
            " inner join FSLeague l on l.FSSeasonID = s.FSSeasonID and l.FSLeagueID = ?" +
            " where w.FSSeasonWeekNo = ?"
    ),
    FSSEASONWEEKS_BY_FSSEASONID(
            "select " + _Cols.getColumnList("FSSeasonWeek", "w.", "") +
            "," + _Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$") +
            " from " + "FSSeasonWeek w " +
            " inner join " + "SeasonWeek sw on sw.SeasonWeekID = w.SeasonWeekID " +
            " where w.FSSeasonID = ?"
    ),
    FSTEAM_BY_FSTEAMID(
            "select " + _Cols.getColumnList("FSTeam", "t.", "") + "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeason", "s.", "FSSeason$") + "," + _Cols.getColumnList("FSGame", "g.", "FSGame$") +
            "," + _Cols.getColumnList("Sport", "sp.", "Sport$") + "," + _Cols.getColumnList("Season", "se.", "Season$") +
            "," + _Cols.getColumnList("FSUser", "u.", "FSUser$") +
            " from " + "FSTeam t " +
            " inner join " + "FSLeague l on l.FSLeagueID = t.FSLeagueID " +
            " inner join " + "FSSeason s on s.FSSeasonID = l.FSSeasonID " +
            " inner join " + "FSGame g on g.FSGameID = s.FSGameID " +
            " inner join " + "Sport sp on sp.SportID = g.SportID " +
            " inner join " + "Season se on se.SeasonID = s.SeasonID " +
            " inner join " + "FSUser u on u.FSUserID = t.FSUserID" +
            " where t.FSTeamID = ?"
    ),
    FSTEAMS_BY_FSLEAGUEID(
            "select " + _Cols.getColumnList("FSTeam", "t.", "") +
            " from " + "FSTeam t " +
            " where t.FSLeagueID = ? and t.isActive = 1 "
    ),
    FSTEAMS_BY_FSUSERID(
            "select " + _Cols.getColumnList("FSTeam", "t.", "") + "," + _Cols.getColumnList("FSLeague", "l.", "FSLeague$") +
            "," + _Cols.getColumnList("FSSeason", "s.", "FSSeason$") + "," + _Cols.getColumnList("FSGame", "g.", "FSGame$") +
            "," + _Cols.getColumnList("Sport", "sp.", "Sport$") + "," + _Cols.getColumnList("Season", "se.", "Season$") +
            "," + _Cols.getColumnList("FSUser", "u.", "FSUser$") +
            " from FSTeam t " +
            " inner join FSLeague l on l.FSLeagueID = t.FSLeagueID " +
            " inner join FSSeason s on s.FSSeasonID = l.FSSeasonID " +
            " inner join FSGame g on g.FSGameID = s.FSGameID " +
            " inner join Sport sp on sp.SportID = g.SportID " +
            " inner join Season se on se.SeasonID = s.SeasonID " +
            " inner join FSUser u on u.FSUserID = t.FSUserID" +
            " where t.FSUserID = ? and t.isActive = 1 and s.DisplayTeams = 1" +
            " order by s.FSSeasonID, l.FSLeagueID, t.FSTeamID "
    ),
    FSUSER_BY_USERID(
            "select " + _Cols.getColumnList("FSUser", "u.", "") +
            " from " + "FSUser u where u.FSUserID = ?"
    ),
    FSUSER_BY_USERNAME(
            "select " + _Cols.getColumnList("FSUser", "u.", "") +
            " from " + "FSUser u where u.Username = ?"
    ),
    FSUSER_BY_USERNAME_PASSWORD(
            "select " + _Cols.getColumnList("FSUser", "u.", "") +
            " from " + "FSUser u where UserName=? and Password=?"
    ),
    GAME_BY_GAMEID(
            "select " + _Cols.getColumnList("Game", "g.", "") +
            ", " + _Cols.getColumnList("Team", "vt.", "VisitorTeam$") +
            ", " + _Cols.getColumnList("Team", "ht.", "HomeTeam$") +
            ", " + _Cols.getColumnList("SeasonWeek", "w.", "SeasonWeek$") +
            ", " + _Cols.getColumnList("Season", "s.", "Season$") +
            " from " + "Game g " +
            " inner join " + "SeasonWeek w on w.SeasonWeekID = g.SeasonWeekID " +
            " inner join " + "Season s on s.SeasonID = w.SeasonID " +
            " inner join " + "Team vt on vt.TeamID = g.VisitorID " +
            " inner join " + "Team ht on ht.TeamID = g.HomeID " +
            " where g.GameID = ?"
    ),
    GAMES_BY_SEASONID(
            "select " + _Cols.getColumnList("Game", "g.", "") +
            ", " + _Cols.getColumnList("Team", "vt.", "VisitorTeam$") +
            ", " + _Cols.getColumnList("Team", "ht.", "HomeTeam$") +
            ", " + _Cols.getColumnList("SeasonWeek", "w.", "SeasonWeek$") +
            ", " + _Cols.getColumnList("Season", "s.", "Season$") +
            " from " + "Game g " +
            " inner join " + "SeasonWeek w on w.SeasonWeekID = g.SeasonWeekID " +
            " inner join " + "Season s on s.SeasonID = w.SeasonID " +
            " inner join " + "Team vt on vt.StatsTeamID = g.VisitorID " +
            " inner join " + "Team ht on ht.StatsTeamID = g.HomeID " +
            " where s.SeasonID = ?"
    ),
    INSERT_FSFOOTBALLSTANDINGS(
            "insert into " + "FSFootballStandings(FSTeamID,FSSeasonWeekID,FantasyPts,TotalFantasyPts, " +
            " GamePoints,TotalGamePoints,SalarySpent,Wins,Losses,Ties,WinPercentage,FantasyPtsAgainst, " +
            " HiScore,TotalHiScores,GamesCorrect,TotalGamesCorrect,GamesWrong,TotalGamesWrong, " +
            " `Rank`,CurrentStreak,LastFive) " +
            " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
    ),
    INSERT_FSROSTER(
            "insert into " + "FSRoster(FSTeamID,PlayerID,FSSeasonWeekID,StarterState, " +
            " ActiveState) " +
            " values (?, ?, ?, ?, ?)"
    ),
    INSERT_NEW_FSFOOTBALLTRANSACTION(
            "insert into " + "FSFootballTransaction(FSLeagueID, FSTeamID, FSSeasonWeekID, TransactionDate, " +
            " DropPlayerID, DropType, PUPlayerID, PUType, TransactionType) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
    ),
    INSERT_NEW_FSFOOTBALLTRANSACTIONREQUEST(
            "insert into " + "FSFootballTransactionRequest(FSSeasonWeekID, FSTeamID, `Rank`, DropPlayerID, " +
            " DropType, PUPlayerID, PUType, RequestDate, Processed, Granted) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, 0, 0)"
    ),
    INSERT_NEW_FSTEAM(
            "insert into " + "FSTeam(FSLeagueID,FSUserID,TeamName,DateCreated,IsActive,IsAlive) " +
            "values (?, ?, ?, ?, 1, 1)"
    ),
    INSERT_NEW_FSUSER(
            "insert into " + "FSUser(Username, Password, FirstName, LastName, Email, Telephone, AlternateTelephone, Address, Address2," +
            "City, State, Zip, " +
            "Country, BadEmailAddress, LeadSource, SendActionAlerts, IsActive, DateCreated, AuthenticationKey) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?, 1, ?, ?)"
    ),
    PLAYER_BY_PLAYERID(
            "select " + _Cols.getColumnList("Player", "p.", "") +
            "," + _Cols.getColumnList("Team", "t.", "Team$") +
            "," + _Cols.getColumnList("Position", "ps.", "Position$") +
            "," + _Cols.getColumnList("Country", "c.", "") +
            " from " + "Player p " +
            " inner join " + "Team t on t.TeamID = p.TeamID " +
            " inner join " + "Position ps on ps.PositionID = p.PositionID " +
            " left join " + "Country c on c.CountryID = p.CountryID " +
            " where p.PlayerID = ?"
    ),
    PLAYERINJURY_BY_PLAYERID(
            "select " + _Cols.getColumnList("Player", "p.", "Player$") +
            "," + _Cols.getColumnList("Team", "t.", "Team$") +
            "," + _Cols.getColumnList("Position", "ps.", "Position$") +
            "," + _Cols.getColumnList("Country", "c.", "") +
            "," + _Cols.getColumnList("PlayerInjury", "pi.", "PlayerInjury$") +
            " from PlayerInjury pi " +
            " inner join Player p on p.PlayerID = pi.PlayerID " +
            " inner join Team t on t.TeamID = p.TeamID " +
            " inner join Position ps on ps.PositionID = p.PositionID " +
            " inner join Country c on c.CountryID = p.CountryID " +
            " where p.PlayerID = ?"
    ),
    PLAYERS(
            "select " + _Cols.getColumnList("Player", "p.", "") +
            "," + _Cols.getColumnList("Team", "t.", "Team$") +
            "," + _Cols.getColumnList("Position", "ps.", "Position$") +
            "," + _Cols.getColumnList("Country", "c.", "") +
            " from " + "Player p " +
            " inner join " + "Team t on t.TeamID = p.TeamID " +
            " inner join " + "Position ps on ps.PositionID = p.PositionID " +
            " left join " + "Country c on c.CountryID = p.CountryID " +
            " where p.IsActive = 1"
    ),
    POSITION_BY_POSITIONID(
            "select " + _Cols.getColumnList("Position", "p.", "") +
            "," + _Cols.getColumnList("Sport", "s.", "") +
            " from " + "Position p " +
            " inner join " + "Sport s on s.SportID = p.PositionID " +
            " where p.PositionID = ?"
    ),
    POSITIONS(
            "select " + _Cols.getColumnList("Position", "", "") +
            " from " + "Position"
    ),
    SEASON_BY_SEASONID(
            "select " + _Cols.getColumnList("Season", "s.", "") +
            " from " + "Season s where s.SeasonID = ?"
    ),
    SEASONWEEK_BY_SEASONWEEKID(
            "select " + _Cols.getColumnList("SeasonWeek", "w.", "") +
            " from " + "SeasonWeek w where w.SeasonWeekID = ?"
    ),
    SPORT_BY_SPORTID(
            "select " + _Cols.getColumnList("Sport", "s.", "") +
            " from " + "Sport s where s.SportID = ?"
    ),
    TEAM_BY_TEAMID(
            "select " + _Cols.getColumnList("Team", "t.", "") +
            " from " + "Team t where TeamID=?"
    ),
    TEAMS(
            "select " + _Cols.getColumnList("Team", "", "") +
            " from " + "Team"
    ),
    UPDATE_FSFOOTBALLMATCHUP(
            " update FSFootballMatchup " +
            " set Team1Pts = ?, Team2Pts = ?, Winner = ? " +
            " where FSLeagueID = ? and FSSeasonWeekID = ? and GameNo = ?"
    ),
    UPDATE_FSFOOTBALLMAXREQUESTS(
            " update FSFootballMaxRequests " +
            " set MaxRequests = ? " +
            " where FSTeamID = ? and FSSeasonWeekID = ?"
    ),
    UPDATE_FSFOOTBALLTRANSACTIONREQUEST(
            " update FSFootballTransactionRequest" +
            " set FSSeasonWeekID = ?, FSTeamID = ?, `Rank` = ?, DropPlayerID = ?, " +
            " DropType = ?, PUPlayerID = ?, PUType = ?, RequestDate = ?, Processed = ?, Granted = ? " +
            " where RequestID = ?"
    ),
    UPDATE_FSUSER(
            "update FSUser set FirstName = ?, LastName = ?, EmailAddress = ?, PhoneNumber = ?, Address = ?, City = ?, StateID = ?, " +
            "Zip = ? where FSUserID = ?"
    ),
    UPDATE_FSROSTER(
            " Update FSRoster " +
            " set FSTeamID = ?, PlayerID = ?, FSSeasonWeekID = ?, StarterState = ?, ActiveState = ? " +
            " where ID = ?"
    ),
    UPDATE_LAST_LOGIN(
            "Update FSUser set LastLogin = ? where FSUserID = ?"
    ),
    STATE_ALL(
            "select " + _Cols.getColumnList("State") + " from " + "State")
    ;

    private String _Query;
    private String _BreakOnID;

    CTDataSetDef(String query) {
        _Query = query;
        _BreakOnID = "";
    }

    CTDataSetDef(String query, String breakOnID) {
        _Query = query;
        _BreakOnID = breakOnID;
    }

    public String getQuery() {
        return _Query;
    }

    public String getBreakOnID() { return _BreakOnID; }

}
