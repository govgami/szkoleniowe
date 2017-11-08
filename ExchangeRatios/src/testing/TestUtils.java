
package testing;

import persistence.db.queries.ObjectOperations;
import persistence.db.queries.PGQSelect;
import persistence.db.table.currency.Country;
import persistence.db.table.currency.Currency;

public class TestUtils {

	public static void exampleCountryInsert(Country c) {
		Country cc = PGQSelect.selectCountry_ByName(c.getName());
		if (cc != null) {
			ObjectOperations.deleteObject(cc);
		}
		ObjectOperations.insert(c);
	}

	public static void exampleCurrencyInsert(Currency c) {
		Currency curr = PGQSelect.selectCurrency_ByCode(c.getCode());
		if (curr != null) {
			ObjectOperations.deleteObject(curr);
		}
		ObjectOperations.insert(c);
	}
}
