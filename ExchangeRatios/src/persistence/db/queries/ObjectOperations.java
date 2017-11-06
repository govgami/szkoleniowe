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

	public static final void InsertGroup(List<Object> list) {
		Session session = openTransaction();
		session.save(list);
		closeSession(session);
	}

	public static final void UpdateObject(Object obj) {
		Session session = openTransaction();
		session.update(obj);
		closeSession(session);
	}

	public static final void UpdateGroup(List<Object> list) {
		Session session = openTransaction();
		session.update(list);
		closeSession(session);
	}

	public static final void InsertOrUpdate(Object obj) {
		Session session = openTransaction();
		session.saveOrUpdate(obj);
		closeSession(session);
	}

	public static final void InsertOrUpdateGroup(List<Object> list) {
		Session session = openTransaction();
		session.saveOrUpdate(list);
		closeSession(session);
	}

	public static final void DeleteObject(Object o) {
		Session session = openTransaction();
		session.delete(o);
		closeSession(session);
	}

	public static final <T> void DeleteGroup(List<T> list) {
		DeleteObject(list);
	}

	public static final <T, T2 extends Serializable> T GetObject(Class<T> cl, T2 obj) {
		Session session = openTransaction();
		T result = session.get(cl, obj);
		closeSession(session);
		return result;
	}
}
