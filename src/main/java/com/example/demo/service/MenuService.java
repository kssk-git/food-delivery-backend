package com.example.demo.service;

import com.example.demo.dto.MenuReqDto;
import com.example.demo.dto.MenuRespDto;
import com.example.demo.dto.RestaurantRespDto;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Restaurant;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MenuService {
    private MenuRepository repository;
    private MenuMapper mapper;
    private RestaurantRepository restaurantRepository;
    public MenuService(MenuRepository repository, MenuMapper mapper,RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.restaurantRepository=restaurantRepository;
    }


    public MenuRespDto add(MenuReqDto request,Long restaurandId){
        Restaurant restaurant=restaurantRepository.findById(restaurandId).orElseThrow(() -> new ResourceNotFoundException("resource not found"));
        Menu menu = mapper.mapToEntity(request,restaurant);
        Menu response = repository.save(menu);
        return mapper.mapToDto(response);
    }

    public MenuRespDto getRestaurant(Long id){
        Menu response = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("resource not found"));
        return mapper.mapToDto(response);
    }

    public List<MenuRespDto> getAll(Long restaurantId){
     if(!restaurantRepository.existsById(restaurantId)){
         throw new ResourceNotFoundException("Restaurant not found:"+restaurantId);
     }
        return repository.findAll().stream()
                .map(mapper::mapToDto)
                .toList();

    }

    public MenuRespDto update(MenuReqDto request , Long id,Long restaurantId){
        Menu existing = findMenu(id,restaurantId);
        mapper.updateEntity(existing,request);
        return mapper.mapToDto(repository.save(existing));
    }

    public void deleting(Long id,Long restaurantId){
        repository.delete(findMenu(id,restaurantId));
    }

    private Menu findMenu(Long id,Long restaurantId){
        return repository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("menu item not found"+id));
    }

}
