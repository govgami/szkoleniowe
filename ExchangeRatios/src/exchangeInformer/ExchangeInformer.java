package exchangeInformer;

import exchangeInformer.connection.Connection;
import exchangeInformer.connection.request.Request;

public class ExchangeInformer {
	final protected String NBPReq="http://api.nbp.pl/api/exchangerates/rates/a/";
final protected String finalPart="/?format=xml";
	
	Connection con;

ExchangeInformer(String shortcutForExchangedMoney){
	con=new Connection(new Request(NBPReq+shortcutForExchangedMoney+finalPart));
}

public String callRequest() {
	return con.getHTML();
}

}
