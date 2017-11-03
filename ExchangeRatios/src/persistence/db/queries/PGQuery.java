package persistence.db.queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Session;

import logging.Log;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.CountryCurrency;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PGQuery extends BasicOperations {

	public static final void ConnectCountryCurrency(Country country, Currency currency) {

		country.addCurrency(currency);
		currency.addCountry(country);
		ObjectOperations.InsertOrUpdate(country);
		ObjectOperations.InsertOrUpdate(currency);

	}

	public static final void DisconnectCountryCurrency(Country country, Currency currency) {
		country.removeCurrency(currency);
		currency.removeCountry(country);
		CountryCurrency ccurr = new CountryCurrency(country, currency);
		ObjectOperations.InsertOrUpdate(country);
		ObjectOperations.InsertOrUpdate(currency);
		ObjectOperations.DeleteObject(ccurr);
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
			Statement stmt;

			stmt = conn.createStatement();
			String sql = "CREATE TABLE country (ID int primary key not null, NAME varchar(50) not null unique);";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE currency (ID int primary key not null, CURRENCY_NAME  varchar(50),  CODE  varchar(4)  not null unique)";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE currency_ratios (ID numeric primary key not null, CURRENCY_ID    int    not null, EFFECTIVE_DATE DATE   not null, ASK_PRICE  numeric, BID_PRICE numeric, AVG_PRICE numeric, foreign key(CURRENCY_ID) references Currency(ID) )";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE country_currency (CURRENCY_ID    int    not null, COUNTRY_ID INT   not null , primary key (COUNTRY_ID, CURRENCY_ID), foreign key(COUNTRY_ID) references Country(ID), foreign key(CURRENCY_ID) references Currency(ID) )";
			stmt.execute(sql);
			stmt.close();

			// seq
			stmt = conn.createStatement();
			sql = "CREATE SEQUENCE hibernate_sequence start with 1 increment by 1  no maxvalue  no minvalue cache 1;";
			stmt.execute(sql);
			stmt.close();

			// alt. country
			session = openTransaction();
			Country country = new Country("Non-classified");
			session.save(new Country("Non-classified"));
			session.close();

			HashSet<Country> c = new HashSet<Country>();
			c.add(country);
			// alt. currency
			session = openTransaction();
			session.save(new Currency(c, "Non-specified Currency", "???"));
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
