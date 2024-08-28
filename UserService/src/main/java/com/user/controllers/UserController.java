package com.user.controllers;

import com.user.entities.User;
import com.user.services.UserService;
import com.user.services.impl.UserServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    //create user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User usr = userService.saveUser(user);
        return  ResponseEntity.status(HttpStatus.OK).body(usr);
    }

    //get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> userList = userService.getAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    //get user by Id
    @GetMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUserByUserId(@PathVariable String userId){
        User user = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    //creating fallback method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
        logger.info("Fallback executed because service is down", ex.getMessage());
        User user = User.builder().email("dummy email").name("dummy name").about("dummy user for fallback").build();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    @PutMapping("/{userId}")
    public  ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String userId){
        User usr = userService.updateUser(user,userId);
        return  ResponseEntity.status(HttpStatus.OK).body(usr);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId){
        Boolean isDeleted = userService.delete(userId);
        if(isDeleted){
            return ResponseEntity.status(HttpStatus.OK).body("Deleted successfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }
    }

}
