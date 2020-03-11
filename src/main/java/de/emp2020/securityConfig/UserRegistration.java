package de.emp2020.securityConfig;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class UserRegistration {

	private Integer id;
	private String userName;
	private String password;
	private Boolean isAdmin;
	private String forename; 
	private String surname; 



	public UserRegistration(){
	}

	public UserRegistration(Integer id, String userName, String forename, String surname, Boolean isAdmin, String password) {
		this.id = id;
		this.userName = userName;
		this.forename = forename;
		this.surname = surname;
		this.isAdmin = isAdmin;
		this.password = password;
	}



	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin= isAdmin;
	}

	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public String getForename() {
		return forename;
	}
	public void setForename(String forename) {
		this.forename = forename;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

}
