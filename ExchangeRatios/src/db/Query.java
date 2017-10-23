package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import logging.Log;

public class Query {
	static final String select = "SELECT FROM ";
	static final String whereDate = "WHERE ";

	public static final String Select5BestFromDay(String tableName, Date date) {
		return null;
	}

	public static final int Delete(Connection c, String tableName, int id) throws SQLException {

		Statement stmt = c.createStatement();
		String sql = "DELETE from " + tableName + " where ID = " + id;
		stmt.executeUpdate(sql);
		stmt.close();
		return id;
	}

	public static void createPreProgrammed(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			String sql = "CREATE TABLE COUNTRY " + "(ID INT PRIMARY KEY     NOT NULL,"
					+ " NAME           TEXT    NOT NULL ";
			stmt.executeUpdate(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE CURRENCY " + "(ID INT PRIMARY KEY     NOT NULL,"
					+ " COUNTRY_ID           INT REFERENCES COUNTRY(ID)    NOT NULL, "
					+ " CURRENCY_NAME           TEXT    NOT NULL, " + " SHORTCUT            CHAR(4)     NOT NULL ";
			stmt.executeUpdate(sql);
			stmt.close();

			stmt = conn.createStatement();
			sql = "CREATE TABLE CURRENCY_RATIOS " + "(ID INT PRIMARY KEY     NOT NULL,"
					+ " CURRENCY_ID           INT REFERENCES CURRENCY(ID)    NOT NULL, "
					+ " EFFECTIVE_DATE           STRING   NOT NULL, " + " ASK_PRICE           NUMERIC    , "
					+ " BID_PRICE            NUMERIC     " + " AVG_PRICE           NUMERIC     ";
			stmt.executeUpdate(sql);
			stmt.close();

			conn.commit();
		} catch (SQLException e) {
			Log.exception("DbConnection Create DB table", e);
			throw new RuntimeException();
		}
	}

}
