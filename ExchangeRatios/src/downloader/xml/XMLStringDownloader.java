package downloader.xml;

import downloader.HttpStringDownloader;

public class XMLStringDownloader extends HttpStringDownloader {
	protected static final String finalPart="/?format=xml";
	
	public XMLStringDownloader(String request) {
		super(request+finalPart);
	}
}
