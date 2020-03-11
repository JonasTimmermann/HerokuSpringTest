package de.emp2020.configuration;

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
public class ConfigController {

	@Autowired
	private ExporterService exporterService;

// Get all Plugnames and status(if excluded or not)
	@RequestMapping(path = "/config/plug/all")
	public List<ExcludePlugSensor> getAllPlugs(){
		return exporterService.getAllPlugs();
	}



	public ConfigController(ExporterService exporterService){
		this.exporterService = exporterService;
	}


	// Stecker ausschließen
	@RequestMapping(method = RequestMethod.POST, value = "/config/plug/edit")
	public String excludePlugs(@RequestBody List<ExcludePlugSensor> ex) {
		return exporterService.editPlugs(ex);
	}


// Get all Sensornames and status (if excluded or not)	
	@RequestMapping(path = "/config/sensor/all")
	public List<ExcludePlugSensor> getAllSensor(){
		return exporterService.getAllSensor();
	}

	// Sensordaten ausschließen
	@RequestMapping(method = RequestMethod.POST, value = "/config/sensor/edit")
	public String excludeSensor(@RequestBody List<ExcludePlugSensor> ex) {
		return exporterService.editSensor(ex);
	}


	 
// Get all Sensornames and corresponding/mapped Names	
	@RequestMapping(path = "/config/plug/name/all")
	public List<PduNameMapping> getAllNames(){
		return exporterService.getAllNames();
	}

	

	// Steckerbenennung aendern
	@RequestMapping(method = RequestMethod.POST, value = "/config/plug/name/edit")
	public String editPlugName(@RequestBody List<PduNameMapping> ex) {
		return exporterService.editPlugName(ex);
	}

	// Steckerbenennung eines Steckers 
	@RequestMapping(method = RequestMethod.POST, value = "/config/plug/name/edit/single")
	public String editSinglePlugName(@RequestBody PduNameMapping ex) {
		return exporterService.editSinglePlugName(ex);
	}

	
	
}


/** 
	// Steckerbenennung (alt mit Repo)
	// Alle Stecker-Objekte werden zurückgegeben
	@RequestMapping(path = "/naming/all")
	public List<NameableNode> getAllMappings(){
		return namingService.getAllMappings();
	}
	// Das Stecker-Objekt mit entsprechender id wird zurückgegeben
	@RequestMapping(path = "/naming/{id}")
	public NameableNode getMapping(@PathVariable Long id){
		return namingService.getNameableNodeById(id);
	}
	// Kann Funktion
	@RequestMapping(method = RequestMethod.POST, value = "/naming/add")
	public NameableNode saveMapping (@RequestBody NameableNode mapping){
		return namingService.saveMapping(mapping);
	}
	// Umbenennung eines existierenden Steckers
	@RequestMapping(method = RequestMethod.POST, value = "/naming/edit")
	public NameableNode editMapping (@RequestBody NameableNode mapping){
		return namingService.editMapping(mapping);
	}
	@RequestMapping("/naming/test")
	public String test (){
		return "Test successfull";
	}
**/
/** 
	public ConfigController(NamingService naming, ExporterService exporterService){
		this.namingService = naming;
		this.exporterService = exporterService;
	}
	
	@Autowired
	private NamingService namingService;
**/

