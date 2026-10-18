package com.example.demo.controller;

import com.example.demo.entity.Restaurant;
import com.example.demo.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Restaurant add(@RequestBody Restaurant r){
      return   repository.save(r);
    }

    @GetMapping("/{id}")
    public Restaurant getone(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow(() ->new RuntimeException("not found"+id) );
    }

    @GetMapping
    public List<Restaurant> findall(){
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public Restaurant update(@RequestBody Restaurant request,
                             @PathVariable Long id){
        Restaurant existing = repository.findById(id)
                .orElseThrow(() ->new RuntimeException("not found"+id) );

        existing.setCity(request.getCity());
        existing.setName(request.getName());

        return repository.save(existing);

    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        repository.deleteById(id);
        return "done";
    }
}
