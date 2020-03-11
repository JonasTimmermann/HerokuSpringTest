package de.emp2020.alertEditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.emp2020.Location.PlaceService;

@CrossOrigin
@RestController
@RequestMapping(path="/alerts")
public class AlertEditorController {
	
	@Autowired
	AlertEditorService service;

	@Autowired
	PlaceService placeService;

	@Autowired
	AlertRepo alertRepo;



/** 

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/place/add")
    public String editPlace(@PathVariable Integer id, @RequestBody Integer idPlace) {
       
		Alert a = service.getAlertByID(id);
		Place p = placeService.getPlaceById(idPlace);
		a.setPlace(p);
		alertRepo.save(a);


    	return "Ok";
    }
**/

	
	@GetMapping("/all")
	public Iterable<Alert> getAllAlerts ()
	{
		return service.getAllAlerts();
	}
	
	
	@GetMapping("/{id}")
	public Alert getAlertByID (@PathVariable int id)
	{
		return service.getAlertByID(id);
	}
	
	
	@PostMapping("/add")
	public boolean addAlert (@RequestBody AlertJson alert)
	{
		//TODO: make sure user is logged in and submit user or userID of the person adding the alert!
		
		return service.addAlert(alert, alert.getOwner());
	}
	
	
	@PostMapping("/edit")
	public boolean editAlert (@RequestBody AlertJson alert)
	{
		//TODO: sanitize alert object
		return service.editAlert(alert);
	}
	
	
	@DeleteMapping("/delete")
	public boolean deleteAlert (
			@RequestParam(name="id", required=true) int id)
	{
		return service.deleteAlert(id);
	}
	
	
	@RequestMapping("/test")
	public String test ()
	{
		return "Test successfull";
	}
	
	
}
