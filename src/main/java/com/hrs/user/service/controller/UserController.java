package com.hrs.user.service.controller;

import com.hrs.user.service.entity.User;
import com.hrs.user.service.service.UserServices;
import com.hrs.user.service.service.UserSreviceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

    @Autowired
    private UserServices userServices;

    private Logger logger = LoggerFactory.getLogger(UserSreviceImpl.class);

    @PostMapping("/saveUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User user1 = userServices.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    int retryCount = 1;

    @GetMapping("userById/{userId}")
//    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        logger.info("Retry Count: {} " + retryCount);
        retryCount ++ ;
        User user = userServices.getUser(userId);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){
        logger.info("Fallback is called because service is down " + ex.getMessage());
        User user = User.builder()
                .name("DummyUser")
                .email("dummy@gmail.com")
                .about("This User is created because some services are down")
                .userId("12345")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> allUser = userServices.getALlUser();
        return ResponseEntity.ok(allUser);
    }

}
