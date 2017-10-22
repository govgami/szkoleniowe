package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import downloader.xml.XMLStringNBPDownloader;
import downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import extraction.xml.specialized.SelectiveSaxDataReader;
import valueReading.ValueReader;
import valueReading.utility.CurrencyPrice;
import valueReading.xml.SAXCurrencyPricesDataReader;
import valueReading.xml.tableService.TablesAB;
import valueReading.xml.tableService.TablesC;

public class HttpXmlNbpPeriodTableCurrency extends AccessXMLInformation{
List<CurrencyPrice> list=new ArrayList<CurrencyPrice>();


HttpXmlNbpPeriodTableCurrency(XMLStringNBPDownloader downloader, ValueReader reader){
	info=downloader;
	resp=new SelectiveSaxDataReader(reader);
}


	@Override
	public List<CurrencyPrice> getXMLData() {
		return (List<CurrencyPrice>)resp.readExt(info.download());
	}

	@Override
	public String getXMLDataString() {
		return resp.read(info.download());
	}

	
	public static List<CurrencyPrice> takeTable(String tableN, Date from, Date to){
		HttpXmlNbpPeriodTableCurrency t;
		if(tableN.toLowerCase().equals("c")) {
		t=workWithC(tableN,from,to);
		}
		else {
			t=workWithAB(tableN, from, to);
		}
		return t.getXMLData();
	}
		
		static HttpXmlNbpPeriodTableCurrency workWithAB(String tableN,Date from, Date to) {
		return new HttpXmlNbpPeriodTableCurrency(HttpXmlExchangeDownloaderFactory.exchangeTableOnDayTo(tableN, from, to), new SAXCurrencyPricesDataReader(new TablesAB()));
		}
		static HttpXmlNbpPeriodTableCurrency workWithC(String tableN,Date from, Date to) {
			return new HttpXmlNbpPeriodTableCurrency(HttpXmlExchangeDownloaderFactory.exchangeTableOnDayTo(tableN, from, to), new SAXCurrencyPricesDataReader(new TablesC()));
			}
		
}
