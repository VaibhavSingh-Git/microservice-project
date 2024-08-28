package com.user.controllers;

import com.user.entities.User;
import com.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

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
    public ResponseEntity<User> getUserByUserId(@PathVariable String userId){
        User user = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
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
