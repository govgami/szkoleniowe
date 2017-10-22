package valueReading.utility;

import java.math.BigDecimal;

import parser.Str2BigDecimal;

public class CurrencyPrice {
	private int id;
	private String currencySign;
	private BigDecimal avgCurrencyPrice;
	private String effectiveDate;
	private BigDecimal sellPrice;
	private BigDecimal buyPrice;

	//DEV insert for searching whole array of currency prices
	public CurrencyPrice() {}
	
	public CurrencyPrice(String currencySign, BigDecimal avgPrice, String effectiveDate) {
		this.currencySign=currencySign;
		this.avgCurrencyPrice=avgPrice;
		this.effectiveDate=effectiveDate;
	}
	public CurrencyPrice(String currencySign, BigDecimal sellPrice, BigDecimal buyPrice, String effectiveDate) {
		this.currencySign=currencySign;
		this.sellPrice=sellPrice;
		this.buyPrice=buyPrice;
		this.effectiveDate=effectiveDate;
	}
	
	public static CurrencyPrice avg(String currencySign,String avgPrice, String effectiveDate) {
		return new CurrencyPrice(currencySign, new Str2BigDecimal(avgPrice).parse(), effectiveDate);
	}
	
	public static CurrencyPrice minMax(String currencySign, String sellPrice, String buyPrice, String effectiveDate) {
		return new CurrencyPrice(currencySign, new Str2BigDecimal(sellPrice).parse(), new Str2BigDecimal(buyPrice).parse(), effectiveDate);	
	}
	
	public int getId() {
		return id;
	}
	
	public String getCurrencySign() {
		return currencySign;
	}

	private void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	public BigDecimal getAvgCurrencyPrice() {
		return avgCurrencyPrice;
	}

	private void setAvgCurrencyPrice(BigDecimal avgCurrencyPrice) {
		this.avgCurrencyPrice = avgCurrencyPrice;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}

	private void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}
	
}
