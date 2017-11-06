package testing;

import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;

public class TestObjects {
	public Currency exampleCurrency1;
	public Currency exampleCurrency2;
	public Country exampleCountry;

	public TestObjects() {
		exampleCurrency1 = new Currency(null, "Dolar Anbar Annicki", "AAD");
		exampleCurrency2 = new Currency(null, "Sennik Anbar Annicki", "ZZZ");
		exampleCountry = new Country("Anbar Annika");

	}
}
