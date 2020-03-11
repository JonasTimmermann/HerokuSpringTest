package de.emp2020.users;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends CrudRepository<User, Integer> {
	

	@Query("SELECT u FROM User u WHERE u.userName = :userName")
	public User findByUserName(@Param("userName") String targetUserName);

	@Modifying
	@Query("DELETE FROM UserToken t WHERE t.token = :token")
	public void deleteUserToken(@Param("token") String token);
	
	
	@Query("SELECT u FROM User u JOIN u.tokens t WHERE t.token = :token")
	public User findByUserToken(@Param("token") String token);
	
	
	@Query("SELECT u FROM User u WHERE u.userName IN :userNames")
	public List<User> findUsersByNames(@Param("userNames") List<String> userNames);
	
}
