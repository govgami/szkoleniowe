package testing;

import static org.testng.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import main.Helper;
import persistence.db.DbConnection;
import persistence.db.PGQuery;
import persistence.db.queries.PGQSelect;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class _DbTest {
	TestObjects objects;
	@BeforeMethod
	public void setUp(){
		objects=new TestObjects();
	}
	
	@Test(expectedExceptions=RuntimeException.class)
	public void shouldThrowRuntimeExceptionDueToWrongParameter() {
		String str="drop database";
		PGQuery.validateQueryArgAgainstSQLInjection(str);
	}
	
  @Test
  public void shouldCreateDefaultConnection() {
	  Connection c=DbConnection.makeDefaultPostgreConnection();
	  assertNotNull(c);
  }
//@Test
  public void shouldInitializeDbStructure() {
	  PGQuery.initDatabase(DbConnection.makeDefaultPostgreConnection());
  }
//  @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
  public void shouldGatheredCurrenciesData() {
		Calendar dateConst = Calendar.getInstance();
		dateConst.set(2015, 5, 1);
		Date date1=new Date( dateConst.getTime().getTime());
		Calendar dateConst2 = Calendar.getInstance();
		dateConst2.set(2015, 7, 30);//dateConst2.set(2015, 5, 8);
		Date date2=new Date( dateConst2.getTime().getTime());
	  Helper.gatherData(date1, date2);
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
	  List<CurrencyRatios> list=PGQSelect.SelectFirstOfLowestBidCurrencyRatios("USD", 3);
	  for(CurrencyRatios c: list) {
		  System.out.print(c.getBidPrice()+":crLowBid:");
	  }
	  System.out.print("\n");
	  Assert.assertEquals(3, list.size());
  }
  @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
  public void shouldGetHighestPriceDifferenceOfCurrencyRatios() {
	  List<CurrencyRatios> list=PGQSelect.SelectHighestPriceDifferenceOfCurrencyRatio("USD", 3);
	  for(CurrencyRatios c: list) {
		  System.out.print(c.getBidPrice()+":crLowBid:");
	  }
	  System.out.print("\n");
	  Assert.assertEquals(3, list.size());
  }
 // @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
  public void shouldInsertNewCurrency() {
//	  try{
	  PGQuery.Insert(objects.exampleCurrency);
//  }catch(RuntimeException e) {
//	  Currency c=PGQSelect.SelectCurrencyByBySignShortcut(objects.exampleCurrency.getSign());
//	  PGQuery.DeleteObject(c);
//	  throw new RuntimeException(e);
//  }
  }
 // @Test(dependsOnMethods = { "shouldCreateDefaultConnection", "shouldInsertNewCurrency" })
  public void shouldInsertNewCountry() {
//	  try {
	  PGQuery.Insert(objects.exampleCountry);
//	  }catch(RuntimeException e) {
//		  Country c=PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
//		  PGQuery.DeleteObject(c);
//		  throw new RuntimeException(e);
//	  }
  }
//  @Test(dependsOnMethods = { "shouldInsertNewCurrency", "shouldInsertNewCountry" })
  public void shouldConnectCurrencyCountry() {
	  try {
		  Country c=PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		  Currency curr=PGQSelect.SelectCurrencyByBySignShortcut(objects.exampleCurrency.getSign());
	  PGQuery.ConnectCountryCurrency(c, curr);
	  }catch(RuntimeException e) {
		  throw new RuntimeException(e);
	  }
  }
  //@Test(dependsOnMethods = { "shouldConnectCurrencyCountry" })
  public void shouldDisconnectCurrencyCountry() {
	  Country c=PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
	  Currency curr=PGQSelect.SelectCurrencyByBySignShortcut(objects.exampleCurrency.getSign());
	  PGQuery.DisconnectCountryCurrency(c, curr);
  }
 // @Test(dependsOnMethods = { "shouldDisconnectCurrencyCountry" })
  public void shouldRemoveObjects() {
	  Currency curr=PGQSelect.SelectCurrencyByBySignShortcut(objects.exampleCurrency.getSign());
	  PGQuery.DeleteObject(curr);
	  Country c=PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
	  PGQuery.DeleteObject(c);
  }
  

}
