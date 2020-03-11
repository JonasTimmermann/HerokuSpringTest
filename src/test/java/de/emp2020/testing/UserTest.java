package de.emp2020.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserTest {
	
//	@LocalServerPort
//	private int port;
	
//	@Autowired
//	private TestRestTemplate restTemplate;
	
//	@Test
//	void userControllerIsReachable () throws Exception {
//		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/users/test", String.class)).contains("Test successfull");
//	}
	
	@Test
	void gettingAllUsers ()
	{
//		fail("Test not implemented yet");
		// TODO: When getting all users, no user is left behind
	}
	
	@Test
	void getCorrectUserByID ()
	{
//		fail("Test not implemented yet");
		// TODO: when getting user by ID, assert you get correct user
	}
	
	@Test
	void getNoUserByWrongID ()
	{
//		fail("Test not implemented yet");
		// TODO: When using non-existant ID, assert that no user is returned
	}
	
	@Test
	void getAddedUser ()
	{
//		fail("Test not implemented yet");
		// TODO: when user has been added, assert that they will be returned when getting all users
	}
	
	@Test
	void editedUserHasCorrectValues ()
	{
//		fail("Test not implemented yet");
		// TODO: edited user has new values
	}
	
	@Test
	void didNotEditWrongUser ()
	{
//		fail("Test not implemented yet");
		// TODO: when editing, no user has been changed incorrectly
	}
	
	@Test
	void canNotGetDeletedUser ()
	{
//		fail("Test not implemented yet");
		// TODO: when deleting user, they won't be available anymore
	}
	
	@Test
	void didNotDeleteWrongUser ()
	{
//		fail("Test not implemented yet");
		// TODO: when deleting user, other users won't be deleted
	}

}
