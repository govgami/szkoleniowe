package nbp.downloader;

import nbp.connection.HttpConnection;
import parser.url.UrlParser;

public class HttpStringDownloader implements StringDownloader {
	protected String httpRequest;
	protected HttpConnection conn;

	public HttpStringDownloader(String request) {
		this.httpRequest = request;
		this.conn = new HttpConnection(new UrlParser(httpRequest).parse());
	}

	@Override
	public String download() {
		return conn.download();
	}
	
	public void alterSource(String newRequest) {
		this.httpRequest = newRequest;
		this.conn = new HttpConnection(new UrlParser(httpRequest).parse());
	}

}
