
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
	@NamedQuery(name = Country.GET_ALL, query = "from Country"),
	@NamedQuery(name = Country.GET_BY_ID, query = "from Country where id = :id"),
	@NamedQuery(name = Country.GET_BY_NAME, query = "from Country where name = :name"),
	@NamedQuery(name = Country.FETCH_BY_NAME, query = "select c from Country c left join fetch c.currencies where c.name = :name"),
	@NamedQuery(name = Country.FETCH_ALL_WITH_COUNTRY_ON_DAY, query = "select c, r from Country c, CountryCurrency cc, CurrencyRatios r where c.name = :name and c.id=cc.country.id and r.currency.id=cc.currency.id and r.effectiveDate= :effectiveDate")
})
@Entity
@Table(name = "COUNTRY", uniqueConstraints = {
	@UniqueConstraint(columnNames = "ID"),
	@UniqueConstraint(columnNames = "NAME")
})
public class Country implements Serializable {

	private static final long serialVersionUID = 1872139910695816592L;

	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "name";

	public static final String GET_ALL = "allCountries";
	public static final String GET_BY_ID = "countryWithGivenId";
	public static final String GET_BY_NAME = "countryWithGivenName";
	public static final String FETCH_BY_NAME = "fetchCountryByName";
	public static final String FETCH_ALL_WITH_COUNTRY_ON_DAY = "fetchCountryByNameAndDate";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	@Column(name = "NAME", unique = true, length = 100)
	private String name;
	@ManyToMany(cascade = {
		CascadeType.PERSIST,
		CascadeType.MERGE
	})
	@JoinTable(name = "COUNTRY_CURRENCY", joinColumns = {
		@JoinColumn(name = "COUNTRY_ID")
	}, inverseJoinColumns = {
		@JoinColumn(name = "CURRENCY_ID")
	})
	private Set<Currency> currencies = new HashSet<Currency>();

	public Country() {
	}

	public Country(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Set<Currency> currencies) {
		this.currencies = currencies;
	}

	public void addCurrency(Currency curr) {
		currencies.add(curr);

	}

	public void removeCurrency(Currency curr) {
		currencies.remove(curr);

	}

}
