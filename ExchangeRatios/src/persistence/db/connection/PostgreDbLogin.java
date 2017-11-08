
package persistence.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import logging.Log;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.CountryCurrency;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PostgreDbLogin {

	public final String DIALECT_PARAM = "hibernate.dialect";
	public final String DRIVER_PARAM = "hibernate.connection.driver_class";
	public final String URL_PARAM = "hibernate.connection.url";
	public final String USERNAME_PARAM = "hibernate.connection.username";
	public final String PASSWORD_PARAM = "hibernate.connection.password";

	protected Configuration conf;
	protected ServiceRegistry servRegistry;
	protected SessionFactory sessionFactory;
	protected String driverName = "org.postgresql.Driver";
	protected String dbDialect = "org.hibernate.dialect.PostgreSQLDialect";
	protected String host = "jdbc:postgresql://localhost:5432/";
	protected String dbName = "postgres";
	protected String username = "postgres";// advent
	protected String password = "postgres";// axt8

	public PostgreDbLogin() {
	}

	public PostgreDbLogin(String host, String db_name, String username, String password) {
		this.host = host;
		this.dbName = db_name;
		this.username = username;
		this.password = password;
		makeCustomConfig();
		servRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		sessionFactory = conf.buildSessionFactory(servRegistry);
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

	protected Configuration makeCustomConfig() {
		checkCredecentials();
		return new Configuration().addAnnotatedClass(Country.class).addAnnotatedClass(Currency.class)
				.addAnnotatedClass(CurrencyRatios.class).addAnnotatedClass(CountryCurrency.class).setProperty(DIALECT_PARAM, dbDialect)
				.setProperty(DRIVER_PARAM, driverName).setProperty(URL_PARAM, host + dbName).setProperty(USERNAME_PARAM, username)
				.setProperty(PASSWORD_PARAM, password);
	}

}
