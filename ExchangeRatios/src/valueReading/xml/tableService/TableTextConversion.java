package valueReading.xml.tableService;

import java.util.*;

import db.table.CurrencyPrice;

public interface TableTextConversion {
public <T> CurrencyPrice serviceMapping(HashMap<String, String> map);
}
