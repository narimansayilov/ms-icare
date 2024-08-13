package com.icare.controller;

import com.icare.dto.request.CityRequest;
import com.icare.dto.response.CityResponse;
import com.icare.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addCity(@RequestBody CityRequest request){
        cityService.save(request);
    }

    @GetMapping
    public List<CityResponse> getAllCities(Pageable pageable){
        return cityService.getAllCities(pageable);
    }

    @GetMapping("/{id}")
    public CityResponse getCityById(@PathVariable Long id){
        return cityService.getById(id);
    }

    @PutMapping("/{id}")
    public CityResponse updateCity(@PathVariable Long id, @RequestBody CityRequest request){
        return cityService.editById(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id){
        cityService.deleteById(id);
    }
}