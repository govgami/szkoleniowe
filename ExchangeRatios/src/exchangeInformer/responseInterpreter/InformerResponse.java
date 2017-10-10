package exchangeInformer.responseInterpreter;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import exchangeInformer.xml.SAXFloatReader;
import exchangeInformer.xml.SAXValueReader;

public class InformerResponse implements ResponseInterpretation {
	public String read(String informerResponse) {
		SAXValueReader sax = readAsSAX(informerResponse);
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
			xmlReader.parse(new InputSource(new StringReader(information)));
			return r;
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
