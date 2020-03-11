package de.emp2020.alertManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.emp2020.alertEditor.Alert;


@CrossOrigin
@RestController
@RequestMapping("/app")
public class AlertManagerController {
	
	
	@Autowired
	AlertManagerService service;
	
	
	@GetMapping("/alerts")
	public Data getAlertsByUserID (
			@RequestParam(name="userId", required=true) int userId,
			@RequestParam(name="userToken", required=false) String userToken)
	{
		
		// TODO: digest params for user authentification
		
		return new Data (service.getAlertsByUser(userId));
	}
	
	
	@PostMapping("/accept")
	public Status acceptAlert (
			@RequestParam(name="user", required=true) int userId,
			@RequestParam(name="userToken", required=false) String userToken,
			@RequestParam(name="alertId", required=true) int alertId,
			@RequestParam(name="message", required=false) String message)
	{
		
		// TODO: digest params for user authentification
		
		service.acceptAlert(userId, alertId, message);
		
		return new Status(true);
	}
	
	
	@PostMapping("/transfer")
	public Status transferAlert (
			@RequestParam(name="user", required=false) int requestingUserId,
			@RequestParam(name="userToken", required=false) String userToken,
			@RequestParam(name="alertId", required=true) int alertId,
			@RequestParam(name="receiver", required=true) String targetUserName,
			@RequestParam(name="message", required=false) String message)
	{
		
		// TODO: digest params for user authentification
		
		service.transferAlert(alertId, targetUserName, message);
		
		return new Status(true);
	}
	
	

	@PostMapping("/register")
	public Iterable<Alert> registerUser (@RequestBody Phone phone)
	{
		return service.registerUser(phone.user, phone.userToken, phone.password);
	}
	



	@PostMapping("/unregister")
	public Status unregisterUser (
			@RequestParam(name="userToken", required=true) String userToken)
	{
		return new Status(service.unregisterUser(userToken));
	}
	
	@PostMapping(value = "/distance")
	public Status saveDistance(@RequestBody DistanceJson distance){
		
		service.saveDistance(distance);
		
		return new Status(true);
	}
	
	
	@RequestMapping("/test")
	public String test ()
	{
		return "Test successfull";
	}
	
	

	
	
	// for testing
	public static class Phone {
		
		private String user;
		private String password;
		private String userToken;
		
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getUserToken() {
			return userToken;
		}
		public void setUserToken(String token) {
			this.userToken = token;
		}
	}
	
	
	
	public static class Status
	{
		private String status;
		
		public Status ()
		{
			
		}
		
		public Status (boolean status)
		{
			if (status)
			{
				this.status = "Ok";
			}
			else
			{
				this.status = "Error";
			}
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
	}
	
	
	public static class Data {
		
		private Iterable<Alert> alerts;
		
		public Data ()
		{
			
		}
		
		public Data (Iterable<Alert> alerts)
		{
			this.alerts = alerts;
		}

		public Iterable<Alert> getAlerts() {
			return alerts;
		}

		public void setAlerts(Iterable<Alert> alerts) {
			this.alerts = alerts;
		}
	}
	
	
}
