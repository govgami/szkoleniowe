package persistence.db.table.currency.util;

import java.util.Comparator;

import persistence.db.table.currency.CurrencyRatios;

public class CurrencyRatiosAskBidDiffComparator implements Comparator<CurrencyRatios> {
	@Override
	public int compare(CurrencyRatios o1, CurrencyRatios o2) {
		return o1.getAskBidPricesDifference().compareTo(o2.getAskBidPricesDifference());
	}

}
