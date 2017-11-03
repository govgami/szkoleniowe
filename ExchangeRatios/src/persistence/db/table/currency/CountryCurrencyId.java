package persistence.db.table.currency;

import javax.persistence.Id;

public class CountryCurrencyId {
	@Id
	Country country;
	@Id
	Currency currency;
}
