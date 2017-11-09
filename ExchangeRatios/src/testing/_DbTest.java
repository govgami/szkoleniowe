
package testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import main.Helper;
import persistence.db.connection.DbAccess;
import persistence.db.queries.ObjectOperations;
import persistence.db.queries.PreparedQueries;
import persistence.db.queries.PreparedSelections;
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
		PreparedQueries.initDatabaseStructure(DbAccess.makeCustomPostgreConnection());
	}

	// @Test
	public void shouldDestroyDbStructure() {
		PreparedQueries.dropDatabaseStructure(DbAccess.makeCustomPostgreConnection());
	}

	// DEV better to use component ID?
	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGatherCurrenciesData() {
		// When
		Helper.gatherData(date1, date2);

		List<Currency> list = PreparedSelections.selectAllCurriencies();

		// Then
		assertThat(list).isNotEmpty();
	}

	// TODO maven artifact
	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetSortedCurrency() {

		// Given
		List<Currency> list = PreparedSelections.selectAllCurrenciesSortedAscByCode(null);

		// Then
		assertThat(list).isSortedAccordingTo(new CurrencyCodeUnicodeComparator());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetLimittedSortedCurrency() {

		// When
		List<Currency> list = PreparedSelections.selectAllCurrenciesSortedAscByCode(LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5).isSortedAccordingTo(new CurrencyCodeUnicodeComparator());
	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency"
	})
	public void shouldGetCurrencyRatio() {

		// Given
		Currency curr = PreparedSelections.selectCurrency_ByCode(examples.currency1.getCode());
		CurrencyRatios ratio = PreparedSelections.selectCurrencyRatioByCodeAndDate(examples.ratio1);
		if (ratio != null) {
			ObjectOperations.deleteObject(ratio);
		}
		assertThat(ratio).isNull();

		// When
		examples.ratio1.setCurrency(curr);
		ObjectOperations.insert(examples.ratio1);

		ratio = PreparedSelections.selectCurrencyRatioByCodeAndDate(examples.ratio1);

		// Then
		ratio = PreparedSelections.selectCurrencyRatioByCodeAndDate(ratio.getCurrency().getCode(), ratio.getDate());
		assertThat(ratio).isNotNull();
		assertThat(ratio.getCurrency()).hasFieldOrPropertyWithValue(Currency.FIELD_CODE, examples.ratio1.getCurrency().getCode());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetLimittedSortedCurrencyRatiosLowestBidPrice() {

		// Given
		List<CurrencyRatios> list = PreparedSelections.selectLowestBidCurrencyRatios("USD", LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5);
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldGetHighestPriceDifferenceOfCurrencyRatios() {

		// Given
		List<CurrencyRatios> list = PreparedSelections.selectHighestPriceDifferenceOfCurrencyRatio("USD", LIMIT_OF_5);

		// Then
		assertThat(list).hasSize(LIMIT_OF_5).isSortedAccordingTo(new CurrencyRatiosReverseAskBidDiffComparator());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldInsertNewCurrency() {

		Currency curr;
		// When
		TestUtils.exampleCurrencyInsert(examples.currency1);
		TestUtils.exampleCurrencyInsert(examples.currency2);

		// Then
		curr = PreparedSelections.selectCurrency_ByCode(examples.currency1.getCode());
		assertThat(curr).hasFieldOrPropertyWithValue(Currency.FIELD_CODE, examples.currency1.getCode());
		curr = PreparedSelections.selectCurrency_ByCode(examples.currency2.getCode());
		assertThat(curr).hasFieldOrPropertyWithValue(Currency.FIELD_CODE, examples.currency2.getCode());
	}

	@Test(dependsOnMethods = {
		"shouldCreateDefaultConnection"
	})
	public void shouldInsertNewCountry() {

		// When
		TestUtils.exampleCountryInsert(examples.country1);
		TestUtils.exampleCountryInsert(examples.country2);

		// Then
		c = PreparedSelections.selectCountry_ByName(examples.country1.getName());
		assertThat(c).hasFieldOrPropertyWithValue(Country.FIELD_NAME, examples.country1.getName());
	}

	@Test(dependsOnMethods = {
		"shouldInsertNewCurrency",
		"shouldInsertNewCountry"
	})
	public void shouldEnsureFetchingResults() {

		// When
		Country c = PreparedSelections.fetchCountry_ByName(examples.country1.getName());
		Currency curr = PreparedSelections.fetchCurrency_ByCode(examples.currency1.getCode());

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
		Country c = PreparedSelections.fetchCountry_ByName(examples.country1.getName());
		Currency curr = PreparedSelections.fetchCurrency_ByCode(examples.currency1.getCode());
		Country c2 = PreparedSelections.fetchCountry_ByName(examples.country2.getName());
		Currency curr2 = PreparedSelections.fetchCurrency_ByCode(examples.currency2.getCode());

		// When
		PreparedQueries.connect_CountryCurrency(c, curr);
		PreparedQueries.connect_CountryCurrency(c, curr2);
		PreparedQueries.connect_CountryCurrency(c2, curr2);

		// Then
		CountryCurrency ccurr = ObjectOperations.getObject(CountryCurrency.class, new CountryCurrencyId(c, curr));
		assertThat(ccurr).isNotNull();
		ccurr = ObjectOperations.getObject(CountryCurrency.class, new CountryCurrencyId(c, curr2));
		assertThat(ccurr).isNotNull();
		ccurr = ObjectOperations.getObject(CountryCurrency.class, new CountryCurrencyId(c2, curr2));
		assertThat(ccurr).isNotNull();

		c = PreparedSelections.fetchCountry_ByName(examples.country1.getName());
		assertThat(c).isNotNull();
		assertThat(c.getCurrencies()).hasSize(2).doesNotHaveDuplicates();

		c = PreparedSelections.fetchCountry_ByName(examples.country2.getName());
		assertThat(c).isNotNull();
		assertThat(c.getCurrencies()).hasSize(1);

	}

	// TODO try prolonging transaction when inserting mass of new data
	@Test(dependsOnMethods = {
		"shouldGetCurrencyRatio",
		"shouldConnectCurrencyCountry"
	})
	public void shouldGetAllRatiosConnectedToCountryOnDay() {

		// When
		// List<Object[]> results = PGQSelect.getCountryAssociates_ByNameAndDay(examples.country.getName(), examples.ratio1.getDate());
		List<CurrencyRatios> results =
				PreparedSelections.selectCountryAssociates_ByNameAndDay(examples.country1.getName(), examples.ratio1.getDate());
		// Then
		assertThat(results).isNotEmpty().hasSize(1);

		// Country country = (Country) results.get(0)[0];
		// List<Currency> currencies = new ArrayList<Currency>();
		// List<CurrencyRatios> ratios = new ArrayList<CurrencyRatios>();
		// for (int i = 1; i < results.get(0).length; i++) {
		// if (results.get(0)[i].getClass() == Currency.class)
		// currencies.add((Currency) results.get(0)[i]);
		// else if (results.get(0)[i].getClass() == CurrencyRatios.class)
		// ratios.add((CurrencyRatios) results.get(0)[i]);
		// System.out.println(i + " " + results.get(0)[i] + " " + ((CurrencyRatios) results.get(0)[i]).getCurrency());
		// }

		// assertThat(countries).isNotNull();// assertThat(countries).isNotEmpty().hasSize(1);
		// assertThat(currencies).isNotEmpty().hasSize(2);
		// assertThat(ratios).isNotEmpty().hasSize(1);

	}

	@Test(dependsOnMethods = {
		"shouldGetCurrencyRatio",
		"shouldConnectCurrencyCountry"
	})
	public void shouldGetCurrencyCountForEachCountry() {

		// When
		List<Country> results = PreparedSelections.getCountriesWithCurrencyCount();

		// Then
		assertThat(results).isNotNull().hasSize(2);

	}

	@Test(dependsOnMethods = {
		"shouldGetCurrencyCountForEachCountry"
	})
	public void shouldGetOnlyMultiCurrencyCountries() {

		// When
		List<Country> results = PreparedSelections.getCountriesWithMultiCurrencyCount();

		// Then
		assertThat(results).isNotNull().hasSize(1);

	}

	@Test(dependsOnMethods = {
		"shouldConnectCurrencyCountry",
		"shouldGetAllRatiosConnectedToCountryOnDay",
		"shouldGetOnlyMultiCurrencyCountries"
	})
	public void shouldDisconnectCurrencyCountryRelations() {

		// Given
		Country c = PreparedSelections.fetchCountry_ByName(examples.country1.getName());
		Country c2 = PreparedSelections.fetchCountry_ByName(examples.country2.getName());
		Currency curr = PreparedSelections.fetchCurrency_ByCode(examples.currency1.getCode());
		Currency curr2 = PreparedSelections.fetchCurrency_ByCode(examples.currency2.getCode());

		// When
		PreparedQueries.disconnect_CountryCurrency(c, curr);
		PreparedQueries.disconnect_CountryCurrency(c, curr2);
		PreparedQueries.disconnect_CountryCurrency(c2, curr2);

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
		Currency curr = PreparedSelections.selectCurrency_ByCode(examples.currency1.getCode());
		Currency curr2 = PreparedSelections.selectCurrency_ByCode(examples.currency2.getCode());
		Country c = PreparedSelections.selectCountry_ByName(examples.country1.getName());
		Country c2 = PreparedSelections.selectCountry_ByName(examples.country2.getName());
		// When
		ObjectOperations.deleteObject(curr);
		ObjectOperations.deleteObject(curr2);
		ObjectOperations.deleteObject(c);
		ObjectOperations.deleteObject(c2);
		// Then
		curr = PreparedSelections.selectCurrency_ByCode(examples.currency1.getCode());
		curr2 = PreparedSelections.selectCurrency_ByCode(examples.currency2.getCode());
		c = PreparedSelections.selectCountry_ByName(examples.country1.getName());
		c2 = PreparedSelections.selectCountry_ByName(examples.country2.getName());
		assertThat(curr).isNull();
		assertThat(curr2).isNull();
		assertThat(c).isNull();
		assertThat(c2).isNull();

	}

}
