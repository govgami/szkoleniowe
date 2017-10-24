package valueReading.xml.tableService;

import java.util.*;

import persistence.db.table.currency.CurrencyPrice;

public interface TableTextConversion {
public <T> CurrencyPrice serviceMapping(HashMap<String, String> map);
}
