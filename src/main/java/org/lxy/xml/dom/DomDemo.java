package org.lxy.xml.dom;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DomDemo {

    public void test() {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("D:/user.xml");
            System.out.printf("Version = %s%n", doc.getXmlVersion());
            System.out.printf("Encoding = %s%n", doc.getXmlEncoding());
            System.out.printf("Standalone = %b%n%n", doc.getXmlStandalone());
            if (doc.hasChildNodes()) {
                NodeList nl = doc.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++)

                {
                    Node node = nl.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE)
                        dump((Element) node);
                }
            }
        } catch (IOException ioe) {
            System.err.println("IOE: " + ioe);
        } catch (SAXException saxe) {
            System.err.println("SAXE: " + saxe);
        } catch (FactoryConfigurationError fce) {
            System.err.println("FCE: " + fce);
        } catch (ParserConfigurationException pce) {
            System.err.println("PCE: " + pce);
        }
    }


    static void dump(Element e) {
        System.out.printf("Element: %s, %s, %s, %s%n", e.getNodeName(),
                e.getLocalName(), e.getPrefix(),
                e.getNamespaceURI());
        NamedNodeMap nnm = e.getAttributes();
        if (nnm != null)
            for (int i = 0; i < nnm.getLength(); i++) {
                Node node = nnm.item(i);
                Attr attr = e.getAttributeNode(node.getNodeName());
                System.out.printf(" Attribute %s = %s%n", attr.getName(),
                        attr.getValue());
            }
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element)
                dump((Element) node);
        }
    }
}
