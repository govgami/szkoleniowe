package valueReading.xml.tableService;

import java.util.HashMap;

import persistence.db.table.currency.CurrencyPrice;

public class TablesC implements TableTextConversion {

	@Override
		public <T> CurrencyPrice serviceMapping(HashMap<String, String> map) {
			return CurrencyPrice.minMax(map.get("Code"), map.get("Bid"), map.get("Ask"), map.get("EffectiveDate"));
	}

}
