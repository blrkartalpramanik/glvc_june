package com.hrs.user.service.service;

import com.hrs.user.service.entity.Hotel;
import com.hrs.user.service.entity.Rating;
import com.hrs.user.service.entity.User;
import com.hrs.user.service.exception.ResourceNotFoundException;
import com.hrs.user.service.external.services.HotelService;
import com.hrs.user.service.external.services.RatingService;
import com.hrs.user.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserSreviceImpl implements UserServices{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RatingService ratingService;
    private Logger logger = LoggerFactory.getLogger(UserSreviceImpl.class);


    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getALlUser() {
        List<User> allUser = userRepository.findAll();
        for (User user : allUser){
            List<Rating> ratingsOfUser = ratingService.getRatings(user.getUserId());
//                    restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
//            List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
            List<Rating> ratingList = ratingsOfUser.stream().map(rating -> {
                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                rating.setHotel(hotel);
                return rating;
            }).collect(Collectors.toList());
            user.setRatings(ratingList);
        }
        return allUser;
    }

    @Override
    public User getUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with given Id is not found on server!! : "+userId));

        List<Rating> ratingsOfUser = ratingService.getRatings(user.getUserId());
//        Rating[] ratingsForUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
//        List<Rating> ratings = Arrays.stream(ratingsForUser).toList();

        List<Rating> ratingList = ratingsOfUser.stream().map(rating -> {
//            ResponseEntity<Hotel> hotelDetail = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        logger.info("{}", ratingsOfUser);

        return user;
    }
}
