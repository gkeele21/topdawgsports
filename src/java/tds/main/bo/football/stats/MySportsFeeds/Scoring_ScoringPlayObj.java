package tds.main.bo.football.stats.MySportsFeeds;

public class Scoring_ScoringPlayObj {

    public int quarterSecondsElapsed;
    public TeamObj team;
    public int scoreChange;
    public int awayScore;
    public int homeScore;
    public String playDescription;

    public int getQuarterSecondsElapsed() {
        return quarterSecondsElapsed;
    }

    public void setQuarterSecondsElapsed(int quarterSecondsElapsed) {
        this.quarterSecondsElapsed = quarterSecondsElapsed;
    }

    public TeamObj getTeam() {
        return team;
    }

    public void setTeam(TeamObj team) {
        this.team = team;
    }

    public int getScoreChange() {
        return scoreChange;
    }

    public void setScoreChange(int scoreChange) {
        this.scoreChange = scoreChange;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public String getPlayDescription() {
        return playDescription;
    }

    public void setPlayDescription(String playDescription) {
        this.playDescription = playDescription;
    }
}
