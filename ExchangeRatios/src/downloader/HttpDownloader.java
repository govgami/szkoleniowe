package downloader;

import connection.HttpConnection;
import parser.url.UrlParser;

public class HttpDownloader implements Downloader{
	protected String httpRequest;
	protected HttpConnection conn;
	
	public HttpDownloader(String request){
		this.httpRequest=request;
		this.conn=new HttpConnection(new UrlParser(httpRequest).parse());
	}

	@Override
	public String download() {
		return conn.download();
	}
	
	
}
