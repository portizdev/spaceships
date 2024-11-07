package com.portizdev.spaceships.controller;

import com.portizdev.spaceships.model.Spaceship;
import com.portizdev.spaceships.service.SpaceshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spaceships")
public class SpaceshipController {

    @Autowired
    private SpaceshipService service;

    @Autowired
    private PagedResourcesAssembler<Spaceship> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<?> getAllSpaceships(Pageable pageable) {
        Page<Spaceship> page = service.getAllSpaceships(pageable);
        return pagedResourcesAssembler.toModel(page);
    }
    
    @GetMapping("/{id}")
    public Spaceship getSpaceshipById(@PathVariable Long id) {
        return service.getSpaceshipById(id);
    }

    @GetMapping("/search")
    public List<Spaceship> searchSpaceships(@RequestParam String name) {
        return service.searchSpaceshipsByName(name);
    }

    @PostMapping
    public Spaceship createSpaceship(@RequestBody Spaceship spaceship) {
        return service.createSpaceship(spaceship);
    }

    @PutMapping("/{id}")
    public Spaceship updateSpaceship(@PathVariable Long id, @RequestBody Spaceship spaceship) {
        return service.updateSpaceship(id, spaceship);
    }

    @DeleteMapping("/{id}")
    public void deleteSpaceship(@PathVariable Long id) {
        service.deleteSpaceship(id);
    }
}
