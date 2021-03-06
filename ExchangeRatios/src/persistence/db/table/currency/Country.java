package persistence.db.table.currency;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@NamedNativeQueries({
	 @NamedNativeQuery(
	 name = "findCountryById",
	 query = "select * from Country country where country.ID = :id",
	        resultClass = Country.class
	 ),
	 @NamedNativeQuery(
	 name = "findCountryByName",
	 query = "select * from Country country where country.NAME = :name order by country.ID asc",
	        resultClass = Country.class
	 )
	})
@Entity
@Table(name = "COUNTRY", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID"),
        @UniqueConstraint(columnNames = "NAME")})
public class Country implements Serializable{
	
	private static final long serialVersionUID = 1872139910695816592L;	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false)
private	Integer id;
	@Column(name = "NAME", unique = true, nullable = true, length = 100)
private String name;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "COUNTRY_CURRENCY", joinColumns = { @JoinColumn(name = "COUNTRY_ID") }, inverseJoinColumns = { @JoinColumn(name = "CURRENCY_ID") })
	@Column(name = "CURRENCIES", unique = false, nullable = true)
private Set<Currency> currencies;
	

public Country(){}

public Country(String name){
	this.name=name;
}

public int getId() {
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
	if(currencies==null) {
		currencies=new HashSet<Currency>();
	}
	else if(!currencies.contains(curr)){
		currencies.add(curr);
	}
	
}
public void removeCurrency(Currency curr) {
	if(currencies==null) {
		return;
	}
	else{
		currencies.remove(curr);
		}
	
}

}
