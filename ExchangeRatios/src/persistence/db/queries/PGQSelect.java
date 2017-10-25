package persistence.db.queries;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import parser.Date2Str;
import persistence.db.PGQuery;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PGQSelect extends PGQuery{
	
	public static final <T> List<T> SelectAllFrom(String tableName) {
		validateQueryArg(tableName);
		Session session = openTransaction();
		Query query = session.createQuery("FROM "+tableName);
		List<T> list = (List<T>) query.getResultList();
		session.close();
		return list;
	}
	
	public static final List<Currency> SelectAllCurriencies(){
		Session session = openTransaction();
		Query query = session.createQuery("FROM Currency");
		List<Currency> list = (List<Currency>) query.getResultList();
		session.close();
		return list;
	}

	public static final <T> List<T> SelectAllSortedFrom(String tableName, String orderBy, boolean ascending) {
		validateQueryArg(tableName);
		validateQueryArg(orderBy);
		Session session = openTransaction();
		Query query = session.createQuery("FROM "+tableName+" ORDER BY "+orderBy+"  "+(ascending ? "ASC" : "DESC"));
		//query.setParameter("ascension", (ascending ? "ASC" : "DESC"));
		List<T> list = (List<T>) query.getResultList();
		session.close();
		return list;
	}
	
	public static final <T> List<T> SelectFirstOfAllSortedFrom(String tableName, String orderBy, boolean ascending, int limit) {
		validateQueryArg(tableName);
		validateQueryArg(orderBy);
		Session session = openTransaction();
		Query query = session.createQuery("FROM "+tableName+" ORDER BY "+orderBy+"  "+(ascending ? "ASC" : "DESC"));
		query.setMaxResults(limit);
		List<T> list = (List<T>) query.getResultList();
		session.close();
		return list;
	}
	
	public static final CurrencyRatios doesCurrencyRatioExist(CurrencyRatios cr) {
		Session session=openTransaction();
		Query query=session.createQuery("FROM CurrencyRatios WHERE currency_id="+cr.getCurrencyId().getId()+" AND effective_date= '"+new Date2Str(cr.getDate()).parse()+"'");
		List<CurrencyRatios> t=query.getResultList();
		session.close();
		if(t.isEmpty()) {
			return null;
		}else {
			return t.get(0);
		}
	}

	
}
