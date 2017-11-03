package persistence.db.table.currency;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
		@NamedQuery(name = "getCountryCurrencyByComponents", query = "from CountryCurrency where country = ? and currency = ?") })
@Entity
@IdClass(CountryCurrencyId.class)
@Table(name = "country_currency", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class CountryCurrency implements Serializable {

	private static final long serialVersionUID = -5465902396253799360L;

	public static final String FieldCountry = "country";
	public static final String FieldCurrency = "currency";
	public static final String Get_ByComponents = "getCountryCurrencyByComponents";

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
