package de.emp2020.alertEditor;

public interface IAlert {
	
	
	/**
	 * Use to get the unique identifier of the alert
	 * @return
	 * 		an integer unique among all persistent alerts
	 */
	public Integer getId ();
	
	
	/**
	 * Use to get the Title of the alert
	 * @return
	 * 		a non unique string defined by user input
	 */
	public String getTitle ();
	
	/**
	 * Use to get description of the alert
	 * @return
	 * 		a non unique string defined by user input
	 */
	public String getDescription ();
	
	/**
	 * Use to get the time of the latest trigger of the specific alert
	 * @return
	 * 		a unix timestamp
	 */
	public Long getTime ();
	
	/**
	 * Get the longitude of the Location assigned to the alert
	 * @return
	 * 		a double corresponding to the degrees longitude
	 */
	public Double getLongitude();
	
	/**
	 * Get the latitude of the location assigned to the alert
	 * @return
	 * 		a double corresponding to the degrees latitude
	 */
	public Double getLatitude();
	
}
