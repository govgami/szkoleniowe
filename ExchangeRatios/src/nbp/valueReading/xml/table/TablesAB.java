package nbp.valueReading.xml.table;

import java.util.HashMap;

import nbp.CurrencyPrice;

public class TablesAB implements TableTextConversion{

	public <T> CurrencyPrice serviceMapping(HashMap<String, String> map) {
			return CurrencyPrice.avg(map.get("Currency"),map.get("Code"), map.get("Mid"), map.get("EffectiveDate"));
	}
	

}
