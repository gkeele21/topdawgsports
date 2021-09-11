package tds.main.bo.football.stats.MySportsFeeds;

public class Players_PlayerObj {

    public Players_PlayerInfo player;

    public Object teamAsOfDate;

    public Players_PlayerInfo getPlayer() {
        return player;
    }

    public void setPlayer(Players_PlayerInfo player) {
        this.player = player;
    }

    public Object getTeamAsOfDate() {
        return teamAsOfDate;
    }

    public void setTeamAsOfDate(Object teamAsOfDate) {
        this.teamAsOfDate = teamAsOfDate;
    }
}
