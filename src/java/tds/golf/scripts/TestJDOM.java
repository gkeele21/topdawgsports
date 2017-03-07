/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;

import org.jdom2.input.SAXBuilder;

/**
 *
 * @author grant.keele
 */
public class TestJDOM implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public TestJDOM() {
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

            _Logger.info("Starting to retrieve World Golf Ranking...");
            test();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in WorldGolfRanking.run()", e);
        }
    }

    public void test() throws Exception {

        final String filePath = "c:\\Grant\\wgr.html";
        
        try {
            File file = new File(filePath);
            
            SAXBuilder jdomBuilder = new SAXBuilder();
            
            Document jdomDocument = jdomBuilder.build(file);
            
//            System.out.println(jdomDocument.getRootElement().getName());
            Element table = jdomDocument.getRootElement();
            
//            System.out.println(table.getNamespacesIntroduced());
            
//            List<Content> rssContents = table.getContent();
//            for (int i = 0; i < 2; i++) {
//                Content content = rssContents.get(i);
//                System.out.println("CType " + content.getCType());
//                System.out.println("Class " + content.getClass());
//            }
            
            List<Element> tableChildren = table.getChildren();
            
            Element tBody = tableChildren.get(1);
            List<Element> tBodyChildren = tBody.getChildren();
            
            int numRows = tBodyChildren.size();
            
            for (int i = 0; i < numRows; i++) {
                Element rowChild = tBodyChildren.get(i);
                
                String fullId = rowChild.getAttributeValue("id");
//                System.out.println("ID : " + fullId);
                
                if (fullId == null)
                {
                    continue;
                }
                
                // parse out the Id to get the PlayerId
                String playerId = fullId.replace("playerStatsRow", "");
                System.out.println("PlayerId : " + playerId);
                
                // for each row - get the td children
                List<Element> rowChildren = rowChild.getChildren();
                
                int numCols = rowChildren.size();
                if (numCols > 0) {
                    // col 0 is this week's ranking
                    Element wgrElement = rowChildren.get(0);
                    
                    String wgr = wgrElement.getText().trim();
                    System.out.println("   WGR : " + wgr);
                    
                    // update this player's WGR for this week
                }
            }
        
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "WorldGolfRanking Update Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {
            
        }

    }

    public static void main(String[] args) {
        try {
            TestJDOM ranking = new TestJDOM();
            ranking.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
