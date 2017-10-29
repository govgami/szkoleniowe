package persistence.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Session;
import logging.Log;
import persistence.db.queries.PGQSelect;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class PGQuery {

	public static final void Insert(Object object) {
		Session session=openTransaction();
		session.save(object);
		closeSession(session);
	}
	
	public static final void InsertGroup(List<Object> list) {
		for(Object o:list) {
			Insert(o);
		}
	}
	
	public static final void InsertActualizedCurrencyRatiosGroup(List<CurrencyRatios>list) {
CurrencyRatios existance;
		for(CurrencyRatios o:list) {
			existance=PGQSelect.doesCurrencyRatioExist((CurrencyRatios)o);
			if(existance!=null) {
				InsertOrUpdate( mergeObjectRatiosData(existance,o));
			}
			else {
				Insert(o);
			}
		}
	}
	
	public static final CurrencyRatios mergeObjectRatiosData(CurrencyRatios base, CurrencyRatios update) {
		if(update.getAskPrice()!=null) {
			base.setAskPrice(update.getAskPrice());
		}
		if(update.getBidPrice()!=null) {
			base.setBidPrice(update.getBidPrice());
		}
		if(update.getAvgPrice()!=null) {
			base.setAvgPrice(update.getAvgPrice());
		}
		return base;
	}
	
	
	public static final void UpdateObject(Object obj) {
		Session session=openTransaction();
		session.update(obj);
		closeSession(session);
	}
	
	public static final void InsertOrUpdate(Object obj) {
		Session session=openTransaction();
		session.saveOrUpdate(obj);
		closeSession(session);
	}

	public static final int Delete(Connection c, String tableName, int id) throws SQLException {

		Statement stmt = c.createStatement();
		String sql = "DELETE from " + tableName + " where ID = " + id;
		stmt.executeUpdate(sql);
		stmt.close();
		return id;
	}
	
	public static final void DeleteObject(Object o) {
		Session session=openTransaction();
		session.delete(o);
		closeSession(session);
	}

	public static void initDatabase(Connection conn) {
		try {
			Session session;
			Statement stmt = conn.createStatement();
			String sql = "CREATE TABLE COUNTRY (ID INT PRIMARY KEY NOT NULL, NAME VARCHAR(50) NOT NULL UNIQUE, CURRENCIES INT ARRAY);";
			stmt.execute(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE CURRENCY (ID INT PRIMARY KEY NOT NULL, COUNTRY_ID INT REFERENCES COUNTRY(ID), CURRENCY_NAME  VARCHAR(50),  SHORTCUT  VARCHAR(4)  NOT NULL )";
			stmt.execute(sql);//executeUpdate(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE CURRENCY_RATIOS (ID NUMERIC PRIMARY KEY NOT NULL, CURRENCY_ID    INT REFERENCES CURRENCY(ID)    NOT NULL, EFFECTIVE_DATE DATE   NOT NULL, ASK_PRICE  NUMERIC , BID_PRICE NUMERIC  , AVG_PRICE NUMERIC     )";
			stmt.execute(sql);
			stmt.close();
			
			stmt = conn.createStatement();
			sql = "CREATE TABLE COUNTRY_CURRENCY (ID INT PRIMARY KEY NOT NULL, CURRENCY_ID    INT REFERENCES CURRENCY(ID)    NOT NULL, COUNTRY_ID INT REFERENCES COUNTRY(ID)   NOT NULL  )";
			stmt.execute(sql);
			stmt.close();
			
			//seq
			stmt = conn.createStatement();
			sql = "CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1  NO MAXVALUE  NO MINVALUE CACHE 1;";
			stmt.execute(sql);
			stmt.close();
			
			//alt. country
			session = openTransaction();
			Country country=new Country("Non-classified");
			session.save(new Country("Non-classified"));
			
			
			
			//alt. currency
			session = openTransaction();
			session.save(new Currency(country, "Non-specified Currency", "???"));
			
			closeQuery(conn);
		} catch (SQLException e) {
			Log.exception("DbConnection Create DB table", e);
			throw new RuntimeException(e);
		}
	}

	public static void closeQuery(Connection conn) {
		try {
			if(!conn.getAutoCommit())
			conn.commit();
		} catch (SQLException e) {
			try {
				Log.exception("unable to commit", e);
				conn.rollback();
			} catch (SQLException e1) {
				Log.exception("rollback impossible", e1);
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		}
	}

	public static void validateQueryArgAgainstSQLInjection(String arg) {
		if (arg.split(" ").length > 1 | arg.isEmpty()) {
			throw new RuntimeException("Invalid argument: " + arg);
		}
	}

	protected static Session openTransaction() {
		Session session = DbConnection.getSessionFactory().openSession();
		session.beginTransaction();
		return session;
	}
	protected static void closeSession(Session s) {
		s.getTransaction().commit();
		s.close();
	}
	
	
}
