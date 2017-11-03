package persistence.db.table.currency;

import java.io.Serializable;
import java.util.Set;

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

@NamedQueries({ @NamedQuery(name = "getAllCurrencies", query = "from Currency"),
		@NamedQuery(name = "getAllCurrenciesSortedByCode", query = "from Currency order by CODE"),
		@NamedQuery(name = "getCurrencyById", query = "from Currency where ID = ?"),
		@NamedQuery(name = "getCurrencyByCode", query = "from Currency where CODE = ?") })
@Entity
@Table(name = "currency", uniqueConstraints = { @UniqueConstraint(columnNames = "ID"),
		@UniqueConstraint(columnNames = "CODE") })
public class Currency implements Serializable {

	private static final long serialVersionUID = -1153042292800443513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	Integer id;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	Set<Country> country;
	@Column(name = "CURRENCY_NAME", length = 100)
	String name;
	@Column(name = "CODE", unique = true, nullable = false, length = 4)
	String code;

	protected Currency() {
	}

	public Currency(Set<Country> country, String name, String code) {
		this.country = country;
		this.name = name;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public Set<Country> getCountry() {
		return country;
	}

	public void setCountry(Set<Country> country) {
		this.country = country;
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

}
