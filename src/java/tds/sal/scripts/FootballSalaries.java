/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.sal.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import tds.main.bo.*;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author grant.keele
 */
public class FootballSalaries implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;
    FSSeason _Season;
    int _FSSeasonWeekID;
    private static final double _Multiplier = 10000;
    public static final double MIN_SALARY = 20000;
    public static final double MAX_SALARY = 450000;
    private static final int _FSSeasonID = 105;
    private static final int _SeasonID = 42;

    public FootballSalaries() {
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

    @Override
    public void run() {
        try {

            _Season = new FSSeason(_FSSeasonID);

//            if (_Args.length>0) {
//                try {
//                    AuDate playDate = new AuDate(_Args[0], FSConstants.PLAYDATE_PATTERN);
//                    _WeekInfo = WeekInfo.getWeekInfo(_Season, playDate);
//                }
//                catch (Exception e) {
//                    _Logger.warning("Did not recognize argument '" + _Args[0] + "' as a date; will calculate last week's results.");
//                }
//            }
//
//            if (_WeekInfo == null || _WeekInfo.getWeekNo()==0) {
//                _WeekInfo = WeekInfo.getWeekInfo(_Season, new AuDate());
//            }

            FSSeasonWeek fsSeasonWeek = _Season.getCurrentFSSeasonWeek();
            _FSSeasonWeekID = fsSeasonWeek.getFSSeasonWeekID();
//            _FSSeasonWeekID = 801;

//            CTApplication._CT_QUICK_DB.executeUpdate("delete from FSPlayerValue where FSSeasonWeekID = " + _FSSeasonWeekID);

            generateSalaries();

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in GenerateSalaries.run()", e);
        }
    }

    public void generateSalaries() throws Exception {
        _Logger.info("Starting salaries for FSSeasonWeekID = " + _FSSeasonWeekID);

        //String sort = "AvgFantasyPts desc";
        String sort = "FantasyPts desc";
        Collection<Position> allPositions = Position.getAllPositions();
        for (Position pos : allPositions) {
            // We don't need to create Salaries for PUNTER and --
            if (pos.getPositionID() < 6 && pos.getPositionID() >= 1) {
//            if (pos.getPositionID() == 4) {
                List<Player> list = Player.getPlayerList(pos, sort, null,_SeasonID, true);
                double avg = grabTopTen(list);
                if (avg < 1) {
                    avg = 6.0;
                }
                _Logger.info(pos.getPositionName() + " : Average Fantasy Pts (top 10) : " + avg);
                createPlayerSalaries(list,avg);
            }
        }

        _Logger.info("Finished salaries");
    }

    public double grabTopTen(List<Player> list) throws Exception {
        // Grab the average of the top 10
        double total = 0.00;
        ListIterator<Player> it = list.listIterator();
        int count = 0;
        while (it.hasNext() && count < 5) {
            count++;
            Player p = it.next();
            total = p.getAvgSalFantasyPts();
            System.out.println("Total " + total);
        }
        double avg = count > 0 ? total / count : 0;

        return total;
    }

    public void createPlayerSalaries(List<Player> list,double avg) throws Exception {
        // Cycle through the list and calculate the player salaries based on the Multiplier and the avg
        // fantasy pts of the top 10 for that position.
        ListIterator<Player> it = list.listIterator(0);
        while (it.hasNext()) {
            Player player = it.next();
            double pavg = player.getAvgSalFantasyPts();
            double ratio = pavg / avg;
            //double ratio = 1;
            if (ratio > 1) {
                ratio = ((ratio - 1) / 2) + 1;
            } else {
                ratio = 1 - ((1 - ratio) / 2);
            }
            double salary = ratio * _Multiplier * pavg;
//            _Logger.info("Player = " + player.getFirstName() + " " + player.getLastName() + " : Avg = " + pavg + " : Ratio = " + ratio + " : Salary = " + salary);
            if (salary < MIN_SALARY) {
                salary = MIN_SALARY;
            }
            if (salary > MAX_SALARY) {
                salary = MAX_SALARY;
            }
            _Logger.info("Player = " + player.getFirstName() + " " + player.getLastName() + " : Avg = " + pavg + " : Ratio = " + ratio + " : Salary = " + salary);
            FSPlayerValue.setPlayerValue(player.getPlayerID(),_FSSeasonWeekID,salary);
        }
    }

    public static void main(String[] args) {
        FootballSalaries sal = new FootballSalaries();
        sal.run();
    }
}
