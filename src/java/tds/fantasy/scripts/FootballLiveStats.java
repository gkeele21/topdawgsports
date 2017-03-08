/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tds.fantasy.scripts;

import bglib.util.AppSettings;
import bglib.util.Application;
import bglib.util.FSUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import tds.main.bo.FSGame;
import tds.main.bo.FSSeason;
import tds.main.bo.FSSeasonWeek;

/**
 *
 * @author grant
 */
public class FootballLiveStats {

    Logger _Logger;
    
    public FootballLiveStats() {
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);
    }
    
    public void setLogger(Logger logger) {
        _Logger = logger;
    }


    public static void main(String[] args) {
        try
        {
            FootballLiveStats stats = new FootballLiveStats();
            
            System.out.println("Starting Football Live Stats...");
            
            FSGame fsGame = new FSGame(1);
            int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
            FSSeason currentFSSeason = new FSSeason(currentFSSeasonID);
            FSSeasonWeek fsseasonweek = currentFSSeason.getCurrentFSSeasonWeek();

            if (fsseasonweek == null)
            {
                System.out.println("Error : no current fsseasonweek foundfor FSSeasonId " + currentFSSeasonID);
            }
            
            int currentSeasonID = currentFSSeason.getSeasonID();
            int seasonWeekNum = fsseasonweek.getFSSeasonWeekNo();
            String statsPath = FootballStatsJson._STATS_DIR;
            
            // Retrieve stats through Python
            System.out.println("Calling Python code...");
            stats.runPythonScripts(FootballStatsJson._Year, seasonWeekNum, statsPath);
            
            // Run Java code to run stats
            FootballStatsJson jsonStats = new FootballStatsJson();
            jsonStats.run(fsseasonweek.getFSSeasonWeekID());
            
            // Run Java code to calc results
            FootballResults results = new FootballResults();
            results.run(false, fsseasonweek.getFSSeasonWeekID());

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void runPythonScripts(String year, int weekNo, String statsPath)
    {
        try
        {
            AppSettings appSettings = Application._GLOBAL_APPLICATION.getAppSettings();
            String pythonPath = Application._GLOBAL_SETTINGS.getProperty(AppSettings.PYTHON_PATH);
            String pythonCommand = Application._GLOBAL_SETTINGS.getProperty(AppSettings.PYTHON_COMMAND);
            if (FSUtils.isEmpty(pythonCommand))
            {
                pythonCommand = "python";
            }

            String[] files = {"getNFLWeekStats.py"};
            
            for (String file : files)
            {
                String command = pythonCommand + " " + pythonPath + file + " " + year + " " + weekNo + " " + statsPath;
                System.out.println("Command : " + command);

                FSUtils.runExternalCommand(command, true, true);
                System.out.println("Done with " + file);
            }

            System.out.println("Done with getting stats through python.");
        } catch (Exception e)
        {
            System.out.println("Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
