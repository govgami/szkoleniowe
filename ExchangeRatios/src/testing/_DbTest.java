package testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import main.Helper;
import persistence.db.DbConnection;
import persistence.db.queries.ObjectOperations;
import persistence.db.queries.PGQSelect;
import persistence.db.queries.PGQuery;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.CountryCurrency;
import persistence.db.table.currency.CountryCurrencyId;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;
import persistence.db.table.currency.util.CurrencyCodeUnicodeComparator;
import persistence.db.table.currency.util.CurrencyRatiosReverseAskBidDiffComparator;

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

	// TODO queries using many to many
	@Test(expectedExceptions = RuntimeException.class)
	public void shouldThrowRuntimeExceptionDueToWrongParameter() {
		// given
		String str = "drop database";
		// when
		PGQuery.validateQueryArgAgainstSQLInjection(str);
	}

	@Test
	public void shouldCreateDefaultConnection() {
		// when
		Connection c = DbConnection.makeDefaultPostgreConnection();
		// then
		assertThat(c).isNotNull();
	}

	// @Test
	public void shouldInitializeDbStructure() {
		PGQuery.initDatabaseStructure(DbConnection.makeDefaultPostgreConnection());
	}

	// DEV check implementation for optimalization rows insertion by code -> batch
	// DEV change countries to lazy-fetching
	// @Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGatherCurrenciesData() {
		// when
		Helper.gatherData(date1, date2);

		List<Currency> list = PGQSelect.SelectAllCurriencies();
		for (Currency c : list) {
			System.out.print(c.getCode() + ":all:");
		}
		System.out.print("\n");
		// then
		assertThat(list).isNotEmpty();
	}

	// TODO maven artifact
	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGetSortedCurrency() {
		// Given
		List<Currency> list = PGQSelect.SelectAllCurrenciesSortedAscByCode(null);

		for (Currency c : list) {
			System.out.print(c.getCode() + ":sorted:");
		}
		System.out.print("\n");

		// TODO kaskady
		// then
		assertThat(list).isSortedAccordingTo(new CurrencyCodeUnicodeComparator());
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGetLimittedSortedCurrency() {
		// When
		List<Currency> list = PGQSelect.SelectAllCurrenciesSortedAscByCode(practicalLimit);
		for (Currency c : list) {
			System.out.print(c.getCode() + ":limit:");
		}
		System.out.print("\n");

		// Then
		assertThat(list).hasSize(practicalLimit).isSortedAccordingTo(new CurrencyCodeUnicodeComparator());
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGetLimittedSortedCurrencyRatiosLowestBidPrice() {
		// Given
		List<CurrencyRatios> list = PGQSelect.SelectLowestBidCurrencyRatios("USD", practicalLimit);
		for (CurrencyRatios c : list) {
			System.out.print(c.getBidPrice() + ":crLowBid:");
		}
		System.out.print("\n");
		// Then
		assertThat(list).hasSize(practicalLimit);
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldGetHighestPriceDifferenceOfCurrencyRatios() {
		// Given
		List<CurrencyRatios> list = PGQSelect.SelectHighestPriceDifferenceOfCurrencyRatio("USD", practicalLimit);
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getBidPrice() + ":crHghDff:");
		}
		System.out.print("\n");
		// Then
		assertThat(list).hasSize(practicalLimit).isSortedAccordingTo(new CurrencyRatiosReverseAskBidDiffComparator());
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldInsertNewCurrency() {
		// Given
		Currency curr = PGQSelect.checkCurrencyCodeExistence(objects.exampleCurrency.getCode());
		if (curr != null) {
			ObjectOperations.DeleteObject(curr);
		}
		// When
		ObjectOperations.Insert(objects.exampleCurrency);

		// Then
		curr = PGQSelect.checkCurrencyCodeExistence(objects.exampleCurrency.getCode());
		assertThat(curr).hasFieldOrPropertyWithValue(Currency.FieldCode, objects.exampleCurrency.getCode());
	}

	@Test(dependsOnMethods = { "shouldCreateDefaultConnection" })
	public void shouldInsertNewCountry() {
		// try {
		Country c = PGQSelect.checkCountryExistence(objects.exampleCountry.getName());
		if (c != null) {
			ObjectOperations.DeleteObject(c);
		}
		ObjectOperations.Insert(objects.exampleCountry);
		// Then
		c = PGQSelect.checkCountryExistence(objects.exampleCountry.getName());
		assertThat(c).hasFieldOrPropertyWithValue(Country.FieldName, objects.exampleCountry.getName());
	}

	@Test(dependsOnMethods = { "shouldInsertNewCurrency", "shouldInsertNewCountry" })
	public void shouldFetchObjects() {
		// When
		Country c = PGQSelect.FetchCountryByName(objects.exampleCountry.getName());
		Currency curr = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency.getCode());
		// Then
		assertThat(curr).isNotNull();
		assertThat(c).isNotNull();
	}

	@Test(dependsOnMethods = { "shouldInsertNewCurrency", "shouldInsertNewCountry", "shouldFetchObjects" })
	public void shouldConnectCurrencyCountry() {
		try {
			// Given
			Country c = PGQSelect.FetchCountryByName(objects.exampleCountry.getName());
			Currency curr = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency.getCode());
			// When
			PGQuery.ConnectCountryCurrency(c, curr);
			// Then
			CountryCurrency ccurr = ObjectOperations.GetObject(CountryCurrency.class, new CountryCurrencyId(c, curr));
			assertThat(ccurr).isNotNull();

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Test(dependsOnMethods = { "shouldConnectCurrencyCountry" })
	public void shouldDisconnectCurrencyCountry() {
		// given
		Country c = PGQSelect.FetchCountryByName(objects.exampleCountry.getName());
		Currency curr = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency.getCode());
		// when
		PGQuery.DisconnectCountryCurrency(c, curr);
		// then
		CountryCurrency ccurr = ObjectOperations.GetObject(CountryCurrency.class, new CountryCurrencyId(c, curr));
		assertThat(ccurr).isNull();
	}

	@Test(dependsOnMethods = { "shouldDisconnectCurrencyCountry" })
	public void shouldRemoveObjects() {
		// given
		Currency curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency.getCode());
		Country c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		// when
		ObjectOperations.DeleteObject(curr);
		ObjectOperations.DeleteObject(c);
		// then
		curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency.getCode());
		c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		assertThat(curr).isNull();
		assertThat(c).isNull();
	}

}
