package com.icare.dto.response;

import com.icare.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Double amount;
    private OrderStatus status;
    private List<RentalResponse> rentals;
}
