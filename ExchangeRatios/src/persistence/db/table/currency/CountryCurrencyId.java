
package persistence.db.table.currency;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class CountryCurrencyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4263109482621119962L;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
	Country country;
	@ManyToOne
	@JoinColumn(name = "CURRENCY_ID", referencedColumnName = "ID")
	Currency currency;

	public CountryCurrencyId() {
	}

	public CountryCurrencyId(Country country, Currency currency) {
		this.country = country;
		this.currency = currency;
	}

	protected void setCountry(Country country) {
		this.country = country;
	}

	protected void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Country getCountry() {
		return country;
	}

	public Currency getCurrency() {
		return currency;
	}

}
