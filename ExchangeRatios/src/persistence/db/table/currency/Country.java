package persistence.db.table.currency;

import java.io.Serializable;

import javax.persistence.*;



@Entity
@Table(name = "COUNTRY", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID") })
public class Country implements Serializable{
	
	private static final long serialVersionUID = 1872139910695816592L;	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
private	Integer id;
	@Column(name = "NAME", unique = false, nullable = true, length = 100)
private String name;

Country(){}

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

}
