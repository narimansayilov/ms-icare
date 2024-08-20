package com.icare.dto.response;

import com.icare.enums.DeliveryMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetailsResponse {
    private DeliveryMethod method;
    private Double amount;
    private DeliveryLegResponse leg;
}
