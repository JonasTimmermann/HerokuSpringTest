package de.emp2020.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserToken {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(nullable=false)
	private String token;

	// wird eigentlich nicht gebraucht
	//private String location;

	/** 
	@ManyToOne
	//@JoinColumn(name = "user_id", nullable=false)
	User user;
**/
	
	public UserToken(){
	}


	public UserToken(String token){ //String location){
		this.token = token;
		//this.location = location;
	}

	
	public String getToken() {
		return this.token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
/** 
	public String getLocation() {
		return this.location;
	}
	
	public void setLocation(String location) { 
		this.location = location;
	}
**/

	
	
}
