package org.lxy.xml.dom;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

@Slf4j
public class XPathDemo {

    public void test() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse("contacts.xml");
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        XPathExpression xpe;
        xpe = xp.compile("//contact[city = 'Chicago']/name/text()");
        Object result = xpe.evaluate(doc, XPathConstants.NODESET);
        NodeList nl = (NodeList) result;
        for (int i = 0; i < nl.getLength(); i++) {
            System.out.println(nl.item(i).getNodeValue());
        }
    }

    @Test
    public void test2() {
        int j = 0;
        for (int i = 0; i < 100; i++) {
            j = j++;
        }
        System.out.println(j);
    }

    @Test
    public void test3() {
        log.info("{}", System.currentTimeMillis());
        log.info(String.format("%04d", 25));
    }
}
