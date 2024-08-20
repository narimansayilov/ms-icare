package com.icare.enums;

import lombok.Getter;

@Getter
public enum RentalStatus {
    ORDER_PLACED,
    PREPARING,
    ON_THE_WAY,
    DELIVERED,
    RECEIVED,
    RETURNED,
    CANCELLED
}
