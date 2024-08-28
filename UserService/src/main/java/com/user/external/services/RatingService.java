package com.user.external.services;

import com.user.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "RATINGSERVICE")
public interface RatingService {
    @PostMapping("/ratings")
    Rating createRating(Rating rating);
}
