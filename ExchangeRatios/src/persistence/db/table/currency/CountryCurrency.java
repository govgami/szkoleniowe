package persistence.db.table.currency;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "country_currency", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class CountryCurrency implements Serializable {

	private static final long serialVersionUID = -5465902396253799360L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	Integer id;
	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
	Country country;
	@ManyToOne
	@JoinColumn(name = "CURRENCY_ID", referencedColumnName = "ID")
	Currency currency;

	protected CountryCurrency() {
	};

	public CountryCurrency(Country country, Currency currency) {
		this.country = country;
		this.currency = currency;
	}

	public Integer getId() {
		return id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountryId(Country country) {
		this.country = country;
	}

	public Currency getCurrencyId() {
		return currency;
	}

	public void setCurrencyId(Currency currency) {
		this.currency = currency;
	}

}
