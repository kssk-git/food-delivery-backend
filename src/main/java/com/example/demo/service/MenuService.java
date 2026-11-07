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
import com.example.demo.security.OwnershipChecker;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MenuService {
    private MenuRepository repository;
    private MenuMapper mapper;
    private RestaurantRepository restaurantRepository;
    private OwnershipChecker ownershipChecker;
    public MenuService(MenuRepository repository, MenuMapper mapper,RestaurantRepository restaurantRepository,OwnershipChecker ownershipChecker) {
        this.repository = repository;
        this.mapper = mapper;
        this.restaurantRepository=restaurantRepository;
        this.ownershipChecker= ownershipChecker;
    }



    public MenuRespDto add(MenuReqDto request, Long restaurantId, Authentication auth) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + restaurantId));
        ownershipChecker.check(restaurant, auth);    // new line
        Menu saved = repository.save(mapper.mapToEntity(request, restaurant));
        return mapper.mapToDto(saved);
    }

    public MenuRespDto update(MenuReqDto request, Long id, Long restaurantId, Authentication auth) {
        Menu existing = findMenu(id, restaurantId);
        ownershipChecker.check(existing.getRestaurant(), auth);   // new line
        mapper.updateEntity(existing, request);
        return mapper.mapToDto(repository.save(existing));
    }

    public void delete(Long id, Long restaurantId, Authentication auth) {
        Menu existing = findMenu(id, restaurantId);
        ownershipChecker.check(existing.getRestaurant(), auth);   // new line
        repository.delete(existing);
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



    private Menu findMenu(Long id,Long restaurantId){
        return repository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("menu item not found"+id));
    }

}
