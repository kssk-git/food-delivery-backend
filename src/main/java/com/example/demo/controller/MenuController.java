package com.example.demo.controller;

import com.example.demo.dto.MenuReqDto;
import com.example.demo.dto.MenuRespDto;
import com.example.demo.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu")
public class MenuController {
    private MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public MenuRespDto add(@PathVariable Long restaurantId,
                           @Valid @RequestBody MenuReqDto request,
                           Authentication authentication) {
        return menuService.add(request, restaurantId, authentication);
    }

    @PutMapping("/{id}")
    public MenuRespDto update(@PathVariable Long restaurantId,
                              @PathVariable Long id,
                              @Valid @RequestBody MenuReqDto request,
                              Authentication authentication) {
        return menuService.update(request, id, restaurantId, authentication);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long restaurantId,
                         @PathVariable Long id,
                         Authentication authentication) {
        menuService.delete(id, restaurantId, authentication);
        return "done";
    }

    @GetMapping
    public List<MenuRespDto> findall(@PathVariable Long restaurantId){
        return menuService.getAll(restaurantId);
    }
}
