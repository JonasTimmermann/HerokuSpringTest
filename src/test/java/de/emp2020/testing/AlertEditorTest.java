package de.emp2020.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import de.emp2020.alertEditor.Alert;
import de.emp2020.alertEditor.AlertRepo;
import de.emp2020.users.User;
import de.emp2020.users.UserRepo;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AlertEditorTest {
	
	private static final int ALERT_COUNT = 10;
	private static final String MOCK_ALERT_1 = "{"
			+ "\"alertId\":1,"
			+ "\"title\":\"Test adding Alerts\","
			+ "\"owner\":\"Jan\","
			+ "\"description\":\"Generic Description\","
			+ "\"promQuery\":\"Generic Query\""
			+ "}";
	
	private static final String MOCK_ALERT_2 = "{"
			+ "\"alertId\":2,"
			+ "\"owner\":\"Jan\""
			+ "}";

	
	@LocalServerPort
	private int port;
	
	@Autowired
	private MockMvc mockMVC;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private Logger log = LoggerFactory.getLogger(AlertEditorTest.class);
	
	@MockBean
	AlertRepo alertRepo;
	@MockBean
	UserRepo userRepo;
	
	private ArrayList<Alert> alerts;
	
	@BeforeEach
	void initialize ()
	{
		User owner = new User ();
		owner.setUserName("Jan");
		
		alerts = new ArrayList<Alert>();
		
		for (int i=0 ; i<ALERT_COUNT ; i++)
		{
			Alert alert = new Alert();
			alert.setTitle("Test Alert " + i);
			alert.setDescription("Generic Alert Description");
			alert.setQuery("Is " + i + "==" + i + "?");
			alert.setId(i);
			alert.setOwner(owner);
			alerts.add(alert);
		}
	}
	
	
	void alertEditorControllerIsReachable () throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/alerts/test", String.class)).contains("Test successfull");
	}
	
	void gettingAllAlerts () throws Exception
	{
		when(alertRepo.findAll()).thenReturn(alerts);

		ResultActions actions = this.mockMVC.perform(get("/alerts/all"))
				.andDo(print()).andExpect(status().isOk());
		for (int i=0 ; i<ALERT_COUNT ; i++)
		{
			actions.andExpect(content().string(containsString("\"title\":\"Test Alert " + i + "\"")));
		}
	}
	
	void getCorrectAlertByID () throws Exception
	{
		for (int i=0 ; i<ALERT_COUNT ; i++)
		{
			when(alertRepo.findById(i)).thenReturn(Optional.of(alerts.get(i)));
			this.mockMVC.perform(get("/alerts/" + i))
					.andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("\"alertId\":" + i)));
		}
	}
	
	void getAddedAlert () throws Exception
	{
		ArgumentCaptor<Alert> catchAlert = ArgumentCaptor.forClass(Alert.class);
		when(userRepo.findByUserName("Jan")).thenReturn(new User());

		this.mockMVC.perform(post("/alerts/add")
				.content(MOCK_ALERT_1)
				.contentType(MediaType.APPLICATION_JSON));
		
		Mockito.verify(alertRepo).save(catchAlert.capture());
		
		assertEquals("Test adding Alerts", catchAlert.getValue().getTitle());
		assertEquals("Generic Description", catchAlert.getValue().getDescription());
		assertEquals("Generic Query", catchAlert.getValue().getQuery());
	}
	
	void editedAlertHasCorrectValues () throws Exception
	{
		ArgumentCaptor<Alert> catchAlert = ArgumentCaptor.forClass(Alert.class);
		when(alertRepo.findById(1)).thenReturn(Optional.of(alerts.get(1)));
		
		this.mockMVC.perform(post("/alerts/edit")
				.content(MOCK_ALERT_1)
				.contentType(MediaType.APPLICATION_JSON));
		
		Mockito.verify(alertRepo).save(catchAlert.capture());
		
		assertEquals("Test adding Alerts", catchAlert.getValue().getTitle());
		assertEquals("Generic Description", catchAlert.getValue().getDescription());
		assertEquals("Generic Query", catchAlert.getValue().getQuery());
		assertEquals(1, catchAlert.getValue().getId());
	}
	
	void editingDoesNotOverwriteWithNull () throws Exception
	{
		ArgumentCaptor<Alert> catchAlert = ArgumentCaptor.forClass(Alert.class);
		Alert alert = alerts.get(2);
		when(alertRepo.findById(2)).thenReturn(Optional.of(alert));
		
		this.mockMVC.perform(post("/alerts/edit")
				.content(MOCK_ALERT_2)
				.contentType(MediaType.APPLICATION_JSON));
		
		Mockito.verify(alertRepo).save(catchAlert.capture());
		
		assertEquals(alert.getTitle(), catchAlert.getValue().getTitle());
		assertEquals(alert.getDescription(), catchAlert.getValue().getDescription());
		assertEquals(alert.getQuery(), catchAlert.getValue().getQuery());
		assertEquals(alert.getId(), catchAlert.getValue().getId());
	}
	
	void canNotGetDeletedAlert () throws Exception
	{
		when(alertRepo.findById(1)).thenReturn(Optional.of(new Alert()));
		Mockito.doNothing().when(alertRepo).deleteById(1);
		this.mockMVC.perform(delete("/alerts/delete").param("id", "1")).andExpect(status().isOk());

		Mockito.verify(alertRepo, times(1)).deleteById(1);
	}
	
}
