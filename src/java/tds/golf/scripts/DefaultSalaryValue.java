/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tds.main.bo.*;

/**
 *
 * @author grant.keele
 */
public class DefaultSalaryValue implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public DefaultSalaryValue() {
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);
    }

    @Override
    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    @Override
    public ResultCode getResultCode() { return _ResultCode; }

    @Override
    public void setScriptArgs(String[] args) { _Args = args; }

    public void run(int pgaTournamentId, int fsSeasonWeekId) {
        try {

            _Logger.info("Starting to calculate Salary Values...");
            calculateValues(pgaTournamentId, fsSeasonWeekId);
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in DefaultSalaryValue.run()", e);
        }
    }

    public void calculateValues(int pgaTournamentId, int fsSeasonWeekId) throws Exception {

        StringBuilder sb = new StringBuilder();
        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);
            
            PGATournamentWeek tournamentWeek = new PGATournamentWeek(pgaTournamentId, fsSeasonWeekId);
            
            List<PGATournamentWeekPlayer> players = tournamentWeek.GetField("twp.WorldGolfRanking");
            
            int playerNum = 1;
            for (PGATournamentWeekPlayer weekPlayer : players) {
//                int wgr = weekPlayer.getWorldGolfRanking();
                
                System.out.print("   PlayerId  : " + weekPlayer.getPlayerID());
                
                // read this up in our default table
                int salaryValue = PGASalaryValueDefault._DefaultValue;
                
                PGASalaryValueDefault salDefault = new PGASalaryValueDefault(playerNum);
                
                if (salDefault != null && salDefault.getRank() > 0) {
                    salaryValue = salDefault.getSalaryValue();
                }
                
                weekPlayer.setSalaryValue(Double.parseDouble(""+salaryValue));
                weekPlayer.Save();
                
                System.out.println(" - Salary Value : " + salaryValue);
                playerNum++;
            }

        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "DefaultSalaryValue Creation Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {

        }

    }

    public static void main(String[] args) {
        try {
            DefaultSalaryValue field = new DefaultSalaryValue();
            
            FSGame fsGame = new FSGame(12);
            int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
            FSSeason fsseason = new FSSeason(currentFSSeasonID);

            FSSeasonWeek fsSeasonWeek = fsseason.getCurrentFSSeasonWeek();
            int fsSeasonWeekId = fsSeasonWeek.getFSSeasonWeekID();
            
            PGATournamentWeek tournamentWeek = PGATournamentWeek.getTournamentWeek(fsSeasonWeekId);
            int pgaTournamentId = tournamentWeek.getPGATournamentID();
            
            if (args != null && args.length > 0) {
                try {
                    pgaTournamentId = Integer.parseInt(args[0]);
                    fsSeasonWeekId = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    
                }
            }
            
            if (fsSeasonWeekId == 0 || pgaTournamentId == 0)
            {
                System.out.println("Error: You must pass in PGATournamentID as the 1st param and FSSeasonWeekId as the 2nd param");
                return;
            }
            field.run(pgaTournamentId, fsSeasonWeekId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
