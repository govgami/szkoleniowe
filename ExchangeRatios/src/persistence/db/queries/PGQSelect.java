
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

	public static final List<Currency> selectAllCurriencies() {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery(Currency.GET_ALL);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<Currency> selectAllCurrenciesSortedAscByCode(Integer limit) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery(Currency.GET_ALL_SORTED_BY_CODE);
		applyOptionalLimitOnResultsNumber(query, limit);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<Country> selectAllCountries() {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.GET_ALL);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final Country fetchCountry_ByName(String name) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.FETCH_BY_NAME);
		query.setParameter(Country.FIELD_NAME, name);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Object fetchCountryAssociates_ByNameAndDay(String name, Date day) {
		Session session = openTransaction();
		Query<Object> query = session.getNamedQuery(Country.FETCH_ALL_WITH_COUNTRY_ON_DAY);
		query.setParameter(Country.FIELD_NAME, name);
		query.setParameter(CurrencyRatios.FIELD_DATE, day);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Country selectCountry_ByName(String name) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.GET_BY_NAME);
		query.setParameter(Country.FIELD_NAME, name);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Currency selectCurrency_ByCode(String code) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery(Currency.GET_BY_CODE);
		query.setParameter(Currency.FIELD_CODE, code);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Currency fetchCurrency_ByCode(String code) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery(Currency.FETCH_BY_CODE);
		query.setParameter(Currency.FIELD_CODE, code);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final List<CurrencyRatios> selectLowestBidCurrencyRatios(String code, Integer limit) {
		validateQueryArgAgainstSimpleSQLInjection(code);

		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery(CurrencyRatios.GET_LOWEST_BID_PRICE_OF_CHOSEN_CODE);
		query.setParameter(Currency.FIELD_CODE, code);
		applyOptionalLimitOnResultsNumber(query, limit);
		return presentQueryResultsAndJustCloseSession(query, session);
	}

	public static final List<CurrencyRatios> selectHighestPriceDifferenceOfCurrencyRatio(String code, Integer limit) {
		validateQueryArgAgainstSimpleSQLInjection(code);

		Session session = openTransaction();
		Query<Object[]> query = session.getNamedQuery(CurrencyRatios.GET_HIGHEST_DIFFERENCE_OF_ASK_AND_BID_PRICE);
		query.setParameter(Currency.FIELD_CODE, code);
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
		Query<CurrencyRatios> query = session.getNamedQuery(CurrencyRatios.GET_BY_CURRENCY_CODE_AND_DATE);
		query.setParameter(Currency.FIELD_CODE, cr.getCurrency().getCode());
		query.setParameter(CurrencyRatios.FIELD_DATE, cr.getDate());
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final CurrencyRatios getCurrencyRatiosInPeriod(String code, Date from, Date to) {
		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery(CurrencyRatios.GET_BY_CURRENCY_CODE_AND_DATE);
		query.setParameter(Currency.FIELD_CODE, code);
		query.setParameter("from", from);
		query.setParameter("to", to);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final CurrencyRatios getCurrencyRatio(String code, Date effectiveDay) {
		Session session = openTransaction();
		Query<CurrencyRatios> query = session.getNamedQuery(CurrencyRatios.GET_BY_CURRENCY_CODE_AND_DATE);
		query.setParameter(Currency.FIELD_CODE, code);
		query.setParameter(CurrencyRatios.FIELD_DATE, effectiveDay);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final Country getCountryById(Long id) {
		Session session = openTransaction();
		Query<Country> query = session.getNamedQuery(Country.GET_BY_ID);
		query.setParameter(Country.FIELD_ID, id);
		return presentQueryResultAndJustCloseSession(query, session);

	}

	public static final Currency getCurrencyById(Long id) {
		Session session = openTransaction();
		Query<Currency> query = session.getNamedQuery(Currency.GET_BY_ID);
		query.setParameter(Currency.FIELD_ID, id);
		return presentQueryResultAndJustCloseSession(query, session);

	}
	// compress instant deploy
	//
	// TODO select countries with multiple currencies
	// TODO select countries using specified currency
	// TODO select county, currencies and

}
