package persistence.db.queries;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import persistence.db.DbConnection;

public class BasicOperations {

	protected static Session openTransaction() {
		Session session = DbConnection.getSessionFactory().openSession();
		session.beginTransaction();
		return session;
	}

	protected static <T> List<T> presentQueryResultsAndFinishSession(Query<T> query, Session session) {
		List<T> list = query.getResultList();
		closeSession(session);
		return list;
	}

	protected static <T> T presentQueryResultAndFinishSession(Query<T> query, Session session) {
		List<T> list = query.getResultList();
		closeSession(session);
		if (!list.isEmpty())
			return list.get(0);
		else
			throw new RuntimeException("Query didn't provide searched object: " + query.getQueryString());
	}

	protected static <T> List<T> presentQueryResultsAndJustCloseSession(Query<T> query, Session session) {
		List<T> list = query.getResultList();
		session.close();
		return list;
	}

	protected static List<Object[]> presentQueryComplexResultsAndJustCloseSession(Query<Object[]> query,
			Session session) {
		List<Object[]> list = query.list();
		session.close();
		return list;
	}

	protected static <T> T presentQueryResultAndJustCloseSession(Query<T> query, Session session) {
		List<T> list = query.getResultList();
		session.close();
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
		// else
		// throw new RuntimeException("Query didn't provide searched object: " +
		// query.getQueryString());
	}

	protected static <T> T checkQueryResultObjectExistenceAndJustCloseSession(Query<T> query, Session session) {
		List<T> list = query.getResultList();
		session.close();
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
		// throw new RuntimeException("Query didn't provide searched object: " +
		// query.getQueryString());
	}

	protected static void closeSession(Session s) {
		s.getTransaction().commit();
		s.close();
	}

	protected static <T> void applyOptionalLimitOnResultsNumber(Query<T> query, Integer limit) {
		if (limit != null) {
			if (limit > 0)
				query.setMaxResults(limit);
		}
	}

}
