package valueReading.xml.tableService;

import java.util.*;

import nbp.CurrencyPrice;

public interface TableTextConversion {
public <T> CurrencyPrice serviceMapping(HashMap<String, String> map);
}
