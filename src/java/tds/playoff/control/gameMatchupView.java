package tds.playoff.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSRoster;
import tds.main.control.BaseTeamView;
import tds.playoff.bo.PlayoffGame;

public class gameMatchupView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;

        int reqGameId = FSUtils.getIntRequestParameter(request, "gid", 0);
        PlayoffGame game = new PlayoffGame(reqGameId);
        
        List<FSRoster> topTeamRoster = FSRoster.getRoster(game.getFSTeam1ID(), game.getRound().getFSSeasonWeekID());
        List<FSRoster> bottomTeamRoster = FSRoster.getRoster(game.getFSTeam2ID(), game.getRound().getFSSeasonWeekID());

        request.setAttribute("game",game);
        request.setAttribute("topTeamRoster",topTeamRoster);
        request.setAttribute("bottomTeamRoster",bottomTeamRoster);

        return nextPage;
    }
}