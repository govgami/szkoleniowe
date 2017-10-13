package exchangeInformer;

import downloader.xml.nbp.ActualExchangeDownloader;
import exchangeInformer.responseInterpreter.InformerResponse;

public class NbpActCurrencyConnector {
ActualExchangeDownloader info;
InformerResponse resp;

NbpActCurrencyConnector(String currencyShortcut){
	info=new ActualExchangeDownloader(currencyShortcut);
	resp=new InformerResponse();
}

public String getCurrencyValueString() {
	return resp.read(info.download());
}

public float getCurrencyValue() {
	return Float.parseFloat(getCurrencyValueString());
}


public static float getValue(String currencyShortcut) {
	NbpActCurrencyConnector nacc=new NbpActCurrencyConnector(currencyShortcut);
return nacc.getCurrencyValue();
}
public static String getValueString(String currencyShortcut) {
	NbpActCurrencyConnector nacc=new NbpActCurrencyConnector(currencyShortcut);
return nacc.getCurrencyValueString();
}
public static NbpActCurrencyConnector linkActCurrConn(String currencyShortcut) {
	return new NbpActCurrencyConnector(currencyShortcut);
}
}
