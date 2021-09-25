package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.Application;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.data.CTDataSetDef;
import tds.util.CTReturnCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;
import static tds.util.CTReturnCode.RC_DB_ERROR;
import static tds.util.CTReturnType.SUCCESS;

public class FSLeague implements Serializable {

    public enum Status {FORMING, ACTIVE, DEAD};

    // DB FIELDS
    private Integer _FSLeagueID;
    private Integer _FSSeasonID;
    private String _LeagueName;
    private String _LeaguePassword;
    private Integer _IsFull;
    private Integer _IsPublic;
    private Integer _NumTeams;
    private String _Description;
    private Integer _IsGeneral;
    private Integer _StartFSSeasonWeekID;
    private Integer _VendorID;
    private String _DraftType;
    private LocalDateTime _DraftDate;
    private Integer _HasPaid;
    private Integer _IsDraftComplete;
    private Integer _CommissionerUserID;
    private Integer _IsCustomLeague;
    private String _ScheduleName;
    private Integer _IsDefaultLeague;
    private String _SignupType;
    private String _Status;
    private Integer _IncludeTEasWR;
    private Integer _IncludeIDP;
    private Integer _IncludeIR;

    // OBJECTS
    private FSSeason _FSSeason;

    // CONSTRUCTORS
    public FSLeague() {
    }

    public FSLeague(int leagueID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSLeague", "l.", ""));
            sql.append(",").append(_Cols.getColumnList("FSSeason", "s.", "FSSeason$"));
            sql.append(",").append(_Cols.getColumnList("FSGame", "g.", "FSGame$"));
            sql.append(",").append(_Cols.getColumnList("Sport", "sp.", "Sport$"));
            sql.append(",").append(_Cols.getColumnList("Season", "se.", "Season$"));
            sql.append(" FROM FSLeague l ");
            sql.append(" INNER JOIN FSSeason s ON s.FSSeasonID = l.FSSeasonID ");
            sql.append(" INNER JOIN FSGame g ON g.FSGameID = s.FSGameID ");
            sql.append(" INNER JOIN Sport sp ON sp.SportID = g.SportID ");
            sql.append(" INNER JOIN Season se ON se.SeasonID = s.SeasonID ");
            sql.append(" WHERE l.FSLeagueID = ").append(leagueID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public FSLeague(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public FSLeague(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getFSLeagueID() {return _FSLeagueID;}
    public Integer getFSSeasonID() {return _FSSeasonID;}
    public String getLeagueName() {return _LeagueName;}
    public String getLeaguePassword() {return _LeaguePassword;}
    public Integer getIsFull() {return _IsFull;}
    public Integer getIsPublic() {return _IsPublic;}
    public Integer getNumTeams() {return _NumTeams;}
    public String getDescription() {return _Description;}
    public Integer getIsGeneral() {return _IsGeneral;}
    public Integer getStartFSSeasonWeekID() {return _StartFSSeasonWeekID;}
    public Integer getVendorID() {return _VendorID;}
    public String getDraftType() {return _DraftType;}
    public LocalDateTime getDraftDate() {return _DraftDate;}
    public Integer getHasPaid() {return _HasPaid;}
    public Integer getIsDraftComplete() {return _IsDraftComplete;}
    public Integer getCommissionerUserID() {return _CommissionerUserID;}
    public Integer getIsCustomLeague() {return _IsCustomLeague;}
    public String getScheduleName() {return _ScheduleName;}
    public Integer getIsDefaultLeague() {return _IsDefaultLeague;}
    public String getSignupType() {return _SignupType;}
    public String getStatus() {return _Status;}
    public Integer getIncludeTEasWR() {return _IncludeTEasWR;}
    public Integer getIncludeIDP() {return _IncludeIDP;}
    public Integer getIncludeIR() {return _IncludeIR;}
    public FSSeason getFSSeason() {if (_FSSeason == null && _FSSeasonID > 0) {_FSSeason = new FSSeason(_FSSeasonID);}return _FSSeason;}

    // SETTERS
    public void setFSLeagueID(Integer FSLeagueID) {_FSLeagueID = FSLeagueID;}
    public void setFSSeasonID(Integer FSSeasonID) {_FSSeasonID = FSSeasonID;}
    public void setLeagueName(String LeagueName) {_LeagueName = LeagueName;}
    public void setLeaguePassword(String LeaguePassword) {_LeaguePassword = LeaguePassword;}
    public void setIsFull(Integer IsFull) {_IsFull = IsFull;}
    public void setIsPublic(Integer IsPublic) {_IsPublic = IsPublic;}
    public void setNumTeams(Integer NumTeams) {_NumTeams = NumTeams;}
    public void setDescription(String Description) {_Description = Description;}
    public void setIsGeneral(Integer IsGeneral) {_IsGeneral = IsGeneral;}
    public void setStartFSSeasonWeekID(Integer StartFSSeasonWeekID) {_StartFSSeasonWeekID = StartFSSeasonWeekID;}
    public void setVendorID(Integer VendorID) {_VendorID = VendorID;}
    public void setDraftType(String DraftType) {_DraftType = DraftType;}
    public void setDraftDate(LocalDateTime DraftDate) {_DraftDate = DraftDate;}
    public void setHasPaid(Integer HasPaid) {_HasPaid = HasPaid;}
    public void setIsDraftComplete(Integer IsDraftComplete) {_IsDraftComplete = IsDraftComplete;}
    public void setCommissionerUserID(Integer CommissionerUserID) {_CommissionerUserID = CommissionerUserID;}
    public void setIsCustomLeague(Integer IsCustomLeague) {_IsCustomLeague = IsCustomLeague;}
    public void setScheduleName(String ScheduleName) {_ScheduleName = ScheduleName;}
    public void setIsDefaultLeague(Integer IsDefaultLeague) {_IsDefaultLeague = IsDefaultLeague;}
    public void setSignupType(String SignupType) {_SignupType = SignupType;}
    public void setStatus(String Status) {_Status = Status;}
    public void setIncludeTEasWR(Integer IncludeTEasWR) {_IncludeTEasWR = IncludeTEasWR;}
    public void setIncludeIDP(Integer IncludeIDP) {_IncludeIDP = IncludeIDP;}
    public void setIncludeIR(Integer IncludeIR) {_IncludeIR = IncludeIR;}
    public void setFSSeason(FSSeason FSSeason) {_FSSeason = FSSeason;}

    // PUBLIC METHODS

    public List<FSFootballStandings> GetStandings(int fsseasonweekID,String sort) {
        return FSFootballStandings.getLeagueStandings(_FSLeagueID,fsseasonweekID,sort);
    }

    public List<FSFootballStandings> GetStandings(int fsseasonweekID,String sort, boolean requireStandingsRecords) {
        return FSFootballStandings.getLeagueStandings(_FSLeagueID,fsseasonweekID,sort, requireStandingsRecords);
    }

    public List<FSFootballMatchup> GetResults(int fsseasonweekID) {
        return FSFootballMatchup.getLeagueResults(_FSLeagueID,fsseasonweekID);
    }

    public List<FSFootballTransaction> GetTransactions(int fsseasonweekID) {
        return FSFootballTransaction.getTransactions(_FSLeagueID,fsseasonweekID);
    }

    public List<FSTeam> GetTransactionOrder(int fsseasonweekID) {
        return FSFootballTransaction.getTransactionOrder(_FSLeagueID, fsseasonweekID);
    }

    public List<Player> GetFreeAgents(int fsSeasonWeekID, String positionName) {
        List<Player> players = new ArrayList<>();

        // Add in the players dropped this week.
        List<FSFootballTransaction> transactions = GetTransactions(fsSeasonWeekID);
        StringBuilder except = new StringBuilder();
        int count = 0;
        for (FSFootballTransaction transaction : transactions) {
            count++;
            if (count > 1) {
                except.append(",");
            }
            except.append(transaction.getDropPlayerID());
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append(",").append(_Cols.getColumnList("FootballStats","st.", "FootballStats$"));
        sql.append(",").append("if(st.Played > 0,st.FantasyPts / st.Played, 0) as FootballStats$AvgFantasyPts");
        sql.append(",").append(_Cols.getColumnList("FSTeam","fst.", "FSTeam$"));
        sql.append(" from Player p");
        sql.append(" join Position ps on ps.PositionID = p.PositionID");
        sql.append(" join Team t on t.TeamID = p.TeamID");
        sql.append(" left join FootballStats st on st.PlayerID = p.PlayerID");
        sql.append(" and st.SeasonWeekID = 0 and st.SeasonID =").append(getFSSeason().getSeasonID());
        sql.append(" left join FSRoster r on r.PlayerID = p.PlayerID");
        sql.append(" and r.FSSeasonWeekID = ").append(fsSeasonWeekID);
        sql.append(" and r.FSTeamID in (select FSTeamID from FSTeam WHERE FSLeagueID =").append(_FSLeagueID).append(")");
        sql.append(" left join FSTeam fst on fst.FSTeamID = r.FSTeamID");
        sql.append(" where p.IsActive = 1");
        if (positionName.length() > 0) {
            sql.append(" and ps.PositionName = '").append(positionName).append("'");
        }
        if (!("").equals(except.toString()) && except.length() >= 1) {
            sql.append(" AND p.PlayerID NOT IN (").append(except).append(")");
        }
        sql.append(" and r.PlayerID is null");
        sql.append(" ORDER BY st.FantasyPts desc, t.abbreviation, p.LastName ");

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                Player player = new Player(crs, "Player$");
                players.add(player);
            }
        } catch (Exception exception) {
            CTApplication._CT_LOG.error(exception);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return players;
    }

    public List<PlayerStats> GetPlayerStatsByWeek(int fsSeasonWeekID, String positionName) {
        List<PlayerStats> players = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append(",").append(_Cols.getColumnList("FootballStats","st.", "FootballStats$"));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek","fssw.", "FSSeasonWeek$"));
        sql.append(",").append("if(st.Played > 0,st.FantasyPts / st.Played, 0) as FootballStats$AvgFantasyPts");
        if (getFSSeason().getFSGameID() == FSGame.HEAD_TO_HEAD) { sql.append(",").append(_Cols.getColumnList("FSTeam","fst.", "FSTeam$")); }
        sql.append(" from Player p");
        sql.append(" join Position ps on ps.PositionID = p.PositionID");
        sql.append(" join Team t on t.TeamID = p.TeamID");
        sql.append(" join FootballStats st on st.PlayerID = p.PlayerID");
        sql.append(" join FSSeasonWeek fssw on fssw.SeasonWeekID = st.SeasonWeekID");
        sql.append(" and fssw.FSSeasonWeekID = ").append(fsSeasonWeekID);
        if (getFSSeason().getFSGameID() == FSGame.HEAD_TO_HEAD) {
            sql.append(" left join FSRoster r on r.PlayerID = p.PlayerID");
            sql.append(" and r.FSSeasonWeekID = fssw.FSSeasonWeekID");
            sql.append(" and r.FSTeamID in (select FSTeamID from FSTeam WHERE FSLeagueID =").append(_FSLeagueID).append(")");
            sql.append(" left join FSTeam fst on fst.FSTeamID = r.FSTeamID");
        }
        sql.append(" where p.IsActive = 1");
        sql.append(" and st.FantasyPts > 0");
        if (positionName.length() > 0) {
            sql.append(" and ps.PositionName = '").append(positionName).append("'");
        }
        sql.append(" order by st.FantasyPts desc");

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                players.add(new PlayerStats(crs));
            }
        } catch (Exception exception) {
            CTApplication._CT_LOG.error(exception);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return players;
    }

    public List<PlayerStats> GetPlayerStatsTotal(int fsSeasonWeekID, String positionName) {
        List<PlayerStats> players = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append(",").append(_Cols.getColumnList("FootballStats","st.", "FootballStats$"));
        sql.append(",").append("if(st.Played > 0,st.FantasyPts / st.Played, 0) as FootballStats$AvgFantasyPts");
        if (getFSSeason().getFSGameID() == FSGame.HEAD_TO_HEAD) { sql.append(",").append(_Cols.getColumnList("FSTeam","fst.", "FSTeam$")); }
        sql.append(" from Player p");
        sql.append(" join Position ps on ps.PositionID = p.PositionID");
        sql.append(" join Team t on t.TeamID = p.TeamID");
        sql.append(" join FootballStats st on st.PlayerID = p.PlayerID");
        sql.append(" and st.SeasonWeekID = 0 and st.SeasonID =").append(getFSSeason().getSeasonID());
        if (getFSSeason().getFSGameID() == FSGame.HEAD_TO_HEAD) {
            sql.append(" left join FSRoster r on r.PlayerID = p.PlayerID");
            sql.append(" and r.FSSeasonWeekID = ").append(fsSeasonWeekID);
            sql.append(" and r.FSTeamID in (select FSTeamID from FSTeam WHERE FSLeagueID =").append(_FSLeagueID).append(")");
            sql.append(" left join FSTeam fst on fst.FSTeamID = r.FSTeamID");
        }
        sql.append(" where p.IsActive = 1");
        sql.append(" and st.FantasyPts > 0");
        if (positionName.length() > 0) {
            sql.append(" and ps.PositionName = '").append(positionName).append("'");
        }
        sql.append(" order by st.FantasyPts desc");

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                players.add(new PlayerStats(crs));
            }
        } catch (Exception exception) {
            CTApplication._CT_LOG.error(exception);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return players;
    }

    public int AddTeam(FSUser user, String teamname) {
        int teamid = 0;
        try {
            teamid = FSTeam.addNewTeam(_FSLeagueID, user.getFSUserID(), teamname);
            if (teamid > 0) {
                // do something
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
        return teamid;
    }

    public CTReturnCode InsertIntoStandings(int fsteamid, int fsseasonweekid, double fantasypts,
                            double totalfantasypts, double gamepoints, double totalgamepoints,
                            int salaryspent, int wins, int losses, int ties, double winpercentage,
                            double fantasyptsagainst, int hiscore, int totalhiscores,
                            int gamescorrect, int totalgamescorrect, int gameswrong,
                            int totalgameswrong, int rank, int currentstreak, String lastfive) {

        int id = CTApplication._CT_DB.updateDataSet(CTDataSetDef.INSERT_FSFOOTBALLSTANDINGS, fsteamid, fsseasonweekid,
                            fantasypts, totalfantasypts, gamepoints, totalgamepoints,
                            salaryspent, wins, losses, ties, winpercentage, fantasyptsagainst,
                            hiscore, totalhiscores, gamescorrect, totalgamescorrect,
                            gameswrong, totalgameswrong, rank, currentstreak, lastfive);

        System.out.println("New user id : " + id);
        CTReturnCode ret = (id>0) ? new CTReturnCode(SUCCESS, id) : RC_DB_ERROR;

        return ret;

    }

    public List<FSTeam> GetTeams() {
        List<FSTeam> teams = new ArrayList<FSTeam>();
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSTeam", "t.", ""));
            sql.append(" FROM FSTeam t ");
            sql.append(" WHERE t.FSLeagueID = ").append(_FSLeagueID);
            sql.append(" AND t.isActive = 1 ");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                FSTeam tempTeam = new FSTeam(crs);
                teams.add(tempTeam);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return teams;
    }

    public List<FSPlayerValueSelected> GetPlayersSelected(int fsseasonweekid,String sort) throws Exception {
        if (sort == null || sort.equals("")) {
            sort = " FSPlayerValueSelected$Count desc";
        }
        if (sort.indexOf("FSPlayerValueSelected$Count") < 0) {
            sort += " ,FSPlayerValueSelected$Count desc";
        }
        List<FSPlayerValueSelected> ret = new ArrayList<FSPlayerValueSelected>();

        List<FSTeam> leagueTeams = GetTeams();
        StringBuffer teamsStr = new StringBuffer();
        int count = 0;
        for (FSTeam team : leagueTeams) {
            count++;
            if (count > 1) {
                teamsStr.append(",");
            }
            teamsStr.append(team.getFSTeamID());
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" select count(r.PlayerID) as FSPlayerValueSelected$Count ");
        sql.append(",").append(_Cols.getColumnList("FSPlayerValue", "pv.", "FSPlayerValue$"));
        sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek","fsw.", "FSSeasonWeek$"));
        sql.append(",").append(_Cols.getColumnList("FootballStats","st.", "FootballStats$"));
        sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
        sql.append(",").append(_Cols.getColumnList("FSRoster","r.", "FSRoster$"));
        sql.append(" from FSPlayerValue pv ");
        sql.append(" inner join Player p on p.PlayerID = pv.PlayerID ");
        sql.append(" inner join Team t on t.TeamID = p.TeamID ");
        sql.append(" inner join FSRoster r on r.PlayerID = p.PlayerID and r.FSSeasonWeekID = ").append(fsseasonweekid);
        sql.append(" and r.FSTeamID in (").append(teamsStr).append(")");
        sql.append(" inner join Position ps on ps.PositionID = p.PositionID ");
        sql.append(" left join Country c on c.CountryID = p.CountryID ");
        sql.append(" inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = pv.FSSeasonWeekID ");
//        sql.append(" left join FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = fsw.SeasonWeekID ");
//        sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 ");
        sql.append(" left join FootballStats st on st.StatsPlayerID = p.NFLGameStatsID and st.SeasonWeekID = fsw.SeasonWeekID ");
        sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.NFLGameStatsID and tst.SeasonWeekID = 0 ");
        sql.append(" where pv.FSSeasonWeekID = ").append(fsseasonweekid);
        sql.append(" group by r.PlayerID ");
        sql.append(" order by ").append(sort);

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                ret.add(new FSPlayerValueSelected(crs,"FSPlayerValueSelected$"));
            }
        } catch (Exception exception) {
            CTApplication._CT_LOG.error(exception);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return ret;

    }

    public boolean GetUserAlreadyInLeague(int fsUserID) {
        boolean exists = false;
        CachedRowSet crs = null;
        if (fsUserID > 0) {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append(" SELECT count(*) as cnt ");
                sql.append(" FROM FSTeam ");
                sql.append(" WHERE FSLeagueID = ").append(_FSLeagueID);
                sql.append(" AND FSUserID = ").append(fsUserID);

                crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
                if (crs.next()) {
                    int cnt = crs.getInt("cnt");
                    if (cnt > 0) {
                        exists = true;
                    }
                }

            } catch (Exception e) {
                CTApplication._CT_LOG.error(e);
            } finally {
                JDBCDatabase.closeCRS(crs);
            }
        }

        return exists;
    }

    public static List<FSLeague> GetLeagues(int fsSeasonId) {
        List<FSLeague> leagues = new ArrayList<FSLeague>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("FSLeague", "l.", "")).append(", ");
        sql.append(_Cols.getColumnList("FSSeason", "fss.", "FSSeason$"));
        sql.append("FROM FSLeague l ");
        sql.append("JOIN FSSeason fss ON fss.FSSeasonID = l.FSSeasonID ");
        sql.append("WHERE l.Status = 'ACTIVE' AND fss.FSSeasonID = ").append(fsSeasonId);

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                leagues.add(new FSLeague(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return leagues;
   }

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSLeague", "FSLeagueID", getFSLeagueID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSLeagueID")) { setFSLeagueID(crs.getInt(prefix + "FSLeagueID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonID")) { setFSSeasonID(crs.getInt(prefix + "FSSeasonID")); }
            if (FSUtils.fieldExists(crs, prefix, "LeagueName")) { setLeagueName(crs.getString(prefix + "LeagueName")); }
            if (FSUtils.fieldExists(crs, prefix, "LeaguePassword")) { setLeaguePassword(crs.getString(prefix + "LeaguePassword")); }
            if (FSUtils.fieldExists(crs, prefix, "IsFull")) { setIsFull(crs.getInt(prefix + "IsFull")); }
            if (FSUtils.fieldExists(crs, prefix, "IsPublic")) { setIsPublic(crs.getInt(prefix + "IsPublic")); }
            if (FSUtils.fieldExists(crs, prefix, "NumTeams")) { setNumTeams(crs.getInt(prefix + "NumTeams")); }
            if (FSUtils.fieldExists(crs, prefix, "Description")) { setDescription(crs.getString(prefix + "Description")); }
            if (FSUtils.fieldExists(crs, prefix, "IsGeneral")) { setIsGeneral(crs.getInt(prefix + "IsGeneral")); }
            if (FSUtils.fieldExists(crs, prefix, "StartFSSeasonWeekID")) { setStartFSSeasonWeekID(crs.getInt(prefix + "StartFSSeasonWeekID")); }
            if (FSUtils.fieldExists(crs, prefix, "VendorID")) { setVendorID(crs.getInt(prefix + "VendorID")); }
            if (FSUtils.fieldExists(crs, prefix, "DraftType")) { setDraftType(crs.getString(prefix + "DraftType")); }
            if (FSUtils.fieldExists(crs, prefix, "DraftDate")) {
                Object s = crs.getObject(prefix + "DraftDate");
                if (s != null) {
                    setDraftDate((LocalDateTime)s);
                }
            }
            if (FSUtils.fieldExists(crs, prefix, "HasPaid")) { setHasPaid(crs.getInt(prefix + "HasPaid")); }
            if (FSUtils.fieldExists(crs, prefix, "IsDraftComplete")) { setIsDraftComplete(crs.getInt(prefix + "IsDraftComplete")); }
            if (FSUtils.fieldExists(crs, prefix, "CommissionerUserID")) { setCommissionerUserID(crs.getInt(prefix + "CommissionerUserID")); }
            if (FSUtils.fieldExists(crs, prefix, "IsCustomLeague")) { setIsCustomLeague(crs.getInt(prefix + "IsCustomLeague")); }
            if (FSUtils.fieldExists(crs, prefix, "ScheduleName")) { setScheduleName(crs.getString(prefix + "ScheduleName")); }
            if (FSUtils.fieldExists(crs, prefix, "IsDefaultLeague")) { setIsDefaultLeague(crs.getInt(prefix + "IsDefaultLeague")); }
            if (FSUtils.fieldExists(crs, prefix, "SignupType")) { setSignupType(crs.getString(prefix + "SignupType")); }
            if (FSUtils.fieldExists(crs, prefix, "Status")) { setStatus(crs.getString(prefix + "Status")); }
            if (FSUtils.fieldExists(crs, prefix, "IncludeTEasWR")) { setIncludeTEasWR(crs.getInt(prefix + "IncludeTEasWR")); }
            if (FSUtils.fieldExists(crs, prefix, "IncludeIDP")) { setIncludeIDP(crs.getInt(prefix + "IncludeIDP")); }
            if (FSUtils.fieldExists(crs, prefix, "IncludeIR")) { setIncludeIR(crs.getInt(prefix + "IncludeIR")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSSeason$", "FSSeasonID")) { setFSSeason(new FSSeason(crs, "FSSeason$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSLeague ");
        sql.append("(FSLeagueID, FSSeasonID, LeagueName, LeaguePassword, IsFull, IsPublic, NumTeams, Description, IsGeneral, StartFSSeasonWeekID, VendorID, DraftType, DraftDate, ");
        sql.append("HasPaid, IsDraftComplete, CommissionerUserID, IsCustomLeague, ScheduleName, IsDefaultLeague, SignupType, Status, IncludeTEasWR, IncludeIDP, IncludeIR) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSLeagueID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getLeagueName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getLeaguePassword(), true));
        sql.append(FSUtils.InsertDBFieldValue(getIsFull()));
        sql.append(FSUtils.InsertDBFieldValue(getIsPublic()));
        sql.append(FSUtils.InsertDBFieldValue(getNumTeams()));
        sql.append(FSUtils.InsertDBFieldValue(getDescription(), true));
        sql.append(FSUtils.InsertDBFieldValue(getIsGeneral()));
        sql.append(FSUtils.InsertDBFieldValue(getStartFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getVendorID()));
        sql.append(FSUtils.InsertDBFieldValue(getDraftType(), true));
        sql.append(FSUtils.InsertDBFieldValue((getDraftDate() == null) ? null : Application._DATE_FORMATTER.format(getDraftDate()), true));
        sql.append(FSUtils.InsertDBFieldValue(getHasPaid()));
        sql.append(FSUtils.InsertDBFieldValue(getIsDraftComplete()));
        sql.append(FSUtils.InsertDBFieldValue(getCommissionerUserID()));
        sql.append(FSUtils.InsertDBFieldValue(getIsCustomLeague()));
        sql.append(FSUtils.InsertDBFieldValue(getScheduleName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getIsDefaultLeague()));
        sql.append(FSUtils.InsertDBFieldValue(getSignupType()));
        sql.append(FSUtils.InsertDBFieldValue(getStatus(), true));
        sql.append(FSUtils.InsertDBFieldValue(getIncludeTEasWR()));
        sql.append(FSUtils.InsertDBFieldValue(getIncludeIDP()));
        sql.append(FSUtils.InsertDBFieldValue(getIncludeIR()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSLeague SET ");
        sql.append(FSUtils.UpdateDBFieldValue("FSSeasonID", getFSSeasonID()));
        sql.append(FSUtils.UpdateDBFieldValue("LeagueName", getLeagueName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("LeaguePassword", getLeaguePassword(), true));
        sql.append(FSUtils.UpdateDBFieldValue("IsFull", getIsFull()));
        sql.append(FSUtils.UpdateDBFieldValue("IsPublic", getIsPublic()));
        sql.append(FSUtils.UpdateDBFieldValue("NumTeams", getNumTeams()));
        sql.append(FSUtils.UpdateDBFieldValue("Description", getDescription(), true));
        sql.append(FSUtils.UpdateDBFieldValue("IsGeneral", getIsGeneral()));
        sql.append(FSUtils.UpdateDBFieldValue("StartFSSeasonWeekID", getStartFSSeasonWeekID()));
        sql.append(FSUtils.UpdateDBFieldValue("VendorID", getVendorID()));
        sql.append(FSUtils.UpdateDBFieldValue("DraftType", getDraftType(), true));
        sql.append(FSUtils.UpdateDBFieldValue("DraftDate", (getDraftDate() == null) ? null : Application._DATE_TIME_FORMATTER.format(getDraftDate()), true));
        sql.append(FSUtils.UpdateDBFieldValue("HasPaid", getHasPaid()));
        sql.append(FSUtils.UpdateDBFieldValue("IsDraftComplete", getIsDraftComplete()));
        sql.append(FSUtils.UpdateDBFieldValue("CommissionerUserID", getCommissionerUserID()));
        sql.append(FSUtils.UpdateDBFieldValue("IsCustomLeague", getIsCustomLeague()));
        sql.append(FSUtils.UpdateDBFieldValue("ScheduleName", getScheduleName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("IsDefaultLeague", getIsDefaultLeague()));
        sql.append(FSUtils.UpdateDBFieldValue("SignupType", getSignupType()));
        sql.append(FSUtils.UpdateDBFieldValue("Status", getStatus(), true));
        sql.append(FSUtils.UpdateDBFieldValue("InclueTEasWR", getIncludeTEasWR()));
        sql.append(FSUtils.UpdateDBFieldValue("InclueIDP", getIncludeIDP()));
        sql.append(FSUtils.UpdateDBFieldValue("InclueIR", getIncludeIR()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSLeagueID = ").append(getFSLeagueID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    public List<FSRoster> GetIRPlayers(int fsSeasonWeekID) {
        List<FSRoster> roster = new ArrayList<FSRoster>();
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ").append(_Cols.getColumnList("FSRoster", "r.", ""));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "fsteam.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","st.","FootballStats$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
            sql.append(",").append(_Cols.getColumnList("Country","cnt.","Country$"));
            sql.append(" from FSRoster r ");
            sql.append(" inner join FSTeam fsteam on fsteam.FSTeamID = r.FSTeamID ");
            sql.append(" inner join Player p on p.PlayerID = r.PlayerID ");
            sql.append(" left join Country cnt on cnt.CountryID = p.CountryID ");
            sql.append(" inner join Position ps on ps.PositionID = p.PositionID ");
            sql.append(" inner join Team t on t.TeamID = p.TeamID ");
            sql.append(" inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" inner join SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID ");
            sql.append(" left join FootballStats st on st.StatsPlayerID = p.NFLGameStatsID and st.SeasonWeekID = sw.SeasonWeekID ");
            sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.NFLGameStatsID and tst.SeasonWeekID = 0 and tst.SeasonID = sw.SeasonID");
            sql.append(" where fsteam.FSLeagueID = ").append(_FSLeagueID);
            sql.append(" and r.FSSeasonWeekID = ").append(fsSeasonWeekID);
            sql.append(" and (r.ActiveState = 'ir' || r.ActiveState = 'ir-covid') ");
            sql.append(" order by r.FSTeamID, p.PositionID, r.ID");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                FSRoster tempRoster = new FSRoster(crs);
                roster.add(tempRoster);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return roster;
    }

}
