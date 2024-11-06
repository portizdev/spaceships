package com.portizdev.spaceships.service;

import com.portizdev.spaceships.model.Spaceship;
import com.portizdev.spaceships.repository.SpaceshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceshipService {
    
    @Autowired
    private SpaceshipRepository repository;

    @Cacheable("spaceships")
    public Page<Spaceship> getAllSpaceships(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Cacheable(value = "spaceship", key = "#id", unless = "#id < 0")
    public Spaceship getSpaceshipById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Spaceship> searchSpaceshipsByName(String name) {
        return repository.findByNameContaining(name);
    }

    public Spaceship createSpaceship(Spaceship spaceship) {
        return repository.save(spaceship);
    }

    public Spaceship updateSpaceship(Long id, Spaceship spaceship) {
        spaceship.setId(id);
        return repository.save(spaceship);
    }

    public void deleteSpaceship(Long id) {
        repository.deleteById(id);
    }
}
