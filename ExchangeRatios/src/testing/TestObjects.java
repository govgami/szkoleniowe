package testing;

import java.math.BigDecimal;
import java.sql.Date;

import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class TestObjects {
	public CurrencyRatios ratio1;
	public Currency currency1;
	public Currency currency2;
	public Country country;

	public TestObjects() {
		currency1 = new Currency(null, "Dolar Anbar Annicki", "AAD");
		currency2 = new Currency(null, "Sennik Anbar Annicki", "ZZZ");
		country = new Country("Anbar Annika");
		ratio1 = new CurrencyRatios(currency1, new Date(1998, 8, 5), new BigDecimal("50.04"), null, null);

	}
}
