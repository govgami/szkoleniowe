package valueReading.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import persistence.db.table.currency.CurrencyPrice;
import valueReading.xml.tableService.TableTextConversion;
import valueReading.ValueReader;

public class SAXCurrencyPricesDataReader extends DefaultHandler implements ValueReader{
	TableTextConversion mapper=null;
	HashMap<String, String> readedObject=new HashMap<String, String>();
	
List<CurrencyPrice> prices=new ArrayList<CurrencyPrice>();

	CurrencyPrice t=null;
	
	String dateElement="EffectiveDate";
	boolean dateReload;
	String effectiveDate=null;
	
	String borderElement="Rate";
	boolean processingElement;
	String currentElement=null;

	public SAXCurrencyPricesDataReader(TableTextConversion mp) {
		mapper=mp;
	}

	public void startDocument() throws SAXException {
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if(qName.equals(dateElement)) {
			dateReload=true;
		}
		else if(qName.equals(borderElement)) {
			readyForNewObject();
		}
		//Log.info(localName+"::"+qName);
		currentElement=qName;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
if(qName.equals(dateElement)) {
	dateReload=false;
}
//else if(qName.equals(borderElement)) {
//	readyForNewObject();
//}
		super.endElement(uri, localName, qName);
	}
	
	void readyForNewObject() {
		processingElement=true;
		if(readedObject.isEmpty()) {/*Log.info("SAXCurrencyPricesDataReader: empty object");*/return;}
		else {
		readedObject.put(dateElement, effectiveDate);
		t=mapper.serviceMapping(readedObject);
		prices.add(t);
		readedObject.clear();
	}
	}

	public void endDocument() throws SAXException {
		readyForNewObject();
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (dateReload) {
			effectiveDate=new String(ch, start,length);
		}
		if (processingElement) {
			readedObject.put(currentElement, new String(ch, start, length));
			//Log.info(currentElement+":processed:"+new String(ch, start, length));
		}
	}

	public List<CurrencyPrice> getFoundValue() {
		return prices;
	}

	public String getFoundValueString() {
		return prices.toString();
	}
	
}
