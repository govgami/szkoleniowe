package testing;

import static org.testng.Assert.assertNotNull;

import java.sql.Connection;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import main.Helper;
import persistence.db.DbConnection;
import persistence.db.PGQuery;
import persistence.db.queries.PGQSelect;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class _DbTest {
	TestObjects objects;
	@BeforeMethod
	public void setUp(){
		objects=new TestObjects();
	}
	
  @Test
  public void shouldCreateDefaultConnection() {
	  Connection c=DbConnection.makeDefaultPostgreConnection();
	  assertNotNull(c);
  }

  public void shouldInitializeDbStructure() {
	  PGQuery.initDatabase(DbConnection.makeDefaultPostgreConnection());
  }
  @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
  public void shouldGatheredCurrenciesData() {
	  Helper.gatherData();
	  List<Currency> list=PGQSelect.SelectAllCurriencies();
	  for(Currency c: list) {
		  System.out.print(c.getSign()+":all:");
	  }
	  System.out.print("\n");
	  Assert.assertNotEquals(0, list.size());
  }
  @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
  public void shouldGetSortedCurrency() {
	  List<Currency> list=PGQSelect.SelectAllSortedFrom("Currency", "shortcut", true);
	  for(Currency c: list) {
		  System.out.print(c.getSign()+":sorted:");
	  }
	  System.out.print("\n");
  }
  
  @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
  public void shouldGetLimittedSortedCurrency() {
	  List<Currency> list=PGQSelect.SelectFirstOfAllSortedFrom("Currency", "shortcut", true, 5);
	  for(Currency c: list) {
		  System.out.print(c.getSign()+":limit:");
	  }
	  System.out.print("\n");
  }
  
  @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
  public void shouldGetLimittedSortedCurrencyRatiosLowestBidPrice() {
	  List<CurrencyRatios> list=PGQSelect.SelectFirstOfAllSortedFrom("CurrencyRatios", "bid_price", true, 3);
	  for(CurrencyRatios c: list) {
		  System.out.print(c.getBidPrice()+":crLowBid:");
	  }
	  System.out.print("\n");
  }
  @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
  public void shouldInsertNewCurrency() {
	  PGQuery.Insert(objects.exampleCurrency);
  }
  @Test(dependsOnMethods = { "shouldCreateDefaultConnection", "shouldInsertNewCurrency" })
  public void shouldInsertNewCountry() {
	  PGQuery.Insert(objects.exampleCountry);
  }
//  @Test(dependsOnMethods = { "shouldInsertNewCurrency", "shouldInsertNewCurrency" })
//  public void shouldConnectAndDisconnectCurrencyCountry() {
//	  PGQuery.Insert(objects.exampleCountry);
//  }
  

}
