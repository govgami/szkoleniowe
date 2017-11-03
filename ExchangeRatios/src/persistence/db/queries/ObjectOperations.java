package persistence.db.queries;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public class ObjectOperations extends BasicOperations {

	public static final void Insert(Object object) {
		Session session = openTransaction();
		session.save(object);
		closeSession(session);
	}

	// TODO insert whole lists for multiple rows with batch?
	public static final void InsertGroup(List<Object> list) {
		for (Object o : list) {
			Insert(o);
		}
	}

	public static final void UpdateObject(Object obj) {
		Session session = openTransaction();
		session.update(obj);
		closeSession(session);
	}

	public static final void InsertOrUpdate(Object obj) {
		Session session = openTransaction();
		session.saveOrUpdate(obj);
		closeSession(session);
	}

	public static final void DeleteObject(Object o) {
		Session session = openTransaction();
		session.delete(o);
		closeSession(session);
	}

	public static final <T extends Serializable> T GetObject(Class<T> cl, T obj) {
		Session session = openTransaction();
		T result = session.get(cl, obj);
		closeSession(session);
		return result;
	}
}
