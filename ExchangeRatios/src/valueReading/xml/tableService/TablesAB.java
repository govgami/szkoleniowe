package valueReading.xml.tableService;

import java.util.HashMap;

import valueReading.utility.CurrencyPrice;

public class TablesAB implements TableTextConversion{

	public <T> CurrencyPrice serviceMapping(HashMap<String, String> map) {
			return CurrencyPrice.avg(map.get("Code"), map.get("Mid"), map.get("EffectiveDate"));
	}
	

}
