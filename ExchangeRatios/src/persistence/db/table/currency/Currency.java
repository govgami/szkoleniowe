package persistence.db.table.currency;

import java.io.Serializable;

import javax.persistence.*;

@NamedNativeQueries({
	 @NamedNativeQuery(
	 name = "findCurrencyById",
	 query = "select * from Currency currency where currency.ID = :id",
	        resultClass = Currency.class
	 )
	})
@Entity
@Table(name = "CURRENCY", uniqueConstraints = { @UniqueConstraint(columnNames = "ID"),
		@UniqueConstraint(columnNames = "SHORTCUT") })
public class Currency implements Serializable{
	
	private static final long serialVersionUID = -1153042292800443513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	int id;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	Country country;
	@Column(name = "CURRENCY_NAME", length = 100)
	String name;
	@Column(name = "SHORTCUT", unique = true, nullable = false, length = 4)
	String sign;

	public Currency() {
	}

	public Currency(Country country, String name, String shortCut) {
		this.country = country;
		this.name = name;
		this.sign = shortCut;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String shortCut) {
		this.sign = shortCut;
	}

}
