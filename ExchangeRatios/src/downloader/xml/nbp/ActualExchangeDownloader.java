package downloader.xml.nbp;

import downloader.Downloader;
import downloader.xml.XMLDownloader;
import connection.HttpConnection;

public class ActualExchangeDownloader extends XMLDownloader implements Downloader{
	protected static String NBPReq="http://api.nbp.pl/api/exchangerates/rates/a/";
	
	HttpConnection con;

public ActualExchangeDownloader(String shortcutForExchangedMoney){
	super(NBPReq+shortcutForExchangedMoney);
}

}
