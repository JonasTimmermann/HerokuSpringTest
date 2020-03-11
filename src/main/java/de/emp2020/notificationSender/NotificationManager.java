package de.emp2020.notificationSender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;

import de.emp2020.alertEditor.IAlert;
import de.emp2020.users.IUser;

@Service
public class NotificationManager implements NotificationSender {

//	public static interface Alert {
//		Integer getId();
//		Long getTime();
//		String getTitle();
//		String getDescription();
//		Double getLongitude();
//		Double getLatitude();
//	}
//	
//	public static interface User {
//		Collection<String> getDeviceTokens();
//	}

	private static final String FIREBASE_SDK_PATH = "emp2020-firebase-adminsdk-k7qac-276088beb9.json";

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (instance == null) {
			synchronized (NotificationManager.class) {
				if (instance == null) {
					instance = new NotificationManager();
				}
			}
		}
		
		return instance;
	}

	private NotificationManager() {
		try {
			InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(FIREBASE_SDK_PATH);
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseApp.initializeApp(new FirebaseOptions.Builder().setCredentials(credentials).build());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void registerAlert(IAlert alert, Collection<IUser> users) {
		Collection<String> tokens = users.stream().flatMap(u -> u.getDeviceTokens().stream()).collect(Collectors.toSet());
		Map<String, String> data = new HashMap<>();
		data.put("type", "register");
		data.put("id", alert.getId().toString());
		data.put("title", alert.getTitle());
		data.put("description", alert.getDescription());
		sendData(tokens, data);
	}

	public void unregisterAlert(IAlert alert, Collection<IUser> users) {
		Collection<String> tokens = users.stream().flatMap(u -> u.getDeviceTokens().stream()).collect(Collectors.toSet());
		Map<String, String> data = new HashMap<>();
		data.put("type", "unregister");
		data.put("id", alert.getId().toString());
		sendData(tokens, data);
	}

	public void sendAlert(IAlert alert, Collection<IUser> users) {
		Collection<String> tokens = users.stream().flatMap(u -> u.getDeviceTokens().stream()).collect(Collectors.toSet());
		Map<String, String> data = new HashMap<>();
		data.put("type", "send");
		data.put("id", alert.getId().toString());
		data.put("title", alert.getTitle());
		data.put("longitude", alert.getLongitude().toString());
		data.put("latitude", alert.getLatitude().toString());
		data.put("time", alert.getTime().toString());
		sendData(tokens, data);
	}
	
	public void sendPriority(IAlert alert, String token, Integer priority) {
		Collection<String> tokens = Collections.singleton(token);
		Map<String, String> data = new HashMap<>();
		data.put("type", "send");
		data.put("id", alert.getId().toString());
		data.put("title", alert.getTitle());
		data.put("priority", priority.toString());
		data.put("longitude", alert.getLongitude().toString());
		data.put("latitude", alert.getLatitude().toString());
		data.put("time", alert.getTime().toString());
		sendData(tokens, data);
	}

	public void transferAlert(IAlert alert, Collection<IUser> users, String msg) {
		Collection<String> tokens = users.stream().flatMap(u -> u.getDeviceTokens().stream()).collect(Collectors.toSet());
		Map<String, String> data = new HashMap<>();
		data.put("type", "transfer");
		data.put("id", alert.getId().toString());
		data.put("title", alert.getTitle());
		data.put("description", alert.getDescription());
		data.put("longitude", alert.getLongitude().toString());
		data.put("latitude", alert.getLatitude().toString());
		data.put("msg", msg);
		data.put("time", alert.getTime().toString());
		sendData(tokens, data);
	}

	public void acceptAlert(IAlert alert, Collection<IUser> users, String msg) {
		Collection<String> tokens = users.stream().flatMap(u -> u.getDeviceTokens().stream()).collect(Collectors.toSet());
		Map<String, String> data = new HashMap<>();
		data.put("type", "accept");
		data.put("id", alert.getId().toString());
		data.put("title", alert.getTitle());
		data.put("msg", msg);
		sendData(tokens, data);
	}

	public void resolveAlert(IAlert alert, Collection<IUser> users) {
		Collection<String> tokens = users.stream().flatMap(u -> u.getDeviceTokens().stream()).collect(Collectors.toSet());
		Map<String, String> data = new HashMap<>();
		data.put("type", "resolve");
		data.put("id", alert.getId().toString());
		data.put("title", alert.getTitle());
		sendData(tokens, data);
	}

	private void sendData(Collection<String> tokens, Map<String, String> data) {
		ArrayList<String> tokenList = new ArrayList<>(tokens);
		for (int from = 0, to = Integer.min(from + 500, tokenList.size()); to <= tokenList.size(); from = to, to += 500) {
			MulticastMessage message = MulticastMessage.builder().addAllTokens(tokenList.subList(from, to)).putAllData(data).build();
			FirebaseMessaging.getInstance().sendMulticastAsync(message);
		}
	}
}
