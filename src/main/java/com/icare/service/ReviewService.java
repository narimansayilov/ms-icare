package com.icare.service;

import com.icare.dto.request.ReviewRequest;
import com.icare.entity.*;
import com.icare.exception.NotFoundException;
import com.icare.exception.ReviewAlreadyExistsException;
import com.icare.exception.UnauthorizedAccessException;
import com.icare.mapper.ReviewMapper;
import com.icare.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        double totalRating = reviewRepository.findAllByProductId(product.getId())
                                                        .stream()
                                                        .mapToDouble(ReviewEntity::getRating)
                                                        .average()
                                                        .orElse(0.0);
        rental.setReviewWritten(true);
        rentalRepository.save(rental);

//        double totalRating = (product.getTotalRating() * product.getReviewCount() + request.getRating()) / (product.getReviewCount() + 1);

        product.setTotalRating(totalRating);
        product.setReviewCount(product.getReviewCount() + 1);
        productRepository.save(product);
    }
}
