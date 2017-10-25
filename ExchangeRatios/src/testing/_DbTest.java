package testing;

import static org.testng.Assert.assertNotNull;

import java.sql.Connection;

import javax.management.Query;

import org.testng.annotations.Test;

import main.Helper;
import persistence.db.DbConnection;
import persistence.db.PGQuery;
import persistence.db.queries.PGQSelect;
import persistence.db.table.currency.Currency;

public class _DbTest {
  @Test
  public void create() {
	  Connection c=DbConnection.makeDefaultPostgreConnection();
	  assertNotNull(c);
  }

  public void createPreConfigured() {
	  PGQuery.createPreProgrammed(DbConnection.makeDefaultPostgreConnection());
  }
  @Test
  public void getData() {
	  Helper.gatherData();
	  for(Currency c: PGQSelect.SelectAllCurriencies()) {
		  System.out.println(c.getSign());
	  }
  }

}
