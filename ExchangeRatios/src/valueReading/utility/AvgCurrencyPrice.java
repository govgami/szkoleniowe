package valueReading.utility;

import java.math.BigDecimal;

public class AvgCurrencyPrice {
	private String currencySign;
	private BigDecimal avgCurrencyPrice;
//DEV insert for searching whole array of currency prices
	public AvgCurrencyPrice() {}
	
	public AvgCurrencyPrice(String currencySign, BigDecimal avgPrice) {
		this.currencySign=currencySign;
		this.avgCurrencyPrice=avgPrice;
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
}
