
package persistence.db.table.currency;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
	@NamedQuery(name = Currency.GET_ALL, query = "from Currency"),
	@NamedQuery(name = Currency.GET_ALL_SORTED_BY_CODE, query = "from Currency order by code"),
	@NamedQuery(name = Currency.GET_BY_ID, query = "from Currency where id = :id"),
	@NamedQuery(name = Currency.GET_BY_CODE, query = "from Currency where code = :code"),
	@NamedQuery(name = Currency.FETCH_BY_CODE, query = "select c from Currency c left join fetch c.countries where code = :code")
})
@Entity
@Table(name = "currency", uniqueConstraints = {
	@UniqueConstraint(columnNames = "ID"),
	@UniqueConstraint(columnNames = "CODE")
})
public class Currency implements Serializable {

	private static final long serialVersionUID = -1153042292800443513L;

	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_CODE = "code";

	public static final String GET_ALL = "allCurrencies";
	public static final String GET_ALL_SORTED_BY_CODE = "allCurrenciesSortedByCode";
	public static final String GET_BY_ID = "currencyWithGivenId";
	public static final String GET_BY_CODE = "currencyWithGivenCode";
	public static final String FETCH_BY_CODE = "fetchCurrencyByCode";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	Long id;
	@ManyToMany(mappedBy = "currencies")
	Set<Country> countries = new HashSet<Country>();
	@Column(name = "CURRENCY_NAME", length = 100)
	String name;
	@Column(name = "CODE", unique = true, nullable = false, length = 4)
	String code;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "currency")
	Set<CurrencyRatios> ratios = new HashSet<CurrencyRatios>();

	protected Currency() {
	}

	public Currency(Set<Country> country, String name, String code) {
		this.countries = country;
		this.name = name;
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public Set<Country> getCountries() {
		return countries;
	}

	public void setCountry(Set<Country> country) {
		this.countries = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void addCountry(Country c) {
		countries.add(c);

	}

	public void removeCountry(Country c) {
		countries.remove(c);

	}

	public Set<CurrencyRatios> getRatios() {
		return ratios;
	}

	public void setRatios(Set<CurrencyRatios> ratios) {
		this.ratios = ratios;
	}

}
