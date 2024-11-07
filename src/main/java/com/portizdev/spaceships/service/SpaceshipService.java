package com.portizdev.spaceships.service;

import com.portizdev.spaceships.kafka.SpaceshipProducer;
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

    @Autowired
    private SpaceshipProducer producer;

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
        Spaceship savedSpaceship = repository.save(spaceship);
        producer.sendSpaceshipEvent(savedSpaceship, "CREATED"); // Evento de creación
        return savedSpaceship;
    }

    public Spaceship updateSpaceship(Long id, Spaceship spaceship) {
        spaceship.setId(id);
        Spaceship updatedSpaceship = repository.save(spaceship);
        producer.sendSpaceshipEvent(updatedSpaceship, "UPDATED"); // Evento de actualización
        return updatedSpaceship;
    }

    public void deleteSpaceship(Long id) {
        repository.deleteById(id);
        producer.sendSpaceshipEvent(new Spaceship(id, "N/A", "N/A", 0), "DELETED"); // Evento de eliminación
    }
}
