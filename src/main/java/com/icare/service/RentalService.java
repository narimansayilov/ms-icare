package com.icare.service;

import com.icare.dto.request.DeliveryRequest;
import com.icare.dto.request.RentalRequest;
import com.icare.dto.response.DeliveryDetailsResponse;
import com.icare.entity.ProductEntity;
import com.icare.entity.RentalEntity;
import com.icare.enums.DeliveryMethod;
import com.icare.enums.RentalStatus;
import com.icare.exception.NotFoundException;
import com.icare.mapper.RentalMapper;
import com.icare.repository.ProductRepository;
import com.icare.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.icare.enums.RentalStatus.*;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final DeliveryService deliveryService;
    private final ProductRepository productRepository;

    public double addRental(RentalRequest request, Long orderId) {
        double totalPrice = 0.0;
        ProductEntity product = productRepository.findById(request.getProductId()).orElseThrow(() ->
                new NotFoundException("PRODUCT_NOT_FOUND"));

        long rentalDays = ChronoUnit.DAYS.between(request.getRentalStartDate(), request.getRentalEndDate());
        double rentalCost = product.getPricePerDay() * rentalDays;
        totalPrice += rentalCost;

        RentalEntity entity = RentalMapper.INSTANCE.requestToEntity(request, orderId);

        double deliveryCost = calculateDeliveryCost(request.getDeliveryMethod(), request.getDeliveryAddress(), request.getProductId(), true);
        entity.setDeliveryCost(deliveryCost);
        totalPrice += deliveryCost;

        double returnedDeliveryCost = calculateDeliveryCost(request.getReturnedDeliveryMethod(), request.getDeliveryAddress(), request.getProductId(), false);
        entity.setReturnedDeliveryCost(returnedDeliveryCost);
        totalPrice += returnedDeliveryCost;

        entity.setRentalConst(rentalCost);
        rentalRepository.save(entity);

        product.setRentalCount(product.getRentalCount() + 1);
        productRepository.save(product);
        return totalPrice;
    }

    public void cancelRental(Long orderId) {
        List<RentalEntity> rentals = rentalRepository.findByOrderId(orderId);
        rentals.forEach(rental -> rental.setStatus(RentalStatus.CANCELLED));
        rentalRepository.saveAll(rentals);
    }


    private double calculateDeliveryCost(DeliveryMethod method, String address, Long productId, boolean isDelivery) {
        if (method == DeliveryMethod.PICKUP) {
            return DeliveryMethod.PICKUP.getPrice();
        } else {
            DeliveryRequest deliveryRequest = DeliveryRequest.builder()
                    .address(address)
                    .productId(productId)
                    .method(method)
                    .build();

            DeliveryDetailsResponse deliveryResponse = isDelivery ?
                    deliveryService.getDelivery(deliveryRequest) :
                    deliveryService.getReturnedDelivery(deliveryRequest);

            return deliveryResponse.getAmount();
        }
    }
}
