package nbp.main;

import java.math.BigDecimal;

import nbp.downloader.xml.XMLStringNBPDownloader;
import nbp.downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import nbp.extraction.xml.specialized.SelectiveSaxDataReader;
import parser.Str2BigDecimal;
import valueReading.ValueReader;
import valueReading.xml.CurrMarkSpecSAXNumericReader;

public class HttpXmlNbpActTableCurrency extends AccessXMLInformation {

	public HttpXmlNbpActTableCurrency(String tableName, XMLStringNBPDownloader downloader, ValueReader reader) {
		info = HttpXmlExchangeDownloaderFactory.exchangeTable(tableName.toLowerCase());
		resp = new SelectiveSaxDataReader(reader);
	}

	public String getXMLDataString() {
		return resp.read(info.download());
	}

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
		return new HttpXmlNbpActSpecCurrency(currencyShortcut,
				HttpXmlExchangeDownloaderFactory.exchangeTable(tableName.toLowerCase()),
				new CurrMarkSpecSAXNumericReader(currencyShortcut, "Mid"));
	}
}
