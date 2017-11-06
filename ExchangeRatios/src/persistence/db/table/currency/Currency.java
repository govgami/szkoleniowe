package persistence.db.table.currency;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({ @NamedQuery(name = "getAllCurrencies", query = "from Currency"),
		@NamedQuery(name = "getAllCurrenciesSortedByCode", query = "from Currency order by code"),
		@NamedQuery(name = "getCurrencyById", query = "from Currency where id = :id"),
		@NamedQuery(name = "getCurrencyByCode", query = "from Currency where code = :code"),
		@NamedQuery(name = "fetchCurrencyByCode", query = "select c from Currency c left join fetch c.countries where code = :code") })
@Entity
@Table(name = "currency", uniqueConstraints = { @UniqueConstraint(columnNames = "ID"),
		@UniqueConstraint(columnNames = "CODE") })
public class Currency implements Serializable {

	private static final long serialVersionUID = -1153042292800443513L;

	public static final String FieldId = "id";
	public static final String FieldName = "name";
	public static final String FieldCode = "code";
	public static final String Get_All = "getAllCurrencies";
	public static final String Get_All_SortedByCode = "getAllCurrenciesSortedByCode";
	public static final String Get_ById = "getCurrencyById";
	public static final String Get_ByCode = "getCurrencyByCode";
	public static final String Fetch_ByCode = "fetchCurrencyByCode";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	Integer id;
	// TODO finish instructions for fetching and finish manytomany Country &
	// Currency
	@ManyToMany(mappedBy = "currencies")
	Set<Country> countries = new HashSet<Country>();
	@Column(name = "CURRENCY_NAME", length = 100)
	String name;
	@Column(name = "CODE", unique = true, nullable = false, length = 4)
	String code;

	protected Currency() {
	}

	public Currency(Set<Country> country, String name, String code) {
		this.countries = country;
		this.name = name;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public Set<Country> getCountry() {
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
}
