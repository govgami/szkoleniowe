package testing;

import static org.testng.Assert.assertEquals;
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
import persistence.db.queries.ObjectOperations;
import persistence.db.queries.PGQSelect;
import persistence.db.queries.PGQuery;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class _DbTest {
	TestObjects objects;
	Calendar dateConst = Calendar.getInstance();
	Date date1;
	Calendar dateConst2 = Calendar.getInstance();
	Date date2;

	final int practicalLimit = 5;

	@BeforeMethod
	public void setUp() {
		objects = new TestObjects();
		dateConst.set(2015, 9, 1);
		date1 = new Date(dateConst.getTime().getTime());
		Calendar dateConst2 = Calendar.getInstance();
		dateConst2.set(2016, 7, 30);
		date2 = new Date(dateConst2.getTime().getTime());
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void shouldThrowRuntimeExceptionDueToWrongParameter() {
		String str = "drop database";
		PGQuery.validateQueryArgAgainstSQLInjection(str);
	}

	@Test
	public void shouldCreateDefaultConnection() {
		Connection c = DbConnection.makeDefaultPostgreConnection();
		assertNotNull(c);
	}

	// @Test
	public void shouldInitializeDbStructure() {
		PGQuery.initDatabaseStructure(DbConnection.makeDefaultPostgreConnection());
	}

	// @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGatherCurrenciesData() {
		Helper.gatherData(date1, date2);
		List<Currency> list = PGQSelect.SelectAllCurriencies();
		for (Currency c : list) {
			System.out.print(c.getCode() + ":all:");
		}
		System.out.print("\n");
		Assert.assertNotEquals(0, list.size());
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGetSortedCurrency() {
		List<Currency> list = PGQSelect.SelectAllCurrenciesSortedByCode(null);
		for (Currency c : list) {
			System.out.print(c.getCode() + ":sorted:");
		}
		System.out.print("\n");
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGetLimittedSortedCurrency() {
		List<Currency> list = PGQSelect.SelectAllCurrenciesSortedByCode(practicalLimit);
		for (Currency c : list) {
			System.out.print(c.getCode() + ":limit:");
		}
		System.out.print("\n");
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGetLimittedSortedCurrencyRatiosLowestBidPrice() {
		List<CurrencyRatios> list = PGQSelect.SelectLowestBidCurrencyRatios("USD", practicalLimit);
		for (CurrencyRatios c : list) {
			System.out.print(c.getBidPrice() + ":crLowBid:");
		}
		System.out.print("\n");
		assertEquals(practicalLimit, list.size());
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGetHighestPriceDifferenceOfCurrencyRatios() {
		List<CurrencyRatios> list = PGQSelect.SelectHighestPriceDifferenceOfCurrencyRatio("USD", practicalLimit);
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getBidPrice() + ":crHghDff:");
		}
		System.out.print("\n");
		Assert.assertEquals(practicalLimit, list.size());
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldInsertNewCurrency() {
		// try{
		Currency curr = PGQSelect.checkCurrencyCodeExistence(objects.exampleCurrency.getCode());
		if (curr != null) {
			ObjectOperations.DeleteObject(curr);
		}
		ObjectOperations.Insert(objects.exampleCurrency);

		// }catch(RuntimeException e) {
		// Currency
		// c=PGQSelect.SelectCurrencyByBySignShortcut(objects.exampleCurrency.getSign());
		// PGQuery.DeleteObject(c);
		// throw new RuntimeException(e);
		// }
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection", "shouldInsertNewCurrency" })
	public void shouldInsertNewCountry() {
		// try {
		Country c = PGQSelect.checkCountryExistence(objects.exampleCountry.getName());
		if (c != null) {
			ObjectOperations.DeleteObject(c);
		}
		ObjectOperations.Insert(objects.exampleCountry);
		// }catch(RuntimeException e) {
		// Country c=PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		// PGQuery.DeleteObject(c);
		// throw new RuntimeException(e);
		// }
	}

	@Test(dependsOnMethods = { "shouldInsertNewCurrency", "shouldInsertNewCountry" })
	public void shouldConnectCurrencyCountry() {
		try {
			Country c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
			Currency curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency.getCode());
			PGQuery.ConnectCountryCurrency(c, curr);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Test(dependsOnMethods = { "shouldConnectCurrencyCountry" })
	public void shouldDisconnectCurrencyCountry() {
		Country c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		Currency curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency.getCode());
		PGQuery.DisconnectCountryCurrency(c, curr);
	}

	@Test(dependsOnMethods = { "shouldDisconnectCurrencyCountry" })
	public void shouldRemoveObjects() {
		Currency curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency.getCode());
		ObjectOperations.DeleteObject(curr);
		Country c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		ObjectOperations.DeleteObject(c);
	}

}
