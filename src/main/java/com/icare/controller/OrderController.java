package com.icare.controller;

import com.icare.dto.request.OrderRequest;
import com.icare.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public void addOrder(@RequestBody OrderRequest request) {
        orderService.addOrder(request);
    }

    @PutMapping("/{id}/cancel")
    public void cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }
}
