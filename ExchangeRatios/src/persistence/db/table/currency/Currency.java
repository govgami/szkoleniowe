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

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({ @NamedQuery(name = "getAllCurrencies", query = "from Currency"),
		@NamedQuery(name = "getCurrencyById", query = "from Currency where ID = ? order by ID"),
		@NamedQuery(name = "getCurrencyByCode", query = "from Currency where CODE = ? order by CODE") })
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
	Country country;
	@Column(name = "CURRENCY_NAME", length = 100)
	String name;
	@Column(name = "CODE", unique = true, nullable = false, length = 4)
	String code;

	protected Currency() {
	}

	public Currency(Country country, String name, String code) {
		this.country = country;
		this.name = name;
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
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
