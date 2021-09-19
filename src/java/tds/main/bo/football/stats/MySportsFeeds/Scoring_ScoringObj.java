package tds.main.bo.football.stats.MySportsFeeds;

import java.util.List;

public class Scoring_ScoringObj {

    public int currentQuarter;
    public int currentQuarterSecondsRemaining;
    public int currentIntermission;
    public int teamInPossession;
    public int currentDown;
    public int currentYardsRemaining;
    public int lineOfScrimmage;
    public int awayScoreTotal;
    public int homeScoreTotal;
    public List<Scoring_QuarterObj> quarters;

    public int getCurrentQuarter() {
        return currentQuarter;
    }

    public void setCurrentQuarter(int currentQuarter) {
        this.currentQuarter = currentQuarter;
    }

    public int getCurrentQuarterSecondsRemaining() {
        return currentQuarterSecondsRemaining;
    }

    public void setCurrentQuarterSecondsRemaining(int currentQuarterSecondsRemaining) {
        this.currentQuarterSecondsRemaining = currentQuarterSecondsRemaining;
    }

    public int getCurrentIntermission() {
        return currentIntermission;
    }

    public void setCurrentIntermission(int currentIntermission) {
        this.currentIntermission = currentIntermission;
    }

    public int getTeamInPossession() {
        return teamInPossession;
    }

    public void setTeamInPossession(int teamInPossession) {
        this.teamInPossession = teamInPossession;
    }

    public int getCurrentDown() {
        return currentDown;
    }

    public void setCurrentDown(int currentDown) {
        this.currentDown = currentDown;
    }

    public int getCurrentYardsRemaining() {
        return currentYardsRemaining;
    }

    public void setCurrentYardsRemaining(int currentYardsRemaining) {
        this.currentYardsRemaining = currentYardsRemaining;
    }

    public int getLineOfScrimmage() {
        return lineOfScrimmage;
    }

    public void setLineOfScrimmage(int lineOfScrimmage) {
        this.lineOfScrimmage = lineOfScrimmage;
    }

    public int getAwayScoreTotal() {
        return awayScoreTotal;
    }

    public void setAwayScoreTotal(int awayScoreTotal) {
        this.awayScoreTotal = awayScoreTotal;
    }

    public int getHomeScoreTotal() {
        return homeScoreTotal;
    }

    public void setHomeScoreTotal(int homeScoreTotal) {
        this.homeScoreTotal = homeScoreTotal;
    }

    public List<Scoring_QuarterObj> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<Scoring_QuarterObj> quarters) {
        this.quarters = quarters;
    }
}
