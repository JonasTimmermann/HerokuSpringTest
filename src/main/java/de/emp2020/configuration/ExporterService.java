package de.emp2020.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Component
public class ExporterService {

    Logger log = LoggerFactory.getLogger(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode name;
    JsonNode root;
    String jsonString;

    @Autowired
    ExporterSender sender;

    
    public ExporterService(ExporterSender sender){
        this.sender = sender;
    }




    public List<ExcludePlugSensor> getAllPlugs(){

        JsonNode rootNodeInfo = getInfo();

        List<String> allNames = mapper.convertValue(rootNodeInfo.get("nodes"), ArrayList.class);
        List<ExcludePlugSensor> listAll = new ArrayList<>();

        if(allNames != null){
            for(int i = 0; i < allNames.size(); i++){
                ExcludePlugSensor zw = new ExcludePlugSensor(allNames.get(i), false);
                listAll.add(zw);
            }
        }       

        JsonNode rootNodeConfig = getConfig();
        List<String> onlyExcludedNames = mapper.convertValue(rootNodeConfig.get("excludeNodes"), ArrayList.class);
        log.info("List of all excluded Plugs: " + onlyExcludedNames.toString());

        if(onlyExcludedNames != null && onlyExcludedNames.size() > 0){
            for(int t = 0; t < onlyExcludedNames.size(); t++){
                String zwn = onlyExcludedNames.get(t);

                for(int z = 0; z < listAll.size(); z++){
                    if(listAll.get(z).getName().equals(zwn)){
                            listAll.get(z).setExclude(true);
                    }
                }
            }
        }
        return listAll;
    }



    public List<ExcludePlugSensor> getAllSensor(){
   
           JsonNode rootNodeInfo = getInfo();
           List<String> allNames = mapper.convertValue(rootNodeInfo.get("sensors"), ArrayList.class);
           List<ExcludePlugSensor> listAll = new ArrayList<>();

            if(allNames != null){
                for(int i = 0; i < allNames.size(); i++){
                    ExcludePlugSensor zw = new ExcludePlugSensor(allNames.get(i), false);
                    listAll.add(zw);
                }
            }

           JsonNode rootNodeConfig = getConfig();
           List<String> onlyExcludedNames = mapper.convertValue(rootNodeConfig.get("excludeSensors"), ArrayList.class);
           log.info("List of all excluded Sensors: " + onlyExcludedNames.toString());

            if(onlyExcludedNames != null && onlyExcludedNames.size() > 0){
                for(int t = 0; t < onlyExcludedNames.size(); t++){
                    String zwn = onlyExcludedNames.get(t);
        
                    for(int z = 0; z < listAll.size(); z++){
                        if(listAll.get(z).getName().equals(zwn)){
                            listAll.get(z).setExclude(true);
                        }
                    }
                }
            } 
        return listAll;
    }
   


    public List<PduNameMapping> getAllNames(){
        
        
        JsonNode rootNodeInfo = getInfo();
        List<String> allNames = mapper.convertValue(rootNodeInfo.get("nodes"), ArrayList.class);
        List<PduNameMapping> listAll = new ArrayList<>();
        
        for(int i = 0; i < allNames.size(); i++){
            PduNameMapping zw = new PduNameMapping(allNames.get(i), "");  // "N/A"
            listAll.add(zw);
        }

        
      
        JsonNode rootNodeConfig = getConfig();
       
        
        //ArrayNode
        if(rootNodeConfig.get("names") instanceof JsonNode) {
         
            //List<PduNameMapping> onlyNamedPdus = mapper.convertValue(rootNodeConfig.get("names"), ArrayList.class);
            Map<String, String> onlyNamedPdus = mapper.convertValue(rootNodeConfig.get("names"), new TypeReference<Map<String, String>>(){});
            List<PduNameMapping> listPdu = new ArrayList<>();
            List<String> resultKey = new ArrayList(onlyNamedPdus.keySet());
            List<String> resultValue = new ArrayList(onlyNamedPdus.values());
           
            for(int h = 0; h < onlyNamedPdus.size(); h++){
                PduNameMapping zww = new PduNameMapping(resultKey.get(h), resultValue.get(h));
                listPdu.add(zww);
            }
         
            if(onlyNamedPdus != null && listPdu.size() > 0){
                for(int t = 0; t < listPdu.size(); t++){
                    String zwn = listPdu.get(t).getTargetName();
        
                    for(int z = 0; z < listAll.size(); z++){
                        if(listAll.get(z).getTargetName().equals(zwn)){
                          
                            listAll.get(z).setMapsTo(listPdu.get(t).getMapsTo());
                            
                        }
                    }
                }
            }  
        
        }
        else {
            return null;
        } 
        /** else if( rootNodeConfig.get("names") instanceof JsonNode) {
           
            PduNameMapping onlyNamedPdus = mapper.convertValue(rootNodeConfig.get("names"), PduNameMapping.class);
           
            if(onlyNamedPdus != null){
                    String zwn = onlyNamedPdus.getTargetName();

                for(int z = 0; z < listAll.size(); z++){
                    if(listAll.get(z).getTargetName().equals(zwn)){
                        listAll.get(z).setMapsTo(onlyNamedPdus.getMapsTo());
                    }
                }
            }
        } **/
       
    return listAll;
    }


// Changes the plug names
    public String editPlugName(List<PduNameMapping> ex){

        JsonNode rootNode = getConfig();
        JsonNode childNode1 = mapper.createObjectNode();

        for(int i = 0; i < ex.size(); i++){

            ((ObjectNode) childNode1).put(ex.get(i).getTargetName(), ex.get(i).getMapsTo());
        }

        ((ObjectNode) rootNode).set("names", childNode1);

        try {
            sender.sendConfig(rootNode);
        } catch(IllegalArgumentException e){
            return "Unable to Change Plugnames, invalit Argument";
        }

        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            log.info("Plugnames send: " + jsonString);
        } catch (JsonProcessingException e) {e.printStackTrace();}
        

        return "Plugnames have been changed succesfully";
    }






   // Changes the plug name of one Plug
    public String editSinglePlugName(PduNameMapping ex){

        JsonNode rootNode = getConfig();
        JsonNode childNode1 = mapper.createObjectNode();
        JsonNode childNode11 = mapper.createObjectNode();


        
        Map<String, String> onlyNamedPdus = mapper.convertValue(rootNode.get("names"), new TypeReference<Map<String, String>>(){});
        List<PduNameMapping> listPdu = new ArrayList<>();

       // onlyNamedPdus.get(ex.getTargetName()).set

        List<String> resultKey = new ArrayList(onlyNamedPdus.keySet());
        List<String> resultValue = new ArrayList(onlyNamedPdus.values());

        boolean added = false;
        for(int h = 0; h < onlyNamedPdus.size(); h++){
            if(resultKey.get(h).equals(ex.getTargetName())){
                listPdu.add(ex);
                added = true;
            }else{
                PduNameMapping zww = new PduNameMapping(resultKey.get(h), resultValue.get(h));
                listPdu.add(zww);
            }
        }

        if(!added){
            listPdu.add(ex);
        }

        for(int i = 0; i < listPdu.size(); i++){

            ((ObjectNode) childNode11).put(listPdu.get(i).getTargetName(), listPdu.get(i).getMapsTo());
        }

        ((ObjectNode) rootNode).set("names", childNode11);

/** 
        ArrayNode arra = new ArrayNode(null, null);
        for(int i = 0; i < listPdu.size(); i++){
            JsonNode childNode2 = mapper.createObjectNode();
            ((ObjectNode) childNode2).put(listPdu.get(i).getTargetName(), listPdu.get(i).getMapsTo());  
        }
        **/
      //  JsonNode jsonNode = mapper.convertValue(listPdu, JsonNode.class);
      //  JsonNode jsonNoder = mapper.valueToTree(listPdu);
       // String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNoder);
/**   
       ObjectMapper mapper6 = new ObjectMapper();
       ObjectNode objectNode = mapper6.convertValue(listPdu, ObjectNode.class);
        ((ObjectNode) rootNode).set("names", objectNode);
**/
/** 
        ObjectMapper mapper4 = new ObjectMapper();
        ArrayNode array = mapper4.valueToTree(listPdu);
        ((ObjectNode) rootNode).set("names", array);
**/
       // ((ObjectNode) childNode1).put(ex.getTargetName(), ex.getMapsTo());
        //((ObjectNode) rootNode).set("names", childNode1);

        try {
            sender.sendConfig(rootNode);
        } catch(IllegalArgumentException e){
            return "Unable to Change Plugnames, invalit Argument";
        }

        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            log.info("Plugnames send: " + jsonString);
        } catch (JsonProcessingException e) {e.printStackTrace();}
        

        return "Plugnames have been changed succesfully";
    }




// Plug that should be excluded from monitoring are sent to Exporter
    public String editPlugs(List<ExcludePlugSensor> ex) {

        JsonNode rootNode = getConfig();
        ObjectMapper mapper2 = new ObjectMapper();
        List<String> er = new ArrayList<String>();

        for(int i = 0; i < ex.size(); i++){
            if(ex.get(i).isExclude()){
                er.add(ex.get(i).getName());
            }
        }

        ArrayNode array = mapper2.valueToTree(er);
        ((ObjectNode) rootNode).set("excludeNodes", array);

        try {
            sender.sendConfig(rootNode);
        } catch(IllegalArgumentException e){
            return "Unable to exclude Plugs, invalit Argument";
        }

        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            log.info("Excluded Plugs send: " + jsonString);
        } catch (JsonProcessingException e) {e.printStackTrace();}
       

        return "Plugs have been succesfully excluded";
    }


// Sensordata that should be excluded from monitoring are sent to Exporter
    public String editSensor(List<ExcludePlugSensor> ex) {

        JsonNode rootNode = getConfig();
        ObjectMapper mapper2 = new ObjectMapper();
        List<String> er = new ArrayList<String>();

        for(int i = 0; i < ex.size(); i++){

            if(ex.get(i).isExclude()){
                er.add(ex.get(i).getName());
            }
        }

        ArrayNode array = mapper2.valueToTree(er);
        ((ObjectNode) rootNode).set("excludeSensors", array);

        try {
            sender.sendConfig(rootNode);
        } catch(IllegalArgumentException e){
            return "Unable to exclude Sensordata, invalit Argument";
        }

        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            log.info("Excluded sensordata send: " + jsonString);
        } catch (JsonProcessingException e) {e.printStackTrace();}

        return "Sensordata have been succesfully excluded";
    }



    public JsonNode getInfo() {

        JsonNode root = sender.checkInfo();

        return root;
    }


    public JsonNode getConfig() {

        JsonNode root = sender.checkConfig();

        return root;
    }


}


//Nur zum Testen !!!!!!!!!!!!
    //@Scheduled(fixedRate = 10000)
    //public void sendConfig(JsonNode node) {
/** 
        JsonNode rootNode = ExampleStructure.getExampleRoot();
        ObjectNode addedNode = ((ObjectNode) rootNode).putObject("address");
        addedNode.put("city", "Seattle")
          .put("state", "Washington")
          .put("country", "United States");
**/
/** 
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        JsonNode childNode1 = mapper.createObjectNode();
        ((ObjectNode) childNode1).put("2_8", "ServerNameHier");
        JsonNode childNode2 = mapper.createObjectNode();
        ((ObjectNode) childNode2).put("excludeNodes", "");
        JsonNode childNode3 = mapper.createObjectNode();
        ((ObjectNode) childNode3).put("excludeSensors", "");
        ((ObjectNode) rootNode).set("names", childNode1);
      //  ((ObjectNode) rootNode).put("excludeNodes", "");
      //  ((ObjectNode) rootNode).put("excludeSensors", "");
**/

/** 
        JsonNode rootNode = mapper.createObjectNode();
        JsonNode childNode1 = mapper.createObjectNode();
        ((ObjectNode) childNode1).put("2_8", "HyperServer");
        ((ObjectNode) rootNode).set("names", childNode1);
        ObjectMapper mapper2 = new ObjectMapper();
        List<String> er = new ArrayList<String>();
        er.add("2_1");
        er.add("2_2");
        ArrayNode array = mapper2.valueToTree(er);
        ((ObjectNode) rootNode).set("excludeNodes", array);
        ObjectMapper mapper3 = new ObjectMapper();
        List<String> er2 = new ArrayList<String>();
        er2.add("voltage");
        //er2.add("activePower");
        ArrayNode array2 = mapper3.valueToTree(er2);
        ((ObjectNode) rootNode).set("excludeSensors", array2);
        //ObjectNode companyNode = mapper2.valueToTree(company);
        //companyNode.putArray("Employee").addAll(array);
        //JsonNode result = mapper.createObjectNode().set("company", companyNode);
        //JsonNode jsonNode2 = mapper.createObjectNode();
        //((ObjectNode) jsonNode2).putArray("excludeNodes").add(object.ge‌​tValue());
        sender.sendConfig(rootNode);
        // sender.sendConfig(node);
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("Send it: " + jsonString);
	}
}
**/

/** 

@Component
public class ExporterScheduler {
    // Define the logger object for this class
   // Logger log = LoggerFactory.getLogger(ResponseEntity<String>);
    Logger log = LoggerFactory.getLogger(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode name;
	@Autowired
    ExporterSender sender;
	@Scheduled(fixedRate = 10000)
	public void reportCurrentTime() {
        //service.triggerAlertQuerry();
        ResponseEntity<String> res = sender.checkConfig();
        try {
            JsonNode root = mapper.readTree(res.getBody());
            this.name = root.path("names");
        } catch (Exception exc) {
            // TODO: handle exception
        }
        log.info("Moin Hier eine Nachricht : " + this.name);
	}
}
**/