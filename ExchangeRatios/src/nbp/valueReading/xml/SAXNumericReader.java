
package nbp.valueReading.xml;

import java.io.File;
import java.math.BigDecimal;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import nbp.valueReading.ValueReader;
import parser.Str2BigDecimal;

public class SAXNumericReader extends DefaultHandler implements ValueReader {

	BigDecimal value = null;
	boolean valueFound = false;
	String searchedQName = null;

	public SAXNumericReader(String searchedQName) {
		this.searchedQName = searchedQName;
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
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

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (valueFound) {
			value = new Str2BigDecimal(new String(ch, start, length)).parse();
			// value = Float.parseFloat(new String(ch, start, length));
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

	@Override
	public BigDecimal getFoundValue() {
		return value;
	}

	@Override
	public String getFoundValueString() {
		return value.toString();
	}
}
