
package nbp.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import logging.Log;

public class HttpConnection implements I_Connectivity {

	URL request;

	public HttpConnection(URL request) {
		this.request = request;
	}

	@Override
	public String download() {
		HttpURLConnection conn = null;
		try {
			StringBuilder result = new StringBuilder();
			conn = getConnection();
			return readResponse(conn, result);
		} catch (IOException e) {
			Log.exception("failure:\n", e);
			throw new RuntimeException(e);
		} finally {
			conn.disconnect();
		}
	}

	HttpURLConnection getConnection() {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) request.openConnection();
		} catch (IOException e) {
			Log.exception("failure:\n", e);
			throw new RuntimeException(e);
		}
		return conn;
	}

	String readResponse(HttpURLConnection conn, StringBuilder result) throws IOException {
		try {
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			return result.toString();
		} catch (IOException e) {
			Log.exception("failure:\n", e);
			throw new RuntimeException(e);
		}
	}
}
