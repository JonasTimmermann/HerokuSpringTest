package de.emp2020.Location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;



@Entity
public class Place {

    @GeneratedValue
    @Id
    private Integer id;
    
	@NotNull
    private String name;

	@NotNull
    private Double verticalPosition;
    
	@NotNull
    private Double horizontalPosition;
    
    //@JsonIgnore
//    @OneToMany(cascade={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })     //  (cascade=CascadeType.ALL)
//    private List<Alert> alertList;

	
	
	public Place(){

	}


	public Place(Integer id, String name, Double verticalPosition, Double horizontalPosition){
		this.id = id;
        this.name = name;
        this.verticalPosition = verticalPosition;
        this.horizontalPosition = horizontalPosition;
	}


//    public void addAlert(Alert alert){
//		alertList.add(alert);
//    }
    


	public Integer getId(){
		return this.id;
	}
	public void setId(Integer id){
		this.id = id;
	}


	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}


	public Double getVerticalPosition(){
		return this.verticalPosition;
	}
	public void setVerticalPosition(Double verticalPosition){
		this.verticalPosition = verticalPosition;
	}
    
    
	public Double getHorizontalPosition(){
		return this.horizontalPosition;
	}
	public void setHorizontalPosition(Double horizontalPosition){
		this.horizontalPosition = horizontalPosition;
    }
    
//    public void setAlertList(List<Alert> alertList) {
//		this.alertList = alertList;
//	}
//	public List<Alert> getAlertList() {
//		return this.alertList;
//	}
	
	
	
}
