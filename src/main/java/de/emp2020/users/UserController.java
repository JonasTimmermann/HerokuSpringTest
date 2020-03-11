package de.emp2020.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping(path="/users")
public class UserController {
	
	@Autowired
	UserService service;
	
	






	
	/**
	 * Fetches all users in the user repository with ALL attached information and provides them as Json to any respective GET-requests
	 * @return
	 * 		an {@code Iterable} over all {@code User} which Spring will parse into a Json
	 */
	@RequestMapping(path = "/all")
	public List<User> getAllUsers(){
		return service.getAllUsers();
	}
	
	
	/**
	 * Fetches a single, specific user in the repository with ALL attached information and provides them as Json to any respective GET-requests
	 * @param id
	 * 		is a unique {@code id} which identifies the user 
	 * @return
	 * 		a pojo of type {@code User} which Spring will parse into a Json
	 */
	@RequestMapping(path = "/{id}")
	public User getUserByID (@PathVariable int id)
	{
		
		// TODO: manage thrown NoSuchEntityException!!!
		
		return service.getUserByID(id);
	}

	
	
	
	/**
	 * Adds a new user into the repository
	 * @param userName
	 * 			is the display name attributed to the {@code User}
	 * @return
	 * 		an {@code Iterable} over all {@code User}, including the new entry, which Spring will parse into a Json
	 */
	@PostMapping("/add")
	public void addUser (@RequestBody User user)
	{
		service.addUser(user);
	}
	
	
	/**
	 * Adds changes to an {@code User} into the repository
	 * @param userName
	 * 			is the display name attributed to the {@code User}
	 * @return
	 * 		an {@code Iterable} over all {@code User}, including the new state of the changed user, which Spring will parse into a Json
	 */
	@PostMapping("/edit")
	public boolean editUser (@RequestBody User user)
	{
		return service.editUser(user);
	}
	
	
	/**
	 * Removes an {@code User} from the repository
	 * @param id
	 * 		is the unique {@code id} which identifies the {@code User}
	 * @return
	 * 		an {@code Iterable} over all {@code User}, without the deleted {@code User}, which Spring will parse into a Json
	 */
	@PostMapping("/delete")
	public boolean deleteUser (
			@RequestParam(name="id", required=true) int id)
	{
		return service.deleteUser(id);
	}
	
	
	@RequestMapping("/test")
	public String test (@RequestBody TestObject test)
	{
		return test.toString();
	}
	
	
	
	
	
}
