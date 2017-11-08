
package persistence.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import logging.Log;
import persistence.db.queries.PGQuery;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.CountryCurrency;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class DbAccess {

	private static Connection conn;
	private static DbLogin login;

	public static void setDbUser(String host, String dbName, String username, String password) {
		login = new DbLogin(host, dbName, username, password);
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
		return login.openConnection();
	}

	public void createPreProgrammed() {
		PGQuery.initDatabaseStructure(conn);
	}

	public ResultSet execQuery(String query) throws SQLException {
		return this.conn.createStatement().executeQuery(query);
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
					.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres")
					.setProperty("hibernate.connection.username", "postgres")
					.setProperty("hibernate.connection.password", "postgres")/* .configure(new File("src/db/table/hibernate.cfg.xml")) */;
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

	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}
}
