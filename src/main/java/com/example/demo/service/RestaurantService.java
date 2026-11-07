package com.example.demo.service;

import com.example.demo.dto.RestaurantReqDto;
import com.example.demo.dto.RestaurantRespDto;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.RestaurantMapper;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.OwnershipChecker;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class RestaurantService {
    private RestaurantRepository repository;
    private RestaurantMapper mapper;
    private final UserRepository userRepository;
    private final OwnershipChecker ownershipChecker;

    public RestaurantService(RestaurantRepository repository, RestaurantMapper mapper,
                             UserRepository userRepository,
                             OwnershipChecker ownershipChecker) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.ownershipChecker = ownershipChecker;
    }


    public RestaurantRespDto createRestaurant(RestaurantReqDto request, String ownerEmail){
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
       Restaurant restaurant = mapper.mapToEntity(request);
       restaurant.setOwner(owner);
       Restaurant response = repository.save(restaurant);
       return mapper.mapToDto(response);
    }

    public RestaurantRespDto getRestaurant(Long id){
        Restaurant response = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("resource not found"));
        return mapper.mapToDto(response);
    }

    public List<RestaurantRespDto> getall(){
        //Restaurant response = repository.findAll();
        return repository.findAll().stream()
                .map(mapper::mapToDto)
                .toList();

    }

    public RestaurantRespDto update(RestaurantReqDto request , Long id, Authentication auth){
        Restaurant existing = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("resource not found"));
         ownershipChecker.check(existing,auth);
        mapper.updateEntity(existing,request);
         return mapper.mapToDto(repository.save(existing));
    }

public void deleting(Long id,Authentication auth){
        Restaurant existing = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
         ownershipChecker.check(existing,auth);
        repository.deleteById(id);
}


}


