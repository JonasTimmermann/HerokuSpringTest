package de.emp2020.users;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserTokenRepository extends CrudRepository<UserToken, Integer> {
    
    @Query("SELECT u FROM UserToken u WHERE u.token = :token")
	public UserToken findByUserToken(@Param("token") String userToken);

}
