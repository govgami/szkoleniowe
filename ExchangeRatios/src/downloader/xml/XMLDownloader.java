package downloader.xml;

import downloader.HttpDownloader;

public class XMLDownloader extends HttpDownloader {
	protected static final String finalPart="/?format=xml";
	
	public XMLDownloader(String request) {
		super(request+finalPart);
	}
}
