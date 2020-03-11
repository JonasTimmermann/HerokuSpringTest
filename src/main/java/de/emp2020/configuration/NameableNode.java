/** 
package de.emp2020.configuration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class NameableNode {
	
	@Id
	private Long id;

	@NotNull
	private String targetName;
	
	private String mapsTo;
	

	
	
	public NameableNode(){
	}

	public NameableNode(Long id, String targetName){
		this.id = id;
		this.targetName = targetName;
	}


	public Long getId(){
		return this.id;
	}
	public void setId(Long id){
		this.id = id;
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
**/