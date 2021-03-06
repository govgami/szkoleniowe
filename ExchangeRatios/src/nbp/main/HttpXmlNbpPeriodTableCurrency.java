package nbp.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nbp.CurrencyPrice;
import nbp.downloader.xml.XMLStringNBPDownloader;
import nbp.downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import nbp.extraction.xml.specialized.SelectiveSaxDataReader;
import nbp.valueReading.ValueReader;
import nbp.valueReading.xml.SAXCurrencyPricesDataReader;
import nbp.valueReading.xml.table.TablesAB;
import nbp.valueReading.xml.table.TablesC;

public class HttpXmlNbpPeriodTableCurrency extends AccessXMLInformation {
	String tableN;
	Date from, to;
	List<Date> dates=new ArrayList<Date>();
	List<CurrencyPrice> list = new ArrayList<CurrencyPrice>();

	HttpXmlNbpPeriodTableCurrency(String tableName, Date from, Date to,XMLStringNBPDownloader downloader, ValueReader reader) {
		info = downloader;
		resp = new SelectiveSaxDataReader(reader);
		this.tableN=tableName;
		this.from=from;
		this.to=to;
	}

	@Override
	public List<CurrencyPrice> getXMLData() {
		//dates=Dates.markValidDates(from, to);
		//iterateThoroughDates();
		list=getXMLSubData();
		return list;
	}
	
	List<CurrencyPrice> getXMLSubData(){
		return (List<CurrencyPrice>) resp.readExt(info.download());
	}

	@Override
	public String getXMLDataString() {
		return resp.read(info.download());
	}

	public static List<CurrencyPrice> takeTable(String tableN, Date from, Date to) {
		HttpXmlNbpPeriodTableCurrency t;
		if (tableN.toLowerCase().equals("c")) {
			t = workWithC(tableN, from, to);
		} else {
			t = workWithAB(tableN, from, to);
		}
		//t.dates=Dates.markValidDates(from, to);
		//t.iterateThoroughDates();
		return t.getXMLData();
	}

	static HttpXmlNbpPeriodTableCurrency workWithAB(String tableN, Date from, Date to) {
		return new HttpXmlNbpPeriodTableCurrency(tableN, from, to,
				HttpXmlExchangeDownloaderFactory.exchangeTableOnDayTo(tableN, from, to),
				new SAXCurrencyPricesDataReader(new TablesAB()));
	}

	static HttpXmlNbpPeriodTableCurrency workWithC(String tableN, Date from, Date to) {
		return new HttpXmlNbpPeriodTableCurrency(tableN, from, to,
				HttpXmlExchangeDownloaderFactory.exchangeTableOnDayTo(tableN, from, to),
				new SAXCurrencyPricesDataReader(new TablesC()));
	}
	
void iterateThoroughDates() {
	list.clear();
	for(int i=0; i<dates.size();i++) {
		info.alterSource(HttpXmlExchangeDownloaderFactory.requestDatedTable(tableN, dates.get(i)));
		for(CurrencyPrice cp:getXMLSubData()) {
			list.add(cp);
		}
	}
}

}
