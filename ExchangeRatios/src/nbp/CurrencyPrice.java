package nbp;

import java.math.BigDecimal;

import parser.Str2BigDecimal;

public class CurrencyPrice {
	private String currencyName;
	private String currencySign;
	private BigDecimal avgCurrencyPrice;
	private String effectiveDate;
	private BigDecimal sellPrice;
	private BigDecimal buyPrice;

	public CurrencyPrice() {
	}

	public CurrencyPrice(String currencyName, String currencySign, BigDecimal avgPrice, String effectiveDate) {
		this.currencyName = currencyName;
		this.currencySign = currencySign;
		this.avgCurrencyPrice = avgPrice;
		this.effectiveDate = effectiveDate;
	}

	public CurrencyPrice(String currencyName, String currencySign, BigDecimal sellPrice, BigDecimal buyPrice,
			String effectiveDate) {
		this.currencyName = currencyName;
		this.currencySign = currencySign;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		this.effectiveDate = effectiveDate;
	}

	public static CurrencyPrice avg(String currencyName, String currencySign, String avgPrice, String effectiveDate) {
		return new CurrencyPrice(currencyName, currencySign, new Str2BigDecimal(avgPrice).parse(), effectiveDate);
	}

	public static CurrencyPrice minMax(String currencyName, String currencySign, String sellPrice, String buyPrice,
			String effectiveDate) {
		return new CurrencyPrice(currencyName, currencySign, new Str2BigDecimal(sellPrice).parse(),
				new Str2BigDecimal(buyPrice).parse(), effectiveDate);
	}

	public String getCurrencySign() {
		return currencySign;
	}

	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	public BigDecimal getAvgCurrencyPrice() {
		return avgCurrencyPrice;
	}

	public void setAvgCurrencyPrice(BigDecimal avgCurrencyPrice) {
		this.avgCurrencyPrice = avgCurrencyPrice;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
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

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

}
