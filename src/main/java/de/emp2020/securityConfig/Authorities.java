/**package de.emp2020.securityConfig;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Authorities {

   
    //@Id
    
    @GeneratedValue
    @Id
    private Integer id;

    @NotNull
    private String authority;

    //@Column(name="username")

    //@EmbeddedId
    @JoinColumn(name="username", referencedColumnName = "username")
    @OneToOne(cascade={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })   
    private Users users;
    
	
	

	
	
	public Authorities(){

    }
    
    public Authorities(String authority, Users users){
        this.authority = authority;
        this.users = users;

	}
    
    


    public String getAuthority(){
        return this.authority;
    }
    public void setAuthority(String authority){
        this.authority = authority;
    }


    public Users getUsername() {
		return this.users;
	}
	public void setUsername(Users users) {
		this.users = users;
    }

    public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
    }


}
**/

/** 
	public List<Users> getUsername() {
		return this.users;
	}
	public void setUsername(List<Users> users) {
		this.users = users;
    }
   **/ 
	

