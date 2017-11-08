
package persistence.db.connection;

import java.sql.Connection;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import persistence.db.table.currency.Country;
import persistence.db.table.currency.CountryCurrency;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PostgreDbLogin extends BasicLogin {

	protected final String DIALECT_PARAM = "hibernate.dialect";
	protected final String DRIVER_PARAM = "hibernate.connection.driver_class";
	protected final String URL_PARAM = "hibernate.connection.url";
	protected final String USERNAME_PARAM = "hibernate.connection.username";
	protected final String PASSWORD_PARAM = "hibernate.connection.password";

	protected Connection conn;
	protected Configuration conf;
	protected ServiceRegistry servRegistry;
	protected SessionFactory sessionFactory;

	public PostgreDbLogin() {
	}

	public PostgreDbLogin(String host, String db_name, String username, String password) {
		super(host, db_name, username, password);
	}

	@Override
	protected Configuration makeCustomConfig() {
		checkCredecentials();
		return new Configuration().addAnnotatedClass(Country.class).addAnnotatedClass(Currency.class)
				.addAnnotatedClass(CurrencyRatios.class).addAnnotatedClass(CountryCurrency.class).setProperty(DIALECT_PARAM, dbDialect)
				.setProperty(DRIVER_PARAM, driverName).setProperty(URL_PARAM, host + dbName).setProperty(USERNAME_PARAM, username)
				.setProperty(PASSWORD_PARAM, password);
	}

}
