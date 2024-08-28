package com.user.services.impl;

import com.user.entities.Hotel;
import com.user.entities.Rating;
import com.user.entities.User;
import com.user.exceptions.ResourceNotFoundException;
import com.user.external.services.HotelService;
import com.user.repositories.UserRepositorie;
import com.user.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepositorie userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String randomUserID = UUID.randomUUID().toString();
        user.setUserId(randomUserID);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();

        return users;
    }

    @Override
    public User getUser(String userId) {
        User user =  userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given id not found on server. "+ userId));
        //fetch rating of above user from rating service

        Rating[] ratingOfUser =  restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("ratingOfUser "+ratingOfUser);
        List<Rating> ratings = Arrays.stream(ratingOfUser).toList();

        List<Rating> ratingsList = ratings.stream().map(rating->{
            //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            //Hotel body = forEntity.getBody();
            Hotel body = hotelService.getHotel(rating.getHotelId());
            rating.setHotel(body);
            return rating;
        }).collect(Collectors.toList());
        user.setRatingsList(ratingsList);
        return user;
    }

    @Override
    public User updateUser(User user,String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            user.setUserId(id);
            return userRepository.save(user);
            }else{
            throw new ResourceNotFoundException("User with given id not found on server. "+ id);
        }
    }

    @Override
    public boolean delete(String userId) {
        try{
            userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given id not found on server. "+ userId));
            userRepository.deleteById(userId);
            return true;
        }catch (Exception e){
            throw new ResourceNotFoundException("User with given id not found on server. "+ userId);
        }
    }
}
