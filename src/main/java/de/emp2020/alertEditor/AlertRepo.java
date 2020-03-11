package de.emp2020.alertEditor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AlertRepo extends CrudRepository<Alert, Integer>{
	
	
	/**
	 * Find a persistent alert based on the {@code owner}, aka. the user who created the alert in the first place
	 * @param userID
	 * 			the unique user ID of the owner
	 * @return
	 * 			an Iterable over all alerts the specified user created
	 */
	@Query("SELECT a FROM Alert a JOIN a.owner o WHERE o.id = :userID")
	public Iterable<Alert> findByUserId(@Param("userID") int userID);
	
	
	/**
	 * Find a persistent alert based on the {@code owner}, aka. the user who created the alert in the first place
	 * @param targetUserName
	 * 			the potentially not unique name of the user
	 * @return
	 * 			an Iterable over all alerts {@code matching} the specified input string
	 */
	@Query("SELECT a FROM Alert a JOIN a.owner o WHERE o.userName LIKE :userName")
	public Iterable<Alert> findByUserName(@Param("userName") String targetUserName);
	
}
