package extraction.xml.specialized;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import dataReading.ValueReader;
import dataReading.xml.SAXNumericReader;
import extraction.XMLDataReading;
import logging.Log;
import parser.xml.XMLInputSourceParser;

public class SAXDataReader implements XMLDataReading {
	ValueReader reader;
	
	public SAXDataReader(ValueReader valueReader){
		reader=valueReader;
	}
	
	public String read(String xmlText) {
		readAsSAX(new XMLInputSourceParser(xmlText).parse());
		return reader.getFoundValueString();
	}

	protected void readAsSAX(InputSource information) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler((ContentHandler)reader);
			xmlReader.parse(information);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			Log.warn("failure:\n"+e.getStackTrace());
			throw new RuntimeException("Improper information for reading as SAXDataReader:"+e.getStackTrace());
			
		}
	}

}
