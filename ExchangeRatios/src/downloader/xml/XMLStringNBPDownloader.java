package downloader.xml;

import downloader.HttpStringDownloader;

public class XMLStringNBPDownloader extends HttpStringDownloader {
	protected static final String NBPAPI = "http://api.nbp.pl/api/";
	protected static final String finalPart = "/?format=xml";

	public XMLStringNBPDownloader(String request) {
		super(NBPAPI + request + finalPart);
	}
}
