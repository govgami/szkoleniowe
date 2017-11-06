package persistence.db.queries;

import java.sql.Date;
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
		Query<Currency> query = session.getNamedQuery(Currency.Get_All);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<Currency> SelectAllCurrenciesSortedAscByCode(Integer limit) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery(Currency.Get_All_SortedByCode);
		applyOptionalLimitOnResultsNumber(query, limit);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<Country> SelectAllCountries() {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.Get_All);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final Country FetchCountryByName(String name) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.Fetch_ByName);
		query.setParameter(Country.FieldName, name);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Country FetchCountryConnectedByName(String name) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.Fetch_AllConnected_ByName);
		query.setParameter(Country.FieldName, name);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Country SelectCountryByName(String name) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.Get_ByName);
		query.setParameter(Country.FieldName, name);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Currency SelectCurrencyByCode(String code) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery("getCurrencyByCode");
		query.setParameter(Currency.FieldCode, code);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Currency FetchCurrencyByCode(String code) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery(Currency.Fetch_ByCode);
		query.setParameter(Currency.FieldCode, code);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Currency checkCurrencyCodeExistence(String code) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery(Currency.Get_ByCode);
		query.setParameter(Currency.FieldCode, code);
		return checkQueryResultObjectExistenceAndJustCloseSession(query, session);
	}

	public static final Country checkCountryExistence(String name) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.Get_ByName);
		query.setParameter(Country.FieldName, name);
		return checkQueryResultObjectExistenceAndJustCloseSession(query, session);
	}

	public static final List<CurrencyRatios> SelectLowestBidCurrencyRatios(String code, Integer limit) {
		validateQueryArgAgainstSQLInjection(code);

		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery(CurrencyRatios.Get_LowestBidPriceOfChosenCode);
		query.setParameter(Currency.FieldCode, code);
		applyOptionalLimitOnResultsNumber(query, limit);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<CurrencyRatios> SelectHighestPriceDifferenceOfCurrencyRatio(String code, Integer limit) {
		validateQueryArgAgainstSQLInjection(code);

		Session session = openTransaction();
		Query<Object[]> query = session.getNamedQuery(CurrencyRatios.Get_HighestDifferenceOf_AskAndBidPrice);
		query.setParameter(Currency.FieldCode, code);
		applyOptionalLimitOnResultsNumber(query, limit);
		List<Object[]> result = presentQueryComplexResultsAndJustCloseSession(query, session);
		List<CurrencyRatios> extract = new ArrayList<CurrencyRatios>();
		for (Object[] oa : result) {
			extract.add((CurrencyRatios) oa[0]);
		}
		return extract;
	}

	// TODO use index for searching
	public static final CurrencyRatios attemptToGetCurrencyRatio(CurrencyRatios cr) {
		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery(CurrencyRatios.Get_ByCurrencyCodeAndDate);
		query.setParameter(Currency.FieldCode, cr.getCurrency().getCode());
		query.setParameter(CurrencyRatios.FieldDate, cr.getDate());
		return checkQueryResultObjectExistenceAndJustCloseSession(query, session);
	}

	public static final CurrencyRatios getCurrencyRatio(String code, Date effectiveDay) {
		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery(CurrencyRatios.Get_ByCurrencyCodeAndDate);
		query.setParameter(Currency.FieldCode, code);
		query.setParameter(CurrencyRatios.FieldDate, effectiveDay);
		return checkQueryResultObjectExistenceAndJustCloseSession(query, session);
	}
}
