package parser.xml;

import java.io.StringReader;

import org.xml.sax.InputSource;

import parser.InnerDataParser;

public class XMLInputSourceParser implements InnerDataParser {
	String source;

	public XMLInputSourceParser(String xmlData) {
		source = xmlData;
	}

	@Override
	public InputSource parse() throws RuntimeException{
		return new InputSource(new StringReader(source));
	}
}
