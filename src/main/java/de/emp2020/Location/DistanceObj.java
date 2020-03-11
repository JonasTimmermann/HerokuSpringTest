package de.emp2020.Location;

import de.emp2020.alertEditor.*;
import de.emp2020.users.User;
import de.emp2020.users.UserToken;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;



class DistanceObj {

    
    private String distance;
    private String userToken; 
    private String alertId; 

    
	
	public DistanceObj(){

	}

	public DistanceObj(String distance, String userToken, String alertId){
        this.distance = distance;
        this.alertId = alertId;
        this.userToken = userToken;
	}

    

/** 
	public Integer getId(){
		return this.id;
	}
	public void setId(Integer id){
		this.id = id;
	}
**/

	public String getDistance(){
		return this.distance;
	}
	public void setDistance(String distance){
		this.distance = distance;
	}

	public String getUserToken(){
		return this.userToken;
	}
	public void setUserToken(String userToken){
		this.userToken = userToken;
    }
    
    public String getAlertId(){
		return this.alertId;
	}
	public void setAlertId(String alertId){
		this.alertId = alertId;
	}
	
	
}
