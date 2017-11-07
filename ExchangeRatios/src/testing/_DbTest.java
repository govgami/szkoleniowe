
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

	Country c;
	Currency curr1, curr2;

	final int LIMIT_OF_5 = 5;

	@BeforeMethod
	public void setUp() {
		objects = new TestObjects();
		dateConst.set(2016, 9, 1);
		date1 = new Date(dateConst.getTime().getTime());
		Calendar dateConst2 = Calendar.getInstance();
		dateConst2.set(2017, 7, 30);
		date2 = new Date(dateConst2.getTime().getTime());
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void shouldThrowRuntimeExceptionDueToWrongParameter() {

		// Given
		String str = "drop database";

		// When
		PGQuery.validateQueryArgAgainstSimpleSQLInjection(str);
	}

	// 133800 349s/ 222s/
	@Test
	public void shouldCreateDefaultConnection() {

		// When
		Connection c = DbConnection.makeDefaultPostgreConnection();

		// Then
		assertThat(c).isNotNull();
	}

	// @Test
	public void shouldInitializeDbStructure() {
		PGQuery.initDatabaseStructure(DbConnection.makeDefaultPostgreConnection());
	}

	// @Test
	public void shouldDestroyDbStructure() {
		PGQuery.dropDatabaseStructure(DbConnection.makeDefaultPostgreConnection());
	}

	// DEV better to use component ID?
	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGatherCurrenciesData() {
		// When
		Helper.gatherData(date1, date2);

		List<Currency> list = PGQSelect.SelectAllCurriencies();

		// Then
		assertThat(list).isNotEmpty();
	}

	// TODO maven artifact
	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetSortedCurrency() {

		// Given
		List<Currency> list = PGQSelect.SelectAllCurrenciesSortedAscByCode(null);

		// Then
		assertThat(list).isSortedAccordingTo(new CurrencyCodeUnicodeComparator());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetLimittedSortedCurrency() {

		// When
		List<Currency> list = PGQSelect.SelectAllCurrenciesSortedAscByCode(LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5).isSortedAccordingTo(new CurrencyCodeUnicodeComparator());
	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency"
	})
	public void shouldGetCurrencyRatio() {

		// Given
		Currency curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency1.getCode());
		CurrencyRatios ratio = PGQSelect.attemptToGetCurrencyRatio(objects.ratio1);
		if (ratio != null) {
			ObjectOperations.DeleteObject(ratio);
		}
		assertThat(ratio).isNull();

		// When
		objects.ratio1.setCurrency(curr);
		ObjectOperations.Insert(objects.ratio1);

		ratio = PGQSelect.attemptToGetCurrencyRatio(objects.ratio1);

		// Then
		ratio = PGQSelect.getCurrencyRatio(ratio.getCurrency().getCode(), ratio.getDate());
		assertThat(ratio).isNotNull();
		assertThat(ratio.getCurrency()).hasFieldOrPropertyWithValue(Currency.FIELD_CODE, objects.ratio1.getCurrency().getCode());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetLimittedSortedCurrencyRatiosLowestBidPrice() {

		// Given
		List<CurrencyRatios> list = PGQSelect.SelectLowestBidCurrencyRatios("USD", LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5);
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetHighestPriceDifferenceOfCurrencyRatios() {

		// Given
		List<CurrencyRatios> list = PGQSelect.SelectHighestPriceDifferenceOfCurrencyRatio("USD", LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5).isSortedAccordingTo(new CurrencyRatiosReverseAskBidDiffComparator());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldInsertNewCurrency() {

		// Given
		Currency curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency1.getCode());
		if (curr != null) {
			ObjectOperations.DeleteObject(curr);
		}

		// When
		ObjectOperations.Insert(objects.exampleCurrency1);

		// Then
		curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency1.getCode());
		assertThat(curr).hasFieldOrPropertyWithValue(Currency.FIELD_CODE, objects.exampleCurrency1.getCode());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldInsertNewCountry() {

		// When
		Country c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		if (c != null) {
			ObjectOperations.DeleteObject(c);
		}
		ObjectOperations.Insert(objects.exampleCountry);

		// Then
		c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		assertThat(c).hasFieldOrPropertyWithValue(Country.FIELD_NAME, objects.exampleCountry.getName());
	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency",
		"shouldInsertNewCountry"
	})
	public void shouldEnsureFetchingResults() {

		// When
		Country c = PGQSelect.FetchCountryByName(objects.exampleCountry.getName());
		Currency curr = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency1.getCode());

		// Then
		assertThat(curr.getCountries()).isEmpty();
		assertThat(c.getCurrencies()).isEmpty();
	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency",
		"shouldInsertNewCountry",
		"shouldEnsureFetchingResults"
	})
	public void shouldConnectCurrencyCountry() {

		// Given
		Country c = PGQSelect.FetchCountryByName(objects.exampleCountry.getName());
		Currency curr = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency1.getCode());

		// When
		PGQuery.ConnectCountryCurrency(c, curr);

		// Then
		CountryCurrency ccurr = ObjectOperations.GetObject(CountryCurrency.class, new CountryCurrencyId(c, curr));
		assertThat(ccurr).isNotNull();

	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency",
		"shouldInsertNewCountry",
		"shouldEnsureFetchingResults"
	})
	public void shouldTemporaryConnectSecondCurrencyCountry() {

		// Given
		curr2 = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency2.getCode());
		if (curr2 != null) {
			ObjectOperations.DeleteObject(curr2);
		}
		ObjectOperations.Insert(objects.exampleCurrency2);

		curr2 = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency2.getCode());
		c = PGQSelect.FetchCountryByName(objects.exampleCountry.getName());

		// When
		PGQuery.ConnectCountryCurrency(c, curr2);

		// Then
		CountryCurrency ccurr = ObjectOperations.GetObject(CountryCurrency.class, new CountryCurrencyId(c, curr2));
		assertThat(ccurr).isNotNull();

		curr1 = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency1.getCode());
		curr2 = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency2.getCode());
		c = PGQSelect.FetchCountryByName(objects.exampleCountry.getName());

		assertThat(c).isNotNull();
		assertThat(c.getCurrencies()).hasSize(2).doesNotHaveDuplicates();

	}

	@Test(dependsOnMethods = {
		"shouldConnectCurrencyCountry",
		"shouldTemporaryConnectSecondCurrencyCountry"
	})
	public void shouldGetAllConnectedToCountryOnDay() {

	}

	@Test(dependsOnMethods = {
		"shouldConnectCurrencyCountry",
		"shouldTemporaryConnectSecondCurrencyCountry"
	})
	public void shouldDisconnectCurrencyCountryRelations() {

		// Given
		Country c = PGQSelect.FetchCountryByName(objects.exampleCountry.getName());
		Currency curr = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency1.getCode());
		Currency curr2 = PGQSelect.FetchCurrencyByCode(objects.exampleCurrency2.getCode());

		// When
		PGQuery.DisconnectCountryCurrency(c, curr);
		PGQuery.DisconnectCountryCurrency(c, curr2);

		// Then
		CountryCurrency ccurr = ObjectOperations.GetObject(CountryCurrency.class, new CountryCurrencyId(c, curr));
		assertThat(ccurr).isNull();
		ccurr = ObjectOperations.GetObject(CountryCurrency.class, new CountryCurrencyId(c, curr2));
		assertThat(ccurr).isNull();
	}

	// TODO when flush, dirty_checking, debugging sql
	@Test(dependsOnMethods = {
		"shouldDisconnectCurrencyCountryRelations",
		"shouldGetCurrencyRatio"
	})
	public void shouldRemoveObjects() {

		// Given
		Currency curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency1.getCode());
		Currency curr2 = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency2.getCode());
		Country c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		// When
		ObjectOperations.DeleteObject(curr);
		ObjectOperations.DeleteObject(curr2);
		ObjectOperations.DeleteObject(c);
		// Then
		curr = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency1.getCode());
		curr2 = PGQSelect.SelectCurrencyByCode(objects.exampleCurrency2.getCode());
		c = PGQSelect.SelectCountryByName(objects.exampleCountry.getName());
		assertThat(curr).isNull();
		assertThat(curr2).isNull();
		assertThat(c).isNull();

	}

}
