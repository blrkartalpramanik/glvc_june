package com.hrs.user.service.external.services;

import com.hrs.user.service.entity.Hotel;
import com.hrs.user.service.entity.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "RATING-SERVICE")
public interface RatingService {

    @GetMapping("/ratings/users/{userId}")
    List<Rating> getRatings(@PathVariable("userId") String userId);

    @PostMapping("/ratings")
    public Rating createRating(Rating rating);

//    public Rating updateRating();
}
