package exchangeInformer;

import java.math.BigDecimal;

import downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import extraction.xml.specialized.SelectiveSaxDataReader;
import parser.Str2BigDecimal;
import valueReading.ValueReader;
import valueReading.xml.CurrMarkSpecSAXNumericReader;

public class HttpXmlNbpActTableCurrency extends AccessXMLInformation{
	
	public HttpXmlNbpActTableCurrency(String tableName, ValueReader reader) {
		info=HttpXmlExchangeDownloaderFactory.exchangeTable(tableName.toLowerCase());
		resp=new SelectiveSaxDataReader(reader);
	}
	
	public String getXMLDataString() {
		return resp.read(info.download());
	}

	public BigDecimal getXMLData() {
		return new Str2BigDecimal(getXMLDataString()).parse();
	}


	public static BigDecimal getValue(String currencyShortcut) {
		HttpXmlNbpActSpecCurrency nacc=linkActCurrConn(currencyShortcut);
	return nacc.getXMLData();
	}
	public static String getValueString(String currencyShortcut) {
		HttpXmlNbpActSpecCurrency nacc=linkActCurrConn(currencyShortcut);
	return nacc.getXMLDataString();
	}
	public static HttpXmlNbpActSpecCurrency linkActCurrConn(String currencyShortcut) {
		return new HttpXmlNbpActSpecCurrency(currencyShortcut, new CurrMarkSpecSAXNumericReader(currencyShortcut, "Mid"));
	}
}
