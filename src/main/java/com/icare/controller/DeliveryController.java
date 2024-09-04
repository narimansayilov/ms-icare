package com.icare.controller;

import com.icare.dto.request.DeliveryRequest;
import com.icare.dto.response.DeliveryDetailsResponse;
import com.icare.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping
    public DeliveryDetailsResponse calculateDelivery(@RequestBody DeliveryRequest request) {
        return deliveryService.getDelivery(request);
    }

    @PostMapping("/returned")
    public DeliveryDetailsResponse calculateReturnedDelivery(@RequestBody DeliveryRequest request) {
        return deliveryService.getReturnedDelivery(request);
    }
}