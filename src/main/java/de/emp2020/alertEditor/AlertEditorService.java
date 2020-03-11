package de.emp2020.alertEditor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.emp2020.notificationSender.NotificationSender;
import de.emp2020.users.IUser;
import de.emp2020.users.User;
import de.emp2020.users.UserRepo;

@Service
public class AlertEditorService {
	
	
	@Autowired
	AlertRepo alertRepo;		// the JPA CRUD repository of all alerts
	@Autowired
	UserRepo userRepo;			// the JPA CRUD repository of all users
	@Autowired
	NotificationSender notificationSender;		// the component tasked with pushing any notifications to the Firebase
	

    Logger log = LoggerFactory.getLogger(String.class);

	/**
	 * Find all alerts regardless of users, id or parameters
	 * @return
	 * 		an Iterable over all persistent Alerts
	 */
	public Iterable<Alert> getAllAlerts ()
	{
		return alertRepo.findAll();
	}
	
	
	/**
	 * Find a single, specific alert by its unique identifier
	 * @param id
	 * 		the unique identifier
	 * @return
	 * 		a singular alert matching the identifier
	 */
	public Alert getAlertByID(Integer id)
	{
		log.info("AlertID ist: " + id);

		// TODO: manage thrown NoSuchEntityException!!!
		
		return alertRepo.findById(id).orElse(null);
	}
	
	
	/**
	 * Inserts a new alert into the database and registers any assigned users with it, pushing a notification to all their registered Android devices
	 * @param alert
	 * 		The new alert, including all user objects
	 * @param userID
	 * 		The unique of the user adding the alert
	 * @return
	 * 		{@code true} if operation successful
	 */
	public boolean addAlert (AlertJson alert, String userName)
	{
		Alert newAlert = new Alert();
		
		newAlert.setTitle(alert.getTitle());
		newAlert.setDescription(alert.getDescription());
		newAlert.setQuery(alert.getPromQuery());
		
		// make sure that adding user becomes owner and non admin only assign themselves
		User user = userRepo.findByUserName(userName);
		newAlert.setOwner(user);
		if (!user.isAdmin())
		{
			newAlert.setAssignedUsers(new ArrayList<User>());
			newAlert.addAssignedUser(user);
		}
		else
		{
			List<User> users = userRepo.findUsersByNames(alert.getAssignedUsers());
			newAlert.setAssignedUsers(users);
		}
		
		// save alert and message all assigned users
		alertRepo.save(newAlert);
		notificationSender.registerAlert(newAlert, newAlert.getAllUsers());
		return true;
	}
	
	
	/**
	 * Updates an existing alert in the database and (un-) registers any newly (un-) assigned users, pushing a notification to all their registered Android devices
	 * @param alert
	 * 		The new alert, including all user objects
	 * @return
	 * 		{@code true} if operation successful
	 */
	public boolean editAlert (AlertJson alert)
	{
		Alert temp = alertRepo.findById(alert.getAlertId()).get();
		
		if (!alert.getOwner().equals(temp.getOwner().getUserName()))
		{
			return false;
		}
		
		if (alert.getDescription() != null)
		{
			temp.setDescription(alert.getDescription());
		}
		if (alert.getPromQuery() != null)
		{
			temp.setQuery(alert.getPromQuery());
		}
		if (alert.getTitle() != null)
		{
			temp.setTitle(alert.getTitle());
		}
		if (alert.getAssignedUsers() != null && !alert.getAssignedUsers().isEmpty())
		{
			
			List<User> users = new ArrayList<User>(userRepo.findUsersByNames(alert.getAssignedUsers()));
			List<IUser> oldUsers = new ArrayList<IUser>(temp.getAssignedUsers());
			oldUsers.removeAll(users);
			List<IUser> newUsers = new ArrayList<IUser>(users);
			newUsers.removeAll(temp.getAssignedUsers());
			
			temp.setAssignedUsers(users);
			
			alertRepo.save(temp);
			
			notificationSender.registerAlert(temp, newUsers);
			notificationSender.unregisterAlert(temp, oldUsers);
		}
		else
		{
			alertRepo.save(temp);
		}
		return true;
	}
	
	
	/**
	 * Deletes and existing alert from the database
	 * @param id
	 * 		the unique identifier of the alert to be deleted
	 * @return
	 * 		{@code true} if operation successful
	 */
	public boolean deleteAlert (int id)
	{
		Alert alert = alertRepo.findById(id).get();
		notificationSender.unregisterAlert(alert, alert.getAllUsers());	
		alertRepo.deleteById(id);
		return true;
	}

	
	
	
}
