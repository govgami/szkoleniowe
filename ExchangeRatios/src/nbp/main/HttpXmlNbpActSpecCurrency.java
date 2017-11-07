
package nbp.main;

import java.math.BigDecimal;

import nbp.downloader.xml.XMLStringNBPDownloader;
import nbp.downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import nbp.extractor.xml.specialized.SelectiveSaxDataReader;
import nbp.valueReading.ValueReader;
import nbp.valueReading.xml.SAXNumericReader;
import parser.Str2BigDecimal;

public class HttpXmlNbpActSpecCurrency extends AccessXMLInformation {

	XMLStringNBPDownloader info;
	SelectiveSaxDataReader resp;

	public HttpXmlNbpActSpecCurrency(String currencyShortcut, XMLStringNBPDownloader downloader, ValueReader reader) {
		info = downloader;
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

	public static BigDecimal getValue(String currencyShortcut) {
		HttpXmlNbpActSpecCurrency nacc = linkActCurrConn(currencyShortcut);
		return nacc.getXMLData();
	}

	public static String getValueString(String currencyShortcut) {
		HttpXmlNbpActSpecCurrency nacc = linkActCurrConn(currencyShortcut);
		return nacc.getXMLDataString();
	}

	public static HttpXmlNbpActSpecCurrency linkActCurrConn(String currencyShortcut) {
		return new HttpXmlNbpActSpecCurrency(currencyShortcut, HttpXmlExchangeDownloaderFactory.exchangeRateA(currencyShortcut),
				new SAXNumericReader("Mid"));
	}
}
