
package persistence.db.table.currency;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
	@NamedQuery(name = CountryCurrency.GET_BY_COMPONENTS, query = "from CountryCurrency where country = :country and currency = :currency")
})
@Entity
@IdClass(CountryCurrencyId.class)
@Table(name = "country_currency")
public class CountryCurrency implements Serializable {

	private static final long serialVersionUID = -5465902396253799360L;

	public static final String FIELD_COUNTRY = "country";
	public static final String FIELD_CURRENCY = "currency";

	public static final String GET_BY_COMPONENTS = "getCountryCurrencyByComponents";

	@Id
	Country country;

	@Id
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

	protected void setCountryId(Country country) {
		this.country = country;
	}

	public Currency getCurrencyId() {
		return currency;
	}

	protected void setCurrencyId(Currency currency) {
		this.currency = currency;
	}

}
