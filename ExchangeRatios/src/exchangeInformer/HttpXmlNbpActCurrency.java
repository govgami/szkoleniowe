package exchangeInformer;

import downloader.xml.nbp.ActualExchangeDownloader;
import exchangeInformer.responseInterpreter.SAXDataReader;

public class HttpXmlNbpActCurrency {
ActualExchangeDownloader info;
SAXDataReader resp;

HttpXmlNbpActCurrency(String currencyShortcut){
	info=new ActualExchangeDownloader(currencyShortcut);
	resp=new SAXDataReader();
}

public String getCurrencyValueString() {
	return resp.read(info.download());
}

public float getCurrencyValue() {
	return Float.parseFloat(getCurrencyValueString());
}


public static float getValue(String currencyShortcut) {
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
