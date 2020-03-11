package de.emp2020.Location;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Null;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.emp2020.alertEditor.Alert;
import de.emp2020.alertEditor.AlertEditorService;
import de.emp2020.alertEditor.AlertRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class PlaceService {
    
    
	@Autowired
    PlaceRepository placeRepo;

    @Autowired
    AlertRepo alertRepo;
    
      
	@Autowired
	AlertEditorService alertService;

    Logger log = LoggerFactory.getLogger(String.class);
    
    
    public PlaceService(PlaceRepository repo){
		this.placeRepo = repo;
	}



    // Get all Places
    public Iterable<Place> getAllPlaces(){
    	return placeRepo.findAll();
    }



    // Get a single place with id
    public Place getPlaceById(Integer id){
    
        return placeRepo.findById(id).orElse(null);
    }



    // Add a new Place to the Repository
    public String savePlace(Place place){
    	
    	placeRepo.save(place);
    	
    	
    	
        
//		Integer idPlace = place.getId();
//        Iterable<Place> listPlaces = getAllPlaces();
//        
//        try{
//
//            for(int i = 0; i < listPlaces.size(); i++) {
//                if(idPlace == listPlaces.get(i).getId()){
//    
//                    return null;
//                }
//            }
//

//            List<Alert> alert = place.getAlertList();
//
//            Place p = new Place(idPlace, place.getName(), place.getVerticalPosition(), place.getHorizontalPosition());
//            placeRepo.save(p);
//
//            Integer idP = p.getId();
//
//          //  log.info("Place saved" + alert.size());
//            for(int i = 0; i < alert.size(); i++){
//                Integer ids = alert.get(i).getId();
//                //log.info("ID ist: " + alertService.getAlertByID(ids));
//
//                if(alertService.getAlertByID(ids) != null ){
//                    //log.info("Place linked" + p.getId() + " | " + alert.get(i).getId());
//                    
//                    addAlertToPlace(p.getId(), alert.get(i).getId());
//                   // return "Existing Alert has been linked to new Place";
//                }else{
//                   // log.info("Alert inserted");
//                    alertRepo.save(alert.get(i));
//
//                    List<Alert> alerListe = new ArrayList<>();
//                    alertRepo.findAll().forEach(alerListe::add);
//                    Integer alertId = alerListe.get(alerListe.size() - 1).getId();
//                    //log.info("Alert inserted");
//                    //log.info("Place linked" + p.getId() + " | " + alert.get(i).getId());
//
//                    if(getPlaceById(idP) == null){
//                        List<Place> pList = getAllPlaces();
//                        Place pp = pList.get(pList.size()-1);
//                        idP = pp.getId();
//                        //log.info("Alert inserted if");
//                    }
//                   // log.info("Alert inserted else");
//                    addAlertToPlace(idP, alertId); //alert.get(i).getId());
//                }
//            }
//            //placeRepo.save(place);
//
//        }catch(IllegalArgumentException e){
//            
//        }catch(NullPointerException e){
//
//        }


		

        return "New Place with new Alerts have been added";
    }



    // Edit a single place with id
    
    public String editPlace(Place place) {

        deletePlaceById(place.getId());

/** 
        Place placeZw = getPlaceById(place.getId());
        placeZw.setHorizontalPosition(place.getHorizontalPosition());
        placeZw.setName(place.getName());
        placeZw.setVerticalPosition(place.getVerticalPosition());
**/
        savePlace(place);
        
        //placeRepo.save(placeZw);

        return "Ok";
    }



    public String deleteAlertFromPlace(Integer alertId){

//        List<Place> listPlace = this.getAllPlaces();
//
//        for(int i = 0; i < listPlace.size(); i++){
//            if(listPlace.get(i).getAlertList() != null){
//                List<Alert> alertList = listPlace.get(i).getAlertList();
//                for(int t = 0; t < alertList.size(); t++){
//                    if(alertList.get(t).getId() == alertId){
//                        alertList.remove(t);
//                        //this.editPlace(listPlace.get(i));
//
//                        Place placeZw = getPlaceById(listPlace.get(i).getId());
//                    // placeZw.setHorizontalPosition(place.getHorizontalPosition());
//                        // placeZw.setName(place.getName());
//                        // placeZw.setVerticalPosition(place.getVerticalPosition());
//                        placeZw.setAlertList(alertList);
//
//                        placeRepo.save(placeZw);
//                    }
//                }
//            }
//        }

        return "Delete success";
    }


      
    public String addAlertToPlace(Integer id, Integer alertId) {

        
        if(getPlaceById(id) == null){
            return "There is no Place with id: " + id;
        }
        Place placeZw = getPlaceById(id);
        //log.info("Place: " + placeZw.toString());
/**    
        List<Alert> li = new ArrayList<>(); 
        placeZw.setAlertList(li);
        placeRepo.save(placeZw);
        log.info("Alerts Hier: 2 " + this.getPlaceById(id).getName() + this.getPlaceById(id).getAlertList().toString());
**/
//        Alert a = alertService.getAlertByID(alertId);
//       // log.info("A: " + a);
//
//        List<Alert> alertList ;
//        if(placeZw.getAlertList() != null){
//            alertList = placeZw.getAlertList();
//            for(int i = 0; i < alertList.size(); i++){
//
//                if(alertList.get(i).getId() == a.getId()){
//                    
//                    return "Already included";
//                    //alertList.add(a);
//                }
//            }
//        }else{
//        //log.info("Deleting: ");
//           alertList = new ArrayList<>();
//        }
//
//        deleteAlertFromPlace(alertId);
//        alertList.add(a);
//        
//        //log.info("Alerts Hier: " + alertList.toString());
//        placeZw.setAlertList(alertList);
        //placeZw.addAlert(a);
/** 
        placeZw.setHorizontalPosition(place.getHorizontalPosition());
        placeZw.setName(place.getName());
        placeZw.setVerticalPosition(place.getVerticalPosition());
**/
        placeRepo.save(placeZw);

        return "Place and Alert linked";
    }

 



// Deletes a question by it's ID
    public String deletePlaceById(Integer id) {
        try{
            
           // List<QuestionCategory> qcList = getQuestionById(id).getQuestionCategories();
           // qcList = questionCategoryService.deleteWhenNoMoreExisting(qcList);	
            
            //Deleting the question now, this is also deleting the row in relation table between question 
            //and category
            placeRepo.deleteById(id);
            
            //Deleting all categories, which we found as empty
            //questionCategoryService.deleteAllCategories(qcList);
            return "Success in deleting this Question.";
        }catch(Exception e) {
            return "Failed to delete this Question.";
        }
        
    }



}