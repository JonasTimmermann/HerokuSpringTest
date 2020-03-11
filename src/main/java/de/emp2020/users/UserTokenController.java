package de.emp2020.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
    
@CrossOrigin
@RestController
public class UserTokenController {

    @Autowired
    private UserTokenService userTokenService;


    public UserTokenController(UserTokenService userTokenService){
        this.userTokenService = userTokenService;
    }
    
 
 

    ///config/all  URLs
    //  PUT /config/edit


// Get all Places
    @RequestMapping(path = "/users/token/all")
    public List<UserToken> getAllUserToken(){

        return userTokenService.getAllUserToken();
    }

// Get a single place with id
   @RequestMapping(path = "/users/token/{id}")
   public UserToken getUserTokenById(@PathVariable Integer id){
       
       return userTokenService.getUserTokenByID(id);
   }

// Add a new Place to the Repository
   @RequestMapping(method = RequestMethod.POST, value = "/users/token/add")
   public String saveUserToken(@RequestBody UserToken token){

        userTokenService.saveUserToken(token);

       return "Ok";
   }


   
/** 
// Edit a single place with id
    @RequestMapping(method = RequestMethod.PUT, value = "/place/{id}/edit")
    public String editPlace(@RequestBody Place place) {
       
        placeService.editPlace(place);

        return "Ok";
    }

**/
    
}
