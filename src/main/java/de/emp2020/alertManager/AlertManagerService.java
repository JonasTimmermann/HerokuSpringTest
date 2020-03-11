package de.emp2020.alertManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.emp2020.alertEditor.Alert;
import de.emp2020.alertEditor.AlertRepo;
import de.emp2020.notificationSender.NotificationSender;
import de.emp2020.users.User;
import de.emp2020.users.UserRepo;

@Service
@Transactional
public class AlertManagerService {
	
	@Autowired
	AlertRepo alertRepo;		// the JPA CRUD repository of all alerts
	@Autowired
	UserRepo userRepo;			// the JPA CRUD repository of all users
	@Autowired
	NotificationSender notificationSender;		// the component tasked with pushing any notifications to the Firebase
	@Autowired
	PromSender promSender;		// the component tasked with querying Prometheus for any alarming metrics
	
	Logger log = LoggerFactory.getLogger(AlertManagerService.class);
	
	
	/**
	 * Find all alerts based on the {@code owner}'s unique identificator
	 * The owner is the person who created the alert
	 * @param userID
	 * 		the unique identifier of the alert's owner
	 * @return
	 * 		an iterable over all alerts owned by the specified user
	 */
	public Iterable<Alert> getAlertsByUser (int userID)
	{
		return alertRepo.findByUserId (userID);
	}
	
	
	/**
	 * Calling user will be registered as dealing with the alert and all users assigned or transfered to the alert will be notified
	 * The alert must be triggered and the user must be assigned or transfered to it to be able to accept the alert
	 * @param userID
	 * 		the unique identifier of the user dealing with the alert
	 * @param alertID
	 * 		the unique identifier of the alert being dealt with
	 * @param message
	 * 		an optional message by the user
	 */
	public void acceptAlert (int userID, int alertID, String message)
	{
		Alert alert = alertRepo.findById(alertID).get();
		User user = userRepo.findById(userID).get();
		if (alert.getAcceptedUser() == null && alert.isTriggered() && alert.getAllUsers().contains(user))
		{
			alert.setAcceptedUser(user);
			notificationSender.acceptAlert(alert, alert.getAllUsers(), message);
		}
	}
	
	
	/**
	 * Calling user will be transfer the alert to another user who will be notified about the alert and receive all future messages regarding the alert up until the alert becomes inactive again
	 * The alert must be triggered and the caller must be assigned to the alert to be able to transfer the alert
	 * @param alertID
	 * 		the unique identifier of the alert being dealt with
	 * @param targetUserName
	 * 		the unique identifier of the user the alert will be transfered to
	 * @param message
	 * 		an optional message by the user
	 */
	public void transferAlert (int alertID, String targetUserName, String message)
	{
		Alert alert = alertRepo.findById(alertID).get();
		if (alert.isTriggered())
		{
			User user = userRepo.findByUserName(targetUserName);
			alert.addTransferredUser(user);
			notificationSender.transferAlert(alert, Collections.singleton(user), message);
		}
	}
	
	
	/**
	 * Will prompt the service to query all alerts from prometheus and notify every user subscribed to them, should they be newly triggered
	 */
	public void triggerAlertQuerry ()
	{
		alertRepo.findAll().forEach(alert -> checkAlert(alert));
	}
	
	/**
	 * Will prompt the service to search all alerts for users that may have received an update to their priority and notify them
	 */
	public void updatePriorities ()
	{
		alertRepo.findAll().forEach(alert -> {
			if (alert.isTriggered() && alert.getAcceptedUser() == null)
			{
				// Sort list first
				alert.getDistances().sort(new Comparator<Distance>() {
					@Override
					public int compare(Distance o1, Distance o2) {
						if (o1.getDistance() == o2.getDistance())
						{
							return 0;
						}
						else if (o1.getDistance() < o2.getDistance())
						{
							return -1;
						}
						else
						{
							return 1;
						}
					}
				});
				
				Iterator<Distance> distances = alert.getDistances().iterator();
				int index = 0;
				// Notify anyone whose priority changed by more than 20%
				while (distances.hasNext())
				{
					Distance next = distances.next();
					if (next.getPriority() < 0
							|| (next.getPriority() != index && index < 5)
							|| (next.getPriority() < index && next.getPriority() >= index * 0.8)
							|| (next.getPriority() > index && next.getPriority() <= index * 1.2))
					{
						
						next.setPriority(index);
						
						notificationSender.sendPriority(alert, next.getToken(), index);
					}
					
					index++;
				}
			}
		});
	}
	
	
	/**
	 * Registers a mobile device to receive notification for a specified user
	 * @param userName
	 * 		The uniquely identifying name of the user who registers
	 * @param userToken
	 * 		a unique token passed by the app with which they will be identified in Firebase
	 * @param password
	 * 		the user's passphrasw which must match the database entry to be able to register
	 * @return
	 * 		an iterable over all alerts already owned by that user
	 */
	public Iterable<Alert> registerUser (String userName, String userToken, String password)
	{
		
		User user = userRepo.findByUserName(userName);
		
		if(user.getPassword() == null || userToken == null || !user.getPassword().equals(password)){
			return null;
		}
		
		userRepo.deleteUserToken(userToken);
		user.addToken(userToken);
		userRepo.save(user);
		
		return alertRepo.findByUserId(user.getId());
	}
	
	
	/**
	 * Unregisters a mobile device from receiving notifications
	 * @param userToken
	 * 		the unique token of the device
	 * @return
	 * 		{@code true} if device was successfully unregistered
	 */
	public boolean unregisterUser (String userToken)
	{
		userRepo.deleteUserToken(userToken);
		
		return true; //TODO: implement
	}
	
	
	
	public void saveDistance (DistanceJson distance)
	{
		Alert alert = alertRepo.findById(distance.getAlertId()).get();
		
		Distance dist = new Distance();
		dist.setDistance(distance.getDistance());
		dist.setToken(distance.getUserToken());
		
		alert.getDistances().remove(dist);
		alert.getDistances().add(dist);
	}
	
	
	
	/**
	 * Sends an alert's query to Prometheus and sends said alert if it was freshly triggered or resolves the alert if it was triggered before but has become inactive
	 * @param alert
	 * 		the alert to query and notify about
	 */
	private void checkAlert (Alert alert)
	{
		boolean triggered = promSender.checkPromIsTriggered(alert.getQuery());
		if (triggered && !alert.isTriggered() && alert.getAllUsers()!=null)
		{
			alert.trigger();
			alertRepo.save(alert);
			notificationSender.sendAlert(alert, alert.getAllUsers());
		}
		else if (!triggered && alert.isTriggered() && alert.getAllUsers()!=null)
		{
			alert.setTriggered(false);
			alertRepo.save(alert);
			notificationSender.resolveAlert(alert, alert.getAllUsers());
			alert.setAcceptedUser(null);
			alert.setTransferredUsers(null);
			alertRepo.save(alert);
		}
	}
	
}
