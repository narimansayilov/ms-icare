package com.icare.service;

import com.icare.dto.request.OrderRequest;
import com.icare.dto.request.RentalRequest;
import com.icare.entity.OrderEntity;
import com.icare.entity.UserEntity;
import com.icare.enums.OrderStatus;
import com.icare.exception.NotActiveException;
import com.icare.exception.NotFoundException;
import com.icare.mapper.OrderMapper;
import com.icare.repository.OrderRepository;
import com.icare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final RentalService rentalService;
    private final UserRepository userRepository;

    @Transactional
    public void addOrder(OrderRequest request){
        UserEntity user = userRepository.findByEmail(userService.getCurrentUsername()).orElseThrow(() ->
                new NotFoundException("USER_NOT_FOUND"));
        if(!user.getStatus()){
            throw new NotActiveException("USER_NOT_ACTIVE");
        }
        OrderEntity entity = OrderMapper.INSTANCE.requestToEntity(user, OrderStatus.PAID);
        entity = orderRepository.save(entity);
        double amount = 0.0;
        for(RentalRequest rental: request.getRentalRequest()){
            amount += rentalService.addRental(rental, entity.getId());
        }
        entity.setAmount(amount);
        orderRepository.save(entity);
    }
}
