package tds.main.bo.football.stats;

public class MySportsFeeds_PlayerList {

    public MySportsFeeds_PlayerObj player;

    public Object teamAsOfDate;

    public MySportsFeeds_PlayerObj getPlayer() {
        return player;
    }

    public void setPlayer(MySportsFeeds_PlayerObj player) {
        this.player = player;
    }

    public Object getTeamAsOfDate() {
        return teamAsOfDate;
    }

    public void setTeamAsOfDate(Object teamAsOfDate) {
        this.teamAsOfDate = teamAsOfDate;
    }
}
