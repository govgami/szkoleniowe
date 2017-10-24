package persistence.db.table.currency.util;

import java.util.Comparator;

import persistence.db.table.currency.CurrencyRatios;

public class CurrencyRatiosComparator implements Comparator<CurrencyRatios> {

	@Override
	public int compare(CurrencyRatios o1, CurrencyRatios o2) {
		return (identity(o1, o2) ? 0 : o1.getDate().after(o2.getDate()) ? 1 : -1);
	}

	protected boolean identity(CurrencyRatios o1, CurrencyRatios o2) {
		return o1.getDate() == o2.getDate() & o1.getCurrencyId().getId() == o2.getCurrencyId().getId();
	}

}
