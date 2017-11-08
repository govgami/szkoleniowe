
package persistence.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import logging.Log;

public class DbLogin {

	protected String driverName = "org.postgresql.Driver";
	protected String host = "jdbc:postgresql://localhost:5432/";
	protected String dbName = "postgres";
	protected String username = "postgres";// advent
	protected String password = "postgres";// axt8

	public DbLogin() {
	}

	public DbLogin(String host, String db_name, String username, String password) {
		this.host = host;
		this.dbName = db_name;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDb_name() {
		return dbName;
	}

	public void setDb_name(String db_name) {
		this.dbName = db_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Connection openConnection() {
		checkCredecentials();
		try {
			Class.forName(driverName);
			return DriverManager.getConnection(host + dbName, username, password);
		} catch (SQLException | ClassNotFoundException e) {
			Log.exception("Connection opening failed", e);
			throw new RuntimeException(e);
		}
	}

	void checkCredecentials() {
		if (host.isEmpty() || dbName.isEmpty() || username.isEmpty() || password.isEmpty()) {
			try {
				throw new SQLException("Database credentials missing");
			} catch (SQLException e) {
				Log.exception("Empty credecential", e);
				throw new RuntimeException(e);
			}
		}
	}
}
