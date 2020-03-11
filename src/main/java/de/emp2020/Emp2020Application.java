package de.emp2020;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import de.emp2020.users.UserRepo;
import de.emp2020.users.UserService;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import de.emp2020.securityConfig.UsersRepo;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class Emp2020Application implements CommandLineRunner{
	
	@Autowired
	UserRepo UserRepo;

	@Autowired
	UsersRepo UsersRepo;
	
	@Autowired
	UserService UserService;


	@Bean
	public RestTemplate createRestTemplate ()
	{
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(Emp2020Application.class, args);
	}


	
	@Override
	public void run(String... arg0) throws Exception {


		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		

		User user = new User("admin","{noop}" + "password", authorities);
		jdbcUserDetailsManager.createUser(user);

		//public User(String userName, String forename, String surname, Boolean isAdmin, String password)

		de.emp2020.users.User u = new de.emp2020.users.User("admin", "Jonas", "Timmermann", true, "password");

		UserRepo.save(u);

	
	}

}
