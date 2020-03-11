package de.emp2020.securityConfig;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Order(99)
@Configuration
@EnableWebSecurity
public class EmployeeSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;


	// Enable jdbc authentication
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource);
	}

	
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
		return jdbcUserDetailsManager;
	}

	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		 // working local
		http.cors().and().csrf().disable();
		//http.csrf().disable().cors().configurationSource(corsConfigurationSource());
		http.authorizeRequests()
				.antMatchers("/user/login").permitAll()
				.antMatchers("/user/register").permitAll()
				.antMatchers("/welcome").permitAll()
				.antMatchers("/hello").hasAnyRole("ADMIN")
				.antMatchers("/moin").hasRole("USER")
				.antMatchers("/exporter:8083/info").permitAll()
				.antMatchers("/exporter:8083/config").permitAll()
				.antMatchers("/app/**").permitAll()
				.antMatchers("/alerts/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/config/**").hasAnyRole("ADMIN")
				.antMatchers("/places/**").hasAnyRole("ADMIN")
				.antMatchers("/users/**").hasAnyRole("ADMIN")
				.anyRequest().authenticated();
	}
}			


//.authorizeRequests()
/** 
				//http.csrf().disable().antMatcher("/**")
				http.csrf().disable().cors().configurationSource(corsConfigurationSource()).and().antMatcher("/**")
		.authorizeRequests().antMatchers("/test").permitAll().antMatchers("/helloworld/get").permitAll()
		.antMatchers("/welcome").hasRole("ADMIN")
		.antMatchers("hello").hasRole("USER")
		.anyRequest()
				.authenticated().and()
				.httpBasic();
**/

				//.and().formLogin()
				//.loginPage("/login").permitAll().and().logout().permitAll();

/** 

		http.csrf().disable().cors().configurationSource(corsConfigurationSource()).and().antMatcher("/**").authorizeRequests().antMatchers("/fragen").permitAll().antMatchers("/fragen/{id}").permitAll()
		.antMatchers("/type/{formType}").permitAll().antMatchers("/category/all").permitAll()
		.antMatchers("/type/{formType}/category/{category}").permitAll().antMatchers("/type/{form_id}/category/{category_id}/findstart").permitAll()
		.antMatchers("/category/{category}").permitAll().antMatchers("/frage/{id}/choices").permitAll()
		.antMatchers("/filled/forms/all").permitAll().antMatchers("/filled/forms/{form_id}").permitAll()
		.antMatchers("/filled/forms/add").permitAll().antMatchers("/filled/forms/{form_id}/update").permitAll() 
		.antMatchers("/filled/forms/{form_id}/delete").permitAll().antMatchers("/filled/forms/{form_id}").permitAll()
		.antMatchers("/forms/all").permitAll().antMatchers("/downloadFile/{fileId:.+}").permitAll()
		.antMatchers("/uploadFile/{answerId}").permitAll().antMatchers("/uploadMultipleFiles/{answerId}").permitAll()
		.antMatchers("/uploadFile/{id}/{answerId}/update").permitAll().antMatchers("/forms/{form_id}/question/{question_id}/answers/add").permitAll()
		.antMatchers("/forms/{form_id}/question/{question_id}/answers/{answers_id}/edit").permitAll().antMatchers("/forms/{form_id}/question/{question_id}/answers").permitAll()
		.antMatchers("/forms/{form_id}/answers/all/delete").permitAll().antMatchers("/forms/{form_id}/answers/all/add").permitAll()
		.antMatchers("/forms/{form_id}/answers/all/edit").permitAll().antMatchers("/form/{form_id}/category/{category_id}/findstart").permitAll()
		.antMatchers("/findAllStarts").permitAll()
		//.antMatchers("/api/v1/basicauth").permitAll()
		.anyRequest()
				.authenticated().and()
				.httpBasic();
				**/
	 /** 
	@Autowired
	 public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
		authenticationMgr.inMemoryAuthentication().withUser("admin").password("admin").authorities("ROLE_USER")
		.and().withUser("jonas").password("password").authorities("ROLE_ADMIN");
	 }
	 **/
	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder authenticationMgr)
	// throws Exception {
	// authenticationMgr.inMemoryAuthentication().withUser("admin").password("admin").authorities("ROLE_USER").and()
	// .withUser("javainuse").password("javainuse").authorities("ROLE_USER",
	// "ROLE_ADMIN");
	// }
