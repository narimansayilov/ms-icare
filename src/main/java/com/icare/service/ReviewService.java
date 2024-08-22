package com.icare.service;

import com.icare.dto.request.ReviewRequest;
import com.icare.dto.response.ReviewResponse;
import com.icare.dto.response.UserResponse;
import com.icare.entity.*;
import com.icare.exception.NotActiveException;
import com.icare.exception.NotFoundException;
import com.icare.exception.ReviewAlreadyExistsException;
import com.icare.exception.UnauthorizedAccessException;
import com.icare.mapper.ReviewMapper;
import com.icare.mapper.UserMapper;
import com.icare.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.icare.enums.RentalStatus.RECEIVED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addReview(ReviewRequest request) {
        RentalEntity rental = rentalRepository.findById(request.getRentalId()).orElseThrow(()
                -> new NotFoundException("RENTAL_NOT_FOUND"));
        if(!(rental.getStatus() == RECEIVED)){
            throw new RuntimeException("CAN'T_REVIEWED");
        }
        UserEntity user = userRepository.findByEmail(userService.getCurrentEmail()).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        OrderEntity order = orderRepository.findById(rental.getOrder().getId()).orElseThrow(()
                -> new NotFoundException("ORDER_NOT_FOUND"));
        if(!Objects.equals(order.getUser().getId(), user.getId())){
            throw new UnauthorizedAccessException("UNAUTHORIZED_ACCESS");
        }
        if(rental.getReviewWritten()){
            throw new ReviewAlreadyExistsException("REVIEW_HAS_BEEN_WRITTEN");
        }
        ProductEntity product = productRepository.findById(rental.getProduct().getId()).orElseThrow(()
                -> new NotFoundException("PRODUCT_NOT_FOUND"));

        ReviewEntity entity = ReviewMapper.INSTANCE.requestToEntity(request, user.getId(), product.getId());
        reviewRepository.save(entity);

        rental.setReviewWritten(true);
        rentalRepository.save(rental);

//        double totalRating = (product.getTotalRating() * product.getReviewCount() + request.getRating()) / (product.getReviewCount() + 1);

        product.setTotalRating(calculateRating(product.getId()));
        product.setReviewCount(product.getReviewCount() + 1);
        productRepository.save(product);
    }

    public List<ReviewResponse> getProductReviews(Long productId, Pageable pageable) {
        Page<ReviewEntity> entities = reviewRepository.findAllByProductIdAndStatus(productId, pageable, true);
        List<ReviewResponse> responses = new ArrayList<>();
        for(ReviewEntity review : entities){
            UserEntity userEntity = userRepository.findById(review.getUser().getId()).orElseThrow(() ->
                    new NotFoundException("USER_NOT_FOUND"));
            UserResponse userResponse = UserMapper.INSTANCE.entityToResponse(userEntity);
            ReviewResponse response = ReviewMapper.INSTANCE.entityToResponse(review);
            response.setUser(userResponse);
            responses.add(response);
        }
        return responses;
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
        ReviewEntity entity = reviewRepository.findById(reviewId).orElseThrow(() ->
                new NotFoundException("REVIEW_NOT_FOUND"));
        ReviewMapper.INSTANCE.mapRequestToEntity(entity, request);
        reviewRepository.save(entity);
        ProductEntity product = productRepository.findById(entity.getProduct().getId()).orElseThrow(()
                -> new NotFoundException("PRODUCT_NOT_FOUND"));

        product.setTotalRating(calculateRating(product.getId()));
        productRepository.save(product);

        return ReviewMapper.INSTANCE.entityToResponse(entity);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        ReviewEntity entity = reviewRepository.findById(reviewId).orElseThrow(() ->
                new NotFoundException("REVIEW_NOT_FOUND"));
        if(!entity.getUser().getEmail().equals(userService.getCurrentEmail())){
            throw new UnauthorizedAccessException("UNAUTHORIZED_ACCESS");
        }
        if(!entity.getStatus()){
            throw new NotActiveException("REVIEW_NOT_ACTIVE");
        }
        entity.setStatus(false);
        reviewRepository.save(entity);

        ProductEntity product = productRepository.findById(entity.getProduct().getId()).orElseThrow(()
                -> new NotFoundException("PRODUCT_NOT_FOUND"));

        product.setTotalRating(calculateRating(product.getId()));
        product.setReviewCount(product.getReviewCount() - 1);
        productRepository.save(product);
    }

    private Double calculateRating(Long productId){
        return reviewRepository.findAllByProductIdAndStatus(productId, true)
                .stream()
                .mapToDouble(ReviewEntity::getRating)
                .average()
                .orElse(0.0);
    }
}
