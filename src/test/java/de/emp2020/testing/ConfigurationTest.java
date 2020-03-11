package de.emp2020.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.emp2020.configuration.ConfigController;
//import de.emp2020.configuration.ConfigService;
import de.emp2020.configuration.ExporterSender;
import de.emp2020.configuration.ExporterService;
import de.emp2020.configuration.PduNameMapping;

//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import org.junit.runner.RunWith;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
//@WebAppConfiguration
//@RunWith(MockitoJUnitRunner.class) 
@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ConfigurationTest {

//	@LocalServerPort
//	private int port;

	private MockMvc mockMvc;

//	@Autowired
//	private TestRestTemplate restTemplate;

	Logger log = LoggerFactory.getLogger(String.class);
/** 
	NamingService serviceMock; // = mock(NamingService.class);
	NamingRepo repoMock; // = mock(NamingRepo.class);
	ConfigController controllerMock; //= mock(ConfigController.class);
	ConfigService configServiceMock;
	NamingService namingService;//= new NamingService(this.repoMock);
	ExporterService exporterService;
**/

	ExporterSender exporterSender;
	ExporterService exporterService;
	ConfigController configController;
	ObjectMapper mapper;



	
	@BeforeEach
    public void setup() {

		exporterSender = mock(ExporterSender.class);
		exporterService = new ExporterService(exporterSender);
		configController = new ConfigController(exporterService);

		this.mockMvc = MockMvcBuilders.standaloneSetup(configController).build();
		mapper = new ObjectMapper();
   
    }

	


	@Test
	public void getAllPlugNames() throws Exception {

		JsonNode rootNode = mapper.createObjectNode();
		JsonNode childNode1 = mapper.createObjectNode();
		((ObjectNode) childNode1).put("2_1", "HyperServer");
		((ObjectNode) childNode1).put("2_7", "SuperServer");
		((ObjectNode) rootNode).set("names", childNode1);
		ObjectMapper mapper2 = new ObjectMapper();
		List<String> er = new ArrayList<String>();
		er.add("2_8");
		er.add("2_2");
		ArrayNode array = mapper2.valueToTree(er);
		((ObjectNode) rootNode).set("excludeNodes", array);
		ObjectMapper mapper3 = new ObjectMapper();
		List<String> er2 = new ArrayList<String>();
		er2.add("voltage");
		er2.add("activePower");
		ArrayNode array2 = mapper3.valueToTree(er2);
		((ObjectNode) rootNode).set("excludeSensors", array2);

		doReturn(rootNode).when(exporterSender).checkConfig();

		JsonNode rootNodeInfo = mapper.createObjectNode();
		ObjectMapper mapperInfo = new ObjectMapper();
		List<String> erInfo = new ArrayList<String>();
		erInfo.add("2_1");
		erInfo.add("2_2");
		erInfo.add("2_5");
		erInfo.add("2_8");
		erInfo.add("2_7");
		ArrayNode arrayInfo = mapperInfo.valueToTree(erInfo);
		((ObjectNode) rootNodeInfo).set("nodes", arrayInfo);
		ObjectMapper mapperInfo2 = new ObjectMapper();
		List<String> erInfo2 = new ArrayList<String>();
		erInfo2.add("voltage");
		erInfo2.add("apparentPower");
		erInfo2.add("outletState");
		erInfo2.add("activePower");
		ArrayNode arrayInfo2 = mapperInfo2.valueToTree(erInfo2);
		((ObjectNode) rootNodeInfo).set("sensors", arrayInfo2);

		doReturn(rootNodeInfo).when(exporterSender).checkInfo();

		
		this.mockMvc.perform(get("/config/plug/name/all"))
                .andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(5)))   
				.andExpect(jsonPath("$[0].targetName", is("2_1"))).andExpect(jsonPath("$[0].mapsTo", is("HyperServer")))
				.andExpect(jsonPath("$[1].targetName", is("2_2"))).andExpect(jsonPath("$[1].mapsTo", is("")))
				.andExpect(jsonPath("$[2].targetName", is("2_5"))).andExpect(jsonPath("$[2].mapsTo", is("")))	
				.andExpect(jsonPath("$[3].targetName", is("2_8"))).andExpect(jsonPath("$[3].mapsTo", is("")))
				.andExpect(jsonPath("$[4].targetName", is("2_7"))).andExpect(jsonPath("$[4].mapsTo", is("SuperServer")));
 
		verify(exporterSender, times(1)).checkInfo();
		verify(exporterSender, times(1)).checkConfig();
	    verifyNoMoreInteractions(exporterSender);
    }






	@Test
	public void getAllSensor() throws Exception {

		JsonNode rootNode = mapper.createObjectNode();
		JsonNode childNode1 = mapper.createObjectNode();
		((ObjectNode) childNode1).put("2_1", "HyperServer");
		((ObjectNode) childNode1).put("2_7", "SuperServer");
		((ObjectNode) rootNode).set("names", childNode1);
		ObjectMapper mapper2 = new ObjectMapper();
		List<String> er = new ArrayList<String>();
		er.add("2_8");
		er.add("2_2");
		ArrayNode array = mapper2.valueToTree(er);
		((ObjectNode) rootNode).set("excludeNodes", array);
		ObjectMapper mapper3 = new ObjectMapper();
		List<String> er2 = new ArrayList<String>();
		er2.add("voltage");
		er2.add("activePower");
		ArrayNode array2 = mapper3.valueToTree(er2);
		((ObjectNode) rootNode).set("excludeSensors", array2);

		doReturn(rootNode).when(exporterSender).checkConfig();

		JsonNode rootNodeInfo = mapper.createObjectNode();
		ObjectMapper mapperInfo = new ObjectMapper();
		List<String> erInfo = new ArrayList<String>();
		erInfo.add("2_1");
		erInfo.add("2_2");
		erInfo.add("2_5");
		erInfo.add("2_8");
		erInfo.add("2_7");
		ArrayNode arrayInfo = mapperInfo.valueToTree(erInfo);
		((ObjectNode) rootNodeInfo).set("nodes", arrayInfo);
		ObjectMapper mapperInfo2 = new ObjectMapper();
		List<String> erInfo2 = new ArrayList<String>();
		erInfo2.add("voltage");
		erInfo2.add("apparentPower");
		erInfo2.add("outletState");
		erInfo2.add("activePower");
		ArrayNode arrayInfo2 = mapperInfo2.valueToTree(erInfo2);
		((ObjectNode) rootNodeInfo).set("sensors", arrayInfo2);

		doReturn(rootNodeInfo).when(exporterSender).checkInfo();


	//	ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	//	String requestJson = ow.writeValueAsString(anObject);
	
		//mockMvc.perform(post("/config/plug/name/edit").contentType(MediaType.APPLICATION_JSON)
		//	.content(requestJson))
		//	.andExpect(status().isOk());
		
			
		this.mockMvc.perform(get("/config/sensor/all"))
                .andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(4)))   
				.andExpect(jsonPath("$[0].name", is("voltage"))).andExpect(jsonPath("$[0].exclude", is(true)))
				.andExpect(jsonPath("$[1].name", is("apparentPower"))).andExpect(jsonPath("$[1].exclude", is(false)))
				.andExpect(jsonPath("$[2].name", is("outletState"))).andExpect(jsonPath("$[2].exclude", is(false)))	
				.andExpect(jsonPath("$[3].name", is("activePower"))).andExpect(jsonPath("$[3].exclude", is(true)));
				
 
		verify(exporterSender, times(1)).checkInfo();
		verify(exporterSender, times(1)).checkConfig();
	    verifyNoMoreInteractions(exporterSender);
    }









/** 

	@Test
	public void getAllNameableNodesCorrectly() throws Exception {
		
		repoMock = mock(NamingRepo.class);
		configServiceMock = mock(ConfigService.class);
		namingService = new NamingService(this.repoMock);
		exporterService = new ExporterService();
		controllerMock = new ConfigController(configServiceMock, namingService, exporterService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controllerMock).build();

		NameableNode first = new NameableNode((long) 1, "PDU_1_Stecker_2");
		first.setMapsTo("MonitoringServer");
		NameableNode second = new NameableNode((long) 2, "PDU_1_Stecker_3");
		second.setMapsTo("UniServer1");
		List<NameableNode> liste = new ArrayList<>();
		liste.add(first);
		liste.add(second);
		
		//when(controllerMock.getAllMappings()).thenReturn(liste);
		doReturn(liste).when(repoMock).findAll();
	
		this.mockMvc.perform(get("/naming/all"))
                .andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))    //contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].targetName", is("PDU_1_Stecker_2")))
                .andExpect(jsonPath("$[0].mapsTo", is("MonitoringServer")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].targetName", is("PDU_1_Stecker_3")))
                .andExpect(jsonPath("$[1].mapsTo", is("UniServer1")));
 
        verify(repoMock, times(1)).findAll();
        verifyNoMoreInteractions(repoMock);
    }

	

	@Test
	public void getAllMappingsTestService() throws Exception {

		repoMock = mock(NamingRepo.class);
		namingService = new NamingService(this.repoMock);

		NameableNode first = new NameableNode((long) 1, "PDU_1_Stecker_2");
		first.setMapsTo("MonitoringServer");
		NameableNode second = new NameableNode((long) 2, "PDU_1_Stecker_3");
		second.setMapsTo("UniServer1");
		List<NameableNode> liste = new ArrayList<>();
		liste.add(first);
		liste.add(second);
		
		doReturn(liste).when(repoMock).findAll();
		assertEquals(liste, namingService.getAllMappings());
    }


**/

//	@Test
//	void configurationControllerIsReachable () throws Exception {
//		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/naming/test", String.class)).contains("Test successfull");
//	}

	@Test
	void getConfigIsCorrect ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void saveConfigChangesSettings ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void correctConfigIsSaved ()
	{
//		fail("Test not implemented yet");
	}

	/** 
=======
	
	/**
>>>>>>> parent of cd94ce6... CrossOrigin added
	@Test
	void gettingNamesIsCorrect() throws Exception{

		this.mockMvc.perform(get("/naming/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, World")));

//		fail("Test not implemented yet");
	}**/
	
	@Test
	public void correctNamesAreSaved() throws Exception{
		
//		fail("Test not implemented yet");
	}
	
	@Test
	void savingNamesMessagesExporter ()
	{
//		fail("Test not implemented yet");
	}

}
