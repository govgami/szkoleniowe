
package nbp.main;

import java.math.BigDecimal;

import nbp.downloader.xml.XMLStringNBPDownloader;
import nbp.downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import nbp.extraction.xml.specialized.SelectiveSaxDataReader;
import nbp.valueReading.ValueReader;
import nbp.valueReading.xml.CurrMarkSpecSAXNumericReader;
import parser.Str2BigDecimal;

public class HttpXmlNbpActTableCurrency extends AccessXMLInformation {

	public HttpXmlNbpActTableCurrency(String tableName, XMLStringNBPDownloader downloader, ValueReader reader) {
		info = HttpXmlExchangeDownloaderFactory.exchangeTable(tableName.toLowerCase());
		resp = new SelectiveSaxDataReader(reader);
	}

	@Override
	public String getXMLDataString() {
		return resp.read(info.download());
	}

	@Override
	public BigDecimal getXMLData() {
		return new Str2BigDecimal(getXMLDataString()).parse();
	}

	public static BigDecimal getValue(String currencyShortcut, String tableName) {
		HttpXmlNbpActSpecCurrency nacc = linkActCurrConn(tableName, currencyShortcut);
		return nacc.getXMLData();
	}

	public static String getValueString(String currencyShortcut, String tableName) {
		HttpXmlNbpActSpecCurrency nacc = linkActCurrConn(tableName, currencyShortcut);
		return nacc.getXMLDataString();
	}

	public static HttpXmlNbpActSpecCurrency linkActCurrConn(String tableName, String currencyShortcut) {
		return new HttpXmlNbpActSpecCurrency(currencyShortcut, HttpXmlExchangeDownloaderFactory.exchangeTable(tableName.toLowerCase()),
				new CurrMarkSpecSAXNumericReader(currencyShortcut, "Mid"));
	}
}
