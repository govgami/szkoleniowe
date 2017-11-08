
package persistence.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import logging.Log;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.CountryCurrency;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class DbAccess {

	private static BasicLogin login;

	static {
		// XXX temporary measure
		login = new PostgreDbLogin(DefaultDbContact.HOST, DefaultDbContact.DB_NAME, DefaultDbContact.USERNAME, DefaultDbContact.PASSWORD);
	}

	public static void setDbUser(BasicLogin newLogin) {
		login = newLogin;
	}

	public static Connection makeDefaultPostgreConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection c = DriverManager.getConnection(DefaultDbContact.HOST + DefaultDbContact.DB_NAME, DefaultDbContact.USERNAME,
					DefaultDbContact.PASSWORD);
			return c;
		} catch (ClassNotFoundException | SQLException e) {
			Log.exception("DbConnection", e);
			throw new RuntimeException(e);
		}
	}

	public static Connection makeCustomPostgreConnection() {
		return login.openNewConnection();
	}

	public static Session openCustomSession() {
		return login.openNewSession();
	}

	public static Statement createStatement() {
		return login.createNewStatement();
	}

	public static ResultSet execQuery(String query) throws SQLException {
		return login.getExistingConnection().createStatement().executeQuery(query);
	}

	// public int insert(String table, Map<String, String> values) throws SQLException {
	// StringBuilder columns = new StringBuilder();
	// StringBuilder vals = new StringBuilder();
	// for (String col : values.keySet()) {
	// columns.append(col).append(",");
	// if (values.get(col) instanceof String) {
	// vals.append("'").append(values.get(col)).append("', ");
	// } else
	// vals.append(values.get(col)).append(",");
	// }
	// columns.setLength(columns.length() - 1);
	// vals.setLength(vals.length() - 1);
	// String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columns.toString(), vals.toString());
	//
	// return this.conn.createStatement().executeUpdate(query);
	// }

	private static final Configuration CONFIGURATION =
			new Configuration().addAnnotatedClass(Country.class).addAnnotatedClass(Currency.class).addAnnotatedClass(CurrencyRatios.class)
					.addAnnotatedClass(CountryCurrency.class).setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
					.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
					.setProperty("hibernate.connection.url", DefaultDbContact.HOST + DefaultDbContact.DB_NAME)
					.setProperty("hibernate.connection.username", DefaultDbContact.USERNAME)
					.setProperty("hibernate.connection.password", DefaultDbContact.PASSWORD);
	private static ServiceRegistry serviceRegistry =
			new StandardServiceRegistryBuilder().applySettings(CONFIGURATION.getProperties()).build();
	private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// SessionFactory from hibernate.cfg.xml
			return CONFIGURATION.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			Log.exception("Initial SessionFactory creation failed", ex);
			throw new RuntimeException(ex);
		}
	}

	protected static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

	public static Session openSession() {
		return SESSION_FACTORY.openSession();
	}

	public static void shutdown() {
		getSessionFactory().close();
	}
}
