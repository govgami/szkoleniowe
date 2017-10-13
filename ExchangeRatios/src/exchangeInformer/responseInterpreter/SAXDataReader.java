package exchangeInformer.responseInterpreter;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import dataReading.ValueReader;
import dataReading.xml.SAXFloatReader;
import parser.xml.XMLInputSourceParser;

public class SAXDataReader implements DataReading {
	public String read(String informerResponse) {
		ValueReader sax = readAsSAX(informerResponse);
		return sax.getFoundValueString();
	}

	protected SAXFloatReader readAsSAX(String information) {
		try {
			SAXFloatReader r = new SAXFloatReader("Mid");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(r);
			xmlReader.parse(new XMLInputSourceParser(information).parse());
			return r;
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
