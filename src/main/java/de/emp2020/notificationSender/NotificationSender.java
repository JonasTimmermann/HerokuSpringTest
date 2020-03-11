package de.emp2020.notificationSender;

import java.util.Collection;

import org.springframework.stereotype.Service;

import de.emp2020.alertEditor.IAlert;
import de.emp2020.users.IUser;

@Service
public interface NotificationSender {
	
	public void registerAlert (IAlert alert, Collection<IUser> users);
	
	public void unregisterAlert (IAlert alert, Collection<IUser> users);
	
	public void sendAlert (IAlert alert, Collection<IUser> users);
	
	public void transferAlert (IAlert alert, Collection<IUser> users, String message);
	
	public void acceptAlert (IAlert alert, Collection<IUser> users, String message);
	
	public void resolveAlert (IAlert alert, Collection<IUser> users);
	
	public void sendPriority (IAlert alert, String token, Integer priority);
	
}
