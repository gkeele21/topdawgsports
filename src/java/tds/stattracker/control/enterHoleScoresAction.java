package tds.stattracker.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.control.BaseAction;

public class enterHoleScoresAction extends BaseAction {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }
/*
        // Save the Hole Score
        
        int holeID = FSUtils.getIntRequestParameter(request, "currentHoleID", 0);
        int golfRoundID = 0;
        if (holeID > 0) {
            System.out.println("HoleID : " + holeID);
            UserSession session = UserSession.getUserSession(request, response);
            FSUser user = (FSUser)session.getHttpSession().getAttribute("validUser");
            Object tempID = session.getHttpSession().getAttribute("golferRoundID");
            if (tempID != null) {
                golfRoundID = Integer.parseInt(tempID.toString());
            }
            GolferRound currentGolferRound = new GolferRound(golfRoundID);
            if (currentGolferRound != null) {
                
                Hole currentHole = (Hole)session.getHttpSession().getAttribute("currentHole");
                
                // Cycle through the players' golferRounds and set their scores
                List<GolferRound> golferRounds = (List<GolferRound>)session.getHttpSession().getAttribute("golfGroup");
                
                for (GolferRound golferRound : golferRounds) {
                    HoleScore holeScore = golferRound.getHoleScore(currentHole);

                    if (holeScore == null) {
                        holeScore = new HoleScore();
                        holeScore.setGolferRoundID(golferRound.getGolferRoundID());
                        holeScore.setHoleID(holeID);
                        HoleScore.InsertHoleScore(holeScore);
                    }
                    // set HoleScore values
                    Integer strokes = FSUtils.getIntegerRequestParameter(request, "score_" + golferRound.getGolferRoundID(),null);
                    if (strokes > 0) {
                        holeScore.setStrokes(strokes);
                    }
                    
                    // update all other data, if this is the logged in user
                    if (user.getFSUserID() == golferRound.getFSUserID()) {
                        Integer gir = FSUtils.getIntegerRequestParameter(request, "GIR_" + golferRound.getGolferRoundID(), null);
                        if (gir != null) {
                            try {
                                holeScore.setGIR(gir);
                            } catch (Exception e) {}
                        }

                        Integer putts = FSUtils.getIntegerRequestParameter(request, "putts_" + golferRound.getGolferRoundID(), null);
                        if (putts != null) {
                            try {
                                holeScore.setPutts(putts);
                            } catch (Exception e) {}
                        }
                        
                        Integer puttDist = FSUtils.getIntegerRequestParameter(request, "puttDistance_" + golferRound.getGolferRoundID(), null);
                        if (puttDist != null) {
                            try {
                                holeScore.setFinalPuttDistance(puttDist);
                            } catch (Exception e) {}
                        }
                        
                        Integer penalty = FSUtils.getIntegerRequestParameter(request, "penaltyStrokes_" + golferRound.getGolferRoundID(), null);
                        if (penalty != null) {
                            try {
                                holeScore.setPenaltyStrokes(penalty);
                            } catch (Exception e) {}
                        }
                        
                        Integer fairway = FSUtils.getIntegerRequestParameter(request, "fairway_" + golferRound.getGolferRoundID(), null);
                        if (fairway != null) {
                            try {
                                holeScore.setHitFairway(fairway);
                            } catch (Exception e) {}
                        }
                        
                        Integer sandAtt = FSUtils.getIntegerRequestParameter(request, "sandSaveOpp_" + golferRound.getGolferRoundID(), null);
                        if (sandAtt != null) {
                            try {
                                holeScore.setSandSaveOpp(sandAtt);
                            } catch (Exception e) {}
                        }
                        
                        Integer sandComp = FSUtils.getIntegerRequestParameter(request, "sandSaveComp_" + golferRound.getGolferRoundID(), null);
                        if (sandComp != null) {
                            try {
                                holeScore.setSandSaveComp(sandComp);
                            } catch (Exception e) {}
                        }
                        
                        Integer upDownAtt = FSUtils.getIntegerRequestParameter(request, "upDownOpp_" + golferRound.getGolferRoundID(), null);
                        if (upDownAtt != null) {
                            try {
                                holeScore.setUpDownOpp(upDownAtt);
                            } catch (Exception e) {}
                        }
                        
                        Integer upDownComp = FSUtils.getIntegerRequestParameter(request, "upDownComp_" + golferRound.getGolferRoundID(), null);
                        if (upDownComp != null) {
                            try {
                                holeScore.setUpDownComp(upDownComp);
                            } catch (Exception e) {}
                        }
                        
                        String notes = FSUtils.getRequestParameter(request, "notes_" + golferRound.getGolferRoundID(), null);
                        if (notes != null) {
                            try {
                                holeScore.setNotes(notes);
                            } catch (Exception e) {}
                        }
                    }
                    
                    HoleScore.SaveHoleScore(holeScore);
                    golferRound.resetHoleScores();
                    
                }
                
                // set to the next hole
                int currentHoleNumber = Integer.parseInt(session.getHttpSession().getAttribute("currentHoleNumber").toString());
                currentHoleNumber++;
                System.out.println("Setting Current Hole Number to " + currentHoleNumber);
                session.getHttpSession().setAttribute("currentHoleNumber", currentHoleNumber);

            }
        }
        */
        nextPage = "enterHoleScores";

        return nextPage;
    }
}