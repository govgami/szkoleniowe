
package persistence.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import logging.Log;

public abstract class BasicLogin {

	protected String driverName = "org.postgresql.Driver";
	protected String dbDialect = "org.hibernate.dialect.PostgreSQLDialect";
	protected String host = "jdbc:postgresql://localhost:5432/";
	protected String dbName = "postgres";
	protected String username = "postgres";// advent
	protected String password = "postgres";// axt8
	protected Connection conn;
	protected Configuration conf;
	protected ServiceRegistry servRegistry;
	protected SessionFactory sessionFactory;

	public BasicLogin() {
	}

	public BasicLogin(String host, String db_name, String username, String password) {
		this.host = host;
		this.dbName = db_name;
		this.username = username;
		this.password = password;
		this.conf = makeCustomConfig();
		servRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		sessionFactory = conf.buildSessionFactory(servRegistry);
		this.conn = openNewConnection();
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

	public Connection openNewConnection() {
		checkCredecentials();
		try {
			Class.forName(driverName);
			clearEarlierConnection();
			conn = DriverManager.getConnection(host + dbName, username, password);
			return conn;
		} catch (SQLException | ClassNotFoundException e) {
			Log.exception("Connection opening failed", e);
			throw new RuntimeException(e);
		}
	}

	public Connection getExistingConnection() {
		if (!isConnectionOpened()) {
			return openNewConnection();
		} else {
			return conn;
		}
	}

	protected boolean isConnectionOpened() {
		try {
			if (conn == null)
				return false;
			else if (conn.isClosed())
				return false;
			else
				return true;
		} catch (SQLException e) {
			Log.exception("exception when attempting to check whether is Connection Closed", e);
			throw new RuntimeException(e);
		}
	}

	protected void clearEarlierConnection() {
		try {
			if (conn != null) {
				if (!conn.isClosed()) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public Session openNewSession() {
		return sessionFactory.openSession();
	}

	public Statement createNewStatement() {
		try {
			return getExistingConnection().createStatement();
		} catch (SQLException e) {
			Log.exception("unable to create new statement", e);
			throw new RuntimeException(e);
		}
	}

	protected void checkCredecentials() {
		if (host.isEmpty() || dbName.isEmpty() || username.isEmpty() || password.isEmpty()) {
			try {
				throw new SQLException("Database credentials missing");
			} catch (SQLException e) {
				Log.exception("Empty credecential", e);
				throw new RuntimeException(e);
			}
		}
	}

	abstract protected Configuration makeCustomConfig();

}
