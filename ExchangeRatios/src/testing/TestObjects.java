package testing;

import java.math.BigDecimal;
import java.sql.Date;

import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class TestObjects {
	public CurrencyRatios ratio1;
	public Currency exampleCurrency1;
	public Currency exampleCurrency2;
	public Country exampleCountry;

	public TestObjects() {
		exampleCurrency1 = new Currency(null, "Dolar Anbar Annicki", "AAD");
		exampleCurrency2 = new Currency(null, "Sennik Anbar Annicki", "ZZZ");
		exampleCountry = new Country("Anbar Annika");
		ratio1 = new CurrencyRatios(exampleCurrency1, new Date(1998, 8, 5), new BigDecimal("50.04"), null, null);

	}
}
