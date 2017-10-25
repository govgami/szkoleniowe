package testing;

import static org.testng.Assert.assertNotNull;

import java.sql.Connection;
import java.util.List;

import javax.management.Query;

import org.testng.annotations.Test;

import main.Helper;
import persistence.db.DbConnection;
import persistence.db.PGQuery;
import persistence.db.queries.PGQSelect;
import persistence.db.table.currency.Currency;

public class _DbTest {
  @Test
  public void createDefaultConnection() {
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
		  System.out.print(c.getSign()+":  :");
	  }
  }
  @Test
  public void getSortedCurrency() {
	  List<Currency> list=PGQSelect.SelectAllSortedFrom("Currency", "shortcut", true);
	  for(Currency c: list) {
		  System.out.println(c.getSign());
	  }
  }
  

}
