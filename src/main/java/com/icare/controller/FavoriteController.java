package com.icare.controller;

import com.icare.dto.response.FavoriteResponse;
import com.icare.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addFavorite(@RequestParam(name = "product") Long id){
        favoriteService.addFavorite(id);
    }

    @GetMapping
    public List<FavoriteResponse> allFavorites(Pageable pageable){
        return favoriteService.getFavorite(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteFavorite(@PathVariable Long id){
        favoriteService.deleteFavorite(id);
    }
}
