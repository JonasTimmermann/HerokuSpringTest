package de.emp2020.Location;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
    
@CrossOrigin
@RestController
public class PlaceController {

    @Autowired
    private PlaceService placeService;


    public PlaceController(PlaceService placeService){
        this.placeService = placeService;
    }
    

    // Location from App POST
    // /distance   "distance":"1234", "token":"123abc"

    ///config/all  URLs
    //  PUT /config/edit


// Get all Places
    @RequestMapping(path = "/place/all")
    public Iterable<Place> getAllPlaces(){

        return placeService.getAllPlaces();
    }

// Get a single place with id
   @RequestMapping(path = "/place/{id}")
   public Place getPlaceById(@PathVariable Integer id){
       
       return placeService.getPlaceById(id);
   }

// Add a new Place to the Repository
   @RequestMapping(method = RequestMethod.POST, value = "/place/add")
   public String savePlace(@RequestBody Place place){

        

       return placeService.savePlace(place);
   }

// Edit a single place with id
    @RequestMapping(method = RequestMethod.POST, value = "/place/edit") //"/place/{id}/edit")
    public String editPlace(@RequestBody Place place) {
       
        placeService.editPlace(place);

        return "Ok";
    }

    // Adds a single alert to the alertList of one Place
    @RequestMapping(method = RequestMethod.POST, value = "/place/{id}/addAlert/{alertId}") //"/place/{id}/edit")
    public String addAlertToPlace(@PathVariable Integer id, @PathVariable Integer alertId) {
       
        placeService.addAlertToPlace(id, alertId);

        return "Ok";
    }

    // Deletes one alert from Places alertList
    @RequestMapping(method = RequestMethod.POST, value = "/place/{id}/alert/delete")
	public String deleteAlertFromPlace(@PathVariable Integer id) {
	
		return placeService.deleteAlertFromPlace(id);
	}


    // Deletes a specific Place
	@RequestMapping(method = RequestMethod.DELETE, value = "/place/{id}/delete")
	public String deletePlaceById(@PathVariable Integer id) {
	
		return placeService.deletePlaceById(id);
	}
        
    
}
