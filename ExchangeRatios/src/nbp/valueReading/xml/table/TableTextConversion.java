
package nbp.valueReading.xml.table;

import java.util.HashMap;

import nbp.CurrencyPrice;

public interface TableTextConversion {

	public <T> CurrencyPrice serviceMapping(HashMap<String, String> map);
}
