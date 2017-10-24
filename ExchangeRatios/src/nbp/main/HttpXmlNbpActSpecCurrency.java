package nbp.main;

import java.math.BigDecimal;

import nbp.downloader.xml.XMLStringNBPDownloader;
import nbp.downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import nbp.extraction.xml.specialized.SelectiveSaxDataReader;
import parser.Str2BigDecimal;
import valueReading.ValueReader;
import valueReading.xml.SAXNumericReader;

public class HttpXmlNbpActSpecCurrency extends AccessXMLInformation {
	XMLStringNBPDownloader info;
	SelectiveSaxDataReader resp;

	public HttpXmlNbpActSpecCurrency(String currencyShortcut, XMLStringNBPDownloader downloader, ValueReader reader) {
		info = downloader;
		resp = new SelectiveSaxDataReader(reader);
	}

	public String getXMLDataString() {
		return resp.read(info.download());
	}

	public BigDecimal getXMLData() {
		return new Str2BigDecimal(getXMLDataString()).parse();
	}

	public static BigDecimal getValue(String currencyShortcut) {
		HttpXmlNbpActSpecCurrency nacc = linkActCurrConn(currencyShortcut);
		return nacc.getXMLData();
	}

	public static String getValueString(String currencyShortcut) {
		HttpXmlNbpActSpecCurrency nacc = linkActCurrConn(currencyShortcut);
		return nacc.getXMLDataString();
	}

	public static HttpXmlNbpActSpecCurrency linkActCurrConn(String currencyShortcut) {
		return new HttpXmlNbpActSpecCurrency(currencyShortcut,
				HttpXmlExchangeDownloaderFactory.exchangeRateA(currencyShortcut), new SAXNumericReader("Mid"));
	}
}
