package de.emp2020.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {
	
	
	/**
	 * The {@code User}-repository
	 */
	@Autowired
	UserTokenRepository userTokenRepo;




	public UserToken getUserTokenByToken(String token){

		return userTokenRepo.findByUserToken(token);
	}
	



	
	
	// TODO: make sure all the operations can only be used by administrators!
	
	
	public List<UserToken> getAllUserToken() {
		List<UserToken> listToken = new ArrayList<>();
		userTokenRepo.findAll().forEach(listToken::add);

		return listToken;
	}
	
	
	public UserToken getUserTokenByID(Integer id){
		// TODO: manage thrown NoSuchEntityException!!!

		return userTokenRepo.findById(id).orElse(null);
	}
	

	public void saveUserToken (UserToken token)
	{
		//TODO: sanitize user object
		userTokenRepo.save(token);
	}
	
	/** 
	public boolean editUser (User user)
	{
		//TODO: sanitize user object
		repo.save(user);
		return true;
	}
	
	
	public boolean deleteUser (int id)
	{
		repo.deleteById(id);
		return true;
	}
	
	**/
}
