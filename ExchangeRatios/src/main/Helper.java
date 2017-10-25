package main;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import nbp.CurrencyPrice;
import nbp.main.HttpXmlNbpPeriodTableCurrency;
import parser.Str2SqlDate;
import persistence.db.PGQuery;
import persistence.db.queries.PGQSelect;
import persistence.db.table.currency.Currency;
import persistence.db.table.currency.CurrencyRatios;

public class Helper {
public static void gatherData() {
	Date from=new Date(Calendar.getInstance().getTimeInMillis()-1000*3600*24*6);
	Date to=new Date(Calendar.getInstance().getTimeInMillis());
	PGQuery.InsertCurrencyRatiosGroup(parsePrice2Ratios(HttpXmlNbpPeriodTableCurrency.takeTable("a", from, to)));
	PGQuery.InsertCurrencyRatiosGroup(parsePrice2Ratios(HttpXmlNbpPeriodTableCurrency.takeTable("a", from, to)));
	//PGQuery.Insert(parsePrice2Ratios(HttpXmlNbpPeriodTableCurrency.takeTable("b", from, to)));


}

public static void gatherData(Date from, Date to) {
	PGQuery.InsertCurrencyRatiosGroup(parsePrice2Ratios(HttpXmlNbpPeriodTableCurrency.takeTable("a", from, to)));
	PGQuery.InsertCurrencyRatiosGroup(parsePrice2Ratios(HttpXmlNbpPeriodTableCurrency.takeTable("b", from, to)));
}

static List<CurrencyRatios> parsePrice2Ratios(List<CurrencyPrice> list) {
	List<CurrencyRatios> result=new ArrayList<CurrencyRatios>();
	HashMap<String, Currency> map=getCurrencies();
	CurrencyRatios t;
	for(CurrencyPrice cp: list) {
		t=new CurrencyRatios();
		t.setDate((Date)new Str2SqlDate(cp.getEffectiveDate()).parse());
		System.out.println(t.getCurrencyId()+" "+t.getDate());
		t.setCurrencyId(map.get(cp.getCurrencySign()));
		saveFromUnknownCurrency(cp, t,map);
		adjustFieldsOf(cp, t);
		//TODO fix!!
		result.add(t);
	}
	return result;
}
static HashMap<String, Currency> getCurrencies(){
	List<Currency> curr=PGQSelect.SelectAllFrom("Currency");
	HashMap<String, Currency> map=new HashMap<String, Currency>();
	for(Currency c:curr) {
		map.put(c.getSign(), c);
	}
	return map;
}

static void saveFromUnknownCurrency(CurrencyPrice cp, CurrencyRatios cr, HashMap<String,Currency>map) {
	if(cr.getCurrencyId()==null) {
		PGQuery.Insert(new Currency(null, null, cp.getCurrencySign()));
		map.clear();
		map.putAll(getCurrencies());
	}
}

static void adjustFieldsOf(CurrencyPrice cp, CurrencyRatios cr) {
	if(cr.getAskPrice()==null) {
		cr.setAskPrice(cp.getBuyPrice());
	}
	if(cr.getBidPrice()==null) {
		cr.setBidPrice(cp.getSellPrice());
	}
	if(cr.getAvgPrice()==null) {
		cr.setAvgPrice(cp.getAvgCurrencyPrice());
	}
}

}
