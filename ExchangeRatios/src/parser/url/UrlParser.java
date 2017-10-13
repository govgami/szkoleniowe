package parser.url;

import java.net.MalformedURLException;
import java.net.URL;

import parser.InnerDataParser;

public class UrlParser implements InnerDataParser{
	String url;

	public UrlParser(String urlString) {
		this.url = urlString;
	}

	public String getRequestUrl() {
		return url;
	}
@Override
	public URL parse(){
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("Attempted to parse malformed URL: "+url);
		}
	}
}
