package de.emp2020.users;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User implements IUser {
	
	@Id
	@GeneratedValue
	private Integer id;
	private String userName;
	private String forename; // = "";
	private String surname; // = "";
	private Boolean isAdmin; // = false;
	private String password;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<UserToken> tokens;
	
	
	public User(){}
	
	public User(String userName, String forename, String surname, Boolean isAdmin, String password){
		this.userName = userName;
		this.forename = forename;
		this.surname = surname;
		this.isAdmin = isAdmin;
		this.password = password;
	}
	
	@Override
	public Collection<String> getDeviceTokens() {
		return tokens.stream().filter(token -> token != null).map(token -> token.getToken()).collect(Collectors.toList());
	}
	public void addToken (String userToken)
	{
		UserToken token = new UserToken();
		token.setToken(userToken);
		tokens.add(token);
	}
	// Autogenerated getters and setters
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setTokens(List<UserToken> tokens) {
		this.tokens = tokens;
	}
	public List<UserToken> getTokens() {
		return tokens;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public Boolean isAdmin() {
		return this.isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
