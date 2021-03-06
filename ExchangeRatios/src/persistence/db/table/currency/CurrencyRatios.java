package persistence.db.table.currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.*;

@NamedNativeQueries({
	 @NamedNativeQuery(
	 name = "findCurrencyRatioById",
	 query = "select * from CurrencyRatio currencyRatio where currencyRatio.ID = :id",
	        resultClass = CurrencyRatios.class
	 ),
	 @NamedNativeQuery(
	 name = "findLowestBidOfChosenSignCurrencyRatio",
	 query = "select * from CurrencyRatio currencyRatio where currencyRatio.SHORTCUT = :sign order by currencyRatio.BID_PRICE asc",
	        resultClass = CurrencyRatios.class
	 )	 
	})
@Entity
@Table(name = "CURRENCY_RATIOS", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class CurrencyRatios implements Serializable{
	
	private static final long serialVersionUID = 4445757466460884024L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	BigDecimal id;
	@ManyToOne
	@JoinColumn(name = "CURRENCY_ID")
	Currency currency;
	@Column(name = "EFFECTIVE_DATE")
	Date date;
	@Column(name = "AVG_PRICE")
	BigDecimal avgPrice;
	@Column(name = "ASK_PRICE")
	BigDecimal askPrice;
	@Column(name = "BID_PRICE")
	BigDecimal bidPrice;

	public BigDecimal getId() {
		return id;
	}

	public Currency getCurrencyId() {
		return currency;
	}

	public void setCurrencyId(Currency currencyId) {
		this.currency = currencyId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

	public BigDecimal getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(BigDecimal askPrice) {
		this.askPrice = askPrice;
	}

	public BigDecimal getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

}
