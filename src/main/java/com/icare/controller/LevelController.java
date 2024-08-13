package com.icare.controller;

import com.icare.dto.request.LevelRequest;
import com.icare.dto.response.LevelResponse;
import com.icare.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/levels")
public class LevelController {
    private final LevelService levelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addLevel(@RequestBody LevelRequest request) {
        levelService.addLevel(request);
    }

    @GetMapping
    public List<LevelResponse> getAllLevels(Pageable pageable) {
        return levelService.getAllLevels(pageable);
    }

    @GetMapping("/{id}")
    public LevelResponse getLevel(@PathVariable Long id) {
        return levelService.getLevel(id);
    }

    @PutMapping("/{id}")
    public LevelResponse updateLevel(@PathVariable Long id, @RequestBody LevelRequest request) {
        return levelService.updateLevel(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteLevel(@PathVariable Long id) {
        levelService.deleteLevel(id);
    }
}