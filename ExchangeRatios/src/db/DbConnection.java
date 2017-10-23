package db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import logging.Log;

public class DbConnection {
	private Connection conn;
	private String host;
	private String dbName;
	private String user;
	private String pass;

	protected DbConnection() {
	}

	public DbConnection(String host, String dbName, String user, String pass) {

		this.host = host;
		this.dbName = dbName;
		this.user = user;
		this.pass = pass;
	}

	public static Connection makeDefaultPostgreConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection c = DriverManager.getConnection(DbContact.HOST + DbContact.DB_NAME, DbContact.USERNAME,
					DbContact.PASSWORD);
			return c;
		} catch (ClassNotFoundException | SQLException e) {
			Log.exception("DbConnection", e);
			throw new RuntimeException();
		}
	}

	public boolean connect() throws SQLException, ClassNotFoundException {
		checkCredecentials();
		Class.forName("org.postgresql.Driver");
		this.conn = DriverManager.getConnection(this.host + this.dbName, this.user, this.pass);
		return true;
	}

	void checkCredecentials() {
		if (host.isEmpty() || dbName.isEmpty() || user.isEmpty() || pass.isEmpty()) {
			try {
				throw new SQLException("Database credentials missing");
			} catch (SQLException e) {
				Log.exception("Empty credecential", e);
				throw new RuntimeException();
			}
		}
	}

	public void createPreProgrammed() {
		Query.createPreProgrammed(conn);
	}

	public ResultSet execQuery(String query) throws SQLException {
		return this.conn.createStatement().executeQuery(query);
	}

	public int insert(String table, Map<String, String> values) throws SQLException {
		StringBuilder columns = new StringBuilder();
		StringBuilder vals = new StringBuilder();
		for (String col : values.keySet()) {
			columns.append(col).append(",");
			if (values.get(col) instanceof String) {
				vals.append("'").append(values.get(col)).append("', ");
			} else
				vals.append(values.get(col)).append(",");
		}
		columns.setLength(columns.length() - 1);
		vals.setLength(vals.length() - 1);
		String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columns.toString(), vals.toString());

		return this.conn.createStatement().executeUpdate(query);
	}
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory()
    {
        try
        {
            // SessionFactory from hibernate.cfg.xml
            return new Configuration().configure(new File("hibernate.cfg.xml")).buildSessionFactory();
        }
        catch (Throwable ex) {
            Log.exception("Initial SessionFactory creation failed", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
  
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
  
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
