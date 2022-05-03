/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.factory;

import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author dnlbe
 */
public class ParserFactory {
 
     public static XMLEventReader createStaxParser(InputStream is) throws XMLStreamException {
         XMLInputFactory factory = XMLInputFactory.newFactory();
         XMLEventReader reader = factory.createXMLEventReader(is);
         return reader;
    }
}
