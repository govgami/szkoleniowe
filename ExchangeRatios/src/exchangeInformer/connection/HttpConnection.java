package exchangeInformer.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import exchangeInformer.connection.request.Request;

public class Connection {
	Request request;

	public Connection(Request request) {
		this.request = request;
	}

	public String getHTML() {
		HttpURLConnection conn = null;
		try {
			StringBuilder result = new StringBuilder();
			conn = getConnection();
			return readResponse(conn, result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return null;
	}

	HttpURLConnection getConnection() {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) request.makeUrl().openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}

	String readResponse(HttpURLConnection conn, StringBuilder result) throws IOException {
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}
}
