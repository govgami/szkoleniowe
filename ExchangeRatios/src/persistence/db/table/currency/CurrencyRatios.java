package persistence.db.table.currency;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.*;


@Entity
@Table(name = "CURRENCY_RATIOS", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class CurrencyRatios {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
