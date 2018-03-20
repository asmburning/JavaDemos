package org.lxy.xml.stax;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;

public class StaxDemo {

    public static final String FILE_PATH = "D:/user.xml";
    public static final String STREAM_OUT_PATH = "D:/StreamOut.xml";
    public static final String EVENT_OUT_PATH = "D:/EventOut.xml";

    public void testStreamReader() throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newDefaultFactory();
        XMLStreamReader xmlsr;
        xmlsr = xmlif.createXMLStreamReader(new FileReader(FILE_PATH));
        while (xmlsr.hasNext()) {
            switch (xmlsr.next()) {
                case XMLStreamReader.START_ELEMENT:
                    System.out.println("START_ELEMENT");
                    System.out.println(" Qname = " + xmlsr.getName());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    System.out.println("END_ELEMENT");
                    System.out.println(" Qname = " + xmlsr.getName());
            }
        }
    }

    public void testEventReader() throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newDefaultFactory();
        XMLEventReader xmler;
        xmler = xmlif.createXMLEventReader(new FileReader(FILE_PATH));
        while (xmler.hasNext()) {
            XMLEvent xmle = xmler.nextEvent();
            switch (xmle.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    System.out.println("START_ELEMENT");
                    System.out.println(" Qname = " +
                            ((StartElement) xmle).getName());
                    break;
                case XMLEvent.END_ELEMENT:
                    System.out.println("END_ELEMENT");
                    System.out.println(" Qname = " +
                            ((EndElement) xmle).getName());
            }
        }
    }


    public void testStreamWriter() throws Exception {
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamWriter xmlsw;
        xmlsw = xmlof.createXMLStreamWriter(new FileWriter(STREAM_OUT_PATH));
        xmlsw.writeStartDocument();
        xmlsw.setPrefix("h", "http://www.w3.org/1999/xhtml");
        xmlsw.writeStartElement("http://www.w3.org/1999/xhtml", "html");
        xmlsw.writeNamespace("h", "http://www.w3.org/1999/xhtml");
        xmlsw.writeNamespace("r", "http://www.javajeff.ca/");
        xmlsw.writeStartElement("http://www.w3.org/1999/xhtml", "head");
        xmlsw.writeStartElement("http://www.w3.org/1999/xhtml", "title");
        xmlsw.writeCharacters("Recipe");
        xmlsw.writeEndElement();
        xmlsw.writeEndElement();
        xmlsw.writeStartElement("http://www.w3.org/1999/xhtml", "body");
        xmlsw.setPrefix("r", "http://www.javajeff.ca/");
        xmlsw.writeStartElement("http://www.javajeff.ca/", "recipe");
        xmlsw.writeStartElement("http://www.javajeff.ca/", "title");
        xmlsw.writeCharacters("Grilled Cheese Sandwich");
        xmlsw.writeEndElement();
        xmlsw.writeStartElement("http://www.javajeff.ca/",
                "ingredients");
        xmlsw.setPrefix("h", "http://www.w3.org/1999/xhtml");
        xmlsw.writeStartElement("http://www.w3.org/1999/xhtml", "ul");
        xmlsw.writeStartElement("http://www.w3.org/1999/xhtml", "li");
        xmlsw.setPrefix("r", "http://www.javajeff.ca/");
        xmlsw.writeStartElement("http://www.javajeff.ca/", "ingredient");
        xmlsw.writeAttribute("qty", "2");
        xmlsw.writeCharacters("bread slice");
        xmlsw.writeEndElement();
        xmlsw.setPrefix("h", "http://www.w3.org/1999/xhtml");
        xmlsw.writeEndElement();
        xmlsw.writeEndElement();
        xmlsw.setPrefix("r", "http://www.javajeff.ca/");
        xmlsw.writeEndElement();
        xmlsw.writeEndDocument();
        xmlsw.flush();
        xmlsw.close();
    }

    public void testEventWriter() throws Exception {
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLEventWriter xmlew;
        xmlew = xmlof.createXMLEventWriter(new FileWriter(EVENT_OUT_PATH));
        final XMLEventFactory xmlef = XMLEventFactory.newFactory();
        XMLEvent event = xmlef.createStartDocument();
        xmlew.add(event);
        Iterator<Namespace> nsIter;
        nsIter = new Iterator<Namespace>() {
            int index = 0;
            Namespace[] ns;

            {
                ns = new Namespace[2];
                ns[0] = xmlef.
                        createNamespace("h",
                                "http://www.w3.org/1999/xhtml");
                ns[1] = xmlef.
                        createNamespace("r",
                                "http://www.javajeff.ca/");
            }

            @Override
            public boolean hasNext() {
                return index != 2;
            }

            @Override
            public Namespace next() {
                return ns[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        event = xmlef.createStartElement("h",
                "http://www.w3.org/1999/xhtml",
                "html", null, nsIter);
        xmlew.add(event);
        event = xmlef.createStartElement("h",
                "http://www.w3.org/1999/xhtml",
                "head");
        xmlew.add(event);
        event = xmlef.createStartElement("h",
                "http://www.w3.org/1999/xhtml",
                "title");
        xmlew.add(event);
        event = xmlef.createCharacters("Recipe");
        xmlew.add(event);
        event = xmlef.createEndElement("h",
                "http://www.w3.org/1999/xhtml",
                "title");
        xmlew.add(event);
        event = xmlef.createEndElement("h",
                "http://www.w3.org/1999/xhtml",
                "head");
        xmlew.add(event);
        event = xmlef.createStartElement("h",
                "http://www.w3.org/1999/xhtml",
                "body");
        xmlew.add(event);
        event = xmlef.createStartElement("r",
                "http://www.javajeff.ca/",
                "recipe");
        xmlew.add(event);
        event = xmlef.createStartElement("r",
                "http://www.javajeff.ca/",
                "title");
        xmlew.add(event);
        event = xmlef.createCharacters("Grilled Cheese Sandwich");
        xmlew.add(event);
        event = xmlef.createEndElement("r",
                "http://www.javajeff.ca/",
                "title");
        xmlew.add(event);
        event = xmlef.createStartElement("r",
                "http://www.javajeff.ca/",
                "ingredients");
        xmlew.add(event);
        event = xmlef.createStartElement("h",
                "http://www.w3.org/1999/xhtml",
                "ul");
        xmlew.add(event);
        event = xmlef.createStartElement("h",
                "http://www.w3.org/1999/xhtml",
                "li");
        xmlew.add(event);
        Iterator<Attribute> attrIter;
        attrIter = new Iterator<Attribute>() {
            int index = 0;
            Attribute[] attrs;

            {
                attrs = new Attribute[1];
                attrs[0] = xmlef.createAttribute("qty", "2");
            }

            @Override
            public boolean hasNext() {
                return index != 1;
            }

            @Override
            public Attribute next() {
                return attrs[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        event = xmlef.createStartElement("r",
                "http://www.javajeff.ca/",
                "ingredient", attrIter, null);
        xmlew.add(event);
        event = xmlef.createCharacters("bread slice");
        xmlew.add(event);
        event = xmlef.createEndElement("r",
                "http://www.javajeff.ca/",
                "ingredient");
        xmlew.add(event);
        event = xmlef.createEndElement("h",
                "http://www.w3.org/1999/xhtml",
                "li");
        xmlew.add(event);
        event = xmlef.createEndElement("h",
                "http://www.w3.org/1999/xhtml",
                "ul");
        xmlew.add(event);
        event = xmlef.createEndElement("r",
                "http://www.javajeff.ca/",
                "ingredients");
        xmlew.add(event);
        event = xmlef.createEndElement("r",
                "http://www.javajeff.ca/",
                "recipe");
        xmlew.add(event);
        event = xmlef.createEndElement("h",
                "http://www.w3.org/1999/xhtml",
                "body");
        xmlew.add(event);
        event = xmlef.createEndElement("h",
                "http://www.w3.org/1999/xhtml",
                "html");
        xmlew.add(event);
        xmlew.flush();
        xmlew.close();
    }
}
