package com.icare.dto.request;

import com.icare.enums.DeliveryMethod;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RentalRequest {
    private Long productId;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private String deliveryAddress;
    private DeliveryMethod deliveryMethod;
    private DeliveryMethod returnedDeliveryMethod;
}
