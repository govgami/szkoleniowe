
package persistence.db.queries;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import persistence.db.connection.DbAccess;

public class SessionAssist {

	protected static Session openTransaction() {
		Session session = DbAccess.openCustomSession();
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

	protected static List<Object[]> presentQueryComplexResultsAndJustCloseSession(Query<Object[]> query, Session session) {
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
	}

	protected static void closeSession(Session s) {
		s.getTransaction().commit();
		s.close();
	}

}
