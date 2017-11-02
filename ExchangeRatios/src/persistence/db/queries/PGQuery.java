package persistence.db.queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Session;

import logging.Log;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PGQuery extends BasicOperations {

	public static final void ConnectCountryCurrency(Country country, Currency currency) {

		country.addCurrency(currency);
		ObjectOperations.InsertOrUpdate(country);

	}

	public static final void DisconnectCountryCurrency(Country country, Currency currency) {
		country.removeCurrency(currency);
		ObjectOperations.InsertOrUpdate(country);
	}

	public static final void InsertActualizedCurrencyRatiosGroup(List<CurrencyRatios> list) {
		CurrencyRatios existance;
		for (CurrencyRatios o : list) {
			existance = PGQSelect.attemptToGetCurrencyRatio(o);
			if (existance != null) {
				ObjectOperations.InsertOrUpdate(mergeObjectRatiosData(existance, o));
			} else {
				ObjectOperations.Insert(o);
			}
		}
	}

	public static final CurrencyRatios mergeObjectRatiosData(CurrencyRatios base, CurrencyRatios update) {
		if (update.getAskPrice() != null) {
			base.setAskPrice(update.getAskPrice());
		}
		if (update.getBidPrice() != null) {
			base.setBidPrice(update.getBidPrice());
		}
		if (update.getAvgPrice() != null) {
			base.setAvgPrice(update.getAvgPrice());
		}
		return base;
	}

	public static void initDatabaseStructure(Connection conn) {
		try {
			Session session;
			Statement stmt = conn.createStatement();
			String sql = "CREATE TABLE country (ID int primary key not null, NAME varchar(50) not null unique, CURRENCIES int array);";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE currency (ID int primary key not null, COUNTRY_ID int references Country(ID), CURRENCY_NAME  varchar(50),  CODE  varchar(4)  not null unique )";
			stmt.execute(sql);// executeUpdate(sql);
			stmt.close();
			// DEV remake SHORTCUT for CODE Query classes
			stmt = conn.createStatement();
			sql = "CREATE TABLE currency_ratios (ID numeric primary key not null, CURRENCY_ID    int references Currency(ID)    not null, EFFECTIVE_DATE DATE   not null, ASK_PRICE  numeric , BID_PRICE numeric  , AVG_PRICE numeric     )";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE country_currency (ID INT PRIMARY KEY NOT NULL, CURRENCY_ID    INT REFERENCES CURRENCY(ID)    NOT NULL, COUNTRY_ID INT REFERENCES COUNTRY(ID)   NOT NULL  )";
			stmt.execute(sql);
			stmt.close();

			// seq
			stmt = conn.createStatement();
			sql = "CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1  NO MAXVALUE  NO MINVALUE CACHE 1;";
			stmt.execute(sql);
			stmt.close();

			// alt. country
			session = openTransaction();
			Country country = new Country("Non-classified");
			session.save(new Country("Non-classified"));
			session.close();

			// alt. currency
			session = openTransaction();
			session.save(new Currency(country, "Non-specified Currency", "???"));
			session.close();

			closeQuery(conn);
		} catch (SQLException e) {
			Log.exception("DbConnection Create DB tablea", e);
			throw new RuntimeException(e);
		}
	}

	public static void dropDatabaseStructure(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			String sql = "DROP table country_currency;";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "DROP table currency_ratios";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "DROP table currency";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "DROP table country";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "DROP sequence hibernate_sequence";
			stmt.execute(sql);
			stmt.close();
		} catch (Exception e) {
			Log.exception("Db Connection Drop DB tables", e);
			throw new RuntimeException(e);
		}
	}

	public static void closeQuery(Connection conn) {
		try {
			if (!conn.getAutoCommit())
				conn.commit();
		} catch (SQLException e) {
			try {
				Log.exception("unable to commit", e);
				conn.rollback();
			} catch (SQLException e1) {
				Log.exception("rollback impossible", e1);
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		}
	}

	public static void validateQueryArgAgainstSQLInjection(String arg) {
		if (arg.split(" ").length > 1 | arg.isEmpty()) {
			throw new RuntimeException("Invalid argument: " + arg);
		}
	}

}
