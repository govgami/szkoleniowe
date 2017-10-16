package valueReading.xml;

import java.math.BigDecimal;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import parser.Str2BigDecimal;
import valueReading.ValueReader;

public class SequentialSAXNumericReader extends DefaultHandler implements ValueReader {
	BigDecimal value = null;
	boolean sequenceStarted;
	String sequenceMarkName=null;
	boolean valueFound = false;
	String searchedQName = null;

	public SequentialSAXNumericReader(String sequenceMarkName, String searchedQName) {
this.sequenceMarkName=sequenceMarkName;
		this.searchedQName = searchedQName;
	}

	public void startDocument() throws SAXException {
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
if(qName.equals(sequenceMarkName)) {
	sequenceStarted=true;
}
		if (qName.equals(searchedQName)) {
			valueFound = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals(searchedQName)) {
			sequenceStarted=false;
			valueFound = false;
		}
		super.endElement(uri, localName, qName);
	}

	public void endDocument() throws SAXException {
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (valueFound&sequenceStarted) {
			value=new Str2BigDecimal(new String(ch, start, length)).parse();
		}
	}

	public BigDecimal getFoundValue() {
		return value;
	}

	public String getFoundValueString() {
		return value.toString();
	}
}
