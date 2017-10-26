package nbp.valueReading.xml.table;

import java.util.HashMap;

import nbp.CurrencyPrice;

public class TablesC implements TableTextConversion {

	@Override
		public <T> CurrencyPrice serviceMapping(HashMap<String, String> map) {
			return CurrencyPrice.minMax(map.get("Currency"), map.get("Code"), map.get("Bid"), map.get("Ask"), map.get("EffectiveDate"));
	}

}
