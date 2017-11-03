package persistence.db.table.currency.util;

import java.util.Comparator;

import persistence.db.table.currency.Currency;

public class CurrencyCodeUnicodeComparator implements Comparator<Currency> {
	@Override
	public int compare(Currency o1, Currency o2) {
		return (o1.getCode().compareTo(o2.getCode()));
	}

}
