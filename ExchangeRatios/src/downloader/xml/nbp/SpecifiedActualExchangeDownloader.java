package downloader.xml.nbp;

import downloader.StringDownloader;
import downloader.xml.XMLStringDownloader;

public class SpecifiedActualExchangeDownloader extends XMLStringDownloader implements StringDownloader{
	protected static String NBPReq="http://api.nbp.pl/api/exchangerates/rates/a/";

public SpecifiedActualExchangeDownloader(String shortcutForExchangedMoney){
	super(NBPReq+shortcutForExchangedMoney);
}

}
