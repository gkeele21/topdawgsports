package tds.main.bo.football.stats.MySportsFeeds;

import java.util.List;

public class Scoring_QuarterObj {

    public int quarterNumber;
    public int awayScore;
    public int homeScore;
    public List<Scoring_ScoringPlayObj> scoringPlays;

    public int getQuarterNumber() {
        return quarterNumber;
    }

    public void setQuarterNumber(int quarterNumber) {
        this.quarterNumber = quarterNumber;
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

    public List<Scoring_ScoringPlayObj> getScoringPlays() {
        return scoringPlays;
    }

    public void setScoringPlays(List<Scoring_ScoringPlayObj> scoringPlays) {
        this.scoringPlays = scoringPlays;
    }
}
