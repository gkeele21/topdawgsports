package bglib.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Sep 18, 2006
 * Time: 9:17:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeUtil {


    /**
    * Return the text that a node contains. This routine:<ul>
    * <li>Ignores comments and processing instructions.
    * <li>Concatenates TEXT nodes, CDATA nodes, and the results of
    *     recursively processing EntityRef nodes.
    * <li>Ignores any element nodes in the sublist.
    *     (Other possible options are to recurse into element sublists
    *      or throw an exception.)
    * </ul>
    * @param    node  a  DOM node
    * @return   a String representing its contents
    */

    public static String getNodeValue(Node node) {

        StringBuffer result = new StringBuffer();
        if (! node.hasChildNodes()) return "";

        NodeList list = node.getChildNodes();
        for (int i=0; i < list.getLength(); i++) {
            Node subnode = list.item(i);
            if (subnode.getNodeType() == Node.TEXT_NODE) {
                result.append(subnode.getNodeValue());
            } else if (subnode.getNodeType() == Node.CDATA_SECTION_NODE) {
                result.append(subnode.getNodeValue());
            } else if (subnode.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
                // Recurse into the subtree for text
                // (and ignore comments)
                result.append(getNodeValue(subnode));
            }
        }
        return result.toString();
    }

    public static Document convertTableToDocument(String orig) {
        Document document = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Remove bad XML text
            orig = replaceNonXMLText(orig);
            ByteArrayInputStream stream = new ByteArrayInputStream(orig.getBytes());
//            System.out.println("Converting to Document Object : " + orig);
            document = builder.parse(stream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return document;
    }

    public static String replaceNonXMLText(String orig) {

        orig = AuUtil.replace(orig,"&nbsp;"," ");
        //orig = AuUtil.replace(orig,"&amp;","&");

        return orig;
    }

}
