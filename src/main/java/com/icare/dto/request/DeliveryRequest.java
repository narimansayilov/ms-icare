package com.icare.dto.request;

import com.icare.enums.DeliveryMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeliveryRequest {
    private Long productId;
    private DeliveryMethod method;
    private String address;
}
