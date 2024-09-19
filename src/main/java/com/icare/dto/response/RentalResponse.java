package com.icare.dto.response;

import com.icare.enums.DeliveryMethod;
import com.icare.enums.RentalStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RentalResponse {
    private Long id;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private Double rentalCost;
    private String deliveryAddress;
    private DeliveryMethod deliveryMethod;
    private Double deliveryCost;
    private DeliveryMethod returnedDeliveryMethod;
    private Double returnedDeliveryCost;
    private RentalStatus status;
    private ProductResponse product;
}
