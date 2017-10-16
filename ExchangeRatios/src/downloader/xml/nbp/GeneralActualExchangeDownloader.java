package downloader.xml.nbp;

import downloader.StringDownloader;
import downloader.xml.XMLStringDownloader;

public class GeneralActualExchangeDownloader extends XMLStringDownloader implements StringDownloader{
	static final protected String request="http://api.nbp.pl/api/exchangerates/tables/a/";
	public GeneralActualExchangeDownloader() {
		super(request);
	}

}
