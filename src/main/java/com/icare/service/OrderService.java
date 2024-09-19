package com.icare.service;

import com.icare.dto.request.OrderRequest;
import com.icare.dto.request.RentalRequest;
import com.icare.dto.response.OrderResponse;
import com.icare.dto.response.RentalResponse;
import com.icare.entity.OrderEntity;
import com.icare.entity.RentalEntity;
import com.icare.entity.UserEntity;
import com.icare.enums.OrderStatus;
import com.icare.exception.NotActiveException;
import com.icare.exception.NotFoundException;
import com.icare.exception.UnauthorizedAccessException;
import com.icare.mapper.OrderMapper;
import com.icare.mapper.RentalMapper;
import com.icare.repository.OrderRepository;
import com.icare.repository.RentalRepository;
import com.icare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final RentalService rentalService;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final ProductService productService;

    @Transactional
    public void addOrder(OrderRequest request){
        UserEntity user = userRepository.findByEmail(userService.getCurrentEmail()).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        if(!user.getStatus()){
            throw new NotActiveException("USER_NOT_ACTIVE");
        }
        OrderEntity entity = OrderMapper.INSTANCE.requestToEntity(user.getId());
        entity = orderRepository.save(entity);
        double amount = 0.0;
        for(RentalRequest rental: request.getRentalRequest()){
            amount += rentalService.addRental(rental, entity.getId());
        }
        entity.setAmount(amount);
        orderRepository.save(entity);
    }

    @Transactional
    public void cancelOrder(Long id){
        OrderEntity order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("ORDER_NOT_FOUND"));
        UserEntity user = userRepository.findByEmail(userService.getCurrentEmail()).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        if(!order.getUser().getId().equals(user.getId())){
            throw new UnauthorizedAccessException("UNAUTHORIZED_ACCESS");
        }
        rentalService.cancelRental(id);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public List<OrderResponse> getOrders(){
        UserEntity user = userRepository.findByEmail(userService.getCurrentEmail()).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        List<OrderEntity> orders = orderRepository.findByUserIdOrderByCreatedAtAsc(user.getId());
        List<OrderResponse> responses = new ArrayList<>();
        for(OrderEntity order : orders){
            List<RentalEntity> rentalEntities = rentalRepository.findByOrderId(order.getId());
            List<RentalResponse> rentalResponses = RentalMapper.INSTANCE.entitiesToResponses(rentalEntities);
            for(RentalResponse rental : rentalResponses){
                rental.setProduct(productService.getProduct(rental.getProduct().getId()));
            }
            responses.add(OrderMapper.INSTANCE.entityToResponse(order,rentalResponses));
        }
        return responses;
    }

    public OrderResponse getOrder(Long id){
        UserEntity user = userRepository.findByEmail(userService.getCurrentEmail()).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        OrderEntity order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("ORDER_NOT_FOUND"));
        if(!order.getUser().getId().equals(user.getId())){
            throw new UnauthorizedAccessException("UNAUTHORIZED_ACCESS");
        }
        List<RentalEntity> rentalEntities = rentalRepository.findByOrderId(order.getId());
        List<RentalResponse> rentalResponses =  RentalMapper.INSTANCE.entitiesToResponses(rentalEntities);
        for(RentalResponse rental : rentalResponses){
            rental.setProduct(productService.getProduct(rental.getProduct().getId()));
        }
        return OrderMapper.INSTANCE.entityToResponse(order, rentalResponses);
    }
}
