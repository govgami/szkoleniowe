package persistence.db.table.currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
		@NamedQuery(name = "getCurrencyRatioByCurrencySignAndDay", query = "select e from CurrencyRatios e inner join e.currency where code = ? and e.effectiveDate = ?"),
		@NamedQuery(name = "getLowestBidOfChosenSignCurrencyRatio", query = "select e from CurrencyRatios e inner join e.currency where code = ? order by e.bidPrice asc"),
		@NamedQuery(name = "getHighestPriceDifferenceOfCurrencyRatio", query = "select c, c.askPrice-c.bidPrice as difference from CurrencyRatios c inner join c.currency where code = ? and c.askPrice is not null and c.bidPrice is not null order by difference desc") })
@Entity
@Table(name = "currency_ratios", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class CurrencyRatios implements Serializable {

	private static final long serialVersionUID = 4445757466460884024L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	BigDecimal id;
	@ManyToOne
	@JoinColumn(name = "CURRENCY_ID")
	Currency currency;
	@Column(name = "EFFECTIVE_DATE")
	Date effectiveDate;
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
		return effectiveDate;
	}

	public void setDate(Date date) {
		this.effectiveDate = date;
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

	@Override
	public String toString() {
		return id + effectiveDate.toString();
	}
}
