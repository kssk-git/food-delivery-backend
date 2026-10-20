package com.example.demo.controller;

import com.example.demo.dto.MenuReqDto;
import com.example.demo.dto.MenuRespDto;
import com.example.demo.service.MenuService;
import jakarta.validation.Valid;
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
    public MenuRespDto add(@Valid @RequestBody MenuReqDto request
    ,@PathVariable Long restaurantId){
        return  menuService.add(request, restaurantId);
    }

    @GetMapping
    public List<MenuRespDto> findall(@PathVariable Long restaurantId){
        return menuService.getAll(restaurantId);
    }

    @PutMapping("/{id}")
    public MenuRespDto update(@Valid @RequestBody MenuReqDto request,
                              @PathVariable Long id
            ,@PathVariable Long restaurantId){
        return menuService.update(request,id,restaurantId);

    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id
    ,@PathVariable Long restaurantId){
        menuService.deleting(id,restaurantId);
        return "done";
    }
}
