package persistence.db.queries;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import persistence.db.PGQuery;

public class PGQSelect extends PGQuery{
	public static final <T> List<T> SelectAllFrom(String tableName) {
		validateQueryArg(tableName);
		Session session = openTransaction();
		Query query = session.createQuery("FROM :tableName");
		query.setParameter("tableName", tableName);
		List<T> list = (List<T>) query.getResultList();
		session.close();
		return list;
	}

	public static final <T> List<T> SelectAllSortedFrom(String tableName, String orderBy, boolean ascending) {
		validateQueryArg(tableName);
		validateQueryArg(orderBy);
		Session session = openTransaction();
		Query query = session.createQuery("FROM :tableName ORDER BY :orderBy :ascension");
		query.setParameter("tableName", tableName);
		query.setParameter("orderBy", orderBy);
		query.setParameter("ascension", (ascending ? "ASC" : "DESC"));
		List<T> list = (List<T>) query.getResultList();
		session.close();
		return list;
	}
	
	
}
