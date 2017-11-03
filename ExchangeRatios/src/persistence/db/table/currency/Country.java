package persistence.db.table.currency;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

@NamedQueries({ @NamedQuery(name = "getAllCountries", query = "from Country"),
		@NamedQuery(name = "getCountryById", query = "from Country where ID = ?"),
		@NamedQuery(name = "getCountryByName", query = "from Country where NAME = ?") })
@Entity
@Table(name = "COUNTRY", uniqueConstraints = { @UniqueConstraint(columnNames = "ID"),
		@UniqueConstraint(columnNames = "NAME") })
public class Country implements Serializable {

	private static final long serialVersionUID = 1872139910695816592L;

	public static final String FieldId = "id";
	public static final String FieldName = "name";
	public static final String Get_All = "getAllCountries";
	public static final String Get_ById = "getCountryById";
	public static final String Get_ByCode = "getCountryByName";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	@Column(name = "NAME", unique = true, length = 100)
	private String name;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "COUNTRY_CURRENCY", joinColumns = { @JoinColumn(name = "COUNTRY_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "CURRENCY_ID") })
	@Column(name = "CURRENCIES", nullable = true)
	private Set<Currency> currencies = new HashSet<Currency>();

	public Country() {
	}

	public Country(String name) {
		this.name = name;
	}

	public Integer getId() {
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
