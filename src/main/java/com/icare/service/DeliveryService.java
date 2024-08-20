package com.icare.service;

import com.icare.client.GoogleMapsClient;
import com.icare.dto.request.DeliveryRequest;
import com.icare.dto.response.DeliveryDetailsResponse;
import com.icare.dto.response.DeliveryLegResponse;
import com.icare.entity.ProductEntity;
import com.icare.enums.DeliveryMethod;
import com.icare.exception.NotFoundException;
import com.icare.mapper.DeliveryMapper;
import com.icare.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final GoogleMapsClient googleMapsClient;
    private final ProductRepository productRepository;

    @Value("${google.maps.api.key}")
    private String apiKey;

    public DeliveryDetailsResponse getDelivery(DeliveryRequest request) {
        return processDelivery(request, true);
    }

    public DeliveryDetailsResponse getReturnedDelivery(DeliveryRequest request) {
        return processDelivery(request, false);
    }

    private DeliveryDetailsResponse processDelivery(DeliveryRequest request, boolean isDelivery) {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND"));

        String startAddress = isDelivery ? product.getAddress() : request.getAddress();
        String endAddress = isDelivery ? request.getAddress() : product.getAddress();

        DeliveryLegResponse leg = getDistance(startAddress, endAddress);
        double price = calculatePrice(request, product, leg.getDistance().getValue());

        return DeliveryMapper.INSTANCE.legToDetails(leg, request.getMethod(), price);
    }

    private double calculatePrice(DeliveryRequest request, ProductEntity product, int distance) {
        if (request.getMethod() == DeliveryMethod.SELLER_DELIVERY) {
            if (!product.getDeliveryAvailable()) {
                throw new NotFoundException("PRODUCT_NOT_AVAILABLE");
            }
            return product.getDeliveryPricePerKm() * distance / 1000.0;
        } else if (request.getMethod() == DeliveryMethod.COMPANY_DELIVERY) {
            return DeliveryMethod.COMPANY_DELIVERY.getPrice() * distance / 1000.0;
        }
        return 0.0;
    }

    private DeliveryLegResponse getDistance(String origin, String destination) {
        String response = googleMapsClient.getDistance(origin, destination, apiKey);
        return DeliveryMapper.extractLegFromResponse(response);
    }
}
