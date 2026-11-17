package com.foodapp.service;

import com.foodapp.dto.PageRespDto;
import com.foodapp.dto.RestaurantReqDto;
import com.foodapp.dto.RestaurantRespDto;
import com.foodapp.entity.Restaurant;
import com.foodapp.entity.User;
import com.foodapp.exception.ResourceNotFoundException;
import com.foodapp.mapper.RestaurantMapper;
import com.foodapp.repository.RestaurantRepository;
import com.foodapp.repository.UserRepository;
import com.foodapp.security.OwnershipChecker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

    public PageRespDto<RestaurantRespDto> getAll(String city, int page, int size, String sortBy) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, Sort.by(sortBy));

        Page<Restaurant> result = (city == null || city.isBlank())
                ? repository.findAll(pageable)
                : repository.findByCityIgnoreCase(city, pageable);

        PageRespDto<RestaurantRespDto> response = new PageRespDto<>();
        response.setContent(result.getContent().stream().map(mapper::mapToDto).toList());
        response.setPage(result.getNumber());
        response.setSize(result.getSize());
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());
        return response;
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


