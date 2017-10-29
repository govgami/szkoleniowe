package testing;

import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;

public class TestObjects {
public Currency exampleCurrency;
public Country exampleCountry;

public TestObjects() {
	exampleCurrency=new Currency(null, "Dolar Anbar Annicki", "AAD");
	exampleCountry=new Country("Anbar Annika");
	
//	exampleCountry.addCurrency(exampleCurrency);
//	exampleCurrency.setCountry(exampleCountry);
}
}
