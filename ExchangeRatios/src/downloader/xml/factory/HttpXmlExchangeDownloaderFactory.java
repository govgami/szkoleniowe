package downloader.xml.factory;

import java.util.Date;

import downloader.xml.XMLStringNBPDownloader;
import parser.Date2Str;

public class HttpXmlExchangeDownloaderFactory{
	
public static XMLStringNBPDownloader exchangeRateA(String shortcutForExchangedMoney) {
	return new XMLStringNBPDownloader("exchangerates/rates/a/"+shortcutForExchangedMoney.toLowerCase());
}
public static XMLStringNBPDownloader exchangeTable(String tableName) {
	return new XMLStringNBPDownloader("exchangerates/tables/"+tableName.toLowerCase());
}
public static XMLStringNBPDownloader exchangeTableOnDay(String tableName, Date time) {
	return new XMLStringNBPDownloader("exchangerates/tables/"+tableName.toLowerCase()+"/"+new Date2Str(time).parse());
}


}
