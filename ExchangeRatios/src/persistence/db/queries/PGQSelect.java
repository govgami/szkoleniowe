package persistence.db.queries;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import parser.Date2Str;
import persistence.db.PGQuery;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PGQSelect extends PGQuery {

	public static final <T> List<T> SelectAllFrom(String tableName) {
		validateQueryArgAgainstSQLInjection(tableName);
		Session session = openTransaction();
		Query query = session.createQuery("FROM " + tableName);
		List<T> list = query.getResultList();
		session.close();
		return list;
	}

	public static final List<Currency> SelectAllCurriencies() {
		Session session = openTransaction();
		Query query = session.createQuery("FROM Currency");
		List<Currency> list = query.getResultList();
		session.close();
		return list;
	}

	public static final <T> List<T> SelectAllSortedFrom(String tableName, String orderBy, boolean ascending) {
		validateQueryArgAgainstSQLInjection(tableName);
		validateQueryArgAgainstSQLInjection(orderBy);
		Session session = openTransaction();
		Query query = session
				.createQuery("FROM " + tableName + " ORDER BY " + orderBy + "  " + (ascending ? "ASC" : "DESC"));
		// query.setParameter("ascension", (ascending ? "ASC" : "DESC"));
		List<T> list = query.getResultList();
		session.close();
		return list;
	}

	public static final Country SelectCountryByName(String name) {
		// validateQueryArgAgainstSQLInjection(name);
		Session session = openTransaction();
		Query query = session.getNamedQuery("findCountryByName");
		query.setParameter("name", name);
		List<Country> list = query.getResultList();
		session.close();
		if (!list.isEmpty())
			return list.get(0);
		else
			throw new RuntimeException("There is no such Country: " + name);
	}

	public static final Currency SelectCurrencyByBySignShortcut(String name) {
		validateQueryArgAgainstSQLInjection(name);
		Session session = openTransaction();
		Query query = session.getNamedQuery("findCurrencyBySign");
		query.setParameter("shortcut", name);
		return presentQueryResultAndJustCloseSession(query, session);
	}

	public static final <T> List<T> SelectFirstOfAllSortedFrom(String tableName, String orderBy, boolean ascending,
			int limit) {
		validateQueryArgAgainstSQLInjection(tableName);
		validateQueryArgAgainstSQLInjection(orderBy);
		Session session = openTransaction();
		Query query = session
				.createQuery("FROM " + tableName + " ORDER BY " + orderBy + "  " + (ascending ? "ASC" : "DESC"));
		query.setMaxResults(limit);
		List<T> list = query.getResultList();
		session.close();
		return list;
	}

	public static final List<CurrencyRatios> SelectFirstOfLowestBidCurrencyRatios(String shortcut, int limit) {
		validateQueryArgAgainstSQLInjection(shortcut);

		Session session = openTransaction();
		Query query = session.getNamedQuery("findLowestBidOfChosenSignCurrencyRatio");// session.createQuery("FROM
																						// "+tableName+" ORDER BY
																						// "+orderBy+" "+(ascending ?
																						// "ASC" : "DESC"));
		query.setParameter("signShortcut", shortcut);
		query.setMaxResults(limit);
		List<CurrencyRatios> list = query.getResultList();
		session.close();
		return list;
	}

	public static final List<CurrencyRatios> SelectHighestPriceDifferenceOfCurrencyRatio(String shortcut, int limit) {
		validateQueryArgAgainstSQLInjection(shortcut);

		Session session = openTransaction();
		Query query = session.getNamedQuery("findHighestPriceDifferenceOfCurrencyRatio");
		query.setParameter("signShortcut", shortcut);
		query.setMaxResults(limit);
		List<CurrencyRatios> list = query.getResultList();
		session.close();
		return list;
	}

	public static final CurrencyRatios doesCurrencyRatioExist(CurrencyRatios cr) {
		Session session = openTransaction();
		Query query = session.createQuery("FROM CurrencyRatios WHERE currency_id=" + cr.getCurrencyId().getId()
				+ " AND effective_date= '" + new Date2Str(cr.getDate()).parse() + "'");
		List<CurrencyRatios> t = query.getResultList();
		session.close();
		if (t.isEmpty()) {
			return null;
		} else {
			return t.get(0);
		}
	}

}
