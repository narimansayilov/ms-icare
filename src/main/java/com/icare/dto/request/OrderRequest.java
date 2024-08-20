package com.icare.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private List<RentalRequest> rentalRequest;
}
