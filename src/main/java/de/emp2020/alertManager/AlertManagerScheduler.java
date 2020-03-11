package de.emp2020.alertManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlertManagerScheduler {
	
	@Autowired
	AlertManagerService service;
	
	@Scheduled(fixedRate = 5000)
	public void triggerAlertQuery() {
		
		service.triggerAlertQuerry();
		
	}
	
	
	@Scheduled(fixedRate = 20000)
	public void updatePriorities () {
		
		service.updatePriorities();
		
	}

	
}
