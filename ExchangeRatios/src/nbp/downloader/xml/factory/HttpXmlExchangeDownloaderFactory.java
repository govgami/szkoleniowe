package nbp.downloader.xml.factory;

import java.util.Date;

import nbp.downloader.xml.XMLStringNBPDownloader;
import parser.Date2Str;

public class HttpXmlExchangeDownloaderFactory {

	public static XMLStringNBPDownloader exchangeRateA(String shortcutForExchangedMoney) {
		return new XMLStringNBPDownloader("exchangerates/rates/a/" + shortcutForExchangedMoney.toLowerCase());
	}

	public static XMLStringNBPDownloader exchangeTable(String tableName) {
		return new XMLStringNBPDownloader("exchangerates/tables/" + tableName.toLowerCase());
	}

	public static XMLStringNBPDownloader exchangeTableOnDay(String tableName, Date time) {
		return new XMLStringNBPDownloader(
				"exchangerates/tables/" + tableName.toLowerCase() + "/" + new Date2Str(time).parse());
	}
	public static XMLStringNBPDownloader exchangeTableOnDayTo(String tableName, Date time, Date last) {
		return new XMLStringNBPDownloader(
				"exchangerates/tables/" + tableName.toLowerCase() + "/" + new Date2Str(time).parse()+"/"+new Date2Str(last).parse()+"/");
	}
	
	public static String requestDatedTable(String tableName, Date time) {
		return "http://api.nbp.pl/api/exchangerates/tables/" + tableName.toLowerCase() + "/" + new Date2Str(time).parse()+"/?format=xml";
	}

}
