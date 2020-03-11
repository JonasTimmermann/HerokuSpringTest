
package de.emp2020.securityConfig;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.persistence.ForeignKey;

@Entity
public class Users {



    @Id
    @NotNull
    @Column(name = "username")   //, foreignKey = @ForeignKey(name="FK_COMPANY__ROUTE")) //referencedColumnName = "username")
    private String username;
    @NotNull
    private String password;
    @NotNull
    private Boolean enabled;

    @ElementCollection
    @JoinTable(name = "authorities", joinColumns = {@JoinColumn(name = "username")})
    @Column(name = "authority")
    private Set<String> roles;

    //@JoinTable(name = "company_id", foreignKey = @ForeignKey(name="FK_COMPANY__ROUTE"))

    //@NotNull
    

   // @OneToOne
   // private String authority;
	
	

	
	
	public Users(){

	}
	
/** 
    public Authorities getAuthority(){
        return this.authorities;
    }
    public void setAuthority(Authorities authorities){
        this.authorities = authorities;
    }

**/

	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
    }
    public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
}
