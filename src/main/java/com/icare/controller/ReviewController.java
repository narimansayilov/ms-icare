package com.icare.controller;

import com.icare.dto.request.ReviewRequest;
import com.icare.dto.response.ReviewResponse;
import com.icare.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@RequestBody ReviewRequest request){
        reviewService.addReview(request);
    }

    @GetMapping("/{id}")
    public List<ReviewResponse> getProductReviews(@PathVariable Long id, Pageable pageable){
        return reviewService.getProductReviews(id, pageable);
    }

    @PutMapping("/{id}")
    public ReviewResponse updateReview(@PathVariable Long id, @RequestBody ReviewRequest request){
        return reviewService.updateReview(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
    }
}
