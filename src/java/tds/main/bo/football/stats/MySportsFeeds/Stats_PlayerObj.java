package tds.main.bo.football.stats.MySportsFeeds;

import java.util.List;

public class Stats_PlayerObj {

    public Stats_PlayerInfo player;
    public List<Stats_PlayerStats> playerStats;

    public Stats_PlayerInfo getPlayer() {
        return player;
    }

    public void setPlayer(Stats_PlayerInfo player) {
        this.player = player;
    }

    public List<Stats_PlayerStats>
    getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(List<Stats_PlayerStats> playerStats) {
        this.playerStats = playerStats;
    }
}
