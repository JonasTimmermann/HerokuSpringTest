package de.emp2020.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	
	/**
	 * The {@code User}-repository
	 */
	@Autowired
	UserRepo repo;
	
	// TODO: make sure all the operations can only be used by administrators!
	
	
	public List<User> getAllUsers() {
		List<User> listUser = new ArrayList<>();
		repo.findAll().forEach(listUser::add);

		return listUser;
	}
	
	
	public User getUserByID(int id){
		// TODO: manage thrown NoSuchEntityException!!!

		return repo.findById(id).orElse(null);
	}
	
	
	public void addUser (User user)
	{
		//TODO: sanitize user object
		repo.save(user);
	}
	
	
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
	
	
}
