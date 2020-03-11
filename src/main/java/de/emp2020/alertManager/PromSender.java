package de.emp2020.alertManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PromSender {
	
	
	// the URL used for querying the Prometheus database
	// TODO: the URLs must be read from the configuration
	
	@Value("${emp2020.prometheus.url}")
	private String prometheusUrl = "http://134.245.1.240:1002/api/v1/query?query=";
	
	// just a logger
	Logger log = LoggerFactory.getLogger(PromSender.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	
	/**
	 * Will query Prometheus with a given expression, this will not evaluate the returned data itself, rather it will consider any relevant data to be returned as fulfillment of the expression
	 * @param query
	 * 			a PromQL expression
	 * @return
	 * 		{@code true} if the expression returns ANY DATA
	 */
	public boolean checkPromIsTriggered (String query)
	{
		String call = prometheusUrl + query;
		log.info(call);
		PromResponse answer = restTemplate.getForObject(call, PromResponse.class);
		
		return answer.isTriggered();
	}
	
	
	
	
	
}
