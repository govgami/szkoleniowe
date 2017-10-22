package extraction.xml.specialized;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import extraction.StringDataReading;
import logging.Log;
import parser.xml.XMLInputSourceParser;
import valueReading.ValueReader;

public class SelectiveSaxDataReader implements StringDataReading {
	ValueReader reader;

	public SelectiveSaxDataReader(ValueReader valueReader) {
		reader = valueReader;
	}

	public String read(String xmlText) {
		readAsSAX(new XMLInputSourceParser(xmlText).parse());
		return reader.getFoundValueString();
	}
	
	public Object readExt(String xmlText) {
		readAsSAX(new XMLInputSourceParser(xmlText).parse());
		return reader.getFoundValue();
	}

	protected void readAsSAX(InputSource information) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler((ContentHandler) reader);
			xmlReader.parse(information);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			Log.exception("failure:\n", e);
			throw new RuntimeException("Improper information for reading as SAXDataReader:" + e.getStackTrace());

		}
	}

}
