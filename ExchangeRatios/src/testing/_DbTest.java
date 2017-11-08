
package testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import main.Helper;
import persistence.db.DbAccess;
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

	TestObjects examples;
	Calendar dateConst = Calendar.getInstance();
	Date date1;
	Calendar dateConst2 = Calendar.getInstance();
	Date date2;

	Country c;
	Currency curr1, curr2;

	final int LIMIT_OF_5 = 5;

	@BeforeMethod
	public void setUp() {
		examples = new TestObjects();
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
		Connection c = DbAccess.makeDefaultPostgreConnection();

		// Then
		assertThat(c).isNotNull();
	}

	// @Test
	public void shouldInitializeDbStructure() {
		PGQuery.initDatabaseStructure(DbAccess.makeDefaultPostgreConnection());
	}

	// @Test
	public void shouldDestroyDbStructure() {
		PGQuery.dropDatabaseStructure(DbAccess.makeDefaultPostgreConnection());
	}

	// DEV better to use component ID?
	// @Test(dependsOnMethods = {
	// "shouldCreateDefaultConnection"
	// })
	public void shouldGatherCurrenciesData() {
		// When
		Helper.gatherData(date1, date2);

		List<Currency> list = PGQSelect.selectAllCurriencies();

		// Then
		assertThat(list).isNotEmpty();
	}

	// TODO maven artifact
	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetSortedCurrency() {

		// Given
		List<Currency> list = PGQSelect.selectAllCurrenciesSortedAscByCode(null);

		// Then
		assertThat(list).isSortedAccordingTo(new CurrencyCodeUnicodeComparator());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetLimittedSortedCurrency() {

		// When
		List<Currency> list = PGQSelect.selectAllCurrenciesSortedAscByCode(LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5).isSortedAccordingTo(new CurrencyCodeUnicodeComparator());
	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency"
	})
	public void shouldGetCurrencyRatio() {

		// Given
		Currency curr = PGQSelect.selectCurrency_ByCode(examples.currency1.getCode());
		CurrencyRatios ratio = PGQSelect.attemptToGetCurrencyRatio(examples.ratio1);
		if (ratio != null) {
			ObjectOperations.deleteObject(ratio);
		}
		assertThat(ratio).isNull();

		// When
		examples.ratio1.setCurrency(curr);
		ObjectOperations.insert(examples.ratio1);

		ratio = PGQSelect.attemptToGetCurrencyRatio(examples.ratio1);

		// Then
		ratio = PGQSelect.getCurrencyRatio(ratio.getCurrency().getCode(), ratio.getDate());
		assertThat(ratio).isNotNull();
		assertThat(ratio.getCurrency()).hasFieldOrPropertyWithValue(Currency.FIELD_CODE, examples.ratio1.getCurrency().getCode());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetLimittedSortedCurrencyRatiosLowestBidPrice() {

		// Given
		List<CurrencyRatios> list = PGQSelect.selectLowestBidCurrencyRatios("USD", LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5);
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetHighestPriceDifferenceOfCurrencyRatios() {

		// Given
		List<CurrencyRatios> list = PGQSelect.selectHighestPriceDifferenceOfCurrencyRatio("USD", LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5).isSortedAccordingTo(new CurrencyRatiosReverseAskBidDiffComparator());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldInsertNewCurrency() {

		// Given
		Currency curr = PGQSelect.selectCurrency_ByCode(examples.currency1.getCode());
		if (curr != null) {
			ObjectOperations.deleteObject(curr);
		}

		// When
		ObjectOperations.insert(examples.currency1);

		// Then
		curr = PGQSelect.selectCurrency_ByCode(examples.currency1.getCode());
		assertThat(curr).hasFieldOrPropertyWithValue(Currency.FIELD_CODE, examples.currency1.getCode());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldInsertNewCountry() {

		// When
		Country c = PGQSelect.selectCountry_ByName(examples.country.getName());
		if (c != null) {
			ObjectOperations.deleteObject(c);
		}
		ObjectOperations.insert(examples.country);

		// Then
		c = PGQSelect.selectCountry_ByName(examples.country.getName());
		assertThat(c).hasFieldOrPropertyWithValue(Country.FIELD_NAME, examples.country.getName());
	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency",
		"shouldInsertNewCountry"
	})
	public void shouldEnsureFetchingResults() {

		// When
		Country c = PGQSelect.fetchCountry_ByName(examples.country.getName());
		Currency curr = PGQSelect.fetchCurrency_ByCode(examples.currency1.getCode());

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
		Country c = PGQSelect.fetchCountry_ByName(examples.country.getName());
		Currency curr = PGQSelect.fetchCurrency_ByCode(examples.currency1.getCode());

		// When
		PGQuery.connectCountryCurrency(c, curr);

		// Then
		CountryCurrency ccurr = ObjectOperations.getObject(CountryCurrency.class, new CountryCurrencyId(c, curr));
		assertThat(ccurr).isNotNull();

	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency",
		"shouldInsertNewCountry",
		"shouldEnsureFetchingResults"
	})
	public void shouldTemporaryConnectSecondCurrencyCountry() {

		// Given
		curr2 = PGQSelect.selectCurrency_ByCode(examples.currency2.getCode());
		if (curr2 != null) {
			ObjectOperations.deleteObject(curr2);
		}
		ObjectOperations.insert(examples.currency2);

		curr2 = PGQSelect.fetchCurrency_ByCode(examples.currency2.getCode());
		c = PGQSelect.fetchCountry_ByName(examples.country.getName());

		// When
		PGQuery.connectCountryCurrency(c, curr2);

		// Then
		CountryCurrency ccurr = ObjectOperations.getObject(CountryCurrency.class, new CountryCurrencyId(c, curr2));
		assertThat(ccurr).isNotNull();

		curr1 = PGQSelect.fetchCurrency_ByCode(examples.currency1.getCode());
		curr2 = PGQSelect.fetchCurrency_ByCode(examples.currency2.getCode());
		c = PGQSelect.fetchCountry_ByName(examples.country.getName());

		assertThat(c).isNotNull();
		assertThat(c.getCurrencies()).hasSize(2).doesNotHaveDuplicates();

	}

	// TODO try prolonging transaction when inserting mass of new data
	@Test(dependsOnMethods = {
		"shouldGetCurrencyRatio",
		"shouldTemporaryConnectSecondCurrencyCountry"
	})
	public void shouldGetAllConnectedToCountryOnDay() {

		// When
		List<Object[]> results = PGQSelect.getCountryAssociates_ByNameAndDay(examples.country.getName(), examples.ratio1.getDate());

		// Then
		assertThat(results).isNotEmpty();
		Country country = (Country) results.get(0)[0];
		List<Currency> currencies = new ArrayList<Currency>();
		List<CurrencyRatios> ratios = new ArrayList<CurrencyRatios>();
		for (int i = 1; i < results.get(0).length; i++) {
			if (results.get(0)[i].getClass() == Currency.class)
				currencies.add((Currency) results.get(0)[i]);
			else if (results.get(0)[i].getClass() == CurrencyRatios.class)
				ratios.add((CurrencyRatios) results.get(0)[i]);
			System.out.println(i + " " + results.get(0)[i] + " " + ((CurrencyRatios) results.get(0)[i]).getCurrency());
		}

		// assertThat(countries).isNotNull();// assertThat(countries).isNotEmpty().hasSize(1);
		// assertThat(currencies).isNotEmpty().hasSize(2);
		assertThat(ratios).isNotEmpty().hasSize(1);

	}

	@Test(dependsOnMethods = {
		"shouldConnectCurrencyCountry",
		"shouldTemporaryConnectSecondCurrencyCountry",
		"shouldGetAllConnectedToCountryOnDay"
	})
	public void shouldDisconnectCurrencyCountryRelations() {

		// Given
		Country c = PGQSelect.fetchCountry_ByName(examples.country.getName());
		Currency curr = PGQSelect.fetchCurrency_ByCode(examples.currency1.getCode());
		Currency curr2 = PGQSelect.fetchCurrency_ByCode(examples.currency2.getCode());

		// When
		PGQuery.disconnect_CountryCurrency(c, curr);
		PGQuery.disconnect_CountryCurrency(c, curr2);

		// Then
		CountryCurrency ccurr = ObjectOperations.getObject(CountryCurrency.class, new CountryCurrencyId(c, curr));
		assertThat(ccurr).isNull();
		ccurr = ObjectOperations.getObject(CountryCurrency.class, new CountryCurrencyId(c, curr2));
		assertThat(ccurr).isNull();
	}

	@Test(dependsOnMethods = {
		"shouldDisconnectCurrencyCountryRelations",
		"shouldGetCurrencyRatio"
	})
	public void shouldRemoveObjects() {

		// Given
		Currency curr = PGQSelect.selectCurrency_ByCode(examples.currency1.getCode());
		Currency curr2 = PGQSelect.selectCurrency_ByCode(examples.currency2.getCode());
		Country c = PGQSelect.selectCountry_ByName(examples.country.getName());
		// When
		ObjectOperations.deleteObject(curr);
		ObjectOperations.deleteObject(curr2);
		ObjectOperations.deleteObject(c);
		// Then
		curr = PGQSelect.selectCurrency_ByCode(examples.currency1.getCode());
		curr2 = PGQSelect.selectCurrency_ByCode(examples.currency2.getCode());
		c = PGQSelect.selectCountry_ByName(examples.country.getName());
		assertThat(curr).isNull();
		assertThat(curr2).isNull();
		assertThat(c).isNull();

	}

}
