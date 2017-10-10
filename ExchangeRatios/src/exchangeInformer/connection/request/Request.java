package exchangeInformer.connection.request;

import java.net.MalformedURLException;
import java.net.URL;

public class Request {
	String url;

	public Request(String url) {
		this.url = url;
	}

	public String getRequestUrl() {
		return url;
	}

	public URL makeUrl() {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
