package de.emp2020.securityConfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.emp2020.users.UserRepo;
import de.emp2020.users.UserService;



@CrossOrigin    
@RestController
public class EmployeeController {   //extends JdbcDaoSupport


	

	@Autowired
	UserRepo UserRepo;

	@Autowired
	UsersRepo UsersRepo;
	
	@Autowired
	UserService UserService;



	@Autowired
	JdbcUserDetailsManager jdbcUserDetailsManager;

	@RequestMapping("/welcome")
	public String firstPage() {
		return "Moin welcome";
	}


	@RequestMapping(path = "/hello")
	public String hello() {
		return "Hello you!";
	}
 
	
	@RequestMapping(path = "/moin")
	public String hello1() {
		return "Hello you!1";
	}
/** 
	@RequestMapping(path = "/hello2")
	public String hello2() {
		return "Hello you!2";
	}
	
	@RequestMapping(path = "/hello3")
	public String hello3() {
		return "Hello you!3";
	}
**/

    
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public String processRegister(@RequestBody UserRegistration userRegistrationObject) {
		
	
		
		List<Users> usersList= new ArrayList<>();
		UsersRepo.findAll().forEach(usersList::add);


		for(int i = 0; i < usersList.size(); i++){
			if(usersList.get(i).getUsername().equals(userRegistrationObject.getUserName())){
				return "UserName already taken";
			}
		}

		/** 
		List<de.emp2020.users.User> userList= UserService.getAllUsers();

		for(int i = 0; i < userList.size(); i++){
			if(userList.get(i).getUserName().equals(userRegistrationObject.getUserName())){
				return "UserName already taken";
			}
		}
		**/

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if(userRegistrationObject.getIsAdmin()){
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}else{
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}


		User user = new User(userRegistrationObject.getUserName(),"{noop}" + userRegistrationObject.getPassword(), authorities);
		jdbcUserDetailsManager.createUser(user);

		//public User(String userName, String forename, String surname, Boolean isAdmin, String password)

		de.emp2020.users.User u = new de.emp2020.users.User(userRegistrationObject.getUserName(), userRegistrationObject.getForename(), userRegistrationObject.getSurname(), userRegistrationObject.getIsAdmin(), userRegistrationObject.getPassword());

		UserRepo.save(u);

		return "Register success";
	}




	@RequestMapping(value = "/user/login", method = RequestMethod.GET)
	public String login() {

		return "Login";
	}


	












   
}
