package com.example.demo.service;

import com.example.demo.dto.RestaurantReqDto;
import com.example.demo.dto.RestaurantRespDto;
import com.example.demo.entity.Restaurant;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.RestaurantMapper;
import com.example.demo.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class RestaurantService {
    private RestaurantRepository repository;
    private RestaurantMapper mapper;

    public RestaurantService(RestaurantRepository repository, RestaurantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public RestaurantRespDto createRestaurant(RestaurantReqDto request){
       Restaurant restaurant = mapper.mapToEntity(request);
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

    public RestaurantRespDto update(RestaurantReqDto request , Long id){
        Restaurant existing = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("resource not found"));
         mapper.updateEntity(existing,request);
         return mapper.mapToDto(repository.save(existing));
    }

public void deleting(Long id){
        repository.deleteById(id);
}


}


