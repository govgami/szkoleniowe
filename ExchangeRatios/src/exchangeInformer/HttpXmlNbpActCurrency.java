package exchangeInformer;

import java.math.BigDecimal;

import dataReading.xml.SAXNumericReader;
import downloader.xml.nbp.SpecifiedActualExchangeDownloader;
import extraction.xml.specialized.SAXDataReader;
import parser.Str2BigDecimal;

public class HttpXmlNbpActCurrency {
SpecifiedActualExchangeDownloader info;
SAXDataReader resp;

public HttpXmlNbpActCurrency(String currencyShortcut){
	info=new SpecifiedActualExchangeDownloader(currencyShortcut.toLowerCase());
	resp=new SAXDataReader(new SAXNumericReader("Mid"));
}

public String getCurrencyValueString() {
	return resp.read(info.download());
}

public BigDecimal getCurrencyValue() {
	return new Str2BigDecimal(getCurrencyValueString()).parse();
}


public static BigDecimal getValue(String currencyShortcut) {
	HttpXmlNbpActCurrency nacc=new HttpXmlNbpActCurrency(currencyShortcut);
return nacc.getCurrencyValue();
}
public static String getValueString(String currencyShortcut) {
	HttpXmlNbpActCurrency nacc=new HttpXmlNbpActCurrency(currencyShortcut);
return nacc.getCurrencyValueString();
}
public static HttpXmlNbpActCurrency linkActCurrConn(String currencyShortcut) {
	return new HttpXmlNbpActCurrency(currencyShortcut);
}
}
