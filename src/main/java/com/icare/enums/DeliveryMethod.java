package com.icare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryMethod {
    PICKUP(0.0),
    SELLER_DELIVERY(1.0),
    COMPANY_DELIVERY(2.0),;
    private final Double price;
}
