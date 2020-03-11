package de.emp2020.alertEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.emp2020.Location.Place;
import de.emp2020.alertManager.Distance;
import de.emp2020.users.IUser;
import de.emp2020.users.User;

@Entity
@Transactional
/**
 * Domain for a single Alert
 */
public class Alert implements IAlert {
	
	@GeneratedValue
	@Id
	private Integer alertId;							// unique autogenerated identifier, used cross service to identify alerts
	private boolean isTriggered = false;				// true if alert is currently active, no further notifications will be sent for repeat triggers
	private String title = "";							// Human readable name of the alert
	private String promQuery = "";						// PromQL expression that, when returning ANY VALUE is considered true and thus trigger the alert
	private String description = "";					// Human readable description of what the alert will be monitoring

	@ManyToOne
	private Place place = null;


	// a list of recent triggers, mostly for logging purposes.
	// The list is expected to be ordered with the first element being the earliest trigger and the last element being the latest trigger.
	@OneToMany(cascade = CascadeType.ALL)
	private List<AlertTimestamp> pastTriggers = new ArrayList<AlertTimestamp>();
	
	// The collection of users that is affiliated with the respective alert
	@ManyToOne
	private User owner;													// The sole person with editing rights to the alert
	@ManyToOne(cascade = CascadeType.ALL)
	private User acceptedUser;		
										// The user who offered to deal with the alert
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> assignedUsers = new ArrayList<User>();			// All users who have been assigned to the alert by an administrator
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> transferredUsers = new ArrayList<User>();		// Any users the alert has been messaged to from the Android application
	
	// Distances of tracked user devices
	@OneToMany(cascade = CascadeType.ALL)
	private List<Distance> distances = new ArrayList<Distance>();
	
	@JsonIgnore
	//@JsonProperty("owner")
	public String getJsonOwner ()
	{
		return owner.getUserName();
	}

	@JsonIgnore
	//@JsonProperty("assignedUsers")
	public List<String> getAssignedJsonUsers ()
	{
		return this.getAssignedUsers().stream().map(user -> user.getUserName()).collect(Collectors.toList());
	}
	
	
	/**
	 * Unites all messageable users into one single collection for retrieval
	 * @return
	 * 		a collection of the interface {@code IUser} containing the owner and all assigned or transfered users
	 */
	@JsonIgnore
	public Collection<IUser> getAllUsers ()
	{
		List<IUser> users = new ArrayList<IUser>();
		if (assignedUsers != null)
		{
			users.addAll(assignedUsers);
		}
		if (transferredUsers != null)
		{
			users.addAll(transferredUsers);
		}
		return users;
	}
	
	/**
	 * Adds another user to be assigned to the Alert
	 */
	@JsonIgnore
	public void addAssignedUser (User user)
	{
		assignedUsers.add(user);
	}
	
	/**
	 * Adds another user who has been transfered to the alert, they are expected to be removed once alert has been resolved
	 * @param user
	 * 		a user who will be tracked as transfered
	 */
	@JsonIgnore
	public void addTransferredUser (User user)
	{
		transferredUsers.add(user);
	}

	/**
	 * Triggers the alert, setting it's status to being triggered and logging a unix timestamp of the current system time  
	 */
	@JsonIgnore
	public void trigger ()
	{
		isTriggered = true;
		AlertTimestamp timeStamp = new AlertTimestamp();
		timeStamp.setStamp(System.currentTimeMillis());
		pastTriggers.add(timeStamp);
	}
	
	/**
	 * reads the unix timestamp of the current latest trigger
	 */
	@JsonIgnore
	public Long getTime ()
	{
		if (pastTriggers == null || pastTriggers.isEmpty())
		{
			return null;
		}
		return pastTriggers.get(pastTriggers.size()-1).getStamp();
	}
	
	
	// Autogenerated Getters and Setters
	
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}
	@JsonProperty("title")
	public void setTitle(String alertName) {
		this.title = alertName;
	}
	@JsonProperty("alertId")
	public Integer getId() {
		return this.alertId;
	}
	@JsonProperty("alertId")
	public void setId(Integer id) {
		this.alertId = id;
	}
	@JsonIgnore
	public User getOwner() {
		return owner;
	}
	@JsonIgnore
	public void setOwner(User owner) {
		this.owner = owner;
	}
	@JsonIgnore
	public User getAcceptedUser() {
		return acceptedUser;
	}
	@JsonIgnore
	public void setAcceptedUser(User acceptedUser) {
		this.acceptedUser = acceptedUser;
	}
	@JsonIgnore
	public List<User> getAssignedUsers() {
		return assignedUsers;
	}
	@JsonIgnore
	public void setAssignedUsers(List<User> assignedUsers) {
		this.assignedUsers = assignedUsers;
	}
	@JsonIgnore
	public List<User> getTransferredUsers() {
		return transferredUsers;
	}
	@JsonIgnore
	public void setTransferredUsers(List<User> transferredUsers) {
		this.transferredUsers = transferredUsers;
	}
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonProperty("promQuery")
	public String getQuery() {
		return promQuery;
	}
	@JsonProperty("promQuery")
	public void setQuery(String query) {
		this.promQuery = query;
	}
	@JsonProperty("isTriggered")
	public boolean isTriggered() {
		return isTriggered;
	}
	@JsonIgnore
	public void setTriggered(boolean isTriggered) {
		this.isTriggered = isTriggered;
	}
	@JsonProperty("pastTriggers")
	public List<AlertTimestamp> getPastTriggers() {
		return pastTriggers;
	}
	@JsonIgnore
	public void setPastTriggers(List<AlertTimestamp> pastTriggers) {
		this.pastTriggers = pastTriggers;
	}
	
	@JsonIgnore
	public String toString ()
	{
		return "{" + alertId + ", " + title + ", " + promQuery + "}";
	}
	
	@JsonIgnore
	public List<Distance> getDistances() {
		return distances;
	}
	@JsonIgnore
	public void setDistances(List<Distance> distances) {
		this.distances = distances;
	}
	
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
	}



	@Override
	public Double getLongitude() {
		if (place == null)
		{
			return null;
		}
		return place.getHorizontalPosition();
	}



	@Override
	public Double getLatitude() {
		if (place == null)
		{
			return null;
		}
		return place.getVerticalPosition();
	}
	
	
}