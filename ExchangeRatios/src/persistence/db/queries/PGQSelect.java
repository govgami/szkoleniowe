package persistence.db.queries;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

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

	public static final List<Currency> SelectAllCurriencies() {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery("getAllCurrencies");
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<Currency> SelectAllCurrenciesSortedByCode(Integer limit) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery("getAllCurrenciesSortedByCode");
		applyOptionalLimitOnResultsNumber(query, limit);
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

	public static final List<CurrencyRatios> SelectLowestBidCurrencyRatios(String code, Integer limit) {
		validateQueryArgAgainstSQLInjection(code);

		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery("getLowestBidOfChosenSignCurrencyRatio");
		query.setParameter(0, code);
		applyOptionalLimitOnResultsNumber(query, limit);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<CurrencyRatios> SelectHighestPriceDifferenceOfCurrencyRatio(String code, Integer limit) {
		validateQueryArgAgainstSQLInjection(code);

		Session session = openTransaction();
		Query<Object[]> query = session.getNamedQuery("getHighestPriceDifferenceOfCurrencyRatio");
		query.setParameter(0, code);
		applyOptionalLimitOnResultsNumber(query, limit);
		List<Object[]> result = presentQueryComplexResultsAndJustCloseSession(query, session);
		List<CurrencyRatios> extract = new ArrayList<CurrencyRatios>();
		for (Object[] oa : result) {
			extract.add((CurrencyRatios) oa[0]);
		}
		return extract;
	}

	public static final CurrencyRatios attemptToGetCurrencyRatio(CurrencyRatios cr) {
		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery("getCurrencyRatioByCurrencySignAndDay");
		query.setParameter(0, cr.getCurrency().getCode());
		query.setParameter(1, cr.getDate());
		return checkQueryResultObjectExistenceAndJustCloseSession(query, session);
	}

}
