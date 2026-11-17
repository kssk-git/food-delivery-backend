package com.foodapp.mapper;

import com.foodapp.dto.MenuReqDto;
import com.foodapp.dto.MenuRespDto;
import com.foodapp.entity.Menu;
import com.foodapp.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    public Menu mapToEntity(MenuReqDto request, Restaurant restaurant){
        Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        menu.setStock(request.getStock());
        menu.setRestaurant(restaurant);
        return menu;
    }


    public void updateEntity(Menu existing,MenuReqDto request){
        existing.setPrice(request.getPrice());
        existing.setName(request.getName());
        existing.setStock(request.getStock());
    }

public MenuRespDto mapToDto(Menu menu){
    MenuRespDto response = new MenuRespDto();
        response.setName(menu.getName());
        response.setId(menu.getId());
        response.setPrice(menu.getPrice());
        response.setStock(menu.getStock());
        response.setRestaurantId(menu.getRestaurant().getId());
        return response;
}
}
