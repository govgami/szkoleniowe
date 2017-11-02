package persistence.db.queries;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import parser.Date2Str;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PGQSelect extends PGQuery {

	public static final <T> List<T> SelectAllFrom(String tableName) {
		validateQueryArgAgainstSQLInjection(tableName);
		Session session = openTransaction();
		Query<T> query = session.createQuery("FROM " + tableName);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final <T> List<T> SelectAllSortedFrom(String tableName, String orderBy, boolean ascending) {
		validateQueryArgAgainstSQLInjection(tableName);
		validateQueryArgAgainstSQLInjection(orderBy);
		Session session = openTransaction();
		Query<T> query = session
				.createQuery("FROM " + tableName + " ORDER BY " + orderBy + "  " + (ascending ? "ASC" : "DESC"));
		// query.setParameter("ascension", (ascending ? "ASC" : "DESC"));
		List<T> list = query.getResultList();
		session.close();
		return list;
	}

	public static final <T> List<T> SelectFirstOfAllSortedFrom(String tableName, String orderBy, boolean ascending,
			int limit) {
		validateQueryArgAgainstSQLInjection(tableName);
		validateQueryArgAgainstSQLInjection(orderBy);
		Session session = openTransaction();
		Query<T> query = session
				.createQuery("FROM " + tableName + " ORDER BY " + orderBy + "  " + (ascending ? "ASC" : "DESC"));
		query.setMaxResults(limit);
		List<T> list = query.getResultList();
		session.close();
		return list;
	}

	public static final List<Currency> SelectAllCurriencies() {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery("getAllCurrencies");
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<Country> SelectAllCountries() {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery("getAllCountries");
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final Country SelectCountryByName(String name) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery("getCountryByName");
		query.setParameter(0, name);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Currency SelectCurrencyByCode(String code) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery("getCurrencyByCode");
		query.setParameter(0, code);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Currency checkCurrencyCodeExistence(String code) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery("getCurrencyByCode");
		query.setParameter(0, code);
		return checkQueryResultObjectExistenceAndJustCloseSession(query, session);
	}

	public static final Country checkCountryExistence(String name) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery("getCountryByName");
		query.setParameter(0, name);
		return checkQueryResultObjectExistenceAndJustCloseSession(query, session);
	}

	public static final List<CurrencyRatios> SelectFirstOfLowestBidCurrencyRatios(String code, int limit) {
		validateQueryArgAgainstSQLInjection(code);

		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery("getLowestBidOfChosenSignCurrencyRatio");
		query.setParameter(0, code);
		query.setMaxResults(limit);
		List<CurrencyRatios> list = query.getResultList();
		session.close();
		return list;
	}

	public static final List<CurrencyRatios> SelectHighestPriceDifferenceOfCurrencyRatio(String code, int limit) {
		validateQueryArgAgainstSQLInjection(code);

		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery("getHighestPriceDifferenceOfCurrencyRatio");
		query.setParameter(0, code);
		query.setMaxResults(limit);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final CurrencyRatios attemptToGetCurrencyRatio(CurrencyRatios cr) {
		Session session = openTransaction();
		Query<CurrencyRatios> query = session.createQuery("FROM CurrencyRatios WHERE currency_id="
				+ cr.getCurrencyId().getId() + " AND effective_date= '" + new Date2Str(cr.getDate()).parse() + "'");
		List<CurrencyRatios> t = query.getResultList();
		session.close();
		if (t.isEmpty()) {
			return null;
		} else {
			return t.get(0);
		}
	}

}
