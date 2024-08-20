package com.icare.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-maps-client", url = "https://maps.googleapis.com/maps/api/directions/json")
public interface GoogleMapsClient {

    @GetMapping
    String getDistance(@RequestParam("origin") String origin,
                       @RequestParam("destination") String destination,
                       @RequestParam("key") String apiKey);
}

