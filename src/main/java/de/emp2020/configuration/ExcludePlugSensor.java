package de.emp2020.configuration;

import java.util.List;

import javax.persistence.Entity;


//@Entity
public class ExcludePlugSensor {
    
    private String name;
    private Boolean exclude;


    public ExcludePlugSensor(String name, Boolean ex){
        this.name = name;
        this.exclude = ex;
    }

	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}


    public Boolean isExclude(){
		return this.exclude;
	}
	public void setExclude(Boolean ex){
		this.exclude = ex;
	}
	
}