package exchangeInformer;

import exchangeInformer.responseInterpreter.InformerResponse;

public class NbpActCurrencyConnector {
ExchangeInformer info;
InformerResponse resp;

NbpActCurrencyConnector(String currencyShortcut){
	info=new ExchangeInformer(currencyShortcut);
	resp=new InformerResponse();
}

public String getCurrencyValueString() {
	return resp.read(info.callRequest());
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
