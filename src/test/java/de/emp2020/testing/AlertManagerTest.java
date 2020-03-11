package de.emp2020.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

import de.emp2020.alertEditor.AlertRepo;
import de.emp2020.users.User;
import de.emp2020.users.UserRepo;

import org.mockito.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AlertManagerTest {
	
//	@LocalServerPort
//	private int port;
	
//	@Autowired
//	private TestRestTemplate restTemplate;
	
//	@Test
//	void alertManagerControllerIsReachable () throws Exception {
//		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/app/test", String.class)).contains("Test successfull");
//	}

	@Autowired
	MockMvc mockMVC;
	
	@MockBean
	AlertRepo alertRepo;
	
	@MockBean
	UserRepo userRepo;
	
	@Mock
	User user;
	
	void testRegister() throws Exception {
		
		when(userRepo.findByUserName("user")).thenReturn(user);
		when(user.getId()).thenReturn(0);
		when(user.getPassword()).thenReturn("password");
		when(alertRepo.findByUserId(0)).thenReturn(null);
		
		this.mockMVC.perform(post("/app/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"user\":\"user\","
						+ "\"password\":\"password\","
						+ "\"userToken\":\"token\""
						+ "}"))
				.andExpect(status().isOk());
		
		verify(user).addToken("token");
		verify(userRepo).save(user);
	}
	
	@Test
	void gettingAllAlertsByUserID ()
	{
//		fail("Test not implemented yet");
		//TODO: when getting alerts by user id, do not leave any alerts behind
	}
	
	@Test
	void notGettingOtherUsersAlerts ()
	{
//		fail("Test not implemented yet");
		//TODO: do not get alerts of other users than specified by id
	}
	
	@Test
	void gettingAllTransferredAlertsByUserID ()
	{
//		fail("Test not implemented yet");
		//TODO when getting alerts by user id, do not forget any transferred alerts
	}
	
	@Test
	void addingAlertToUserMessagesUsers ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void editingAlertMessagesUsers ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void removingAlertFromUserMessagesUser ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void correctAlertBeingAccepted ()
	{
//		fail("Test not implemented yet");
		//TODO when accepting alert, it chooses the correct alert
	}
	
	@Test
	void triggeredAlertMessagesCorrectUsers ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void triggeredAlertDoesNotMessageWrongUsers ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void transferringAlertsMessagesCorrectUser ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void transferringAlertsDoesNotMessageWrongUser ()
	{
//		fail("Test not implemented yet");
	}
	
	@Test
	void acceptingAlertsMessagesSubscribers ()
	{
//		fail("Test not implemented yet");
		//TODO when an alert is accepted, all users of the alert will be messaged
	}
}
