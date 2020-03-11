package de.emp2020.configuration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


public class PduNameMapping {


	private String targetName;
	
	private String mapsTo; //= "N/A";
	

	public PduNameMapping(){
	}

	public PduNameMapping(String targetName, String mapsTo){
        this.targetName = targetName;
        this.mapsTo = mapsTo;
	}


	public String getTargetName(){
		return this.targetName;
	}
	public void setTargetName(String targetName){
		this.targetName = targetName;
	}
	
	public String getMapsTo() {
		return this.mapsTo;
	}
	public void setMapsTo(String mapsTo) {
		this.mapsTo = mapsTo;
	}
	
}
