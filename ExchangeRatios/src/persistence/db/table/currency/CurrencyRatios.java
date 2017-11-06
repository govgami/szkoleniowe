package persistence.db.table.currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
		@NamedQuery(name = "getCurrencyRatioByCurrencySignAndDay", query = "select e from CurrencyRatios e inner join fetch e.currency where code = :code and e.effectiveDate = :effectiveDate"),
		@NamedQuery(name = "getLowestBidOfChosenSignCurrencyRatio", query = "select e from CurrencyRatios e inner join fetch e.currency where code = :code order by e.bidPrice asc"),
		@NamedQuery(name = "getHighestPriceDifferenceOfCurrencyRatio", query = "select c, c.askPrice-c.bidPrice as difference from CurrencyRatios c inner join fetch c.currency where code = :code and c.askPrice is not null and c.bidPrice is not null order by difference desc"),
		@NamedQuery(name = "dropByCode", query = "delete CurrencyRatios c where c.currency.code = :code") })

@Entity
@Table(name = "currency_ratios", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") }, indexes = {
		@Index(name = "effective_day", columnList = "effective_date") })
public class CurrencyRatios implements Serializable {

	private static final long serialVersionUID = 4445757466460884024L;

	public static final String FieldId = "id";
	public static final String FieldCurrency = "currency";
	public static final String FieldDate = "effectiveDate";
	public static final String FieldAvgPrice = "avgPrice";
	public static final String FieldAskPrice = "askPrice";
	public static final String FieldBidPrice = "bidPrice";
	public static final String IndexOnDay = "effective_day";
	public static final String Get_ByCurrencyCodeAndDate = "getCurrencyRatioByCurrencySignAndDay";
	public static final String Get_LowestBidPriceOfChosenCode = "getLowestBidOfChosenSignCurrencyRatio";
	public static final String Get_HighestDifferenceOf_AskAndBidPrice = "getHighestPriceDifferenceOfCurrencyRatio";
	public static final String Drop_ByCode = "dropByCode";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	BigDecimal id;
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

	protected CurrencyRatios() {
	}

	public CurrencyRatios(Currency currency, Date effectiveDate, BigDecimal avgPrice, BigDecimal askPrice,
			BigDecimal bidPrice) {
		this.currency = currency;
		this.effectiveDate = effectiveDate;
		this.avgPrice = avgPrice;
		this.askPrice = askPrice;
		this.bidPrice = bidPrice;
	}

	public BigDecimal getId() {
		return id;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currencyId) {
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

	public BigDecimal getAskBidPricesDifference() {
		return askPrice.subtract(bidPrice);
	}

	@Override
	public String toString() {
		return id + effectiveDate.toString();
	}

}
