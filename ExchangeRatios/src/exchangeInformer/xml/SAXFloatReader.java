package exchangeInformer.xml;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.io.*;

public class SAXFloatReader extends DefaultHandler implements SAXValueReader {
	float value = -1;
	boolean valueFound = false;
	String searchedQName = "Mid";

	public SAXFloatReader(String searchedQName) {
		this.searchedQName = searchedQName;
	}

	public void startDocument() throws SAXException {
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

		if (qName.equals(searchedQName)) {
			valueFound = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals(searchedQName)) {
			valueFound = false;
		}
		super.endElement(uri, localName, qName);
	}

	public void endDocument() throws SAXException {
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (valueFound) {
			value = Float.parseFloat(new String(ch, start, length));
		}
	}

	private static String convertToFileURL(String filename) {
		String path = new File(filename).getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return "file:" + path;
	}

	public float getFoundValue() {
		return value;
	}

	public String getFoundValueString() {
		return Float.toString(value);
	}
}
