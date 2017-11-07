package persistence.db.queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import logging.Log;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.CountryCurrency;
import persistence.db.table.currency.CountryCurrencyId;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PGQuery extends BasicOperations {

	public static final void ConnectCountryCurrency(Country country, Currency currency) {

		country.addCurrency(currency);
		// currency.addCountry(country);
		ObjectOperations.InsertOrUpdate(country);
		// ObjectOperations.InsertOrUpdate(currency);
		// ObjectOperations.InsertOrUpdate(new CountryCurrency(country, currency));

	}

	public static final void DisconnectCountryCurrency(Country country, Currency currency) {
		// country.removeCurrency(currency);
		// currency.removeCountry(country);
		CountryCurrency ccurr = ObjectOperations.GetObject(CountryCurrency.class,
				new CountryCurrencyId(country, currency));
		// ObjectOperations.InsertOrUpdate(country);
		// ObjectOperations.InsertOrUpdate(currency);
		ObjectOperations.DeleteObject(ccurr);
	}

	public static final void InsertActualizedCurrencyRatiosGroup(List<CurrencyRatios> list) {
		Session session;
		List<CurrencyRatios> resultList;
		for (CurrencyRatios o : list) {
			session = openTransaction();
			Query<CurrencyRatios> query = session.getNamedQuery(CurrencyRatios.Get_ByCurrencyCodeAndDate);
			query.setParameter(Currency.FieldCode, o.getCurrency().getCode());
			query.setParameter(CurrencyRatios.FieldDate, o.getDate());
			resultList = query.getResultList();
			if (!resultList.isEmpty()) {
				session.saveOrUpdate(mergeObjectRatiosData(resultList.get(0), o));
			} else {
				session.save(o);
			}
			closeSession(session);
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
		Session session;

		executeStatement(conn, "CREATE TABLE country (ID int primary key not null, NAME varchar(50) not null unique);");
		executeStatement(conn,
				"CREATE TABLE currency (ID int primary key not null, CURRENCY_NAME  varchar(50),  CODE  varchar(4)  not null unique)");
		executeStatement(conn,
				"CREATE TABLE currency_ratios (ID numeric primary key not null, CURRENCY_ID    int    not null, EFFECTIVE_DATE DATE   not null, ASK_PRICE  numeric, BID_PRICE numeric, AVG_PRICE numeric, foreign key(CURRENCY_ID) references Currency(ID) )");
		executeStatement(conn,
				"CREATE TABLE country_currency (CURRENCY_ID int not null, COUNTRY_ID INT not null, primary key (COUNTRY_ID, CURRENCY_ID), foreign key(COUNTRY_ID) references Country(ID) on delete restrict, foreign key(CURRENCY_ID) references Currency(ID) on delete restrict )");

		executeStatement(conn, "CREATE INDEX effective_day on currency_ratios (effective_date)");

		executeStatement(conn,
				"CREATE SEQUENCE hibernate_sequence start with 1 increment by 1  no maxvalue  no minvalue cache 1;");

		// // alt. country
		// session = openTransaction();
		// Country country = new Country("Non-classified");
		// session.save(new Country("Non-classified"));
		// session.close();
		//
		// HashSet<Country> c = new HashSet<Country>();
		// c.add(country);
		// // alt. currency
		// session = openTransaction();
		// session.save(new Currency(c, "Non-specified Currency", "???"));
		// session.close();

		closeQuery(conn);

	}

	public static void dropDatabaseStructure(Connection conn) {

		executeStatement(conn, "DROP table country_currency;");
		executeStatement(conn, "DROP index effective_day");
		executeStatement(conn, "DROP table currency_ratios");
		executeStatement(conn, "DROP table currency");
		executeStatement(conn, "DROP table country");
		executeStatement(conn, "DROP sequence hibernate_sequence");
	}

	protected static void executeStatement(Connection conn, String query) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(query);
			stmt.close();
		} catch (SQLException e) {
			Log.exception(query, e);
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
