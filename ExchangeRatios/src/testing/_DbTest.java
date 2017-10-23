package testing;

import static org.testng.Assert.assertNotNull;

import java.sql.Connection;

import org.testng.annotations.Test;

import db.DbConnection;

public class _DbTest {
  @Test
  public void create() {
	  Connection c=DbConnection.makeDefaultPostgreConnection();
	  assertNotNull(c);
  }
}
