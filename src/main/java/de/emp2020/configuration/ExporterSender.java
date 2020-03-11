package de.emp2020.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExporterSender {

    private String promExporterExternalURL = "http://kolab-gateway.rz.uni-kiel.de:1003/config";
    private String promExporterInfoURL = "http://kolab-gateway.rz.uni-kiel.de:1003/info";
    // private String promExporterExternalURL = "http://exporter:8083/config";
    // private String promExporterInfoURL = "http://exporter:8083/info";

    ObjectMapper mapper = new ObjectMapper();
    JsonNode root;
    JsonNode name;
    HttpHeaders headers;
    Logger log = LoggerFactory.getLogger(String.class);
    String jsonString;

    @Autowired
    RestTemplate restTemplate;
    

    // Gets the current Configuration 
    public JsonNode checkConfig() {

        String call = promExporterExternalURL;
        ResponseEntity<String> answer = restTemplate.getForEntity(call, String.class);

        try {
             root = mapper.readTree(answer.getBody());
             this.name = root.path("names");
             log.info("Current Configuration: " + this.name);
        } catch (JsonMappingException e) {e.printStackTrace();
        } catch (JsonProcessingException e) {e.printStackTrace();}
        
		return root;
    }	



// Gets the general Information from Exporter 
// about the current available Plugs and Sensors
    public JsonNode checkInfo(){
		String call = promExporterInfoURL;
        ResponseEntity<String> answer = restTemplate.getForEntity(call, String.class);

        try {
            root = mapper.readTree(answer.getBody());
            this.name = root.path("nodes");
            log.info("All current available Plugs: " + this.name);
       } catch (JsonMappingException e) {e.printStackTrace();
       } catch (JsonProcessingException e) {e.printStackTrace();}
    
       return root;
    }	
    


    public void sendConfig(JsonNode node){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<JsonNode> request = new HttpEntity<>(node);
        ResponseEntity<JsonNode> response = restTemplate.exchange(promExporterExternalURL, HttpMethod.POST, request, JsonNode.class);
        //JsonNode responsePayload = response.getBody(); 

        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            log.info("Json sendet: " + jsonString);
        } catch (JsonProcessingException e) {e.printStackTrace();}
        
    }
}
        // Geht auch
/** 
		String call = promExporterExternalURL;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestSend = new HttpEntity<String>(node.toString(), headers);
        JsonNode resultAsJsonStr = restTemplate.postForObject(call, node, JsonNode.class);
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultAsJsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("Send it2: " + jsonString);

**/
/** zum Test
        namingJsonObject = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("1_1", "Server4242");
        namingJsonObject.put("names", user);
        HttpEntity<String> requestSend = new HttpEntity<String>(namingJsonObject.toString(), headers);
        String resultAsJsonStr = restTemplate.postForObject(call, requestSend, String.class);
        log.info("Moin Hier eine Nachricht : " + resultAsJsonStr);
        //JsonNode root = objectMapper.readTree(personResultAsJsonStr);

**/
        

