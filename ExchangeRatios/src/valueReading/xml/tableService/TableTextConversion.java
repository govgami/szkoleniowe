package valueReading.xml.tableService;

import java.util.*;

import valueReading.utility.CurrencyPrice;

public interface TableTextConversion {
public <T> CurrencyPrice serviceMapping(HashMap<String, String> map);
}
